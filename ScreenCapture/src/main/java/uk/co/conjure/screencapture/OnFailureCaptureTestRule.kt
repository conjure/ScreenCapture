package uk.co.conjure.screencapture

import androidx.test.espresso.Espresso
import androidx.test.espresso.base.DefaultFailureHandler
import androidx.test.platform.app.InstrumentationRegistry

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * A TestRule that will take a screenshot if an Espresso assertion fails.
 *
 * Won't be triggered on crashes or JUnit assert errors!
 */
class OnFailureCaptureTestRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {

                val delegate =
                    DefaultFailureHandler(InstrumentationRegistry.getInstrumentation().targetContext)

                Espresso.setFailureHandler { throwable, matcher ->
                    ScreenCapture.capture(description.displayName ?: "", "ScreenCapture")
                    println("Espresso assertion failed - Screen captured!")
                    delegate.handle(throwable, matcher)
                }

                try {
                    base.evaluate()
                } catch (t: Throwable) {
                    throw t
                } finally {
                    Espresso.setFailureHandler(delegate)
                }
            }
        }
    }
}