package io.github.mamedovilkin.finexetf.view.adapter.blog

import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.PostRecyclerViewItemBinding
import io.github.mamedovilkin.finexetf.databinding.PostsRecyclerViewFooterBinding
import io.github.mamedovilkin.finexetf.di.GlideApp
import io.github.mamedovilkin.network.model.blog.Post
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.show
import java.text.SimpleDateFormat
import java.util.Locale

class PostRecyclerViewAdapter(private val posts: List<Post>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val LIST: Int = 0
    private val FOOTER: Int = 1
    var onClickListener: OnClickListener? = null
    var isLoading: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == LIST) {
            val binding: PostRecyclerViewItemBinding = DataBindingUtil
                .inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.post_recycler_view_item,
                    parent,
                    false
                )

            return PostRecyclerViewViewHolder(binding)
        } else {
            val binding: PostsRecyclerViewFooterBinding = DataBindingUtil
                .inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.posts_recycler_view_footer,
                    parent,
                    false
                )

            return PostsFooterRecyclerViewViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == posts.size) {
            FOOTER
        } else {
            LIST
        }
    }

    override fun getItemCount(): Int {
        return posts.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PostRecyclerViewViewHolder) {
            val post = posts[position]
            holder.bind(post)
            holder.itemView.setOnClickListener {
                onClickListener?.onPostClickListener(post.url)
            }
        } else if (holder is PostsFooterRecyclerViewViewHolder) {
            holder.bind()
        }
    }

    inner class PostRecyclerViewViewHolder(private val binding: PostRecyclerViewItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.apply {
                if (post.featureImage == null) {
                    constraintLayout.hide()
                } else {
                    constraintLayout.show()
                    GlideApp.with(root.context).load(post.featureImage)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(imageView)
                }

                titleTextView.text = post.title
                excerptTextView.text = post.excerpt

                val timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ROOT).parse(post.publishedAt)?.time
                val publishedAt = DateUtils.getRelativeTimeSpanString(timestamp ?: 0) as String
                publishedAtTextView.text = publishedAt

                shareImageButton.setOnClickListener {
                    val sendIntent = Intent()
                    sendIntent.setAction(Intent.ACTION_SEND)
                    sendIntent.putExtra(Intent.EXTRA_TEXT, post.url)
                    sendIntent.setType("text/plain")

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    root.context.startActivity(shareIntent)
                }
            }
        }
    }

    inner class PostsFooterRecyclerViewViewHolder(private val binding: PostsRecyclerViewFooterBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.apply {
                if (isLoading) {
                    progressBar.show()
                } else {
                    progressBar.hide()
                }
            }
        }
    }
}