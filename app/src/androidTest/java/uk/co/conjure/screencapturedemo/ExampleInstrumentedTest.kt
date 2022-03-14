package uk.co.conjure.screencapturedemo


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.co.conjure.screencapture.OnFailureCaptureTestRule
import uk.co.conjure.screencapture.ScreenCapture

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val onFailureCaptureTestRule = OnFailureCaptureTestRule()

    @Before
    fun init() {
        ScreenCapture.screenCaptureEnabled = true
    }

    @Test
    fun testScreenShot() {
        onView(withText("Hello World!")).check(matches(isDisplayed()))
        ScreenCapture.capture()
        ScreenCapture.capture()
    }

    @Test
    fun testScreenShotOnAssertionFailure() {
        onView(withText("DOES NOT EXIST")).check(matches(isDisplayed()))
    }
}