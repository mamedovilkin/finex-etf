package io.github.mamedovilkin.finexetf.view.adapter.blog

import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.PostRecyclerViewItemBinding
import io.github.mamedovilkin.finexetf.model.network.Post
import java.text.SimpleDateFormat
import java.util.Locale

class PostRecyclerViewAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostRecyclerViewAdapter.PostRecyclerViewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostRecyclerViewViewHolder {
        val binding: PostRecyclerViewItemBinding = DataBindingUtil
            .inflate(
                LayoutInflater.from(parent.context),
                R.layout.post_recycler_view_item,
                parent,
                false
            )

        return PostRecyclerViewViewHolder(binding)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: PostRecyclerViewViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    inner class PostRecyclerViewViewHolder(private val binding: PostRecyclerViewItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.apply {
                Glide.with(binding.root.context).load(post.featureImage).fitCenter().into(imageView)

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
}