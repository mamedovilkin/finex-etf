package io.github.mamedovilkin.finexetf.view.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.finexetf.databinding.FragmentBlogBinding
import io.github.mamedovilkin.finexetf.util.hide
import io.github.mamedovilkin.finexetf.util.show
import io.github.mamedovilkin.finexetf.view.adapter.blog.PostRecyclerViewAdapter
import io.github.mamedovilkin.finexetf.viewmodel.BlogViewModel

@AndroidEntryPoint
class BlogFragment : Fragment() {

    private var _binding: FragmentBlogBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentBlogBinding must not be null")
    private lateinit var viewModel: BlogViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBlogBinding.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity())[BlogViewModel::class]

        viewModel.posts.observe(viewLifecycleOwner) {
            binding.apply {
                postsRecyclerView.setHasFixedSize(true)
                postsRecyclerView.layoutManager = LinearLayoutManager(context)
                postsRecyclerView.adapter = PostRecyclerViewAdapter(it.posts)
                postsRecyclerView.show()
                progressBar.hide()
            }
        }

        return binding.root
    }
}