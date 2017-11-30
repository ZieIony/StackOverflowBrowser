package com.github.zieiony.stackoverflowbrowser.ui.question

import android.text.Html
import android.view.ViewGroup
import carbon.component.DataBindingComponent
import com.github.zieiony.stackoverflowbrowser.R
import com.github.zieiony.stackoverflowbrowser.api.data.Answer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_answer.view.*

class AnswerRow(parent: ViewGroup?) : DataBindingComponent<Answer>(parent, R.layout.row_answer) {
    override fun bind(data: Answer) {
        super.bind(data)

        view.answerRow_text.text = Html.fromHtml(data.body)

        Picasso.with(view.context).load(data.owner!!.profile_image).into(view.answerRow_ownerPicture)
    }
}