package com.demo.myideas.data.repository.ideas

import com.demo.myideas.data.model.Idea
import com.demo.myideas.data.network.ApiError
import io.reactivex.disposables.Disposable


interface IdeasRepository {
    fun fetchIdeas(itemPerPage :Int,
        success: (MutableList<Idea>?) -> Unit,
        failure: (ApiError) -> Unit): Disposable

    fun insertIdea(idea: Idea,
                   success: (Idea) -> Unit,
                   failure: (ApiError) -> Unit = {}
                   ): Disposable

    fun updateIdeas(ideas : Idea,
                    success: (Idea) -> Unit,
                    failure: (ApiError) -> Unit = {}): Disposable

    fun deleteIdea(idea: Idea,
                   success: (Idea) -> Unit,
                   failure: (ApiError) -> Unit = {},
                   terminate: () -> Unit = {}): Disposable

    fun logoutUser(success: () -> Unit, failure: (ApiError) -> Unit): Disposable

}