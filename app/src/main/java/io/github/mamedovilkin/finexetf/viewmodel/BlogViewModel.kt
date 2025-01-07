package io.github.mamedovilkin.finexetf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.finexetf.model.network.blog.Posts
import io.github.mamedovilkin.finexetf.repository.UseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BlogViewModel @Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow(true)
    val loadingState: StateFlow<Boolean>
        get() = _loadingState

    fun getPosts(page: Int): LiveData<Posts> {
        return liveData {
            _loadingState.value = true
            useCase.getPosts(page).body()?.let {
                emit(it)
            }
            _loadingState.value = false
        }
    }
}