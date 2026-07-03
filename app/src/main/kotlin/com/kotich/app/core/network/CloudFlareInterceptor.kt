package com.kotich.app.core.network

import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import com.kotich.app.core.exceptions.CloudFlareBlockedException
import com.kotich.app.core.exceptions.CloudFlareProtectedException
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.network.CloudFlareHelper

class CloudFlareInterceptor : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		val response = chain.proceed(request)
		return when (CloudFlareHelper.checkResponseForProtection(response)) {
			CloudFlareHelper.PROTECTION_BLOCKED -> response.closeThrowing(
				CloudFlareBlockedException(
					url = request.url.toString(),
					source = request.tag(MangaSource::class.java),
				),
			)

			CloudFlareHelper.PROTECTION_CAPTCHA -> {
				// Auto-retry: close response and let CaptchaHandler resolve it
				// The session cookies are persisted, so once solved it stays solved
				response.closeThrowing(
					CloudFlareProtectedException(
						url = request.url.toString(),
						source = request.tag(MangaSource::class.java),
						headers = request.headers,
					),
				)
			}

			else -> response
		}
	}

	private fun Response.closeThrowing(error: IOException): Nothing {
		try {
			close()
		} catch (e: Exception) {
			error.addSuppressed(e)
		}
		throw error
	}
}
