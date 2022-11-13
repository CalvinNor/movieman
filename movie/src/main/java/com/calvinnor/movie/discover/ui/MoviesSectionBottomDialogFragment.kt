package com.calvinnor.movie.discover.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import com.calvinnor.core.extensions.colorFrom
import com.calvinnor.core.extensions.setTint
import com.calvinnor.movie.R
import com.calvinnor.movie.commons.model.MoviesSection
import com.calvinnor.movie.databinding.ItemBottomNavigationBinding
import com.calvinnor.movie.databinding.LayoutBottomNavigationMenuBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Shows the bottom sheet to select Movie Section.
 */
class MoviesSectionBottomDialogFragment : BottomSheetDialogFragment() {

    private lateinit var interactions: NavigationInteractions

    private var _binding: LayoutBottomNavigationMenuBinding? = null
    private val viewBinding get() = _binding!!

    private val selectedSection by lazy {
        arguments?.getSerializable(ARGS_SELECTED_SECTION)
            ?: "Must pass selection section in newInstance()"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        interactions = when {
            context is NavigationInteractions -> context
            parentFragment is NavigationInteractions -> parentFragment as NavigationInteractions
            else -> throw IllegalStateException("Parent activity or fragment must implement NavigationInteractions")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layoutBottomNavigationMenuBinding =
            LayoutBottomNavigationMenuBinding.inflate(inflater, container, false)
        _binding = layoutBottomNavigationMenuBinding
        return layoutBottomNavigationMenuBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addNavigationItems()
    }

    private fun addNavigationItems() = viewBinding.llNavigationBottomSheet.run {
        removeAllViews()
        getNavigationItems().forEach { addView(buildBottomNavigationView(it).root) }
    }

    private fun buildBottomNavigationView(itemData: NavigationItemData) =
        ItemBottomNavigationBinding.inflate(layoutInflater).apply {
            ivNavIcon.setImageResource(itemData.icon)
            tvNavTitle.setText(itemData.title)

            val isSelected = itemData.moviesSection == selectedSection
            if (isSelected) {
                llNavItemContainer.setBackgroundColor(colorFrom(R.color.colorAccentLight))
                ivNavIcon.setTint(R.color.colorAccent)
                tvNavTitle.setTextColor(colorFrom(R.color.colorAccent))
            } else root.setOnClickListener {
                dismiss()
                interactions.onNavigationItemPicked(itemData.moviesSection)
            }
        }

    private fun getNavigationItems() = listOf(
        NavigationItemData(
            MoviesSection.POPULAR,
            R.string.bottom_nav_popular,
            R.drawable.ic_popular
        ),
        NavigationItemData(
            MoviesSection.NOW_PLAYING,
            R.string.bottom_nav_now_playing,
            R.drawable.ic_now_playing
        ),
        NavigationItemData(
            MoviesSection.UPCOMING,
            R.string.bottom_nav_upcoming,
            R.drawable.ic_upcoming
        ),
        NavigationItemData(
            MoviesSection.TOP_RATED,
            R.string.bottom_nav_top_rated,
            R.drawable.ic_top_rated
        )
    )

    data class NavigationItemData(
        val moviesSection: MoviesSection,
        @StringRes val title: Int,
        @DrawableRes val icon: Int
    )

    companion object {
        const val TAG = "BottomNavigationDialogFragment"
        private const val ARGS_SELECTED_SECTION = "args_selected_movie_section"

        fun newInstance(selectedMoviesSection: MoviesSection): MoviesSectionBottomDialogFragment {
            return MoviesSectionBottomDialogFragment().apply {
                arguments = bundleOf(ARGS_SELECTED_SECTION to selectedMoviesSection)
            }
        }

        interface NavigationInteractions {
            fun onNavigationItemPicked(moviesSection: MoviesSection)
        }
    }
}
