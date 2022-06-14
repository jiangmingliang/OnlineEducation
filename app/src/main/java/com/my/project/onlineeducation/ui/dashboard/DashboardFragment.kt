package com.my.project.onlineeducation.ui.dashboard

import android.app.Service
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.my.project.onlineeducation.R
import com.my.project.onlineeducation.ui.home.CnPerson
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel


class DashboardFragment : Fragment() {

    private val dashboardViewModel: DashboardViewModel by viewModel()
//    val vm by viewModels<DashboardViewModel> { defaultViewModelProviderFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })


        val aas = AAServcie()
        aas.showAA()


        return root
    }

}

class AAServcie : Service() {

    val aa = get<CnPerson>()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    fun showAA() = aa.running()

}

class AAcp : ContentProvider() {

    val aa = get<CnPerson>()

    fun showAA() = aa.running()
    override fun onCreate(): Boolean {
        TODO("Not yet implemented")
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        TODO("Not yet implemented")
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

}