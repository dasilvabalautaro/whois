package com.globalhiddenodds.whois.presentation.component

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.globalhiddenodds.whois.R
import com.globalhiddenodds.whois.databinding.ViewRowImageBinding
import com.globalhiddenodds.whois.presentation.data.FaceView
import com.globalhiddenodds.whois.presentation.extension.inflate
import com.globalhiddenodds.whois.presentation.navigation.Navigator
import com.globalhiddenodds.whois.presentation.view.tools.Transform
import javax.inject.Inject
import kotlin.properties.Delegates

class FaceAdapter @Inject constructor():
    RecyclerView.Adapter<FaceAdapter.ViewHolder>() {

    internal var collection: List<FaceView> by Delegates.observable(emptyList()) {
            _, _, _ -> notifyDataSetChanged()
    }

    private var clickListener: (FaceView, Navigator.Extras) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.view_row_image))

    override fun onBindViewHolder(viewHolder: ViewHolder,
                                  position: Int) =
        viewHolder.bind(collection[position], clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(face: FaceView, clickListener: (FaceView, Navigator.Extras) -> Unit) {
            val viewRowImageBinding = ViewRowImageBinding.bind(itemView)

            if (face.name.isNullOrEmpty()){
                viewRowImageBinding.tvName.visibility = View.INVISIBLE
                viewRowImageBinding.tvLblName.visibility = View.INVISIBLE
                viewRowImageBinding.tvDocument.visibility = View.INVISIBLE
                viewRowImageBinding.tvLblDocument.visibility = View.INVISIBLE
                viewRowImageBinding.ivSuccess.setImageResource(R.drawable.ic_unknow)
            }
            else{
                viewRowImageBinding.tvName.text = face.name
                viewRowImageBinding.tvDocument.text = face.document
                viewRowImageBinding.tvName.visibility = View.VISIBLE
                viewRowImageBinding.tvLblName.visibility = View.VISIBLE
                viewRowImageBinding.tvDocument.visibility = View.VISIBLE
                viewRowImageBinding.tvLblDocument.visibility = View.VISIBLE
                viewRowImageBinding.ivSuccess.setImageResource(R.drawable.ic_done)
            }
            viewRowImageBinding.tvLatitude.text = face.latitude.toString()
            viewRowImageBinding.tvLongitude.text = face.longitude.toString()
            viewRowImageBinding.tvDate.text = Transform.convertLongToTime(face.date)
            viewRowImageBinding.ivPhoto.setImageBitmap(face.image)
            itemView.setOnClickListener {
                clickListener(face,
                    Navigator.Extras(viewRowImageBinding.ivPhoto))

            }
        }
    }
}