package com.kotich.app.main.ui.protect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.biometric.AuthenticationRequest
import androidx.biometric.AuthenticationRequest.Biometric
import androidx.biometric.AuthenticationResult
import androidx.biometric.AuthenticationResultCallback
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.biometric.registerForAuthenticationResult
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withResumed
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.kotich.app.R
import com.kotich.app.core.ui.BaseActivity
import com.kotich.app.core.ui.util.DefaultTextWatcher
import com.kotich.app.core.util.ext.consumeAllSystemBarsInsets
import com.kotich.app.core.util.ext.getDisplayMessage
import com.kotich.app.core.util.ext.getParcelableExtraCompat
import com.kotich.app.core.util.ext.observe
import com.kotich.app.core.util.ext.observeEvent
import com.kotich.app.core.util.ext.systemBarsInsets
import com.kotich.app.databinding.ActivityProtectBinding
import com.google.android.material.R as materialR

/**
 * Strict fingerprint-only lock screen.
 * Biometric STRONG required (fingerprint, face unlock with hardware backing).
 * Password field hidden by default — only shown as fallback if biometric fails.
 */
@AndroidEntryPoint
class ProtectActivity :
	BaseActivity<ActivityProtectBinding>(),
	TextView.OnEditorActionListener,
	DefaultTextWatcher,
	View.OnClickListener,
	AuthenticationResultCallback {

	private val viewModel by viewModels<ProtectViewModel>()
	private var biometricAttempted = false

	private val biometricPrompt = registerForAuthenticationResult(resultCallback = this)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
		setContentView(ActivityProtectBinding.inflate(layoutInflater))

		viewBinding.editPassword.setOnEditorActionListener(this)
		viewBinding.editPassword.addTextChangedListener(this)
		viewBinding.buttonNext.setOnClickListener(this)
		viewBinding.buttonCancel.setOnClickListener(this)

		// Hide password field by default — fingerprint first
		viewBinding.layoutPassword.isVisible = false
		viewBinding.buttonNext.isVisible = false

		viewBinding.editPassword.inputType = if (viewModel.isNumericPassword) {
			EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD
		} else {
			EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
		}

		viewModel.onError.observeEvent(this, this::onError)
		viewModel.isLoading.observe(this, this::onLoadingStateChanged)
		viewModel.onUnlockSuccess.observeEvent(this) {
			val intent = intent.getParcelableExtraCompat<Intent>(EXTRA_INTENT)
			startActivity(intent)
			finishAfterTransition()
		}

		lifecycleScope.launch {
			withResumed {
				// Launch fingerprint immediately
				launchBiometric()
			}
		}
	}

	override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
		val barsInsets = insets.systemBarsInsets
		val basePadding = resources.getDimensionPixelOffset(R.dimen.screen_padding)
		viewBinding.root.setPadding(
			barsInsets.left + basePadding,
			barsInsets.top + basePadding,
			barsInsets.right + basePadding,
			barsInsets.bottom + basePadding,
		)
		return insets.consumeAllSystemBarsInsets()
	}

	override fun onClick(v: View) {
		when (v.id) {
			R.id.button_next -> viewModel.tryUnlock(viewBinding.editPassword.text?.toString().orEmpty())
			R.id.button_cancel -> finish()
			materialR.id.text_input_end_icon -> launchBiometric()
		}
	}

	override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
		return if (actionId == EditorInfo.IME_ACTION_DONE && viewBinding.buttonNext.isEnabled) {
			viewBinding.buttonNext.performClick()
			true
		} else {
			false
		}
	}

	override fun afterTextChanged(s: Editable?) {
		viewBinding.layoutPassword.error = null
		viewBinding.buttonNext.isEnabled = !s.isNullOrEmpty()
		updateEndIcon()
	}

	override fun onAuthResult(result: AuthenticationResult) {
		if (result.isSuccess()) {
			viewModel.unlock()
		} else {
			// Biometric failed — show password fallback
			showPasswordFallback()
		}
	}

	private fun onError(e: Throwable) {
		viewBinding.layoutPassword.error = e.getDisplayMessage(resources)
	}

	private fun onLoadingStateChanged(isLoading: Boolean) {
		viewBinding.layoutPassword.isEnabled = !isLoading
	}

	/**
	 * Launch biometric prompt with STRONG requirement (hardware-backed fingerprint/face).
	 * No PIN/password fallback — only biometric or app password.
	 */
	private fun launchBiometric(): Boolean {
		if (!viewModel.isBiometricEnabled) {
			showPasswordFallback()
			return false
		}
		if (BiometricManager.from(this).canAuthenticate(BIOMETRIC_STRONG) != BIOMETRIC_SUCCESS) {
			showPasswordFallback()
			return false
		}
		biometricAttempted = true
		val request = AuthenticationRequest.biometricRequest(
			title = getString(R.string.app_name),
			authFallback = Biometric.Fallback.NegativeButton(getString(android.R.string.cancel)),
			init = {
				setMinStrength(Biometric.Strength.Class2)
				setIsConfirmationRequired(false)
			},
		)
		biometricPrompt.launch(request)
		return true
	}

	/**
	 * Show password field as fallback when biometric is unavailable or fails.
	 */
	private fun showPasswordFallback() {
		viewBinding.layoutPassword.isVisible = true
		viewBinding.buttonNext.isVisible = true
		viewBinding.editPassword.requestFocus()
	}

	private fun updateEndIcon() = with(viewBinding.layoutPassword) {
		val isFingerprintIcon = viewModel.isBiometricEnabled && viewBinding.editPassword.text.isNullOrEmpty()
		if (isFingerprintIcon == (endIconMode == TextInputLayout.END_ICON_CUSTOM)) {
			return@with
		}
		if (isFingerprintIcon) {
			endIconMode = TextInputLayout.END_ICON_CUSTOM
			setEndIconDrawable(androidx.biometric.R.drawable.fingerprint_dialog_fp_icon)
			endIconContentDescription = getString(androidx.biometric.R.string.use_biometric_label)
			setEndIconOnClickListener(this@ProtectActivity)
		} else {
			setEndIconOnClickListener(null)
			setEndIconDrawable(0)
			endIconContentDescription = null
			endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
		}
	}

	companion object {

		private const val EXTRA_INTENT = "src_intent"

		fun newIntent(context: Context, sourceIntent: Intent): Intent {
			return Intent(context, ProtectActivity::class.java)
				.putExtra(EXTRA_INTENT, sourceIntent)
		}
	}
}
