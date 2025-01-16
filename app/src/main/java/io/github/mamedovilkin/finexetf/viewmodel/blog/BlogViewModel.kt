package io.github.mamedovilkin.finexetf.viewmodel.blog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mamedovilkin.network.model.blog.Posts
import io.github.mamedovilkin.finexetf.repository.UseCase
import io.github.mamedovilkin.network.model.blog.Meta
import io.github.mamedovilkin.network.model.blog.Pagination
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
            try {
                val posts = useCase.getPosts(page)
                if (posts.isSuccessful) {
                    posts.body()?.let {
                        emit(it)
                    }
                } else {
                    emit(Posts(emptyList(), Meta(Pagination(0, 0, 0, 0))))
                    return@liveData
                }
                _loadingState.value = false
            } catch (e: Exception) {
                Log.e("BlogViewModel", e.message.toString())
            }
        }
    }
}