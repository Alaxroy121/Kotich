package com.kotich.app.core.util

import kotlinx.coroutines.CoroutineExceptionHandler
import com.kotich.app.core.util.ext.printStackTraceDebug
import com.kotich.app.core.util.ext.report
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class AcraCoroutineErrorHandler : AbstractCoroutineContextElement(CoroutineExceptionHandler),
	CoroutineExceptionHandler {

	override fun handleException(context: CoroutineContext, exception: Throwable) {
		exception.printStackTraceDebug()
		exception.report()
	}
}
