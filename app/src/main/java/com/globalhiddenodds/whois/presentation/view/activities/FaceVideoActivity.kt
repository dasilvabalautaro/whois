package com.globalhiddenodds.whois.presentation.view.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import com.globalhiddenodds.whois.App
import com.globalhiddenodds.whois.R
import com.globalhiddenodds.whois.di.ApplicationComponent
import com.globalhiddenodds.whois.presentation.data.DataTemp
import com.globalhiddenodds.whois.presentation.opencv.DetectionBasedTracker
import org.opencv.android.*
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.math.roundToInt


class FaceVideoActivity: CameraActivity(),
    CvCameraViewListener2, View.OnTouchListener {

    private val appComponent: ApplicationComponent by
    lazy(mode = LazyThreadSafetyMode.NONE) {
        (application as App).appComponent
    }

    //@Inject
    //lateinit var enablePermissions: EnablePermissions

    private val faceRectColor = Scalar(0.0, 255.0, 0.0, 255.0)
    private val javaDetector = 0
    private val nativeDetector = 1
    private var mRgba: Mat? = null
    private var mGray: Mat? = null
    private var mCascadeFile: File? = null
    private var mJavaDetector: CascadeClassifier? = null
    private var mNativeDetector: DetectionBasedTracker? = null

    private var mDetectorType: Int = 1
    private var mDetectorName: Array<String> = emptyArray()

    private var mRelativeFaceSize = 0.2f
    private var mAbsoluteFaceSize = 0
    private var mOpenCvCameraView: CameraBridgeViewBase? = null

    companion object {
        var optionDetector: Int = 1
        var sizeFace: Float = 0.2f
        fun callingIntent(context: Context) = Intent(
            context,
            FaceVideoActivity::class.java
        )
    }

    init{
        mDetectorName = Array(2) { "" }
        mDetectorName[javaDetector] = "Java"
        mDetectorName[nativeDetector] = "Native (tracking)"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        //enablePermissions.permissionCamera(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.view_detect_face_video)
        mOpenCvCameraView = findViewById<View>(R.id.surface_view) as CameraBridgeViewBase
        mOpenCvCameraView!!.visibility = CameraBridgeViewBase.VISIBLE
        mOpenCvCameraView!!.setCvCameraViewListener(this)

    }

    private fun loadClassifier(){
        println("OpenCV loaded successfully")
        // Load native library after(!) OpenCV initialization
        System.loadLibrary("detection_based_tracker")
        try {
            // load cascade file from application resources
            val input = resources.openRawResource(R.raw.lbpcascade_frontalface)
            val cascadeDir = getDir("cascade", MODE_PRIVATE)
            mCascadeFile = File(cascadeDir, "lbpcascade_frontalface.xml")
            val os = FileOutputStream(mCascadeFile)
            val buffer = ByteArray(4096)
            var bytesRead: Int
            while (input.read(buffer).also { bytesRead = it } != -1) {
                os.write(buffer, 0, bytesRead)
            }
            input.close()
            os.close()
            mJavaDetector = CascadeClassifier(mCascadeFile!!.absolutePath)
            if (mJavaDetector!!.empty()) {
                println("Failed to load cascade classifier")
                mJavaDetector = null
            } else println("Loaded cascade classifier from ")

            mNativeDetector = DetectionBasedTracker(mCascadeFile!!.absolutePath, 0)
            cascadeDir.delete()
        } catch (e: IOException) {
            e.printStackTrace()
            println("Failed to load cascade. Exception thrown: $e")

        }
        mOpenCvCameraView!!.enableView()
    }

    private val mLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                SUCCESS -> {
                    loadClassifier()
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (mOpenCvCameraView != null) mOpenCvCameraView!!.disableView()
    }

    override fun onResume() {
        super.onResume()
        setMinFaceSize(sizeFace)
        setDetectorType(optionDetector)
        if (!OpenCVLoader.initDebug()) {
            println("Internal OpenCV library not found. Using OpenCV Manager for initialization")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback)
        } else {
            println("OpenCV library found inside package. Using it!")
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mOpenCvCameraView != null) mOpenCvCameraView!!.disableView()
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        mGray = Mat()
        mRgba = Mat()

    }

    override fun onCameraViewStopped() {
        mGray!!.release()
        mRgba!!.release()
    }
    override fun getCameraViewList(): List<CameraBridgeViewBase?>? {
        return Collections.singletonList(mOpenCvCameraView)
    }

    override fun onCameraFrame(inputFrame: CvCameraViewFrame): Mat? {
        mRgba = inputFrame.rgba()
        mGray = inputFrame.gray()
        if (mAbsoluteFaceSize == 0) {
            val height = mGray!!.rows()
            if ((height * mRelativeFaceSize).roundToInt() > 0) {
                mAbsoluteFaceSize = (height * mRelativeFaceSize).roundToInt()
            }
            mNativeDetector!!.setMinFaceSize(mAbsoluteFaceSize)
        }
        val faces = MatOfRect()

        if (mDetectorType == javaDetector) {
            if (mJavaDetector != null) mJavaDetector!!.detectMultiScale(
                mGray, faces, 1.1, 2, 2,
                Size(mAbsoluteFaceSize.toDouble(), mAbsoluteFaceSize.toDouble()), Size()
            )
        } else if (mDetectorType == nativeDetector) {
            if (mNativeDetector != null) mNativeDetector!!.detect(mGray!!, faces)
        } else {
            println("Detection method is not selected!")
        }
        DataTemp.clearListBitmap()
        val facesArray = faces.toArray()
        for (i in facesArray.indices) {
            Imgproc.rectangle(mRgba, facesArray[i].tl(),
                facesArray[i].br(), faceRectColor, 3)

            try {
                val imgCrop = Mat(mRgba, facesArray[i])
                val bmp: Bitmap = Bitmap.createBitmap(imgCrop.cols(),
                    imgCrop.rows(), Bitmap.Config.ARGB_8888)
                Utils.matToBitmap(imgCrop, bmp)
                DataTemp.addBitmap(bmp)
            }catch (ex: CvException){
                println("Error OpenCv: ${ex.message}")
            }
        }
        return mRgba
    }

    private fun setMinFaceSize(faceSize: Float) {
        mRelativeFaceSize = faceSize
        mAbsoluteFaceSize = 0
    }

    private fun setDetectorType(type: Int) {
        if (mDetectorType != type) {
            mDetectorType = type
            if (type == nativeDetector) {
                println("Detection Based Tracker enabled")
                mNativeDetector!!.start()
            } else {
                println("Cascade detector enabled")
                if(mNativeDetector != null)
                    mNativeDetector!!.stop()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        this.finish()
        return super.onTouchEvent(event)

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }
}