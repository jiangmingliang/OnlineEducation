package com.cniao5.common.db

import androidx.room.*
import androidx.room.ForeignKey.SET_DEFAULT

/**
 * 临时演示的demo的entity
 */
//room数据库的注解标记,数据表entity  (tableName="db_user",indices = {@Index(value = "uname",unique = true)})
@Entity(
    tableName = "db_user", foreignKeys = [ForeignKey(
        entity = Book::class,
        parentColumns = ["bid"], childColumns = ["bookId"], onDelete = SET_DEFAULT
    )], indices = [Index("uid"), Index("bookId")]
)
class DbUser {
    //注意这里 标记了，表中的uid自增，而我们创建的时候，都没赋值
    @PrimaryKey(autoGenerate = true)
    var uid = 0

    @ColumnInfo(name = "uname")
    var name: String? = null

    var city: String? = null

    var age = 0

    //如此数据表中不会有@Ignore标记的属性字段，所以查询出来的都是没有值的，也就是默认false
    @Ignore
    var isSingle = false

    @Embedded
    var baby: Child? = null//孩子

    var bookId: Int = 0

    override fun toString(): String {
        return "DbUser(uid=$uid, name=$name, city=$city, age=$age, isSingle=$isSingle, baby=$baby)"
    }


}

data class Child(
    val cid: Int,
    val cname: String,
    val cAge: Int,
    val sex: Int
)

@Entity
data class Book(
    @PrimaryKey
    val bid: Int,
    val name: String,
    val price: Double
)

/**
 * 这个注解，表明该数据类是sql的执行结果数据，可用于其他的dao操作,用于class较为合适，而不是data class
 * You can SELECT FROM a DatabaseView similar to an Entity, but you can not INSERT, DELETE or UPDATE into a DatabaseView.
 */
@DatabaseView("select uname,name from db_user,book where uid=3 or bookId=3", viewName = "tempBean")
class TempBean {
    var uname = ""
    var name = ""

    override fun toString(): String {
        return "TempBean(uname='$uname', name='$name')"
    }

}