package com.hellokhdev.sovary.rustv.watch

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.hellokhdev.sovary.rustv.main.MainActivity
import com.newdev.beta.rustv.R

class WatchMP4Activity : AppCompatActivity(){

    companion object{
        var isFullScreen = false
        var isLock = false
    }
    lateinit var handler: Handler
    lateinit var simpleExoPlayer: SimpleExoPlayer
    lateinit var bt_fullscreen: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_mult)
        FullScreencall()
        handler = Handler(Looper.getMainLooper())
        val playerView = findViewById<PlayerView>(R.id.player)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        bt_fullscreen =  findViewById<ImageView>(R.id.bt_fullscreen)
        val bt_lockscreen = findViewById<ImageView>(R.id.exo_lock)

        val bt_back = findViewById<ImageView>(R.id.exo_return)
        bt_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        val bt_1 = findViewById<TextView>(R.id.exo_1)
        val bt_2 = findViewById<TextView>(R.id.exo_2)
        val bt_3 = findViewById<TextView>(R.id.exo_3)
        val bt_4 = findViewById<TextView>(R.id.exo_4)
        val bt_5 = findViewById<TextView>(R.id.exo_5)
        val bt_6 = findViewById<TextView>(R.id.exo_6)
        val bt_7 = findViewById<TextView>(R.id.exo_7)
        val bt_8 = findViewById<TextView>(R.id.exo_8)
        val bt_9 = findViewById<TextView>(R.id.exo_9)
        val bt_10 = findViewById<TextView>(R.id.exo_10)
        val bt_11 = findViewById<TextView>(R.id.exo_11)
        val bt_12 = findViewById<TextView>(R.id.exo_12)
        val bt_13 = findViewById<TextView>(R.id.exo_13)
        val bt_14 = findViewById<TextView>(R.id.exo_14)
        val bt_15 = findViewById<TextView>(R.id.exo_15)
        val bt_16 = findViewById<TextView>(R.id.exo_16)
        val bt_17 = findViewById<TextView>(R.id.exo_17)


        val videoSource1 = Uri.parse("https://mults.info/mp4/skazka_o_soldate.mp4") // cазка о солдате
        val videoSource2 = Uri.parse("https://mults.info/mp4/diadia_stepa_militsioner.mp4") // Дядя степа
        val videoSource3 = Uri.parse("https://mults.info/mp4/opjat_dvoyka.mp4") // Опять двойка
        val videoSource4 = Uri.parse("https://mults.info/mp4/fedya_zaycev.mp4") // федя зайцев
        val videoSource5 = Uri.parse("https://mults.info/mp4/hrabry_zayac.mp4") // Храбрый заяц
        val videoSource6 = Uri.parse("https://mults.info/mp4/vasilisa_mikulishna.mp4") // Василиса Микулишна
        val videoSource7 = Uri.parse("https://mults.info/mp4/tarakanishe.mp4") // Тараканище
        val videoSource8 = Uri.parse("https://mults.info/mp4/korablik.mp4") // Кораблик
        val videoSource9 = Uri.parse("https://mults.info/mp4/kot_kotoryi_guljal_sam_po_sebe.mp4") // Кот, который гулял сам по себе
        val videoSource10 = Uri.parse("https://mults.info/mp4/molodilnye_yabloki.mp4") // Молодильные яблоки
        val videoSource11 = Uri.parse("https://mults.info/mp4/skazka_pro_len.mp4") // Сказка про лень
        val videoSource12 = Uri.parse("https://mults.info/mp4/tsarevna_lyagushka.mp4") // Царевна-Лягушка
        val videoSource13 = Uri.parse("https://mults.info/mp4/38_popugaev.mp4") // 38 попугаев
        val videoSource14 = Uri.parse("https://mults.info/mp4/chto_takoe_horosho.mp4") // Что такое хорошо и что такое плохо
        val videoSource15 = Uri.parse("https://mults.info/mp4/kak_griby_s_goroxom_voevali.mp4") // Как грибы с горохом воевали
        val videoSource16 = Uri.parse("https://mults.info/mp4/labirint_podvig_teseja.mp4") // Лабиринт
        val videoSource17 = Uri.parse("https://mults.info/mp4/a_vy_druzya_kak_ne_sadites.mp4") // А вы друзья как ни садитесь

        bt_fullscreen.setOnClickListener {

            if(!isFullScreen)
            {
                bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                    R.drawable.ic_baseline_fullscreen_exit
                ))
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
            }
            else{
                bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                    R.drawable.ic_baseline_fullscreen
                ))
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            isFullScreen = !isFullScreen
        }

        bt_lockscreen.setOnClickListener {
            if(!isLock)
            {
                bt_lockscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                    R.drawable.ic_baseline_lock
                ))
            }
            else
            {
                bt_lockscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                    R.drawable.ic_baseline_lock_open
                ))
            }
            isLock = !isLock
            lockScreen(isLock)
        }

        fun setChanel(Source: Uri){
            val mediaItem = MediaItem.fromUri(Source)
            simpleExoPlayer.setMediaItem(mediaItem)
            simpleExoPlayer.prepare()
            simpleExoPlayer.play()
        }


        bt_1.setOnClickListener {
            setChanel(videoSource1)
        }

        bt_2.setOnClickListener {
            setChanel(videoSource2)
        }

        bt_3.setOnClickListener {
            setChanel(videoSource3)
        }

        bt_4.setOnClickListener {
            setChanel(videoSource4)
        }

        bt_5.setOnClickListener {
            setChanel(videoSource5)
        }

        bt_6.setOnClickListener {
            setChanel(videoSource6)
        }

        bt_7.setOnClickListener {
            setChanel(videoSource7)
        }

        bt_8.setOnClickListener {
            setChanel(videoSource8)
        }

        bt_9.setOnClickListener {
            setChanel(videoSource9)
        }

        bt_10.setOnClickListener {
            setChanel(videoSource10)
        }

        bt_11.setOnClickListener {
            setChanel(videoSource11)
        }

        bt_12.setOnClickListener {
            setChanel(videoSource12)
        }

        bt_13.setOnClickListener {
            setChanel(videoSource13)
        }

        bt_14.setOnClickListener {
            setChanel(videoSource14)
        }

        bt_15.setOnClickListener {
            setChanel(videoSource15)
        }

        bt_16.setOnClickListener {
            setChanel(videoSource16)
        }

        bt_17.setOnClickListener {
            setChanel(videoSource17)
        }

        simpleExoPlayer= SimpleExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()
        playerView.player = simpleExoPlayer
        playerView.keepScreenOn = true
        simpleExoPlayer.addListener(object: Player.Listener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int)
            {
                if(playbackState == Player.STATE_BUFFERING)
                {
                    progressBar.visibility = View.VISIBLE
                }
                else if(playbackState == Player.STATE_READY)
                {
                    progressBar.visibility = View.GONE
                }

                if(!simpleExoPlayer.playWhenReady)
                {
                    handler.removeCallbacks(updateProgressAction)
                }
                else
                {
                    onProgress()
                }
            }
        })
        val videoSource = Uri.parse("https://mults.info/mp4/skazka_o_soldate.mp4")
        setChanel(videoSource)
    }

    val ad = 4000
    var check = false
    fun onProgress()
    {
        val player= simpleExoPlayer
        val position :Long =if(player == null) 0 else player.currentPosition
        handler.removeCallbacks(updateProgressAction)
        val playbackState = if(player ==null) Player.STATE_IDLE else player.playbackState
        if(playbackState != Player.STATE_IDLE && playbackState!= Player.STATE_ENDED)
        {
            var delayMs :Long
            if(player.playWhenReady && playbackState == Player.STATE_READY)
            {
                delayMs  = 1000 - position % 1000
                if(delayMs < 200)
                {
                    delayMs+=1000
                }
            }
            else{
                delayMs = 1000
            }

            //check to display ad
            if((ad-3000 <= position && position<=ad) &&!check)
            {
                check =true
                initAd()
            }
            handler.postDelayed(updateProgressAction,delayMs)
        }
    }

    var rewardedInterstitialAd: RewardedInterstitialAd?=null
    private fun initAd()
    {
        if(rewardedInterstitialAd!=null) return
        MobileAds.initialize(this)
        RewardedInterstitialAd.load(this,"ca-app-pub-3940256099942544/5354046379",
            AdRequest.Builder().build(), object : RewardedInterstitialAdLoadCallback(){
                override fun onAdLoaded(p0: RewardedInterstitialAd) {
                    rewardedInterstitialAd =p0
                    rewardedInterstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback()
                    {
                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            Log.d("WatchActivity_AD",p0?.message.toString())
                        }

                        override fun onAdShowedFullScreenContent() {
                            handler.removeCallbacks(updateProgressAction)
                        }

                        override fun onAdDismissedFullScreenContent() {
                            //resume play
                            simpleExoPlayer.playWhenReady = true
                            rewardedInterstitialAd = null
                            check=false
                        }
                    }
                    val sec_ad_countdown = findViewById<LinearLayout>(R.id.sect_ad_countdown)
                    val tx_ad_countdown = findViewById<TextView>(R.id.tx_ad_countdown)
                    sec_ad_countdown.visibility = View.VISIBLE
                    object : CountDownTimer(4000,1000)
                    {
                        override fun onTick(p0: Long) {
                            tx_ad_countdown.text = "Ad in ${p0/1000}"
                        }

                        override fun onFinish() {
                            sec_ad_countdown.visibility = View.GONE
                            rewardedInterstitialAd!!.show(this@WatchMP4Activity, object :
                                OnUserEarnedRewardListener {
                                override fun onUserEarnedReward(p0: RewardItem) {

                                }

                            })
                        }

                    }.start()
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    rewardedInterstitialAd = null
                }


            })
    }

    private val updateProgressAction = Runnable { onProgress() }
    private fun lockScreen(lock: Boolean) {
        val sec_mid = findViewById<LinearLayout>(R.id.sec_controlvid1)
        val sec_bottom = findViewById<LinearLayout>(R.id.sec_controlvid2)
        if(lock)
        {
            sec_mid.visibility = View.INVISIBLE
            sec_bottom.visibility = View.INVISIBLE
        }
        else
        {
            sec_mid.visibility = View.VISIBLE
            sec_bottom.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if(isLock) return
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            bt_fullscreen.performClick()
        }
        else super.onBackPressed()

    }

    override fun onStop() {
        super.onStop()
        simpleExoPlayer.stop()
    }

    fun FullScreencall() {
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
    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.pause()
    }
}