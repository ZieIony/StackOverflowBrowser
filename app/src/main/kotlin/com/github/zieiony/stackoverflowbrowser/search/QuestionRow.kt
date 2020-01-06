package com.github.zieiony.stackoverflowbrowser.search

import android.view.ViewGroup
import carbon.component.DataBindingComponent
import carbon.widget.TextView
import com.github.zieiony.stackoverflowbrowser.R
import com.github.zieiony.stackoverflowbrowser.api.data.Question
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_question.view.*

class QuestionRow(parent: ViewGroup?) : DataBindingComponent<Question>(parent, R.layout.row_question) {
    override fun bind(data: Question) {
        super.bind(data)

        data.is_answered?.let {
            view.questionRow_answers.isSelected = it
        }

        Picasso.get().load(data.owner!!.profile_image).into(view.questionRow_ownerPicture)

        view.questionRow_tags.removeAllViews()
        data.tags?.let {
            for (tag in it) {
                view.questionRow_tags.addView(TextView(view.context, tag), ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            }
        }
    }
}