package com.kotich.app.list.ui.adapter

import com.kotich.app.list.domain.ListFilterOption

interface QuickFilterClickListener {

	fun onFilterOptionClick(option: ListFilterOption)
}
