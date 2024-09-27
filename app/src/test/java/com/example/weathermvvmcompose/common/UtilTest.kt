package com.example.weathermvvmcompose.common

import org.junit.Assert
import org.junit.Test

class UtilTest {

    @Test
    fun roundDigit() {
        val res = Util.roundDigit(3.14, 1)
        Assert.assertTrue(3.1 == res)
    }

    @Test
    fun buildRetrofit() {
        var res = Util.buildRetrofit("https://google.com")
        Assert.assertTrue(res.baseUrl().isHttps)

        res = Util.buildRetrofit("http://google.com")
        Assert.assertTrue(!res.baseUrl().isHttps)
    }
}