package com.kryptkode.carz.utils.idling

import androidx.test.espresso.IdlingResource

interface IAppIdlingResource : IdlingResource {
    fun setIdleState(isIdleNow: Boolean)
}
