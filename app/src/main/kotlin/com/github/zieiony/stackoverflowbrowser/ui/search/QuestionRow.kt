package com.github.zieiony.stackoverflowbrowser.ui.search

import android.view.ViewGroup
import carbon.component.DataBindingComponent
import com.github.zieiony.stackoverflowbrowser.R
import com.github.zieiony.stackoverflowbrowser.api.data.Question
import com.github.zieiony.stackoverflowbrowser.widget.TagView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_question.view.*

class QuestionRow(parent: ViewGroup?) : DataBindingComponent<Question>(parent, R.layout.row_question) {
    override fun bind(data: Question) {
        super.bind(data)

        data.is_answered?.let {
            view.questionRow_answers.isSelected = it
        }

        Picasso.with(view.context).load(data.owner!!.profile_image).into(view.questionRow_ownerPicture)

        view.questionRow_tags.removeAllViews()
        data.tags?.let {
            for (tag in it) {
                view.questionRow_tags.addView(TagView(view.context, tag), ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            }
        }
    }
}