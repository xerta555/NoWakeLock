package com.js.nowakelock.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.js.nowakelock.LiveDataTestUtil
import com.js.nowakelock.data.TestData
import com.js.nowakelock.data.db.AppDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AppInfoRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var aR: AppInfoRepository
    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        aR = AppInfoRepository(db)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getAppLists() {
        val appInfos = aR.getAppLists()
        assertTrue(LiveDataTestUtil.getValue(appInfos).isEmpty())
        runBlocking { db.appInfoDao().insertAll(TestData.appInfos) }

        assertEquals(LiveDataTestUtil.getValue(appInfos).size, 10)
    }

    @Test
    fun sync() {
        val appInfos = aR.getAppLists()
        assertTrue(LiveDataTestUtil.getValue(appInfos).isEmpty())
        runBlocking { aR.syncAppInfos() }
        assertTrue(LiveDataTestUtil.getValue(appInfos).isNotEmpty())
    }
}