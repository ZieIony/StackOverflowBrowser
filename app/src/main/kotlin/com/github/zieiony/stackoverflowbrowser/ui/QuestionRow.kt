package com.github.zieiony.stackoverflowbrowser.ui

import android.view.ViewGroup
import carbon.component.DataBindingComponent
import carbon.widget.Chip
import com.github.zieiony.stackoverflowbrowser.R
import com.github.zieiony.stackoverflowbrowser.api.data.Question
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_question.view.*

class QuestionRow(parent: ViewGroup?) : DataBindingComponent<Question>(parent, R.layout.row_question) {
    override fun bind(data: Question?) {
        super.bind(data)

        Picasso.with(view.context).load(data!!.owner!!.profile_image).into(view.questionRow_ownerPicture)

        view.questionRow_tags.removeAllViews()
        data.tags?.let {
            for (tag in it) {
                val chip = Chip(view.context)
                chip.text = tag
                chip.isRemovable = false
                view.questionRow_tags.addView(chip, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            }
        }
    }
}