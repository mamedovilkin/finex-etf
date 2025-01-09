package io.github.mamedovilkin.finexetf.view.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.R
import io.github.mamedovilkin.finexetf.databinding.FragmentBlogBinding
import io.github.mamedovilkin.finexetf.model.network.blog.Post
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.isNetworkAvailable
import io.github.mamedovilkin.finexetf.util.show
import io.github.mamedovilkin.finexetf.view.adapter.blog.OnClickListener
import io.github.mamedovilkin.finexetf.view.adapter.blog.PaginationScrollListener
import io.github.mamedovilkin.finexetf.view.adapter.blog.PostRecyclerViewAdapter
import io.github.mamedovilkin.finexetf.viewmodel.BlogViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class BlogFragment : Fragment(), OnClickListener {

    private var _binding: FragmentBlogBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentBlogBinding must not be null")
    private lateinit var viewModel: BlogViewModel
    private lateinit var adapter: PostRecyclerViewAdapter
    private var posts: MutableList<Post> = mutableListOf()
    private var currentPage = 1
    private var totalPages = 1
    private var isLoading = true

    override fun onResume() {
        super.onResume()
        currentPage = 1
        totalPages = 1
        isLoading = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBlogBinding.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity())[BlogViewModel::class]

        if (isNetworkAvailable(binding.root.context)) {
            lifecycleScope.launch {
                viewModel.loadingState.collect {
                    isLoading = it
                    withContext(Dispatchers.Main) {
                        adapter.isLoading = it
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            getPosts(currentPage)

            binding.apply {
                postsRecyclerView.setHasFixedSize(true)
                postsRecyclerView.layoutManager = LinearLayoutManager(context)
                adapter = PostRecyclerViewAdapter(posts)
                adapter.onClickListener = this@BlogFragment
                adapter.isLoading = isLoading
                postsRecyclerView.adapter = adapter
                postsRecyclerView.addOnScrollListener(object :
                    PaginationScrollListener(postsRecyclerView.layoutManager as LinearLayoutManager) {
                    override fun loadMore() {
                        getPosts(currentPage + 1)
                    }

                    override fun isLastPage() = posts.size == totalPages

                    override fun isLoading() = isLoading
                })
                postsRecyclerView.show()
                placeholderLinearLayout.hide()
            }
        } else {
            binding.apply {
                placeholderImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.no_internet_connection, null))
                placeholderTextView.text = resources.getString(R.string.no_internet_connection)
                placeholderLinearLayout.show()
            }
        }

        return binding.root
    }

    private fun getPosts(page: Int) {
        viewModel.getPosts(page).observe(viewLifecycleOwner) {
            posts.addAll(it.posts)
            currentPage = it.meta.pagination.page
            totalPages = it.meta.pagination.total
            adapter.notifyDataSetChanged()
        }
    }

    override fun onPostClickListener(url: String) {
        if (isNetworkAvailable(binding.root.context)) {
            findNavController().navigate(R.id.action_blog_to_post, bundleOf("url" to url))
        } else {
            Toast.makeText(binding.root.context, resources.getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
        }
    }
}