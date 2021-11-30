package io.tednology

import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * @author Edward Smith
 */
class GreeterTest {
    @Test
    fun testAppHasAGreeting() {
        val classUnderTest = Greeter(greeting = "Hello tests")
        assertNotNull(classUnderTest.greeting, "app should have a greeting")
    }
}