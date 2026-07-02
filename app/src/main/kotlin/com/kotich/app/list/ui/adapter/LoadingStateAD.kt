package com.kotich.app.list.ui.adapter

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.kotich.app.R
import com.kotich.app.list.ui.model.ListModel
import com.kotich.app.list.ui.model.LoadingState

fun loadingStateAD() = adapterDelegate<LoadingState, ListModel>(R.layout.item_loading_state) {
}