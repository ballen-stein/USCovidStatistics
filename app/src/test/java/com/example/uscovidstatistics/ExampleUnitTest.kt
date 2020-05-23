package com.example.uscovidstatistics

import com.example.uscovidstatistics.utils.AppUtils
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun test_numToFormattedString() {
        val num = 1234567890
        val numFormatted = AppUtils().formatNumbers(num)
        assertEquals("1,234,567,890", numFormatted)
    }
    @Test
    fun test_getPercent() {
        val num1 =  10000
        val num2 = 100000
        val percent = AppUtils().getPercent(num1, num2)
        assertEquals("10.0", percent.toString())
    }
    @Test
    fun test_getStringPercent() {
        val num1 =  12300
        val num2 = 100000
        val percent = AppUtils().getStringPercent(num1, num2)
        assertEquals("10.01", percent)
    }
}
