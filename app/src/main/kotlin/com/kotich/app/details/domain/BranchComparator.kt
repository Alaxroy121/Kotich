package com.kotich.app.details.domain

import com.kotich.app.core.util.LocaleStringComparator
import com.kotich.app.details.ui.model.MangaBranch

class BranchComparator : Comparator<MangaBranch> {

	private val delegate = LocaleStringComparator()

	override fun compare(o1: MangaBranch, o2: MangaBranch): Int = delegate.compare(o1.name, o2.name)
}
