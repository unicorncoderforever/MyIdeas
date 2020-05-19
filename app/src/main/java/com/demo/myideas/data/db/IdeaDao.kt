package com.demo.myideas.data.db

import androidx.room.*
import com.demo.myideas.data.model.Idea
import io.reactivex.Completable
import io.reactivex.Observable


@Dao
 interface IdeaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIdea(idea: Idea): Completable

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insertAllIdea(idea: List<Idea>): Completable


    @Query("DELETE FROM ideas")
    fun deleteAllIdea(): Completable

    @Query("SELECT * FROM ideas ORDER BY avg DESC")
    fun getAllIdeas(): Observable<List<Idea>>

    @Update
    fun updateIdea(vararg idea: Idea) : Completable

    @Delete
    fun deleteIdea(vararg idea: Idea): Completable

}
