package com.hellokhdev.sovary.rustv.main




import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.hellokhdev.sovary.rustv.tvprogram.tvprogramm
import com.hellokhdev.sovary.rustv.util.ScreenUtil.screenHeight
import com.hellokhdev.sovary.rustv.util.ScreenUtil.screenWidth
import com.hellokhdev.sovary.rustv.watch.WatchActivity
import com.hellokhdev.sovary.rustv.watch.WatchMP4Activity
import com.newdev.beta.rustv.R
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.InitializationListener
import com.yandex.mobile.ads.common.MobileAds
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {
    private val YANDEX_MOBILE_ADS_TAG = "YandexMobileAds"
    lateinit var mBannerAdView : BannerAdView
    private var bannerWidth = 0
    private var bannerHeight = 0
    val AdUnitId = "R-M-2278524-1" //"R-M-2278524-1"      demo-banner-yandex

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FullScreencall()
        MobileAds.initialize(this,
            InitializationListener { Log.d(YANDEX_MOBILE_ADS_TAG, "SDK initialized") })

        mBannerAdView = findViewById (R.id.banner_ad_view);
        mBannerAdView.setAdUnitId(AdUnitId)
        bannerWidth = screenWidth
        bannerHeight = screenHeight / 5
        mBannerAdView.setAdSize(BannerAdSize.stickySize(this,bannerWidth))
        mBannerAdView.setBannerAdEventListener()

        val adViewLayoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        adViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        mBannerAdView.layoutParams = adViewLayoutParams

        val adRequest: AdRequest = AdRequest.Builder().build()
        mBannerAdView.loadAd(adRequest)

        val tv_prog = findViewById<Button>(R.id.tv_prog)
        tv_prog.setOnClickListener {
            val intent = Intent(this, tvprogramm::class.java)
            startActivity(intent)
        }

        val tv_exit = findViewById<Button>(R.id.bt_exit)
        tv_exit.setOnClickListener {
            finishAffinity()
        }

        val btn = findViewById<Button>(R.id.bt_watch)
        btn.setOnClickListener {
            val intent = Intent(this, WatchActivity::class.java)
            startActivity(intent)
        }

        val btn_mult = findViewById<Button>(R.id.bt_watch_mult)
        btn_mult.setOnClickListener {
            val intent = Intent(this, WatchMP4Activity::class.java)
            startActivity(intent)
        }


    }

    fun FullScreencall() {
        getSupportActionBar()?.hide()
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            val v = this.window.decorView
            v.systemUiVisibility = View.GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            val decorView = window.decorView
            val uiOptions =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            decorView.systemUiVisibility = uiOptions
        }
    }

    fun pause() = runBlocking {
        delay(15000L)
        exit()
    }

    override fun onResume() {
        super.onResume()
        FullScreencall()
    }
    fun exit(){
        finishAffinity()
    }
    private fun BannerAdView.setBannerAdEventListener() {

    }

}



