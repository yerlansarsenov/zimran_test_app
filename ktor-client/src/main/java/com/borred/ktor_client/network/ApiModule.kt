package com.borred.ktor_client.network

import com.borred.ktor_client.network.auth.GithubAuthApi
import com.borred.ktor_client.network.auth.GithubAuthApiImpl
import com.borred.ktor_client.network.search.repos.SearchRepositoryApi
import com.borred.ktor_client.network.search.repos.SearchRepositoryApiImpl
import com.borred.ktor_client.network.search.users.SearchUsersApi
import com.borred.ktor_client.network.search.users.SearchUsersApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ApiModule {

    @Provides
    @ViewModelScoped
    internal fun provideGithubAuthApi(
        impl: GithubAuthApiImpl
    ): GithubAuthApi {
        return impl
    }

    @Provides
    @ViewModelScoped
    internal fun provideSearchRepositoryApi(
        impl: SearchRepositoryApiImpl
    ): SearchRepositoryApi {
        return impl
    }

    @Provides
    @ViewModelScoped
    internal fun provideSearchUsersApi(
        impl: SearchUsersApiImpl
    ): SearchUsersApi {
        return impl
    }
}
