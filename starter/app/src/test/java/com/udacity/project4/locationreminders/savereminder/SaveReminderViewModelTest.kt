package com.udacity.project4.locationreminders

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestCoroutineRule : TestRule{
    public val testCoroutineDispatcher=TestCoroutineDispatcher()
    public val testCoroutineScope = TestCoroutineScope((testCoroutineDispatcher))

    override fun apply(base: Statement, description: Description?)= object: Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            Dispatchers.setMain(testCoroutineDispatcher)
            base.evaluate()
            Dispatchers.resetMain()
            testCoroutineScope.cleanupTestCoroutines()
        }
    }
    fun runBlockingTest(block:suspend TestCoroutineScope.()->Unit)= testCoroutineScope.runBlockingTest{block()}
}