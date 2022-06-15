package com.cniao5.common.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.my.common.db.JUser

/**
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