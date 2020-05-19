package com.demo.myideas.ui.ideas.IdeaCalculator

import com.demo.myideas.data.model.Idea

class IdeaCalculator(var idea : Idea,val isUpdate: Boolean){

    fun setNewData(impact: Int, ease: Int, confidence : Int){
       idea.impact = impact
       idea.ease = ease
       idea.confidence = confidence
       idea.avg = listOf(impact,ease,confidence).average().toFloat()
    }
    fun getAvg():Float{
        return idea.avg ?: 0.0f
    }

    fun setContent(content : String){
        idea.content = content
    }
}