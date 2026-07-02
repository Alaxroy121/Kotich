package com.kotich.app.reader.ui.colorfilter

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import com.kotich.app.core.model.parcelable.ParcelableManga
import com.kotich.app.core.model.parcelable.ParcelableMangaPage
import com.kotich.app.core.nav.AppRouter
import com.kotich.app.core.parser.MangaDataRepository
import com.kotich.app.core.prefs.AppSettings
import com.kotich.app.core.ui.BaseViewModel
import com.kotich.app.core.util.ext.MutableEventFlow
import com.kotich.app.core.util.ext.call
import com.kotich.app.core.util.ext.require
import com.kotich.app.reader.domain.ReaderColorFilter
import javax.inject.Inject

@HiltViewModel
class ColorFilterConfigViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val settings: AppSettings,
	private val mangaDataRepository: MangaDataRepository,
) : BaseViewModel() {

	private val manga = savedStateHandle.require<ParcelableManga>(AppRouter.KEY_MANGA).manga

	private var initialColorFilter: ReaderColorFilter? = null
	val colorFilter = MutableStateFlow<ReaderColorFilter?>(null)
	val onDismiss = MutableEventFlow<Unit>()
	val preview = savedStateHandle.require<ParcelableMangaPage>(AppRouter.KEY_PAGES).page

	val isChanged: Boolean
		get() = colorFilter.value != initialColorFilter

	init {
		launchLoadingJob {
			initialColorFilter = mangaDataRepository.getColorFilter(manga.id) ?: settings.readerColorFilter
			colorFilter.value = initialColorFilter
		}
	}

	fun setBrightness(brightness: Float) {
		updateColorFilter { it.copy(brightness = brightness) }
	}

	fun setContrast(contrast: Float) {
		updateColorFilter { it.copy(contrast = contrast) }
	}

	fun setInversion(invert: Boolean) {
		updateColorFilter { it.copy(isInverted = invert) }
	}

	fun setGrayscale(grayscale: Boolean) {
		updateColorFilter { it.copy(isGrayscale = grayscale) }
	}

	fun setBookEffect(book: Boolean) {
		updateColorFilter { it.copy(isBookBackground = book) }
	}

	fun reset() {
		colorFilter.value = null
	}

	fun save() {
		launchLoadingJob(Dispatchers.Default) {
			mangaDataRepository.saveColorFilter(manga, colorFilter.value)
			onDismiss.call(Unit)
		}
	}

	fun saveGlobally() {
		launchLoadingJob(Dispatchers.Default) {
			settings.readerColorFilter = colorFilter.value
			mangaDataRepository.resetColorFilters()
			onDismiss.call(Unit)
		}
	}

	private inline fun updateColorFilter(block: (ReaderColorFilter) -> ReaderColorFilter) {
		colorFilter.value = block(
			colorFilter.value ?: ReaderColorFilter.EMPTY,
		).takeUnless { it.isEmpty }
	}
}
