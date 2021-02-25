package com.kryptkode.carz.navigator

import android.app.Activity
import com.kryptkode.carz.ui.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
interface NavigatorModule {
    companion object {
        @Provides
        @ActivityScoped
        fun provideNavControllerProvider(activity: Activity): NavComponentsProvider {
            return (activity as MainActivity)
        }
    }
}
