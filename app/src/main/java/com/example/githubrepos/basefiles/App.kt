package com.example.githubrepos.basefiles

import com.example.githubrepos.utils.GithubConstants.Companion.TRENDING_REPO_URL

class App : BaseApplication() {
    override fun getBaseURL(): String {
        return TRENDING_REPO_URL
    }

}