package com.globalhiddenodds.whois.presentation.navigation

import android.content.Context
import android.view.View
import com.globalhiddenodds.whois.presentation.view.activities.FaceVideoActivity
import com.globalhiddenodds.whois.presentation.view.activities.MenuActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {
    fun showFaceVideo(context: Context) = context
        .startActivity(FaceVideoActivity.callingIntent(context))
    fun showMenu(context: Context) = context
        .startActivity(MenuActivity.callingIntent(context))
    class Extras(val transitionSharedElement: View)
}