package com.my.project.onlineeducation.ui.home
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.my.project.onlineeducation.R
import org.koin.android.ext.android.get
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier


class HomeFragment : Fragment() {

//    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        homeViewModel =            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        val homeViewModel = getViewModel<HomeViewModel>()
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        //测试
        tv = textView
        testLog()
        return root
    }
    //region 普通使用

    val p: CnPerson by inject<CnPerson>()//lazy 模式
    val p2 = get<CnPerson>()


//    val s  = get<CnStudent> ()
//    val s2  = inject <CnStudent> () // by 代表委托的意思，

    //多构造函数的注入使用
    val ss = get<CnStudent>(named("name"))
    val ss2 = get<CnStudent>(qualifier<CnPerson>())

    //外部参数
    var tv: TextView? = null//别都懒
    val viewInfo by inject<ViewInfo> { parametersOf(tv) }


    //endregion


    private fun testLog() {
        logIt(p)
        p.running()
        logIt(p2)

//        logIt(s)
//        s2.value.study()
//        logIt(s2)

        logIt(ss)
        ss2.study()
        logIt(ss2)

        logIt(viewInfo)
        viewInfo.showId()
        //获取
        val name = getKoin().getProperty("yourName")
        logIt("$name 读取乱码")

    }

    private fun logIt(any: Any) {
        Log.d("HomeFragment", "$any")
    }
}


class CnPerson {
    fun running() {
        Log.i("Person", "running 跑得很快呀！")
    }
}

//多构造函数演示
class CnStudent(val name: String) {

    constructor(p: CnPerson) : this(p.toString())

    fun study() {
        Log.i("Person", "CnStudent ${name}学习很努力！")
    }
}

//外部参数演示
class ViewInfo(val view: View) : KoinComponent {

    val s: CnStudent by inject<CnStudent>(named("name"))
    val p = get<CnPerson>()

    fun showId() {
        Log.d("ViewInfo", "show view的id ${view.id} p $p  s ${s.name}")
    }
}