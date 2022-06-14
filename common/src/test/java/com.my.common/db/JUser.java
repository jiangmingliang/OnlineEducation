package com.my.common.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2020年09月01日 02:51
 * 签名： 天行健，君子以自强不息；地势坤，君子以厚德载物。
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/  -- 志威 zhiwei.org
 *
 * You never know what you can do until you try !
 * ----------------------------------------------------------------
 */
@Entity(tableName = "tb_juser")
public class JUser {

    public String name = "哈哈哈";
    @PrimaryKey(autoGenerate = true)
    int jId;
    int age = 20;

    private JUser() {
    }

    public JUser(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "JUser{" +
                "jId=" + jId +
                ", age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
