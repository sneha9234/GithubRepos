package com.example.githubrepos.data

import com.example.githubrepos.model.GithubReposResponse
import com.example.networkmodule.core.RemoteSource
import com.example.networkmodule.core.Result
import com.example.networkmodule.wrapper.NetworkController

class GithubRepository {
    private val githubApiInterface: GithubApiInterface by lazy {
        NetworkController.getAPIClient().getApiService(
            GithubApiInterface::class.java
        ) as GithubApiInterface
    }

    suspend fun topRepos(): Result<List<GithubReposResponse>> {
        return RemoteSource.safeApiCall { githubApiInterface.getTrendingRepos() }
    }
}