package com.cniao5.common.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.my.common.db.JUser

/**
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2020年08月29日 08:49
 * 签名： 天行健，君子以自强不息；地势坤，君子以厚德载物。
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/  -- 志威 zhiwei.org
 *
 * You never know what you can do until you try !
 * ----------------------------------------------------------------
 * room数据库的dataBase抽象类
 */
@Database(
    entities = [DbUser::class, JUser::class, Book::class],
    version = 1,
    exportSchema = true,
    views = [TempBean::class]
)
abstract class UserDatabase : RoomDatabase() {
    abstract val userDao: UserDao?

    companion object {
        const val DB_NAME = "user.db"
        private var instance: UserDatabase? = null

        @Synchronized
        fun getInstance(context: Context?): UserDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context!!,
                    UserDatabase::class.java,
                    DB_NAME
                )
                    .allowMainThreadQueries() //默认room不允许在主线程操作数据库，这里设置允许
                    .addMigrations(migration1_2)
                    .build()
            }
            return instance
        }

        /**
         * 数据库的升级 迁移
         */
        val migration1_2 = object : Migration(2, 1) {
            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL()

            }

        }
    }
}