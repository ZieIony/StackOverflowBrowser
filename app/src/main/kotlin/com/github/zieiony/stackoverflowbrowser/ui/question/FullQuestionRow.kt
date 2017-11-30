package com.github.zieiony.stackoverflowbrowser.ui.question

import android.text.Html
import android.view.ViewGroup
import carbon.component.DataBindingComponent
import com.github.zieiony.stackoverflowbrowser.R
import com.github.zieiony.stackoverflowbrowser.api.data.Question
import com.github.zieiony.stackoverflowbrowser.widget.TagView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_fullquestion.view.*

class FullQuestionRow(parent: ViewGroup?) : DataBindingComponent<Question>(parent, R.layout.row_fullquestion) {
    override fun bind(data: Question) {
        super.bind(data)

        view.fullQuestionRow_text.text = Html.fromHtml(data.body)

        Picasso.with(view.context).load(data.owner!!.profile_image).into(view.fullQuestionRow_ownerPicture)

        view.fullQuestionRow_tags.removeAllViews()
        data.tags?.let {
            for (tag in it) {
                view.fullQuestionRow_tags.addView(TagView(view.context, tag), ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            }
        }
    }
}