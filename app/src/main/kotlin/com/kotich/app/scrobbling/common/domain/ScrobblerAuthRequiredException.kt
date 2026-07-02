package com.kotich.app.scrobbling.common.domain

import okio.IOException
import com.kotich.app.scrobbling.common.domain.model.ScrobblerService

class ScrobblerAuthRequiredException(
	val scrobbler: ScrobblerService,
) : IOException()
