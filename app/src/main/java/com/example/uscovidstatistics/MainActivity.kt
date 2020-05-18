package com.example.uscovidstatistics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.network.NetworkClient
import com.example.uscovidstatistics.network.NetworkObserver
import com.example.uscovidstatistics.utils.AppUtils
import es.dmoral.toasty.Toasty
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.reactivestreams.Subscriber
import java.lang.Exception
import java.lang.reflect.Executable

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivityTag"
    private lateinit var response: Response

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppUtils().checkPermissions(this)

        Thread(Runnable {
            NetworkObserver(false, "", this).createNewNetworkRequest()
            //println("${AppConstants.RESPONSE_DATA} new resp")
        }).start()

        /*Observable.defer {
            try {
                Observable.just(AppConstants.RESPONSE_DATA)
            } catch (e: Exception) {
                Observable.error<Exception>(e)
            }.subscribeOn(Schedulers.io())
                .subscribe(Subscriber<String>)
                /*.subscribe(
                    { onNext -> println("${AppConstants.RESPONSE_DATA} \nNew Resp Logged")},
                    { onError -> println(onError.toString() + " onError area")},
                    { }
                )*/
        }*/

        //Observable.just(AppConstants.RESPONSE_DATA).subscribe({ onNext -> println(onNext)})

        /*
        Observable.defer {
            try {
                val apiResponse = NetworkObserver(false, "").getAllData()
                Observable.just(apiResponse)
            } catch (e: Exception) {
                Observable.error<Exception>(e)
            }
        }.subscribeOn(Schedulers.io())
            .subscribe(
                { onNext -> response = onNext as Response},
                { onError -> println(onError.toString() + " onError area")},
                { AppConstants.RESPONSE_DATA = response.body!!
                    println("New Response : ${AppConstants.RESPONSE_DATA.string()}")}
            )

         */
    }

    fun printDataSet() {
        println("${AppConstants.RESPONSE_DATA.string()} \nNew Resp Logged")
    }
}
