package com.kotich.app.details.ui.pager.chapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.kotich.app.core.parser.MangaRepository
import com.kotich.app.core.ui.sheet.BaseAdaptiveSheet
import com.kotich.app.databinding.ItemChapterPreviewPageBinding
import com.kotich.app.databinding.SheetChapterPreviewBinding
import com.kotich.app.details.ui.pager.ChaptersPagesViewModel
import org.koitharu.kotatsu.parsers.model.MangaPage
import javax.inject.Inject

/**
 * Bottom sheet that shows chapter page previews.
 *
 * Smart sampling: loads totalPages ÷ 2 images (every 2nd page).
 * Only fetches the page list URL + loads sampled images — fast.
 *
 * 10 pages → 5 preview images (pages 1, 3, 5, 7, 9)
 * 20 pages → 10 preview images (pages 1, 3, 5, ... 19)
 * 30 pages → 15 preview images (pages 1, 3, 5, ... 29)
 */
@AndroidEntryPoint
class ChapterPreviewSheet : BaseAdaptiveSheet<SheetChapterPreviewBinding>() {

	@Inject
	lateinit var repositoryFactory: MangaRepository.Factory

	private val parentViewModel by ChaptersPagesViewModel.ActivityVMLazy(this)

	override fun onCreateViewBinding(
		inflater: LayoutInflater,
		container: ViewGroup?,
	) = SheetChapterPreviewBinding.inflate(inflater, container, false)

	override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat = insets

	override fun onViewBindingCreated(binding: SheetChapterPreviewBinding, savedInstanceState: Bundle?) {
		super.onViewBindingCreated(binding, savedInstanceState)

		val chapterId = arguments?.getLong(ARG_CHAPTER_ID, -1L) ?: -1L
		if (chapterId == -1L) {
			dismiss()
			return
		}

		val manga = parentViewModel.getMangaOrNull()
		if (manga == null) {
			dismiss()
			return
		}

		val details = parentViewModel.mangaDetails.value
		val chapter = details?.chapters?.values?.flatten()?.find { it.id == chapterId }
		if (chapter == null) {
			dismiss()
			return
		}

		binding.textViewChapterTitle.text = chapter.title ?: "Chapter ${chapter.number.toInt()}"

		val adapter = PreviewPageAdapter()
		binding.recyclerViewPreview.adapter = adapter

		// Load preview in lifecycle scope
		viewLifecycleOwner.lifecycleScope.launch {
			binding.progressBar.isVisible = true
			binding.textViewError.isVisible = false

			try {
				val repo = repositoryFactory.create(manga.source)
				val allPages = withContext(Dispatchers.Default) {
					repo.getPages(chapter)
				}
				val total = allPages.size

				if (total == 0) {
					binding.progressBar.isVisible = false
					binding.textViewError.isVisible = true
					binding.textViewError.text = "No preview available"
					return@launch
				}

				// Smart sampling: images = totalPages ÷ 2, every 2nd page
				val sampled = (0 until total step 2).map { allPages[it] }
				val count = sampled.size

				binding.progressBar.isVisible = false
				binding.textViewChapterInfo.text = "$total pages • showing $count previews"
				binding.recyclerViewPreview.isVisible = true

				adapter.submitList(sampled.mapIndexed { index, page ->
					PreviewPageItem(page, pageNumber = (index * 2) + 1)
				})
			} catch (e: Exception) {
				binding.progressBar.isVisible = false
				binding.textViewError.isVisible = true
				binding.textViewError.text = e.message ?: "Failed to load preview"
			}
		}

		binding.buttonClose.setOnClickListener { dismiss() }
	}

	companion object {

		const val TAG = "ChapterPreviewSheet"
		private const val ARG_CHAPTER_ID = "chapter_id"

		fun newInstance(chapterId: Long): ChapterPreviewSheet {
			return ChapterPreviewSheet().apply {
				arguments = Bundle().apply {
					putLong(ARG_CHAPTER_ID, chapterId)
				}
			}
		}
	}
}

private data class PreviewPageItem(
	val page: MangaPage,
	val pageNumber: Int,
)

private class PreviewPageAdapter : ListAdapter<PreviewPageItem, PreviewPageAdapter.ViewHolder>(
	object : DiffUtil.ItemCallback<PreviewPageItem>() {
		override fun areItemsTheSame(old: PreviewPageItem, new: PreviewPageItem) = old.page.id == new.page.id
		override fun areContentsTheSame(old: PreviewPageItem, new: PreviewPageItem) = old == new
	},
) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val binding = ItemChapterPreviewPageBinding.inflate(
			LayoutInflater.from(parent.context), parent, false,
		)
		return ViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	class ViewHolder(
		private val binding: ItemChapterPreviewPageBinding,
	) : RecyclerView.ViewHolder(binding.root) {

		fun bind(item: PreviewPageItem) {
			binding.imageViewPreview.setImageAsync(item.page)
			binding.textViewPageNumber.text = "Page ${item.pageNumber}"
		}
	}
}
