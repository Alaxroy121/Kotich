package com.kotich.app.main.ui.welcome

import android.accounts.AccountManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.kotich.app.R
import com.kotich.app.core.model.titleResId
import com.kotich.app.core.nav.router
import com.kotich.app.core.prefs.ColorScheme
import com.kotich.app.core.ui.sheet.BaseAdaptiveSheet
import com.kotich.app.core.ui.widgets.ChipsView
import com.kotich.app.core.util.ext.consume
import com.kotich.app.core.util.ext.getDisplayName
import com.kotich.app.core.util.ext.observe
import com.kotich.app.core.util.ext.tryLaunch
import com.kotich.app.databinding.SheetWelcomeBinding
import com.kotich.app.filter.ui.model.FilterProperty
import org.koitharu.kotatsu.parsers.model.ContentType
import java.util.Locale

@AndroidEntryPoint
class WelcomeSheet : BaseAdaptiveSheet<SheetWelcomeBinding>(), ChipsView.OnChipClickListener, View.OnClickListener,
	ActivityResultCallback<Uri?> {

	private val viewModel by viewModels<WelcomeViewModel>()

	private val backupSelectCall = registerForActivityResult(
		ActivityResultContracts.OpenDocument(),
		this,
	)

	override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?): SheetWelcomeBinding {
		return SheetWelcomeBinding.inflate(inflater, container, false)
	}

	override fun onViewBindingCreated(binding: SheetWelcomeBinding, savedInstanceState: Bundle?) {
		super.onViewBindingCreated(binding, savedInstanceState)
		binding.textViewWelcomeTitle.isGone = resources.getBoolean(R.bool.is_tablet)
		binding.chipsLocales.onChipClickListener = this
		binding.chipsType.onChipClickListener = this
		binding.chipBackup.setOnClickListener(this)
		binding.chipSync.setOnClickListener(this)
		binding.chipDirectories.setOnClickListener(this)

		// iOS 27 Liquid Glass: apply glass styling to welcome sheet
		try {
			val entryPoint = dagger.hilt.android.EntryPointAccessors.fromApplication<
				com.kotich.app.core.ui.BaseActivityEntryPoint
			>(requireContext().applicationContext)
			if (entryPoint.settings.colorScheme == ColorScheme.LIQUID) {
				binding.root.setBackgroundColor(
					androidx.core.content.ContextCompat.getColor(requireContext(), R.color.ios27_glass_white)
				)
				binding.textViewWelcomeTitle.setTextColor(
					androidx.core.content.ContextCompat.getColor(requireContext(), R.color.ios27_blue)
				)
			}
		} catch (_: Exception) {
			// Entry point not available, skip styling
		}

		viewModel.locales.observe(viewLifecycleOwner, ::onLocalesChanged)
		viewModel.types.observe(viewLifecycleOwner, ::onTypesChanged)
	}

	override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
		val typeMask = WindowInsetsCompat.Type.systemBars()
		viewBinding?.scrollView?.updatePadding(
			bottom = insets.getInsets(typeMask).bottom,
		)
		return insets.consume(v, typeMask, bottom = true)
	}

	override fun onChipClick(chip: Chip, data: Any?) {
		when (data) {
			is ContentType -> viewModel.setTypeChecked(data, !chip.isChecked)
			is Locale -> viewModel.setLocaleChecked(data, !chip.isChecked)
		}
	}

	override fun onClick(v: View) {
		when (v.id) {
			R.id.chip_backup -> {
				if (!backupSelectCall.tryLaunch(arrayOf("*/*"))) {
					Snackbar.make(
						v, R.string.operation_not_supported, Snackbar.LENGTH_SHORT,
					).show()
				}
			}

			R.id.chip_sync -> {
				val am = AccountManager.get(v.context)
				val accountType = getString(R.string.account_type_sync)
				am.addAccount(accountType, accountType, null, null, requireActivity(), null, null)
			}

            R.id.chip_directories -> {
                router.openDirectoriesSettings()
            }
		}
	}

	override fun onActivityResult(result: Uri?) {
		if (result != null) {
			router.showBackupRestoreDialog(result)
		}
	}

	private fun onLocalesChanged(value: FilterProperty<Locale>) {
		val chips = viewBinding?.chipsLocales ?: return
		chips.setChips(
			value.availableItems.map {
				ChipsView.ChipModel(
					title = it.getDisplayName(chips.context),
					isChecked = it in value.selectedItems,
					data = it,
				)
			},
		)
	}

	private fun onTypesChanged(value: FilterProperty<ContentType>) {
		val chips = viewBinding?.chipsType ?: return
		chips.setChips(
			value.availableItems.map {
				ChipsView.ChipModel(
					title = getString(it.titleResId),
					isChecked = it in value.selectedItems,
					data = it,
				)
			},
		)
	}
}
