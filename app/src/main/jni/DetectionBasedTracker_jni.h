/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>

#ifndef _Included_com_globalhiddenodds_whois_DetectionBasedTracker
#define _Included_com_globalhiddenodds_whois_DetectionBasedTracker
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_globalhiddenodds_whois_DetectionBasedTracker
 * Method:    nativeCreateObject
 * Signature: (Ljava/lang/String;F)J
 */
JNIEXPORT jlong JNICALL Java_com_globalhiddenodds_whois_presentation_opencv_DetectionBasedTracker_nativeCreateObject
  (JNIEnv *, jclass, jstring, jint);

/*
 * Class:     com_globalhiddenodds_whois_DetectionBasedTracker
 * Method:    nativeDestroyObject
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_globalhiddenodds_whois_presentation_opencv_DetectionBasedTracker_nativeDestroyObject
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_globalhiddenodds_whois_DetectionBasedTracker
 * Method:    nativeStart
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_globalhiddenodds_whois_presentation_opencv_DetectionBasedTracker_nativeStart
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_globalhiddenodds_whois_DetectionBasedTracker
 * Method:    nativeStop
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_globalhiddenodds_whois_presentation_opencv_DetectionBasedTracker_nativeStop
  (JNIEnv *, jclass, jlong);

  /*
   * Class:     com_globalhiddenodds_whois_DetectionBasedTracker
   * Method:    nativeSetFaceSize
   * Signature: (JI)V
   */
  JNIEXPORT void JNICALL Java_com_globalhiddenodds_whois_presentation_opencv_DetectionBasedTracker_nativeSetFaceSize
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     com_globalhiddenodds_whois_DetectionBasedTracker
 * Method:    nativeDetect
 * Signature: (JJJ)V
 */
JNIEXPORT void JNICALL Java_com_globalhiddenodds_whois_presentation_opencv_DetectionBasedTracker_nativeDetect
  (JNIEnv *, jclass, jlong, jlong, jlong);

#ifdef __cplusplus
}
#endif
#endif