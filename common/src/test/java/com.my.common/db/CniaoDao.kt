package com.cniao5.common.db

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2020年08月29日 08:55
 * 签名： 天行健，君子以自强不息；地势坤，君子以厚德载物。
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/  -- 志威 zhiwei.org
 *
 * You never know what you can do until you try !
 * ----------------------------------------------------------------
 * demo演示的dao接口文件
 */

@Dao
interface UserDao {
    //查询所有数据，若返回liveData则为 LiveData<List<DbUser>>
    @Query(value = "select * from db_user")
    fun getAll(): List<DbUser?>?

    @Query("SELECT * FROM db_user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray?): List<DbUser?>? //根据uid查询

    @Query(
        "SELECT * FROM db_user WHERE uname LIKE :name AND "
                + "age LIKE :age LIMIT 1"
    )
    fun findByName(name: String?, age: Int): DbUser?

    @Query("select * from db_user where uid like :id")
    fun getUserById(id: Int): DbUser?

    @Insert
    fun insertAll(vararg users: DbUser?): List<Long> //支持可变参数,返回的是插入行的rowId

    @Delete
    fun delete(user: DbUser?): Int //删除指定的user

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(user: DbUser?): Int //更新，若出现冲突，则使用替换策略，还有其他策略可选择


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJuser(user: JUser)

    @Query("select * from tb_juser")
    fun queryJuser(): List<JUser>


    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertBook(book: Book)

    @Query("select * from book")
    fun getBook(): LiveData<List<Book>>

    @Query("select * from tempBean")
    fun queryUserBook(): List<TempBean>

}