package com.android.salamandra._core.data.sqlDelight.user

import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra._core.data.sqlDelight.util.DbInstantiation
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.util.EXAMPLE_USER
import com.android.salamandra.util.EXAMPLE_USER_ENTITY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserDataSourceTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var userDataSource: UserDataSource
    private lateinit var db: SalamandraLocalDB

    @Before
    fun setUp() {
        db = DbInstantiation.instantiateTestDB()
        userDataSource = UserDataSource(db, testDispatcher)
    }

    @After
    fun clearDatabase() = runTest{
        userDataSource.clearDatabase()
    }


    @Test
    fun testBasicInsertionAndGet() = runTest {
        // Arrange
        val expectedUserEntity = EXAMPLE_USER_ENTITY

        // Act
        userDataSource.insertUser(EXAMPLE_USER)
        runCurrent()
        val wk = userDataSource.getUserByID(EXAMPLE_USER.uid)
        runCurrent()

        // Assert
        assert(wk is Result.Success && wk.data == expectedUserEntity)
//        userDataSource.clearDatabase()
    }
}
