package com.example.githubrepos.data

import com.example.githubrepos.model.GithubReposResponse
import com.example.githubrepos.utils.GithubConstants
import retrofit2.Response
import retrofit2.http.GET

interface GithubApiInterface {
    @GET(GithubConstants.TRENDING_REPO_URL)
    suspend fun getTrendingRepos(): Response<List<GithubReposResponse>>
}