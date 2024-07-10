package com.android.salamandra.home.data

import com.android.salamandra._core.data.network.SalamandraApiService
import com.android.salamandra._core.domain.LocalDbRepository
import com.android.salamandra._core.domain.model.workout.WorkoutPreview
import com.android.salamandra.home.domain.Repository
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(
    private val localDb: LocalDbRepository,
) : Repository {
    override fun getWkPreviews(): Flow<List<WorkoutPreview>> = localDb.getAllWkPreviews()
}