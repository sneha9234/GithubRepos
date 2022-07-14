package com.example.githubrepos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubrepos.data.GithubRepository
import com.example.githubrepos.model.GithubReposResponse
import com.example.networkmodule.core.ViewState
import kotlinx.coroutines.launch
import com.example.networkmodule.core.Result

class GithubViewModel (application: Application) : AndroidViewModel(application) {

    private val repos by lazy { GithubRepository() }

    private val _githubReposResponse: MutableLiveData<ViewState<List<GithubReposResponse>>> =
        MutableLiveData<ViewState<List<GithubReposResponse>>>()
    val githubReposResponse: LiveData<ViewState<List<GithubReposResponse>>> = _githubReposResponse

    fun getListofTrendingRepos() {
        _githubReposResponse.value = ViewState.Loading
        viewModelScope.launch {
            when(val githubResponse = repos.topRepos()){
                is Result.Error -> {_githubReposResponse.postValue(ViewState.Error(githubResponse.error.message))}
                is Result.Success -> {_githubReposResponse.postValue(ViewState.Data(githubResponse.data))}
            }
        }
    }
}