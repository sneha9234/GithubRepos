package com.example.githubrepos.model

data class GithubReposResponse(
    val author: String? = null,
    val name: String? = null,
    val avatar: String? = null,
    val url: String? = null,
    val description: String? = null,
    val language: String? = null,
    val languageColor: String? = null,
    val stars: Int? = null,
    val forks: Int? = null,
    val currentPeriodStars: Int? = null,
    val builtBy: List<BuiltBy>? = null,
    var isSelected: Boolean? = false
)

data class BuiltBy(
    val username: String? = null,
    val href: String? = null,
    val avatar: String? = null
)