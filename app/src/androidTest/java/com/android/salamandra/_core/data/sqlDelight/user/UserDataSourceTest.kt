package com.android.salamandra._core.data.sqlDelight.user

import androidx.test.platform.app.InstrumentationRegistry
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.turbine.test
import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra.SalamandraLocalDB.Companion.Schema
import com.android.salamandra._core.data.adapter.BooleanAdapter
import com.android.salamandra._core.data.adapter.DateAdapter
import com.android.salamandra._core.data.adapter.IntAdapter
import com.android.salamandra._core.data.sqlDelight.workoutTemplate.WorkoutTemplateDataSource
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
import workout.WorkoutTemplateEntity
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class UserDataSourceTest{

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var dataSource: UserDataSource
    private lateinit var db: SalamandraLocalDB

    @Before
    fun setUp() {
        val dateAdapter = DateAdapter()
        val intAdapter = IntAdapter()
        val driver = AndroidSqliteDriver(
            Schema,
            InstrumentationRegistry.getInstrumentation().targetContext,
            "test.db"
        )
        db = SalamandraLocalDB(driver, UserEntity.Adapter(dateAdapter, dateAdapter, intAdapter, intAdapter, intAdapter, intAdapter), WorkoutTemplateEntity.Adapter(
            DateAdapter(), BooleanAdapter()
        ))
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