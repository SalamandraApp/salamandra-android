package com.android.salamandra._core.data.sqlDelight.workoutTemplate

import com.android.salamandra.SalamandraLocalDB
import com.android.salamandra._core.data.sqlDelight.util.DbInstantiation
import com.android.salamandra._core.domain.error.Result
import com.android.salamandra.util.CoroutineRule
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP
import com.android.salamandra.util.EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WorkoutTemplateElementDataSourceTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    private lateinit var dataSource: WorkoutTemplateElementDataSource
    private lateinit var db: SalamandraLocalDB

    private val wkTemplateElementList = mutableListOf(EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP)

    @Before
    fun setUp() {
        db = DbInstantiation.instantiateTestDB()
        dataSource = WorkoutTemplateElementDataSource(db, testDispatcher)
        for (i in 1..5) {
            wkTemplateElementList.add(
                EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_PUSH_UP.copy(
                    templateElementId = i.toString()
                ),
            )
            wkTemplateElementList.add(EXAMPLE_WORKOUT_TEMPLATE_ELEMENT_SQUAT.copy(templateElementId = (i * 10).toString()))
        }
    }

    @After
    fun clearDatabase() = runTest {
        dataSource.clearDatabase()
    }

    @Test
    fun testInsertAndGetWkTemplateElement() = runTest {
        // Act
        wkTemplateElementList.forEachIndexed { index, element ->
            dataSource.insertWkTemplateElement(index.toString(), element)
        }
        val firstElementCount = dataSource.countElements().toInt()
        dataSource.deleteTemplateElementById("1")
        val secondElementCount = dataSource.countElements().toInt()

        // Assert
        assert(firstElementCount == 11 && secondElementCount == 10)
    }

    @Test
    fun testGettingAllElementsFromWorkout() = runTest {
        // Arrange
        wkTemplateElementList.forEach { element ->
            dataSource.insertWkTemplateElement("1", element)
        }

        // Act
        val elements = dataSource.getWkTemplateElementsByWorkoutTemplateId("1")
        dataSource.clearDatabase()
        val finalCount = dataSource.countElements().toInt()

        // Assert
        assert(elements is Result.Success && elements.data.size == 11 && finalCount == 0)
    }

}