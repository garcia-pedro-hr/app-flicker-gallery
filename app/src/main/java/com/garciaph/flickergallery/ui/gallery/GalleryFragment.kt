package com.garciaph.flickergallery.ui.gallery

import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.garciaph.flickergallery.R
import com.garciaph.flickergallery.domain.entities.Photo
import com.garciaph.flickergallery.ui.gallery.adapters.PhotosAdapter
import com.garciaph.flickergallery.utils.hideKeyboard
import kotlinx.coroutines.launch
import timber.log.Timber


class GalleryFragment : Fragment() {

    private val viewModel: GalleryViewModel by viewModels()

    private val photosAdapter: PhotosAdapter by lazy {
        PhotosAdapter(object : PhotosAdapter.IOnItemClickListener<Photo> {
            override fun onItemClicked(obj: Photo, pos: Int) {
                Timber.d("Item clicked: obj=${obj.id} pos=$pos")
                findNavController()
                    .navigate(GalleryFragmentDirections.toFullscreenFragment(obj))
            }
        })
    }

    // Views
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var gradientBackground: View
    private lateinit var photosList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_gallery, container, false).apply {
        progressBar = findViewById(R.id.progress_bar)

        initAppBar(this)
        initSearchView(this)
        initRecyclerView(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.photosPager.observe(viewLifecycleOwner) { data ->
            lifecycleScope.launch {
                /* Add empty item to be detected as the end of the list
                   in order to show a progressBar */
                photosAdapter.submitData(data)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading && progressBar.visibility == View.GONE) {
                showProgressBar()
            } else if (!isLoading && progressBar.visibility == View.VISIBLE){
                hideProgressBar()
            }
        }
    }

    private fun initAppBar(root: View?) = root?.apply {
        appBarLayout = findViewById(R.id.app_bar_layout)
        gradientBackground = findViewById(R.id.gradient_background)

        appBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { layout, verticalOffset ->
                // Slowly hide the search view with action bar collapse
                val transparencyAlpha =
                    (layout.totalScrollRange + verticalOffset).toFloat() / layout.totalScrollRange
                searchView.alpha = transparencyAlpha
                gradientBackground.alpha = transparencyAlpha

                if (transparencyAlpha <= 0F) {
                    searchView.visibility = View.INVISIBLE
                    gradientBackground.visibility = View.INVISIBLE
                } else {
                    searchView.visibility = View.VISIBLE
                    gradientBackground.visibility = View.VISIBLE
                }

            }
        )

        /* Clicking on the icon will scroll the photo recycler view to the beginning */
        findViewById<ImageButton>(R.id.iv_icon).setOnClickListener {
            val scroller = LinearSmoothScroller(context).apply { targetPosition = 0 }
            photosList.layoutManager?.startSmoothScroll(scroller)
        }

        (activity as? AppCompatActivity)?.let {
            it.setSupportActionBar(findViewById(R.id.main_toolbar))
            it.supportActionBar?.setDisplayShowTitleEnabled(false)
        }

    }

    private fun initSearchView(root: View?) = root?.apply {
        searchView = findViewById(R.id.search_view)
        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        Timber.d("New query: $it")
                        viewModel.fetchPhotos(it)
                        appBarLayout.setExpanded(false, true)
                    }

                    activity?.hideKeyboard()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = false
            })
        }
    }

    private fun initRecyclerView(root: View?) = root?.apply {
        photosList = findViewById(R.id.rv_photos)
        photosList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = photosAdapter
        }
    }

    private fun showProgressBar() {
        val appearAnimation = AnimationUtils.loadAnimation(context, R.anim.translate_up)

        progressBar.startAnimation(appearAnimation)
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        val disappearAnimation =
            AnimationUtils.loadAnimation(context, R.anim.translate_down).apply {
                setAnimationListener(object: Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        /* do nothing */
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        progressBar.visibility = View.GONE
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                        /* do nothing */
                    }
                })
            }

        progressBar.startAnimation(disappearAnimation)
    }
}