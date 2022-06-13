package com.home.network

import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var map =
            mapOf<String, String>("key" to "free", "appid" to "0", "msg" to "你好呀，我想和你做朋友，可以吗，哈哈")
        val httpApi = OkHttpApi()
        httpApi.get(map, "api.php", object : IHttpCallback {
            override fun onSuccess(data: Any?) {
                LogUtils.d("success: ${data.toString()}")
                runOnUiThread {
                    tv_hello.text = data.toString()
                }
            }

            override fun onFailed(error: Any?) {
                LogUtils.d("failed:${error.toString()}")
            }

        })

        httpApi.post(loginBoy,"",object :IHttpCallback{
            override fun onSuccess(data: Any?) {
                LogUtils.d("success: ${data.toString()}")
                runOnUiThread {
                    tv_hello.text = data.toString()
                }
            }

            override fun onFailed(error: Any?) {
                LogUtils.d("failed:${error.toString()}")
            }
        })
        SystemClock.sleep(200)
        httpApi.cancelRequest(loginBoy)
    }

    val loginBoy=LoginReq()
    val httpApi=OkHttpApi()

    data class LoginReq(
        val mobi: String = "18648957777",
        val password: String = "cn5123456"
    )
}
