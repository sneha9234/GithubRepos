package com.example.githubrepos.ui

import android.graphics.Color
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coremodule.utils.debouncedOnClick
import com.example.coremodule.utils.hide
import com.example.coremodule.utils.isValidString
import com.example.coremodule.utils.show
import com.example.githubrepos.R
import com.example.githubrepos.databinding.RepoItemsBinding
import com.example.githubrepos.model.GithubReposResponse


class RepoItemsViewHolder(
    itemView: View,
    private val listener: ItemClickListener,
    binding: RepoItemsBinding
) : RecyclerView.ViewHolder(itemView) {

    private val txtAuthorName = binding.txtAuthorName
    private val llBuiltBy = binding.llBuiltBy
    private val txtDescription = binding.txtDescription
    private val txtLanguage = binding.txtLanguage
    private val txtStars = binding.txtStars
    private val txtForks = binding.txtForks
    private val txtStarsToday = binding.txtStarsToday
    private val clParent = binding.clParent

    fun bind(item: GithubReposResponse?, charSearch: String?) {
        item?.let {

            if (item.isSelected == true) {
                clParent.background = AppCompatResources.getDrawable(
                    itemView.context,
                    R.drawable.selected_item_background
                )
            } else {
                clParent.background = AppCompatResources.getDrawable(
                    itemView.context,
                    R.drawable.plain_border_radius5
                )
            }

            txtAuthorName.text =
                itemView.context.getString(R.string.author_name, item.author, item.name)

            if (isValidString(charSearch)) {
                if (txtAuthorName.text.toString().lowercase().contains(charSearch?.lowercase()!!)) {
                    val startPos: Int =
                        txtAuthorName.text.toString().lowercase().indexOf(charSearch.lowercase())
                    val endPos: Int = startPos + charSearch.length
                    val spanString =
                        Spannable.Factory.getInstance().newSpannable(txtAuthorName.text)
                    spanString.setSpan(
                        ForegroundColorSpan(Color.RED),
                        startPos,
                        endPos,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    txtAuthorName.text = spanString
                }
            }

            llBuiltBy.removeAllViews()
            if (item.builtBy != null) {
                var size = if (item.builtBy.size > 5) {
                    5
                } else {
                    item.builtBy.size
                }
                for (i in 0 until size) {
                    var imgview = ImageView(itemView.context)
                    val lp = LinearLayout.LayoutParams(40, 40)
                    lp.setMargins(10, 0, 0, 0)
                    imgview.layoutParams = lp
                    llBuiltBy.addView(imgview)
                    Glide
                        .with(itemView.context)
                        .load(item.builtBy[i].avatar)
                        .circleCrop()
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .into(imgview)
                }
            }

            txtDescription.text = item.description

            if (item.language != null) {
                txtLanguage.show()
                txtLanguage.text = item.language
                if (item.languageColor != null) {
                    txtLanguage.compoundDrawablesRelative[0]?.setTint(Color.parseColor(item.languageColor))
                }
            } else {
                txtLanguage.hide()
            }

            txtStars.text = item.stars.toString()
            txtForks.text = item.forks.toString()
            txtStarsToday.text =
                itemView.context.getString(R.string.stars_today, item.currentPeriodStars.toString())

            itemView.debouncedOnClick {
                if(item.isSelected == true) {
                    listener.onItemClicked(adapterPosition, item)
                }
                else{
                    listener.onItemClicked(adapterPosition, item)
                }
            }
        }
    }
}