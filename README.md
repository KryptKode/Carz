[![Actions Status](https://github.com/KryptKode/Carz/workflows/android/badge.svg)](https://github.com/KryptKode/Carz/actions)
[![codecov](https://codecov.io/gh/KryptKode/Carz/branch/master/graph/badge.svg?token=R1UHNXH1CZ)](https://codecov.io/gh/KryptKode/Carz)
[![Kotlin Version](https://img.shields.io/badge/kotlin-1.4.21-blue.svg)](http://kotlinlang.org/)
[![AGP](https://img.shields.io/badge/AGP-4.1.0-blue)](https://developer.android.com/studio/releases/gradle-plugin)
[![Gradle](https://img.shields.io/badge/Gradle-6.5-blue)](https://gradle.org)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

# Carz
A simple android in Kotlin that applies the clean architecture concept.
It uses Kotlin Gradle DSL ([buildSrc](https://gradle.org/kotlin/) ) which brings Kotlin’s rich
language features to Gradle configuration. The project also uses
[detekt](https://github.com/detekt/detekt) to detect code smells and
[ktlint](https://github.com/pinterest/ktlint) to enforce proper code style.
[Github actions](https://github.com/KryptKode/Carz/actions) handle continuous integration and
 run detekt, ktlint, lint and unit tests concurrently.
 A pre-commit git hook verifies the project’s code style before committing code.
  Test coverage reports are uploaded to [codecov](https://codecov.io/gh/KryptKode/Carz/).


## Outline

- App Walkthrough
- App Installation
- Building source
- Top features
- Architecture
- Libraries
- Testing
- Extras

### App Walkthrough

The app fetches car manufacturers and as the user scrolls the list, more pages are gotten from the server.
 When the user clicks on a car manufacturer, a screen to choose the car type opens. On the car type screen, the app displays the lists of car types for that manufacturer.

There is a search field on the screen where the car types are filtered by name, according to what is entered on the search field. When the user clicks on a car type, the screen to select the date the car what built opens.

On the screen to choose a build date, the app fetches the list and displays it and when the user clicks on any of them, a screen that shows the summary of their selections shows.


<h4 align="center">
<img alt="Light mode screenshot" src="https://user-images.githubusercontent.com/25648077/109139334-b0fc8300-775b-11eb-84ae-9e964c9addd5.gif" width="35%" vspace="10" hspace="10">
Light mode
<img alt="Dark mode screenshot" src="https://user-images.githubusercontent.com/25648077/109142311-04240500-775f-11eb-8586-4bdf1104541e.gif" width="35%" vspace="10" hspace="10">
 Dark mode
<br>


### App Installation

You can download the APK from [releases](https://github.com/KryptKode/Carz/releases).

### Building source

In the `local.properties` file in your project-level directory,  add the following code to the file. Replace `YOUR_API_KEY` and `YOUR_BASE_URL`  with your API key and base URL respectively.

```
BASE_URL=YOUR_BASE_URL
API_KEY=YOUR_API_KEY
```

To build this project, you require:

- Android Studio 4.1.0  or higher
- Gradle 6.5 or higher

### Top features

- Applies the  [clean architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) concept
- Kotlin coroutines with Flow
- Dependency injection with [Dagger-Hilt](https://dagger.dev/hilt/)
- API request with [Retrofit](http://square.github.io/retrofit) and [Moshi](https://github.com/square/moshi) for JSON serialisation
- CI  with [Github actions](https://github.com/features/actions)
- Code coverage with [jacoco](https://github.com/vanniktech/gradle-android-junit-jacoco-plugin) with reports uploaded to [codecov](https://codecov.io/gh/KryptKode/Carz/)
- Code lint check with [Ktlint](https://github.com/pinterest/ktlint) using a [gradle plugin](https://github.com/JLLeitschuh/ktlint-gradle)
- Static code analysis with [detekt](https://github.com/detekt/detekt)
- Dependency management with [buildSrc](https://gradle.org/kotlin/)  (Kotlin DSL)
- Dependency updates with [buildSrcVersions](https://jmfayard.github.io/refreshVersions/)
- [Git hooks](https://github.com/KryptKode/Carz/blob/master/scripts/git-hooks/pre-commit.sh) to perform ktlint, detekt and lint checks before committing
- Some UI tests in [Espresso](https://developer.android.com/training/testing/espresso)

### Architecture

The app follows the [clean architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) concept in the most minimal way appropriate for the current state of the app. There are three layers in the app. The data layer, the domain and the presentation.

On the data layer, an API request is done with the [Retrofit](http://square.github.io/retrofit) library. Models are defined for the expected response.

The domain layer contains the model class and the use case for getting car information (manufacturers, types and build dates). It defines a state class, [`DataState`](https://github.com/KryptKode/Carz/blob/master/app/src/main/java/com/kryptkode/carz/data/model/DataState.kt) to notify the presentation layer of the loading, success and error states. For paginated data, the [`PagedDataState`](https://github.com/KryptKode/Carz/blob/master/app/src/main/java/com/kryptkode/carz/data/model/PagingDataState.kt) class defines an additional state for when the data is on the last page.
 In the app, asynchronous operations are carried out with coroutines. The domain layer defines a class to abstract the different dispatchers.

The presentation layer is implemented with `MVVM` using `ViewModel`. A view state data class is defined. The view controller is a `Fragment`.  The fragment observes the view state which is wrapped in a `StateFlow`. Whenever a use-case is executed in the `ViewModel`, a reducer, which receives the old state and the results (error, failure, or loading) creates a copy of the new state. This new state is posted to the view.

### Libraries Used

- [Material Components](https://github.com/material-components/material-components-android/) - comes with ready made material UI view components
- [Constraint Layout](https://developer.android.com/reference/android/support/constraint/ConstraintLayout) - it enables creation of layouts flat view hierarchies
- [Retrofit](http://square.github.io/retrofit) for REST api communication, it is actively developed and stable
- [Moshi](https://github.com/square/moshi) for JSON serialisation and deserialisation, it's lightweight, stable and works well with Retrofit
- [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - for  asynchronous concurrency because it's easy to use and comes out of the box with kotlin
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - to manage UI data in a lifecycle conscious way, surviving configuration change
- [Navigation Architecture Component](https://developer.android.com/guide/navigation/navigation-getting-started) - for easy navigation, especially with fragments no need to interact with the fragment manager
- [Glide](https://github.com/bumptech/glide) - for image loading and caching with smooth scrolling. It is actively developed and stable.
- [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - to interact easily with views from XML through an auto-generated binding class.
- [LeakCanary](https://square.github.io/leakcanary/getting_started/) - to detecting memory leaks in development
- [Mockk](https://github.com/mockk/mockk) for mocking in tests.
- [Dagger-Hilt](https://dagger.dev/hilt/) for dependency injection
- [Kotlin Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html) for concurrency
- [Espresso](https://developer.android.com/training/testing/espresso) for UI test
- [Turbine](https://github.com/cashapp/turbine) for testing flow
- [Ktlint gradle plugin](https://github.com/JLLeitschuh/ktlint-gradle) for code lint checks
- [Detekt](https://github.com/detekt/detekt) for static code analysis

## Testing

Testing is done with the JUnit4 testing framework, and with Google Truth for making assertions. [Mockk](https://github.com/mockk/mockk) is used to provide mocks in some of the tests. The unit tests run on the CI and the code coverage report is generated by [jacoco](https://github.com/vanniktech/gradle-android-junit-jacoco-plugin) can be tracked [here](https://codecov.io/gh/KryptKode/Carz/). Instrumentation tests are written with [Espresso](https://developer.android.com/training/testing/espresso).

### If I had more time, I should

- Setup UI test CI with Firebase test lab
- Improve Unit test coverage
- Write more UI tests
- Improve UI/UX
