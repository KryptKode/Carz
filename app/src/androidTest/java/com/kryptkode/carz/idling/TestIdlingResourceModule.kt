package com.kryptkode.carz.idling

import com.kryptkode.carz.utils.idling.IAppIdlingResource
import com.kryptkode.carz.utils.idling.IdlingResourceModule
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ActivityComponent::class],
    replaces = [IdlingResourceModule::class]
)
interface TestIdlingResourceModule {
    @Binds
    fun bindIdlingResource(appIdlingResource: TestAppIdlingResource): IAppIdlingResource
}
