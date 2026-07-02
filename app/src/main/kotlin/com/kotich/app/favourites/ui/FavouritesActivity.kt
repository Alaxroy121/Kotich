package com.kotich.app.favourites.ui

import android.os.Bundle
import com.kotich.app.core.nav.AppRouter
import com.kotich.app.core.ui.FragmentContainerActivity
import com.kotich.app.favourites.ui.list.FavouritesListFragment

class FavouritesActivity : FragmentContainerActivity(FavouritesListFragment::class.java) {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val categoryTitle = intent.getStringExtra(AppRouter.KEY_TITLE)
		if (categoryTitle != null) {
			title = categoryTitle
		}
	}
}
