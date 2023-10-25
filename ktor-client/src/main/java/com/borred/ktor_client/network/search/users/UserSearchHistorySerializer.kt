package com.borred.ktor_client.network.search.users

import androidx.datastore.core.Serializer
import com.borred.ktor_client.network.search.users.model.GitUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSearchHistorySerializer @Inject constructor() : Serializer<List<GitUser>> {

    override val defaultValue: List<GitUser> = emptyList()

    override suspend fun readFrom(input: InputStream): List<GitUser> {
        return try {
            Json.decodeFromString(
                deserializer = ListSerializer(GitUser.serializer()),
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun writeTo(t: List<GitUser>, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = ListSerializer(GitUser.serializer()),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}
