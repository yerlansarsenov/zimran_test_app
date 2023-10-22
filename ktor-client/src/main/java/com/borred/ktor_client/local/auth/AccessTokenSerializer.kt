package com.borred.ktor_client.local.auth

import androidx.datastore.core.Serializer
import com.borred.ktor_client.network.auth.model.AccessToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessTokenSerializer
@Inject constructor() : Serializer<AccessToken?> {

    override val defaultValue: AccessToken? = null

    override suspend fun readFrom(input: InputStream): AccessToken? {
        return try {
            Json.decodeFromString(
                deserializer = AccessToken.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun writeTo(t: AccessToken?, output: OutputStream) {
        withContext(Dispatchers.IO) {
            if (t == null) {
                output.write(byteArrayOf())
                return@withContext
            }

            output.write(
                Json.encodeToString(
                    serializer = AccessToken.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}
