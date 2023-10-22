package com.borred.ktor_client.local.auth

import android.content.Context
import androidx.datastore.dataStore
import com.borred.ktor_client.network.auth.model.AccessToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessTokenDataStore
@Inject
constructor(
    @ApplicationContext private val applicationContext: Context,
    accessTokenSerializer: AccessTokenSerializer
) {

    private val Context.accessTokenDataStore by dataStore(
        fileName = ACCESS_TOKEN_DATA_STORE,
        serializer = accessTokenSerializer
    )

    fun getAccessTokenFlow(): Flow<AccessToken?> =
        applicationContext.accessTokenDataStore.data

    suspend fun setAccessToken(accessToken: AccessToken?) {
        applicationContext.accessTokenDataStore.updateData {
            accessToken
        }
    }

    companion object {
        private const val ACCESS_TOKEN_DATA_STORE = "access_token_data_store.json"
    }
}
