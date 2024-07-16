package com.android.salamandra._core.data.sqlDelight.user

import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra._core.data.sqlDelight.util.DbInstantiation
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.util.EXAMPLE_USER
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import user.UserEntity

@OptIn(ExperimentalCoroutinesApi::class)
class ExerciseDataSourceTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var dataSource: UserDataSource
    private lateinit var db: SalamandraLocalDB

    @Before
    fun setUp() {
        db = DbInstantiation.instantiateTestDB()
        dataSource = UserDataSource(db, testDispatcher)
    }

    private val expectedUserEntity = UserEntity(
        id = EXAMPLE_USER.uid,
        username = EXAMPLE_USER.username,
        displayName = EXAMPLE_USER.displayName,
        dateJoined = EXAMPLE_USER.dateJoined,
        dateOfBirth = EXAMPLE_USER.dateOfBirth,
        height = EXAMPLE_USER.height,
        weight = EXAMPLE_USER.weight,
        gender = EXAMPLE_USER.gender?.ordinal,
        fitnessLevel = EXAMPLE_USER.fitnessLevel?.ordinal,
        fitnessGoal = EXAMPLE_USER.fitnessGoal?.ordinal
    )

    @Test
    fun testBasicInsertionAndGet() = runTest {
        // Act
        val insertion = dataSource.insertUser(EXAMPLE_USER)
        runCurrent()
        val wk = dataSource.getUserByID(EXAMPLE_USER.uid)
        runCurrent()

        // Assert
        assert(wk is Result.Success && wk.data == expectedUserEntity)
        dataSource.clearDatabase()
    }
}