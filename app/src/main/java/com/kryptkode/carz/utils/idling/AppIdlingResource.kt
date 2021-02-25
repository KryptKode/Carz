package com.kryptkode.carz.utils.idling

import androidx.test.espresso.IdlingResource
import javax.inject.Inject

/**
 * Fake idling resource
 * */
class AppIdlingResource @Inject constructor() : IAppIdlingResource {

    override fun getName(): String {
        return this.javaClass.name
    }

    override fun isIdleNow(): Boolean {
        return false
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
    }

    override fun setIdleState(isIdleNow: Boolean) {
    }
}
