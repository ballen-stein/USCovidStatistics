package com.example.uscovidstatistics

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.uscovidstatistics.utils.AppUtils

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.util.*
import kotlin.collections.HashSet

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.uscovidstatistics", appContext.packageName)
    }
    @Test
    fun testArrayDifferences() {
        val temp1 = arrayOf(5,3,4)
        val temp2 = arrayOf(4,3,10,6)

        val set1 = HashSet<Int>()
        Collections.addAll(set1, *temp1)

        val set2 = HashSet<Int>()
        Collections.addAll(set2, *temp2)

        var answer1 = "\nNumbers in array 1 that aren't in array 2:\n"


        for ((i, value) in set1.withIndex()) {
            if (value != temp2[i]) {
                answer1 += "$value\n"
            }
        }

    }
    @Test
    fun testTerritoriesChecker() {
        val context = InstrumentationRegistry.getInstrumentation().context
    }
}
