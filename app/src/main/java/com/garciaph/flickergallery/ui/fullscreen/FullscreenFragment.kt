package com.garciaph.flickergallery.ui.fullscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.garciaph.flickergallery.R


class FullscreenFragment : Fragment() {

    private val args: FullscreenFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_fullscreen, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        args.photo?.let {
            val fullscreenImageView: ImageView = view.findViewById(R.id.iv_fullscreen)
            val titleTextView: TextView = view.findViewById(R.id.tv_title)
            val ownerTextView: TextView = view.findViewById(R.id.tv_owner_name)

            titleTextView.text = it.title
            ownerTextView.text = it.owner
            Glide.with(this).load(it.fullscreenUrl).into(fullscreenImageView)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }
}