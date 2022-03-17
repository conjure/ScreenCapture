# Espresso ScreenCapture

Take screenshots during Espresso tests and save them to the devices media folder.

It is great for developers as it works locally and without complicated setup. If you are looking for a solution to automate taking screenshots as part of your CI I recommend fastlane screenshots https://docs.fastlane.tools/getting-started/android/screenshots/

## Add Dependency

In the root build.gradle add `jitpack.io` to the repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Add the dependency 
```
dependencies {
        implementation 'uk.co.conjure:screencapture:<version>'
}
```


## Take a screenshot

1. Enable ScreenCapture before taking screenshots
```
    @Before
    fun init() {
        ScreenCapture.screenCaptureEnabled = true
    }
```
2. Take a screenshots by calling
```
	ScreenCapture.capture()
```

## Take a screenshot when an espresso rule fails

A TestRule is available to take a screenshot when an Espresso assertion fails.
Simply add the rule to your test class:
```
    @get:Rule
    val onFailureCaptureTestRule = OnFailureCaptureTestRule()
```

Please note that it ***DOES NOT*** take a screenshot when an Exception is thrown or a JUnit assertion fails. The screenshot works only for Espresso tests.
E.g. `onView(withText("DOES NOT EXIST")).check(matches(isDisplayed()))`
