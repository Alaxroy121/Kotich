package com.kotich.app.explore.ui.adapter

import android.view.View
import com.kotich.app.list.ui.adapter.ListHeaderClickListener
import com.kotich.app.list.ui.adapter.ListStateHolderListener

interface ExploreListEventListener : ListStateHolderListener, View.OnClickListener, ListHeaderClickListener
