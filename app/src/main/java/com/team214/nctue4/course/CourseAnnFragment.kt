package com.team214.nctue4.course

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.team214.nctue4.R
import com.team214.nctue4.ann.AnnActivity
import com.team214.nctue4.client.E3Client
import com.team214.nctue4.model.AnnItem
import com.team214.nctue4.model.CourseItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_course_ann.*
import kotlinx.android.synthetic.main.status_empty.*
import kotlinx.android.synthetic.main.status_error.*

class CourseAnnFragment : Fragment() {
    lateinit var client: E3Client
    lateinit var courseItem: CourseItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_course_ann, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        courseItem = (activity as CourseActivity).courseItem
        client = (activity as CourseActivity).client
        getData()
    }

    private fun getData() {
        error_request.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
        client.getCourseAnn(courseItem)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    displayData(it)
                    progress_bar.visibility = View.GONE
                },
                onError = {
                    error_request.visibility = View.VISIBLE
                    error_request_retry.setOnClickListener { getData() }
                    progress_bar.visibility = View.GONE
                }
            )
    }

    private fun displayData(annItems: MutableList<AnnItem>) {
        if (annItems.isEmpty()) {
            empty_request.visibility = View.VISIBLE
            return
        }
        announcement_course_recycler_view.layoutManager = LinearLayoutManager(context)
        announcement_course_recycler_view.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        announcement_course_recycler_view.adapter = CourseAnnAdapter(annItems) {
            val intent = Intent()
            intent.setClass(activity!!, AnnActivity::class.java)
            intent.putExtra("annItem", it)
            startActivity(intent)
        }
        announcement_course_recycler_view.visibility = View.VISIBLE
    }
}