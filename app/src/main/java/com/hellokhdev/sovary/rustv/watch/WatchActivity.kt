package com.hellokhdev.sovary.rustv.watch



import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.*
import com.google.android.exoplayer2.ui.PlayerView
import com.hellokhdev.sovary.rustv.main.MainActivity
import com.newdev.beta.rustv.R
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener


class WatchActivity : AppCompatActivity() {

    companion object{
        var isFullScreen = false
        var isLock = false

    }



    private val eventLogger = InterstitialAdEventLogger()

    private val adUnitId = "R-M-2278524-2" //demo-interstitial-yandex    R-M-2278524-2


    lateinit var handler: Handler
    lateinit var simpleExoPlayer:SimpleExoPlayer
    lateinit var bt_fullscreen: ImageView


    private var mInterstitialAd: InterstitialAd? = null

    lateinit var lastCh: SharedPreferences
    private val save_key: String = "last_ch"
    lateinit var videoSource: Uri

    lateinit var FvCh: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch)
        FullScreencall()
        handler = Handler(Looper.getMainLooper())

        lastCh = getSharedPreferences("LastChanel", MODE_PRIVATE)
        FvCh = getSharedPreferences("FvChanel", MODE_PRIVATE)

        mInterstitialAd = InterstitialAd(this);
        mInterstitialAd!!.setAdUnitId(adUnitId);

       loadInterstitial()


        
        
        val playerView = findViewById<PlayerView>(R.id.player)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        bt_fullscreen =  findViewById<ImageView>(R.id.bt_fullscreen)
        val bt_lockscreen = findViewById<ImageView>(R.id.exo_lock)

        val bt_back = findViewById<ImageView>(R.id.exo_return)
        bt_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        FullScreencall()

        val bt_ren_fv = findViewById<ImageView>(R.id.exo_ren_fv)
        val bt_1st_fv = findViewById<ImageView>(R.id.exo_1_fv)
        val bt_r1_fv = findViewById<ImageView>(R.id.exo_r1_fv)
        val bt_r24_fv = findViewById<ImageView>(R.id.exo_r24_fv)
        val bt_sts_fv = findViewById<ImageView>(R.id.exo_sts_fv)
        val bt_mir_fv = findViewById<ImageView>(R.id.exo_mir_fv)
        val bt_mult_fv = findViewById<ImageView>(R.id.exo_mult_fv)
        val bt_5CH_fv = findViewById<ImageView>(R.id.exo_5_fv)
        val bt_rk_fv = findViewById<ImageView>(R.id.exo_rusk_fv)
        val bt_star_fv = findViewById<ImageView>(R.id.exo_star_fv)
        val bt_solnze_fv = findViewById<ImageView>(R.id.exo_solnze_fv)
        val bt_tv1000_act_fv = findViewById<ImageView>(R.id.exo_tv1000_act_fv)
        val bt_tv1000_fv = findViewById<ImageView>(R.id.exo_tv1000_fv)
        val bt_tv1000_rus_fv = findViewById<ImageView>(R.id.exo_tv1000rus_fv)
        val bt_Match_fv = findViewById<ImageView>(R.id.exo_match_fv)
        val bt_NTV_fv = findViewById<ImageView>(R.id.exo_ntv_fv)
        val bt_TV3_fv = findViewById<ImageView>(R.id.exo_tv3_fv)
        val bt_Spas_fv= findViewById<ImageView>(R.id.exo_spas_fv)
        val bt_TNT_fv= findViewById<ImageView>(R.id.exo_tnt_fv)
        val bt_IZ_fv= findViewById<ImageView>(R.id.exo_iz_fv)
        val bt_pyatniza_fv = findViewById<ImageView>(R.id.exo_pyatniza_fv)
        val bt_MuzTv_fv = findViewById<ImageView>(R.id.exo_muztv_fv)
        val bt_vhs_fv = findViewById<ImageView>(R.id.exo_vhs_fv)
        val bt_nikolodion_fv = findViewById<ImageView>(R.id.exo_nikolodion_fv)
        val bt_red_fv = findViewById<ImageView>(R.id.exo_red_fv)
        val bt_black_fv = findViewById<ImageView>(R.id.exo_black_fv)
        val bt_history_fv = findViewById<ImageView>(R.id.exo_history_fv)
        val bt_U_fv = findViewById<ImageView>(R.id.exo_U_fv)
        val bt_Dom_fv = findViewById<ImageView>(R.id.exo_Dom_fv)
        val bt_turist_fv = findViewById<ImageView>(R.id.exo_turist_fv)
        val bt_ViasExp_fv = findViewById<ImageView>(R.id.exo_ViasExp_fv)
        val bt_ViasNat_fv = findViewById<ImageView>(R.id.exo_ViasNat_fv)
        val bt_Scifi_fv = findViewById<ImageView>(R.id.exo_Scifi_fv)
        val bt_Start_world_fv = findViewById<ImageView>(R.id.exo_Start_world_fv)
        val bt_Che_fv = findViewById<ImageView>(R.id.exo_Che_fv)
        val bt_StsLove_fv = findViewById<ImageView>(R.id.exo_sts_love_fv)
        val bt_vhs2_fv = findViewById<ImageView>(R.id.exo_vhs2_fv)
        val bt_tvc_fv = findViewById<ImageView>(R.id.exo_tvc_fv)

        val bt_ren = findViewById<ImageView>(R.id.exo_ren)   //1
        val bt_1st = findViewById<ImageView>(R.id.exo_1)     //2
        val bt_r1 = findViewById<ImageView>(R.id.exo_r1)     //3
        val bt_r24 = findViewById<ImageView>(R.id.exo_r24)      //4
        val bt_sts = findViewById<ImageView>(R.id.exo_sts)      //5
        val bt_mir = findViewById<ImageView>(R.id.exo_mir)      //6
        val bt_mult = findViewById<ImageView>(R.id.exo_mult)    //7
        val bt_5CH = findViewById<ImageView>(R.id.exo_5)       //8
        val bt_rk = findViewById<ImageView>(R.id.exo_rusk)      //9
        val bt_star = findViewById<ImageView>(R.id.exo_star)    //10
        val bt_solnze = findViewById<ImageView>(R.id.exo_solnze)    //11
        val bt_tv1000_act = findViewById<ImageView>(R.id.exo_tv1000_act)    //12
        val bt_tv1000 = findViewById<ImageView>(R.id.exo_tv1000)    //13
        val bt_tv1000_rus = findViewById<ImageView>(R.id.exo_tv1000rus) //14
        val bt_Match = findViewById<ImageView>(R.id.exo_match)  //15
        val bt_NTV= findViewById<ImageView>(R.id.exo_ntv)   //16
        val bt_TV3= findViewById<ImageView>(R.id.exo_tv3)   //17
        val bt_Spas= findViewById<ImageView>(R.id.exo_spas) //18
        val bt_TNT= findViewById<ImageView>(R.id.exo_tnt)   //19
        val bt_IZ= findViewById<ImageView>(R.id.exo_iz) //20
        val bt_pyatniza= findViewById<ImageView>(R.id.exo_pyatniza)    //21
        val bt_MuzTv= findViewById<ImageView>(R.id.exo_muztv)   //22
        val bt_vhs = findViewById<ImageView>(R.id.exo_vhs)  //23
        val bt_nikolodion = findViewById<ImageView>(R.id.exo_nikolodion)    //24
        val bt_red = findViewById<ImageView>(R.id.exo_red)      //25
        val bt_black = findViewById<ImageView>(R.id.exo_black)      //26
        val bt_history = findViewById<ImageView>(R.id.exo_history)      //27
        val bt_U = findViewById<ImageView>(R.id.exo_U)      //28
        val bt_Dom = findViewById<ImageView>(R.id.exo_Dom)      //29
        val bt_turist = findViewById<ImageView>(R.id.exo_turist)        //30
        val bt_ViasExp = findViewById<ImageView>(R.id.exo_ViasExp)      //31
        val bt_ViasNat = findViewById<ImageView>(R.id.exo_ViasNat)      //32
        val bt_Scifi = findViewById<ImageView>(R.id.exo_Scifi)      //33
        val bt_Start_world = findViewById<ImageView>(R.id.exo_Start_world)      //34
        val bt_Che = findViewById<ImageView>(R.id.exo_Che)      //35
        val bt_StsLove = findViewById<ImageView>(R.id.exo_sts_love)     //36
        val bt_vhs2 = findViewById<ImageView>(R.id.exo_vhs2)     //37
        val bt_tvc = findViewById<ImageView>(R.id.exo_tvc)     //38


        val videoSourceTV3 = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/TV3_OTT_HD.m3u8")
        val videoSourceSpas = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Spas.m3u8")
        val videoSourceTNT = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/TNT_OTT_HD.m3u8")
        val videoSourceIZ = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Izvestiya_HD_2.m3u8")
        val videoSourcePyatniza = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Pyatnizza_OTT_HD.m3u8")
        val videoSourceMuztv = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/MuzTV.m3u8")
        val videoSourceNTV = Uri.parse("https://cdn.ntv.ru/ntv-msk_hd/tracks-v1a1/playlist.m3u8")
        val videoSourceMatch = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Match_OTT_HD.m3u8")
        val videoSourceTV1000Rus = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/TV1000_Rus_Kino_HD/1080p.m3u8")
        val videoSourceTV1000 = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/TV1000_HD.m3u8")
        val videoSourceSolnze = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Disney.m3u8")
        val videoSourceREN = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Rentv_HD_OTT.m3u8")
        val videoSourceMIR = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Mir_OTT.m3u8")
        val videoSourceSTS = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/CTC_HD_OTT.m3u8")
        val videoSourceRUS24 = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Russia24.m3u8")
        val videoSourceRUS1 = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Russia1HD.m3u8")
        val videoSourceFirst = Uri.parse("https://edge4.1internet.tv/dash-live2/streams/1tv-dvr/1tvdash.mpd") // ПЕРВЫЙ КАНАЛ      https://edge4.1internet.tv/dash-live2/streams/1tv-dvr/1tvdash.mpd
        val videoSourceRUSK = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Russia_K_SD.m3u8")
        val videoSourceStar = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Zvezda_SD.m3u8")
        val videoSourceTV1000ACT = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/TV1000_Action_HD/480p.m3u8")
        val videoSource5Ch= Uri.parse("https://okkotv-live.cdnvideo.ru/channel/5_OTT.m3u8")
        val videoSourceMult= Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Multilandia_HD.m3u8")
        val videoSourcevhs = Uri.parse("https://autopilot.catcast.tv/content/37925/index.m3u8")
        val videoSourceNickolodion = Uri.parse("https://sc.id-tv.kz/Nickelodeon.m3u8")
        val videoSourcered = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Sony_ET/480p.m3u8")
        val videoSourceblack = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Sony_Turbo/480p.m3u8")
        val videoSourcehistory = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Viasat_History_ad_HD/1080p.m3u8")
        val videoSourceU = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Yu_OTT.m3u8?zi/")
        val videoSourceDom = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Dom_HD_OTT.m3u8?zi/")
        val videoSourceStart_world = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/START_World_HD.m3u8?zi/")
        val videoSourceScifi = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Sony_SciFi.m3u8?zi/")
        val videoSourceViasNat = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Viasat_Nature_ad_HD.m3u8?zi/")
        val videoSourceViasExp = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Viasat_Explore_HD.m3u8?zi/")
        val videoSourceturist = Uri.parse("https://livetv.mylifeisgood.ml/channels/glazamiturista")
        val videoSourceChe = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Che_OTT_2.m3u8?zi/")
        val videoSourceStsLove = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/CTC_Love_OTT_2.m3u8?zi/")
        val videoSourceVhs2 = Uri.parse("https://autopilot.catcast.tv/content/38821/index.m3u8")
        val videoSourceTvc = Uri.parse("https://tvc-hls.cdnvideo.ru/tvc-res/smil:vd9221_2.smil/playlist.m3u8")


        bt_fullscreen.setOnClickListener {

            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

            if (Build.VERSION.SDK_INT >= 19) {
                window.decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            }

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


        val player = findViewById<PlayerView>(R.id.player)
        val zoom1 = findViewById<ImageView>(R.id.zoom1)
        val zoom2 = findViewById<ImageView>(R.id.zoom2)
        val zoom3 = findViewById<ImageView>(R.id.zoom3)

        zoom1.setOnClickListener{
            player.setResizeMode(RESIZE_MODE_FILL)
        }
        zoom2.setOnClickListener{
            player.setResizeMode(RESIZE_MODE_ZOOM)
        }
        zoom3.setOnClickListener{
            player.setResizeMode(RESIZE_MODE_FIT)
        }

        val text_fav = findViewById<TextView>(R.id.textfav)

        var visfav = true

        val ll_fav = findViewById<LinearLayout>(R.id.ll_favorit)

        text_fav.setOnClickListener{
            if(visfav){
               ll_fav.visibility = View.GONE
                visfav=!visfav
            }else{
                ll_fav.visibility = View.VISIBLE
                visfav=!visfav
            }
        }


        val bt_plus_volum = findViewById<ImageView>(R.id.volum_plus)
        val bt_minus_volum = findViewById<ImageView>(R.id.volum_minus)
        val bt_qual_HD = findViewById<ImageView>(R.id.qual_HD)
        val bt_qual_auto = findViewById<ImageView>(R.id.qual_auto)


        val audioManager: AudioManager =
            getSystemService(AUDIO_SERVICE) as AudioManager


        bt_plus_volum.setOnClickListener {
           val plus =  audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + 1
            audioManager.setMediaVolume(plus)
        }

        bt_minus_volum.setOnClickListener {
            val minus =  audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) - 1
            audioManager.setMediaVolume(minus)
        }

        bt_qual_HD.setOnClickListener {
           startHd()
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
            onGetLast()
        }

        bt_qual_auto.setOnClickListener {
           startauto()
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
            onGetLast()
        }

        val bt_fav = findViewById<ImageView>(R.id.exo_add_fv)
        var setChanelChoose = 2


        val editFv = FvCh.edit()

        if(FvCh.getInt(1.toString(),0)==1)
        {
            bt_ren_fv.visibility = View.VISIBLE
        }else{
            bt_ren_fv.visibility = View.GONE
        }
        if(FvCh.getInt(2.toString(),0)==1)
        {
            bt_1st_fv.visibility = View.VISIBLE
        }else{
            bt_1st_fv.visibility = View.GONE
        }
        if(FvCh.getInt(3.toString(),0)==1)
        {
            bt_r1_fv.visibility = View.VISIBLE
        }else{
            bt_r1_fv.visibility = View.GONE
        }
        if(FvCh.getInt(4.toString(),0)==1)
        {
            bt_r24_fv.visibility = View.VISIBLE
        }else{
            bt_r24_fv.visibility = View.GONE
        }
        if(FvCh.getInt(5.toString(),0)==1)
        {
            bt_sts_fv.visibility = View.VISIBLE
        }else{
            bt_sts_fv.visibility = View.GONE
        }
        if(FvCh.getInt(6.toString(),0)==1)
        {
            bt_mir_fv.visibility = View.VISIBLE
        }else{
            bt_mir_fv.visibility = View.GONE
        }
        if(FvCh.getInt(7.toString(),0)==1)
        {
            bt_mult_fv.visibility = View.VISIBLE
        }else{
            bt_mult_fv.visibility = View.GONE
        }
        if(FvCh.getInt(8.toString(),0)==1)
        {
            bt_5CH_fv.visibility = View.VISIBLE
        }else{
            bt_5CH_fv.visibility = View.GONE
        }
        if(FvCh.getInt(9.toString(),0)==1)
        {
            bt_rk_fv.visibility = View.VISIBLE
        }else{
            bt_rk_fv.visibility = View.GONE
        }
        if(FvCh.getInt(10.toString(),0)==1)
        {
            bt_star_fv.visibility = View.VISIBLE
        }else{
            bt_star_fv.visibility = View.GONE
        }
        if(FvCh.getInt(11.toString(),0)==1)
        {
            bt_solnze_fv.visibility = View.VISIBLE
        }else{
            bt_solnze_fv.visibility = View.GONE
        }
        if(FvCh.getInt(12.toString(),0)==1)
        {
            bt_tv1000_act_fv.visibility = View.VISIBLE
        }else{
            bt_tv1000_act_fv.visibility = View.GONE
        }
        if(FvCh.getInt(13.toString(),0)==1)
        {
            bt_tv1000_fv.visibility = View.VISIBLE
        }else{
            bt_tv1000_fv.visibility = View.GONE
        }
        if(FvCh.getInt(14.toString(),0)==1)
        {
            bt_tv1000_rus_fv.visibility = View.VISIBLE
        }else{
            bt_tv1000_rus_fv.visibility = View.GONE
        }
        if(FvCh.getInt(15.toString(),0)==1)
        {
            bt_Match_fv.visibility = View.VISIBLE
        }else{
            bt_Match_fv.visibility = View.GONE
        }
        if(FvCh.getInt(16.toString(),0)==1)
        {
            bt_NTV_fv.visibility = View.VISIBLE
        }else{
            bt_NTV_fv.visibility = View.GONE
        }
        if(FvCh.getInt(17.toString(),0)==1)
        {
            bt_TV3_fv.visibility = View.VISIBLE
        }else{
            bt_TV3_fv.visibility = View.GONE
        }
        if(FvCh.getInt(18.toString(),0)==1)
        {
            bt_Spas_fv.visibility = View.VISIBLE
        }else{
            bt_Spas_fv.visibility = View.GONE
        }
        if(FvCh.getInt(19.toString(),0)==1)
        {
            bt_TNT_fv.visibility = View.VISIBLE
        }else{
            bt_TNT_fv.visibility = View.GONE
        }
        if(FvCh.getInt(20.toString(),0)==1)
        {
            bt_IZ_fv.visibility = View.VISIBLE
        }else{
            bt_IZ_fv.visibility = View.GONE
        }
        if(FvCh.getInt(21.toString(),0)==1)
        {
            bt_pyatniza_fv.visibility = View.VISIBLE
        }else{
            bt_pyatniza_fv.visibility = View.GONE
        }
        if(FvCh.getInt(22.toString(),0)==1)
        {
            bt_MuzTv_fv.visibility = View.VISIBLE
        }else{
            bt_MuzTv_fv.visibility = View.GONE
        }
        if(FvCh.getInt(23.toString(),0)==1)
        {
            bt_vhs_fv.visibility = View.VISIBLE
        }else{
            bt_vhs_fv.visibility = View.GONE
        }
        if(FvCh.getInt(24.toString(),0)==1)
        {
            bt_nikolodion_fv.visibility = View.VISIBLE
        }else{
            bt_nikolodion_fv.visibility = View.GONE
        }
        if(FvCh.getInt(25.toString(),0)==1)
        {
            bt_red_fv.visibility = View.VISIBLE
        }else{
            bt_red_fv.visibility = View.GONE
        }
        if(FvCh.getInt(26.toString(),0)==1)
        {
            bt_black_fv.visibility = View.VISIBLE
        }else{
            bt_black_fv.visibility = View.GONE
        }
        if(FvCh.getInt(27.toString(),0)==1)
        {
            bt_history_fv.visibility = View.VISIBLE
        }else{
            bt_history_fv.visibility = View.GONE
        }
        if(FvCh.getInt(28.toString(),0)==1)
        {
            bt_U_fv.visibility = View.VISIBLE
        }else{
            bt_U_fv.visibility = View.GONE
        }
        if(FvCh.getInt(29.toString(),0)==1)
        {
            bt_Dom_fv.visibility = View.VISIBLE
        }else{
            bt_Dom_fv.visibility = View.GONE
        }
        if(FvCh.getInt(30.toString(),0)==1)
        {
            bt_turist_fv.visibility = View.VISIBLE
        }else{
            bt_turist_fv.visibility = View.GONE
        }
        if(FvCh.getInt(31.toString(),0)==1)
        {
            bt_ViasExp_fv.visibility = View.VISIBLE
        }else{
            bt_ViasExp_fv.visibility = View.GONE
        }
        if(FvCh.getInt(32.toString(),0)==1)
        {
            bt_ViasNat_fv.visibility = View.VISIBLE
        }else{
            bt_ViasNat_fv.visibility = View.GONE
        }
        if(FvCh.getInt(33.toString(),0)==1)
        {
            bt_Scifi_fv.visibility = View.VISIBLE
        }else{
            bt_Scifi_fv.visibility = View.GONE
        }
        if(FvCh.getInt(34.toString(),0)==1)
        {
            bt_Start_world_fv.visibility = View.VISIBLE
        }else{
            bt_Start_world_fv.visibility = View.GONE
        }
        if(FvCh.getInt(35.toString(),0)==1)
        {
            bt_Che_fv.visibility = View.VISIBLE
        }else{
            bt_Che_fv.visibility = View.GONE
        }
        if(FvCh.getInt(36.toString(),0)==1)
        {
            bt_StsLove_fv.visibility = View.VISIBLE
        }else{
            bt_StsLove_fv.visibility = View.GONE
        }
        if(FvCh.getInt(37.toString(),0)==1)
        {
            bt_vhs2_fv.visibility = View.VISIBLE
        }else{
            bt_vhs2_fv.visibility = View.GONE
        }
        if(FvCh.getInt(38.toString(),0)==1)
        {
            bt_tvc_fv.visibility = View.VISIBLE
        }else{
            bt_tvc_fv.visibility = View.GONE
        }

        bt_fav.setOnClickListener {
            if(FvCh.getInt(setChanelChoose.toString(),0)==0) {
                editFv.putInt(setChanelChoose.toString(), 1)
                editFv.apply()
            }else{
                editFv.putInt(setChanelChoose.toString(),0)
                editFv.apply()
            }

            if(FvCh.getInt(1.toString(),0)==1)
            {
                bt_ren_fv.visibility = View.VISIBLE
            }else{
                bt_ren_fv.visibility = View.GONE
            }
            if(FvCh.getInt(2.toString(),0)==1)
            {
                bt_1st_fv.visibility = View.VISIBLE
            }else{
                bt_1st_fv.visibility = View.GONE
            }
            if(FvCh.getInt(3.toString(),0)==1)
            {
                bt_r1_fv.visibility = View.VISIBLE
            }else{
                bt_r1_fv.visibility = View.GONE
            }
            if(FvCh.getInt(4.toString(),0)==1)
            {
                bt_r24_fv.visibility = View.VISIBLE
            }else{
                bt_r24_fv.visibility = View.GONE
            }
            if(FvCh.getInt(5.toString(),0)==1)
            {
                bt_sts_fv.visibility = View.VISIBLE
            }else{
                bt_sts_fv.visibility = View.GONE
            }
            if(FvCh.getInt(6.toString(),0)==1)
            {
                bt_mir_fv.visibility = View.VISIBLE
            }else{
                bt_mir_fv.visibility = View.GONE
            }
            if(FvCh.getInt(7.toString(),0)==1)
            {
                bt_mult_fv.visibility = View.VISIBLE
            }else{
                bt_mult_fv.visibility = View.GONE
            }
            if(FvCh.getInt(8.toString(),0)==1)
            {
                bt_5CH_fv.visibility = View.VISIBLE
            }else{
                bt_5CH_fv.visibility = View.GONE
            }
            if(FvCh.getInt(9.toString(),0)==1)
            {
                bt_rk_fv.visibility = View.VISIBLE
            }else{
                bt_rk_fv.visibility = View.GONE
            }
            if(FvCh.getInt(10.toString(),0)==1)
            {
                bt_star_fv.visibility = View.VISIBLE
            }else{
                bt_star_fv.visibility = View.GONE
            }
            if(FvCh.getInt(11.toString(),0)==1)
            {
                bt_solnze_fv.visibility = View.VISIBLE
            }else{
                bt_solnze_fv.visibility = View.GONE
            }
            if(FvCh.getInt(12.toString(),0)==1)
            {
                bt_tv1000_act_fv.visibility = View.VISIBLE
            }else{
                bt_tv1000_act_fv.visibility = View.GONE
            }
            if(FvCh.getInt(13.toString(),0)==1)
            {
                bt_tv1000_fv.visibility = View.VISIBLE
            }else{
                bt_tv1000_fv.visibility = View.GONE
            }
            if(FvCh.getInt(14.toString(),0)==1)
            {
                bt_tv1000_rus_fv.visibility = View.VISIBLE
            }else{
                bt_tv1000_rus_fv.visibility = View.GONE
            }
            if(FvCh.getInt(15.toString(),0)==1)
            {
                bt_Match_fv.visibility = View.VISIBLE
            }else{
                bt_Match_fv.visibility = View.GONE
            }
            if(FvCh.getInt(16.toString(),0)==1)
            {
                bt_NTV_fv.visibility = View.VISIBLE
            }else{
                bt_NTV_fv.visibility = View.GONE
            }
            if(FvCh.getInt(17.toString(),0)==1)
            {
                bt_TV3_fv.visibility = View.VISIBLE
            }else{
                bt_TV3_fv.visibility = View.GONE
            }
            if(FvCh.getInt(18.toString(),0)==1)
            {
                bt_Spas_fv.visibility = View.VISIBLE
            }else{
                bt_Spas_fv.visibility = View.GONE
            }
            if(FvCh.getInt(19.toString(),0)==1)
            {
                bt_TNT_fv.visibility = View.VISIBLE
            }else{
                bt_TNT_fv.visibility = View.GONE
            }
            if(FvCh.getInt(20.toString(),0)==1)
            {
                bt_IZ_fv.visibility = View.VISIBLE
            }else{
                bt_IZ_fv.visibility = View.GONE
            }
            if(FvCh.getInt(21.toString(),0)==1)
            {
                bt_pyatniza_fv.visibility = View.VISIBLE
            }else{
                bt_pyatniza_fv.visibility = View.GONE
            }
            if(FvCh.getInt(22.toString(),0)==1)
            {
                bt_MuzTv_fv.visibility = View.VISIBLE
            }else{
                bt_MuzTv_fv.visibility = View.GONE
            }
            if(FvCh.getInt(23.toString(),0)==1)
            {
                bt_vhs_fv.visibility = View.VISIBLE
            }else{
                bt_vhs_fv.visibility = View.GONE
            }
            if(FvCh.getInt(24.toString(),0)==1)
            {
                bt_nikolodion_fv.visibility = View.VISIBLE
            }else{
                bt_nikolodion_fv.visibility = View.GONE
            }
            if(FvCh.getInt(25.toString(),0)==1)
            {
                bt_red_fv.visibility = View.VISIBLE
            }else{
                bt_red_fv.visibility = View.GONE
            }
            if(FvCh.getInt(26.toString(),0)==1)
            {
                bt_black_fv.visibility = View.VISIBLE
            }else{
                bt_black_fv.visibility = View.GONE
            }
            if(FvCh.getInt(27.toString(),0)==1)
            {
                bt_history_fv.visibility = View.VISIBLE
            }else{
                bt_history_fv.visibility = View.GONE
            }
            if(FvCh.getInt(28.toString(),0)==1)
            {
                bt_U_fv.visibility = View.VISIBLE
            }else{
                bt_U_fv.visibility = View.GONE
            }
            if(FvCh.getInt(29.toString(),0)==1)
            {
                bt_Dom_fv.visibility = View.VISIBLE
            }else{
                bt_Dom_fv.visibility = View.GONE
            }
            if(FvCh.getInt(30.toString(),0)==1)
            {
                bt_turist_fv.visibility = View.VISIBLE
            }else{
                bt_turist_fv.visibility = View.GONE
            }
            if(FvCh.getInt(31.toString(),0)==1)
            {
                bt_ViasExp_fv.visibility = View.VISIBLE
            }else{
                bt_ViasExp_fv.visibility = View.GONE
            }
            if(FvCh.getInt(32.toString(),0)==1)
            {
                bt_ViasNat_fv.visibility = View.VISIBLE
            }else{
                bt_ViasNat_fv.visibility = View.GONE
            }
            if(FvCh.getInt(33.toString(),0)==1)
            {
                bt_Scifi_fv.visibility = View.VISIBLE
            }else{
                bt_Scifi_fv.visibility = View.GONE
            }
            if(FvCh.getInt(34.toString(),0)==1)
            {
                bt_Start_world_fv.visibility = View.VISIBLE
            }else{
                bt_Start_world_fv.visibility = View.GONE
            }
            if(FvCh.getInt(35.toString(),0)==1)
            {
                bt_Che_fv.visibility = View.VISIBLE
            }else{
                bt_Che_fv.visibility = View.GONE
            }
            if(FvCh.getInt(36.toString(),0)==1)
            {
                bt_StsLove_fv.visibility = View.VISIBLE
            }else{
                bt_StsLove_fv.visibility = View.GONE
            }
            if(FvCh.getInt(37.toString(),0)==1)
            {
                bt_vhs2_fv.visibility = View.VISIBLE
            }else{
                bt_vhs2_fv.visibility = View.GONE
            }
            if(FvCh.getInt(38.toString(),0)==1)
            {
                bt_tvc_fv.visibility = View.VISIBLE
            }else{
                bt_tvc_fv.visibility = View.GONE
            }

        }


        bt_black_fv.setOnClickListener {
            setChanel(videoSourceblack)
            onSaveLast(videoSourceblack)
        }

        bt_Che_fv.setOnClickListener {
            setChanel(videoSourceChe)
            onSaveLast(videoSourceChe)
        }

        bt_Start_world_fv.setOnClickListener {
            setChanel(videoSourceStart_world)
            onSaveLast(videoSourceStart_world)
        }
        bt_StsLove_fv.setOnClickListener {
            setChanel(videoSourceStsLove)
            onSaveLast(videoSourceStsLove)
        }
        bt_ViasNat_fv.setOnClickListener {
            setChanel(videoSourceViasNat)
            onSaveLast(videoSourceViasNat)
        }
        bt_ViasExp_fv.setOnClickListener {
            setChanel(videoSourceViasExp)
            onSaveLast(videoSourceViasExp)
        }
        bt_Scifi_fv.setOnClickListener {
            setChanel(videoSourceScifi)
            onSaveLast(videoSourceScifi)
        }
        bt_turist_fv.setOnClickListener {
            setChanel(videoSourceturist)
            onSaveLast(videoSourceturist)
        }
        bt_Dom_fv.setOnClickListener {
            setChanel(videoSourceDom)
            onSaveLast(videoSourceDom)
        }
        bt_red_fv.setOnClickListener {
            setChanel(videoSourcered)
            onSaveLast(videoSourcered)
        }
        bt_nikolodion_fv.setOnClickListener {
            setChanel(videoSourceNickolodion)
            onSaveLast(videoSourceNickolodion)
        }
        bt_U_fv.setOnClickListener {
            setChanel(videoSourceU)
            onSaveLast(videoSourceU)
        }
        bt_history_fv.setOnClickListener {
            setChanel(videoSourcehistory)
            onSaveLast(videoSourcehistory)
        }

        bt_pyatniza_fv.setOnClickListener {
            setChanel(videoSourcePyatniza)
            onSaveLast(videoSourcePyatniza)
        }

        bt_MuzTv_fv.setOnClickListener {
            setChanel(videoSourceMuztv)
            onSaveLast(videoSourceMuztv)
        }

        bt_IZ_fv.setOnClickListener {
            setChanel(videoSourceIZ)
            onSaveLast(videoSourceIZ)
        }

        bt_TNT_fv.setOnClickListener {
            setChanel(videoSourceTNT)
            onSaveLast(videoSourceTNT)
        }

        bt_Spas_fv.setOnClickListener {
            setChanel(videoSourceSpas)
            onSaveLast(videoSourceSpas)
        }

        bt_TV3_fv.setOnClickListener {
            setChanel(videoSourceTV3)
            onSaveLast(videoSourceTV3)
        }

        bt_NTV_fv.setOnClickListener {
            setChanel(videoSourceNTV)
            onSaveLast(videoSourceNTV)
        }

        bt_tv1000_fv.setOnClickListener {
            setChanel(videoSourceTV1000)
            onSaveLast(videoSourceTV1000)
        }

        bt_Match_fv.setOnClickListener {
            setChanel(videoSourceMatch)
            onSaveLast(videoSourceMatch)
        }

        bt_tv1000_rus_fv.setOnClickListener {
            setChanel(videoSourceTV1000Rus)
            onSaveLast(videoSourceTV1000Rus)
        }

        bt_solnze_fv.setOnClickListener {
            setChanel(videoSourceSolnze)
            onSaveLast(videoSourceSolnze)
        }

        bt_5CH_fv.setOnClickListener {
            setChanel(videoSource5Ch)
            onSaveLast(videoSource5Ch)
        }

        bt_mult_fv.setOnClickListener {
            setChanel(videoSourceMult)
            onSaveLast(videoSourceMult)
        }

        bt_rk_fv.setOnClickListener {
            setChanel(videoSourceRUSK)
            onSaveLast(videoSourceRUSK)
        }

        bt_star_fv.setOnClickListener {
            setChanel(videoSourceStar)
            onSaveLast(videoSourceStar)
        }

        bt_vhs_fv.setOnClickListener {
            setChanel(videoSourcevhs)
            onSaveLast(videoSourcevhs)
        }

        bt_tv1000_act_fv.setOnClickListener {
            setChanel(videoSourceTV1000ACT)
            onSaveLast(videoSourceTV1000ACT)
        }

        bt_mir_fv.setOnClickListener {
            setChanel(videoSourceMIR)
            onSaveLast(videoSourceMIR)
        }

        bt_ren_fv.setOnClickListener {
            setChanel(videoSourceREN)
            onSaveLast(videoSourceREN)
        }

        bt_r1_fv.setOnClickListener {
            setChanel(videoSourceRUS1)
            onSaveLast(videoSourceRUS1)
        }

        bt_r24_fv.setOnClickListener {
            setChanel(videoSourceRUS24)
            onSaveLast(videoSourceRUS24)
        }

        bt_sts_fv.setOnClickListener {
            setChanel(videoSourceSTS)
            onSaveLast(videoSourceSTS)
        }

        bt_1st_fv.setOnClickListener {
            setChanel(videoSourceFirst)
            onSaveLast(videoSourceFirst)
        }
        bt_vhs2_fv.setOnClickListener {
            setChanel(videoSourceVhs2)
            onSaveLast(videoSourceVhs2)
        }
        bt_tvc_fv.setOnClickListener {
            setChanel(videoSourceTvc)
            onSaveLast(videoSourceTvc)
        }





        bt_black.setOnClickListener {
            setChanel(videoSourceblack)
            onSaveLast(videoSourceblack)
            setChanelChoose = 26
        }

        bt_Che.setOnClickListener {
            setChanel(videoSourceChe)
            onSaveLast(videoSourceChe)
            setChanelChoose = 35
        }

        bt_Start_world.setOnClickListener {
            setChanel(videoSourceStart_world)
            onSaveLast(videoSourceStart_world)
            setChanelChoose = 34
        }
        bt_StsLove.setOnClickListener {
            setChanel(videoSourceStsLove)
            onSaveLast(videoSourceStsLove)
            setChanelChoose = 36
        }
        bt_ViasNat.setOnClickListener {
            setChanel(videoSourceViasNat)
            onSaveLast(videoSourceViasNat)
            setChanelChoose = 32
        }
        bt_ViasExp.setOnClickListener {
            setChanel(videoSourceViasExp)
            onSaveLast(videoSourceViasExp)
            setChanelChoose = 31
        }
        bt_Scifi.setOnClickListener {
            setChanel(videoSourceScifi)
            onSaveLast(videoSourceScifi)
            setChanelChoose = 33
        }
        bt_turist.setOnClickListener {
            setChanel(videoSourceturist)
            onSaveLast(videoSourceturist)
            setChanelChoose = 30
        }
        bt_Dom.setOnClickListener {
            setChanel(videoSourceDom)
            onSaveLast(videoSourceDom)
            setChanelChoose = 29
        }
        bt_red.setOnClickListener {
            setChanel(videoSourcered)
            onSaveLast(videoSourcered)
            setChanelChoose = 25
        }
        bt_nikolodion.setOnClickListener {
            setChanel(videoSourceNickolodion)
            onSaveLast(videoSourceNickolodion)
            setChanelChoose = 24
        }
        bt_U.setOnClickListener {
            setChanel(videoSourceU)
            onSaveLast(videoSourceU)
            setChanelChoose = 28
        }
        bt_history.setOnClickListener {
            setChanel(videoSourcehistory)
            onSaveLast(videoSourcehistory)
            setChanelChoose = 27
        }

        bt_pyatniza.setOnClickListener {
            setChanel(videoSourcePyatniza)
            onSaveLast(videoSourcePyatniza)
            setChanelChoose = 21
        }

        bt_MuzTv.setOnClickListener {
            setChanel(videoSourceMuztv)
            onSaveLast(videoSourceMuztv)
            setChanelChoose = 22
        }

        bt_IZ.setOnClickListener {
            setChanel(videoSourceIZ)
            onSaveLast(videoSourceIZ)
            setChanelChoose = 20
        }

        bt_TNT.setOnClickListener {
            setChanel(videoSourceTNT)
            onSaveLast(videoSourceTNT)
            setChanelChoose = 19
        }

        bt_Spas.setOnClickListener {
            setChanel(videoSourceSpas)
            onSaveLast(videoSourceSpas)
            setChanelChoose = 18
        }

        bt_TV3.setOnClickListener {
            setChanel(videoSourceTV3)
            onSaveLast(videoSourceTV3)
            setChanelChoose = 17
        }

        bt_NTV.setOnClickListener {
            setChanel(videoSourceNTV)
            onSaveLast(videoSourceNTV)
            setChanelChoose = 16
        }

        bt_tv1000.setOnClickListener {
            setChanel(videoSourceTV1000)
            onSaveLast(videoSourceTV1000)
            setChanelChoose = 13
        }

        bt_Match.setOnClickListener {
            setChanel(videoSourceMatch)
            onSaveLast(videoSourceMatch)
            setChanelChoose = 15
        }

        bt_tv1000_rus.setOnClickListener {
            setChanel(videoSourceTV1000Rus)
            onSaveLast(videoSourceTV1000Rus)
            setChanelChoose = 14
        }

        bt_solnze.setOnClickListener {
            setChanel(videoSourceSolnze)
            onSaveLast(videoSourceSolnze)
            setChanelChoose = 11

        }

        bt_5CH.setOnClickListener {
            setChanel(videoSource5Ch)
            onSaveLast(videoSource5Ch)
            setChanelChoose = 8
        }

        bt_mult.setOnClickListener {
            setChanel(videoSourceMult)
            onSaveLast(videoSourceMult)
            setChanelChoose = 7

        }

        bt_rk.setOnClickListener {
            setChanel(videoSourceRUSK)
            onSaveLast(videoSourceRUSK)
            setChanelChoose = 9
        }

        bt_star.setOnClickListener {
            setChanel(videoSourceStar)
            onSaveLast(videoSourceStar)
            setChanelChoose = 10
        }

        bt_vhs.setOnClickListener {
            setChanel(videoSourcevhs)
            onSaveLast(videoSourcevhs)
            setChanelChoose = 23
        }

        bt_tv1000_act.setOnClickListener {
            setChanel(videoSourceTV1000ACT)
            onSaveLast(videoSourceTV1000ACT)
            setChanelChoose = 12
        }

        bt_mir.setOnClickListener {
            setChanel(videoSourceMIR)
            onSaveLast(videoSourceMIR)
            setChanelChoose = 6
        }

        bt_ren.setOnClickListener {
            setChanel(videoSourceREN)
            onSaveLast(videoSourceREN)
            setChanelChoose = 1
        }

        bt_r1.setOnClickListener {
            setChanel(videoSourceRUS1)
            onSaveLast(videoSourceRUS1)
            setChanelChoose = 3
        }

        bt_r24.setOnClickListener {
            setChanel(videoSourceRUS24)
            onSaveLast(videoSourceRUS24)
            setChanelChoose = 4
        }

        bt_sts.setOnClickListener {
            setChanel(videoSourceSTS)
            onSaveLast(videoSourceSTS)
            setChanelChoose = 5
        }

        bt_1st.setOnClickListener {
            setChanel(videoSourceFirst)
            onSaveLast(videoSourceFirst)
            setChanelChoose = 2
        }

        bt_vhs2.setOnClickListener {
            setChanel(videoSourceVhs2)
            onSaveLast(videoSourceVhs2)
            setChanelChoose = 37
        }

        bt_tvc.setOnClickListener {
            setChanel(videoSourceTvc)
            onSaveLast(videoSourceTvc)
            setChanelChoose = 38
        }





        simpleExoPlayer= SimpleExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            //.setTrackSelector(trackSelector)
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

        //videoSource = Uri.parse("https://cdn10.1internet.tv/dash-live12/streams/1tv/1tvdash.mpd") //https://cdn10.1internet.tv/dash-live12/streams/1tv/1tvdash.mpd

        onGetLast()




    }



    fun startHd(){
        simpleExoPlayer.stop()
        simpleExoPlayer= SimpleExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            //.setTrackSelector(trackSelector)
            .build()

    }

    fun startauto(){
        val trackSelector = DefaultTrackSelector(this)
        trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd())
        simpleExoPlayer.stop()
        simpleExoPlayer= SimpleExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .setTrackSelector(trackSelector)
            .build()
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
            }
            handler.postDelayed(updateProgressAction,delayMs)
        }
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


    private fun loadInterstitial() {
            destroyInterstitial()
            createInterstitial()
            val adRequest =  AdRequest.Builder().build()
            mInterstitialAd?.loadAd(adRequest)
    }

    private fun createInterstitial() {
        mInterstitialAd = InterstitialAd(this).apply {
            setAdUnitId(adUnitId)
            setInterstitialAdEventListener(eventLogger)
        }
    }

    private  fun FullScreencall() {
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

    private fun destroyInterstitial() {
        mInterstitialAd?.destroy()
        mInterstitialAd = null
    }



    private inner class InterstitialAdEventLogger : InterstitialAdEventListener {

        override fun onAdLoaded() {
            mInterstitialAd?.show()
        }

        override fun onAdFailedToLoad(error: AdRequestError) {

        }

        override fun onAdShown() {
        }

        override fun onAdDismissed() {
        }

        override fun onAdClicked() {
        }

        override fun onLeftApplication() {
        }

        override fun onReturnedToApplication() {
        }

        override fun onImpression(data: ImpressionData?) {
        }
    }

    fun onSaveLast(Source: Uri){
        val edit = lastCh.edit()
        edit.putString(save_key, Source.toString())
        edit.apply()
    }

    fun onGetLast(){
        videoSource = Uri.parse(lastCh.getString(save_key,"https://cdn10.1internet.tv/dash-live12/streams/1tv/1tvdash.mpd"))      //https://cdn10.1internet.tv/dash-live12/streams/1tv/1tvdash.mpd
        setChanel(videoSource)
    }

    fun setChanel(Source: Uri) {
        val mediaItem = MediaItem.fromUri(Source)
        simpleExoPlayer.setMediaItem(mediaItem)
        simpleExoPlayer.prepare()
        simpleExoPlayer.play()
    }

    override fun onStop() {
        super.onStop()
        simpleExoPlayer.stop()
    }

    override fun onDestroy() {
        destroyInterstitial()
        super.onDestroy()
        simpleExoPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.pause()
    }

    fun AudioManager.setMediaVolume(volumeIndex:Int) {
        // Set media volume level
        this.setStreamVolume(
            AudioManager.STREAM_MUSIC, // Stream type
            volumeIndex, // Volume index
            AudioManager.FLAG_SHOW_UI// Flags
        )
    }
}










