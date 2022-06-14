package com.my.project.onlineeducation

import android.app.Application
import com.cniao5.app.di.cnModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

/**
 * Application的声明类
 */
class CnApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //koin的注解使用
        startKoin {
            androidLogger(Level.ERROR)//目前已知bug，除了level.error外，使用androidlogger会导致崩溃
            //context
            androidContext(this@CnApplication)
            //assets 资源数据
            androidFileProperties("ass.file")//默认取值assets下koin.properties文件内的属性配置，可自定义
            //加载需要的module
            modules(cnModules)
        }

        //外部加载 module
        loadKoinModules(otherModuel)
    }
}

val otherModuel = module {

}

class OtherModule() {

}