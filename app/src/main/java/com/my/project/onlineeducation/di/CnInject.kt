package com.cniao5.app.di

import android.view.View
import com.my.project.onlineeducation.ui.dashboard.DashboardViewModel
import com.my.project.onlineeducation.ui.home.CnPerson
import com.my.project.onlineeducation.ui.home.CnStudent

import com.my.project.onlineeducation.ui.home.HomeViewModel
import com.my.project.onlineeducation.ui.home.ViewInfo

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.TypeQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * 使用koin依赖注入框架需要的必要声明
 */

val cnModules = module {

    //fixme 不要空声明，会报错
//    //单例模式
//    single { }
    single(createdAtStart = false) { CnPerson() }/* bind DashboardFragment::class*/
    //bind的意思就是，可以通过get<DashboardFragment>（）
//    //工厂模式，就是将创建过程眼不见为净

    //覆盖声明
    factory(override = true) { CnPerson() }

    //多构造函数
    factory(named("name")) { CnStudent("haha") }
    factory(TypeQualifier(CnPerson::class)) { CnStudent(get<CnPerson>()) }

    //接收外部参数的形式
    factory { (view: View) ->
        ViewInfo(view)
    }

    //viewModel
    viewModel { DashboardViewModel() }
    viewModel { HomeViewModel() }
//    viewModel { NotificationsViewModel() }


//    fragment { HomeFragment() }

}