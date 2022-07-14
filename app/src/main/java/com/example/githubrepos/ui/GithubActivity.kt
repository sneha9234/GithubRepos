package com.example.githubrepos.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubrepos.R
import com.example.githubrepos.basefiles.BaseActivity
import com.example.githubrepos.databinding.ActivityGithubBinding
import com.example.githubrepos.model.GithubReposResponse
import com.example.githubrepos.viewmodel.GithubViewModel
import com.example.networkmodule.core.ViewState

class GithubActivity : BaseActivity(), ItemClickListener {
    private var _binding: ActivityGithubBinding? = null
    private val binding get() = _binding!!
    private val githubReposAdapter: GithubReposAdapter by lazy {
        GithubReposAdapter(
            this
        )
    }
    private val viewModel: GithubViewModel by viewModels()
    private val KEY_RECYCLER_STATE = "recycler_state"
    private var mBundleRecyclerViewState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityGithubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        hitApis()
        initObserver()
        initObserver()
        initListener()
    }

    private fun initListener() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            githubReposAdapter.clearData()
            hitApis()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initView() {

        setSupportActionBar(binding.myToolbar)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        binding.recyclerView.adapter = githubReposAdapter
    }

    private fun hitApis() {
        viewModel.getListofTrendingRepos()
    }

    private fun initObserver() {
        viewModel.githubReposResponse.observe(this) { res ->
            when (res) {
                is ViewState.Data -> {
                    res.data.let {
                        githubReposAdapter.setData(it)
                    }
                    dismissLoading()
                }
                is ViewState.Error -> {
                    showError(content = res.error)
                    dismissLoading()
                }
                ViewState.Loading -> {
                    showLoading()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search)?.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    githubReposAdapter.filter.filter(query.toString().trim())
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    githubReposAdapter.filter.filter(newText.toString().trim())
                    return true
                }

            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onItemClicked(position: Int, item: GithubReposResponse) {
        if (position >= 0) {
            if(item.isSelected == true) {
                item.isSelected = false
                githubReposAdapter.updateSelection(position, item)
            }
            else {
                item.isSelected = true
                githubReposAdapter.updateSelection(position, item)
            }
        }
    }
}