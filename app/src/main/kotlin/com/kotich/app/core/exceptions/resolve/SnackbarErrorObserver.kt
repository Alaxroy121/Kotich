package com.kotich.app.core.exceptions.resolve

import android.view.View
import androidx.core.util.Consumer
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.kotich.app.R
import com.kotich.app.core.util.ext.getDisplayMessage
import com.kotich.app.core.util.ext.isSerializable
import com.kotich.app.main.ui.owners.BottomNavOwner
import com.kotich.app.main.ui.owners.BottomSheetOwner
import org.koitharu.kotatsu.parsers.exception.ParseException

class SnackbarErrorObserver(
	host: View,
	fragment: Fragment?,
	resolver: ExceptionResolver?,
	onResolved: Consumer<Boolean>?,
) : ErrorObserver(host, fragment, resolver, onResolved) {

	constructor(
		host: View,
		fragment: Fragment?,
	) : this(host, fragment, null, null)

	override suspend fun emit(value: Throwable) {
		val snackbar = Snackbar.make(host, value.getDisplayMessage(host.context.resources), Snackbar.LENGTH_SHORT)
		when (activity) {
			is BottomNavOwner -> snackbar.anchorView = activity.bottomNav
			is BottomSheetOwner -> snackbar.anchorView = activity.bottomSheet
		}
		if (canResolve(value)) {
			snackbar.setAction(ExceptionResolver.getResolveStringId(value)) {
				resolve(value)
			}
		} else if (value is ParseException) {
			val router = router()
			if (router != null && value.isSerializable()) {
				snackbar.setAction(R.string.details) {
					router.showErrorDialog(value)
				}
			}
		}
		snackbar.show()
	}
}
