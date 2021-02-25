[![Actions Status](https://github.com/KryptKode/Carz/workflows/android/badge.svg)](https://github.com/KryptKode/Carz/actions)
[![codecov](https://codecov.io/gh/KryptKode/Carz/branch/master/graph/badge.svg?token=R1UHNXH1CZ)](https://codecov.io/gh/KryptKode/Carz)
[![Kotlin Version](https://img.shields.io/badge/kotlin-1.4.21-blue.svg)](http://kotlinlang.org/)
[![AGP](https://img.shields.io/badge/AGP-4.1.0-blue)](https://developer.android.com/studio/releases/gradle-plugin)
[![Gradle](https://img.shields.io/badge/Gradle-6.5-blue)](https://gradle.org)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

# Android App Template

A simple android template in Kotlin.
The Gradle script uses Kotlin Gradle DSL ([buildSrc](https://gradle.org/kotlin/) )
which brings Kotlin's rich language features to Gradle configuration.
The project also uses detekt to detect code smells and ktlint to enforce proper code style.
Github actions handle continuous integration and run detekt, ktlint, lint and unit tests concurrently.
A pre-commit git hook verifies the project's code style before committing code.
Test coverage reports are uploaded to [codecov](https://codecov.io/gh/KryptKode/Carz/).

### Outline

- [Building source](https://github.com/KryptKode/Carz#building-source)
- [Top features](https://github.com/KryptKode/Carz#top-features)
- [Libraries](https://github.com/KryptKode/Carz#libraries)


### Building source

To build this project, you require:

- Android Studio 4.1.0  or higher
- Gradle 6.5 or higher

### Top features

- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- Kotlin coroutines with [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html)
- Dependency injection with [Dagger-Hilt](https://dagger.dev/hilt/)
- API request with [Retrofit](http://square.github.io/retrofit) and [Moshi](https://github.com/square/moshi) for JSON serialisation
- CI  with [Github actions](https://github.com/features/actions)
- Code coverage with [jacoco](https://github.com/vanniktech/gradle-android-junit-jacoco-plugin) with reports uploaded to [codecov](https://codecov.io/gh/KryptKode/Carz/)
- Code lint check with [Ktlint](https://github.com/pinterest/ktlint) using a [gradle plugin](https://github.com/JLLeitschuh/ktlint-gradle)
- Static code analysis with [detekt](https://github.com/detekt/detekt)
- Dependency management with [buildSrc](https://gradle.org/kotlin/)  (Kotlin DSL)
- Dependency updates with [buildSrcVersions](https://jmfayard.github.io/refreshVersions/)
- [Git hooks](https://github.com/KryptKode/Carz/tree/master/scripts/git-hooks) to perform ktlint, detekt and lint checks before committing


### Libraries Used

- [Material Components](https://github.com/material-components/material-components-android/) - comes with ready made material UI view components
- [Constraint Layout](https://developer.android.com/reference/android/support/constraint/ConstraintLayout) - it enables creation of layouts flat view hierarchies
- [Retrofit](http://square.github.io/retrofit) for REST api communication, it is actively developed and stable
- [Moshi](https://github.com/square/moshi) for JSON serialisation and deserialisation, it's lightweight, stable and works well with Retrofit
- [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - for  asynchronous concurrency because it's easy to use and comes out of the box with kotlin
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - to manage UI data in a lifecycle conscious way, surviving configuration change
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - to hold and observe UI state in a lifecycle aware way in the app
- [Navigation Architecture Component](https://developer.android.com/guide/navigation/navigation-getting-started) - for easy navigation, especially with fragments no need to interact with the fragment manager
- [Glide](https://github.com/bumptech/glide) - for image loading and caching with smooth scrolling. It is actively developed and stable.
- [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - to interact easily with views from XML through an auto-generated binding class.
- [LeakCanary](https://square.github.io/leakcanary/getting_started/) - to detecting memory leaks in development
- [Mockk](https://github.com/mockk/mockk) for mocking in tests.
- [Dagger-Hilt](https://dagger.dev/hilt/) for dependency injection
- [Kotlin Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html) for concurrency
- [Turbine](https://github.com/cashapp/turbine) for testing flow
- [Ktlint gradle plugin](https://github.com/JLLeitschuh/ktlint-gradle) for code lint checks
- [Detekt](https://github.com/detekt/detekt) for static code analysis



