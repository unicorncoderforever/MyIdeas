package com.demo.myideas.ui.ideas

import androidx.lifecycle.MutableLiveData
import com.demo.myideas.data.model.Idea
import com.demo.myideas.data.network.ApiError
import com.demo.myideas.data.repository.ideas.IdeasRepository
import com.demo.myideas.ui.base.BaseViewModel
import javax.inject.Inject

class IdeasViewModel @Inject constructor(private val ideaRepository: IdeasRepository) :
    BaseViewModel() {
    val ideasList: MutableLiveData<MutableList<Idea>> = MutableLiveData<MutableList<Idea>>().apply {
        this.value = ArrayList()
    }

    val TAG = "IdeasViewModel";

    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }


    val error: MutableLiveData<ApiError> by lazy { MutableLiveData<ApiError>() }
    fun fetchIdeas(itemPerPage: Int, success: () -> Unit) {
        ideaRepository.fetchIdeas(itemPerPage,
            { ideas ->
                ideasList.value?.clear()
                ideasList.value?.addAll(ideas as MutableList)
                ideasList.notifyObserver()
            },
            {
                error.value = it
            }
        )
    }

    fun insertIdea(
        idea: Idea?, success: (Idea) -> Unit,
        failure: (ApiError) -> Unit = {}
    ) {
        if (idea != null) {
            ideaRepository.insertIdea(idea, { ideas ->
                ideasList.value?.add(0, ideas)
                success(idea)
                ideasList.notifyObserver()
            }, { failure(it) })
        }
    }

    fun deleteIdea(
        idea: Idea?, success: (Idea) -> Unit,
        failure: (ApiError) -> Unit
    ) {
        if (idea != null) {
            ideaRepository.deleteIdea(idea, { ideas ->
                ideasList.value?.remove(idea)
                ideasList.notifyObserver()
                success(idea)
            },
                {
                    failure(it)
                })
        }
    }

    fun updateIdea(
        idea: Idea?, success: (Idea) -> Unit,
        failure: (ApiError) -> Unit = {},
        terminate: () -> Unit = {}
    ) {

        if (idea != null) {
            ideaRepository.updateIdeas(idea, { ideas ->
                ideasList.value?.remove(idea)
                ideasList.notifyObserver()
                success(idea)
            },
                {
                    failure(it)
                }
            )
        }
    }


    fun logoutUser(success: () -> Unit, failure: (ApiError) -> Unit) {
        ideaRepository.logoutUser(success, failure)
    }


}