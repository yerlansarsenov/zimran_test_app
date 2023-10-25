package com.borred.ktor_client.network.search.repos

import androidx.datastore.core.Serializer
import com.borred.ktor_client.network.search.repos.model.GitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoSearchHistorySerializer @Inject constructor() : Serializer<List<GitRepository>> {

    override val defaultValue: List<GitRepository> = emptyList()

    override suspend fun readFrom(input: InputStream): List<GitRepository> {
        return try {
            Json.decodeFromString(
                deserializer = ListSerializer(GitRepository.serializer()),
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun writeTo(t: List<GitRepository>, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = ListSerializer(GitRepository.serializer()),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}
