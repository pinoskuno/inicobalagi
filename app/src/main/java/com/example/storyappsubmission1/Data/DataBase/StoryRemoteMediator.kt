package com.example.storyappsubmission1.Data.DataBase

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.storyapp.Database.StoryDatabase
import com.example.storyapp.api.ApiService
import com.example.storyapp.datastore.UserPreference
import com.example.storyapp.response.ListStoryItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import androidx.room.withTransaction
import com.example.storyapp.Database.RemoteKeys
import com.example.storyappsubmission1.Data.API.Service
import com.example.storyappsubmission1.Data.Functon.Preference
import com.example.storyappsubmission1.Data.Response.ListStoryR

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val _service: Service,
    private val context: Context
): RemoteMediator<Int, ListStoryR>() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preference")

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryR>
    ): MediatorResult {
        val pref = context.dataStore
        val dataPreference = Preference.getInstance(pref)
        val tokenAuth = runBlocking { dataPreference.getToken().first() }

        val page = when(loadType){
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = _service.getAllStory("Bearer $tokenAuth", page, state.config.pageSize)
            val listStory = responseData.listStory

            val endOfPaginationReached = listStory.isEmpty()

            database.withTransaction{
                if (loadType == LoadType.REFRESH){
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.storyDao().deleteAll()
                }
                val prevKey = if(page == 1) null else page - 1
                val nextkey = if(endOfPaginationReached) null else page + 1
                val keys = listStory.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextkey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.storyDao().insertStory(listStory)
            }

            return MediatorResult.Success(endOfPaginationReached)
        }catch (exception: Exception){
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ListStoryItem>): RemoteKeys?{
        return state.pages.lastOrNull{it.data.isNotEmpty()}?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.pages.firstOrNull{it.data.isNotEmpty()}?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    companion object{
        const val INITIAL_PAGE_INDEX = 1
    }
}