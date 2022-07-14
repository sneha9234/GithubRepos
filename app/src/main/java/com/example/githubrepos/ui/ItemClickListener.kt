package com.example.githubrepos.ui

import com.example.githubrepos.model.GithubReposResponse

interface ItemClickListener {
    fun onItemClicked(position: Int, item: GithubReposResponse)
}