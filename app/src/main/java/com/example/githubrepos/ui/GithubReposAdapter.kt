package com.example.githubrepos.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.githubrepos.databinding.RepoItemsBinding
import com.example.githubrepos.model.GithubReposResponse
import java.util.*
import kotlin.collections.ArrayList

class GithubReposAdapter(private val listener: ItemClickListener):
    RecyclerView.Adapter<RepoItemsViewHolder>(), Filterable {

    lateinit var binding: RepoItemsBinding

    private var repoFilterList = mutableListOf<GithubReposResponse>()
    private var repo = mutableListOf<GithubReposResponse>()
    var charSearch: String ?= ""

    init {
        repoFilterList = repo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoItemsViewHolder {
        binding = RepoItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        val view = binding.root
        return RepoItemsViewHolder(view, listener, binding)
    }

    override fun onBindViewHolder(holder: RepoItemsViewHolder, position: Int) {
        holder.bind(repoFilterList[position], charSearch)
    }

    override fun getItemCount(): Int {
        return repoFilterList.size
    }

    fun updateSelection(position: Int, gitResponse: GithubReposResponse){
        notifyItemChanged(position, gitResponse)
    }

    fun setData(newData: List<GithubReposResponse>){
        repo.clear()
        repoFilterList.clear()
        repo.addAll(newData)
        notifyDataSetChanged()
    }

    fun clearData(){
        repo.removeAll(mutableListOf())
        repoFilterList.removeAll(mutableListOf())
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                charSearch = constraint.toString()
                repoFilterList = if (charSearch?.isEmpty() == true) {
                    repo
                } else {
                    val resultList = ArrayList<GithubReposResponse>()
                    for (row in repo) {
                        if (charSearch?.lowercase()?.let { row.name?.lowercase()?.contains(it) } == true || charSearch?.lowercase()
                                ?.let { row.author?.lowercase(Locale.ROOT)?.contains(it) } == true) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = repoFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                repoFilterList = results?.values as ArrayList<GithubReposResponse>
                notifyDataSetChanged()
            }

        }
    }
}