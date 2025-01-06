package io.github.mamedovilkin.finexetf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.model.network.Posts
import io.github.mamedovilkin.finexetf.repository.UseCase
import javax.inject.Inject

@HiltViewModel
class BlogViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    val posts: LiveData<Posts> = liveData { useCase.getPosts().body()?.let { emit(it) } }
}