package com.hellokhdev.sovary.rustv.watch




//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.*
import com.google.android.exoplayer2.ui.PlayerView
import com.hellokhdev.sovary.rustv.main.MainActivity
import com.newdev.beta.rustv.R
import com.squareup.picasso.Picasso
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader

class WatchActivity : AppCompatActivity(), InterstitialAdLoadListener, RewardedAdLoadListener {

    companion object {
        var isFullScreen = false
        var isLock = false
    }
    lateinit var handler: Handler
    lateinit var simpleExoPlayer: SimpleExoPlayer
    lateinit var bt_fullscreen: ImageView

    var str_data = ""
    var str_Uri = mutableListOf("")
    var str_logo = mutableListOf("")
    var str_stat = mutableListOf("")
    val apiSample = "https://pomogu1c.ru/index.php/user/list?limit=20"

//-------
    private val adUnitId = "R-M-2278524-2" //demo-interstitial-yandex    R-M-2278524-2
    private val adUnitId_chanell = "R-M-2278524-4"
    private val eventLogger = InterstitialAdEventLogger()
    private var interstitialAdLoader: InterstitialAdLoader? = null
    private var interstitialAd: InterstitialAd? = null

//-------
    private var rewardedAd: RewardedAd? = null
    private val eventLoggerRew = RewardedAdEventLogger()
    private var adUnitIdRew = "R-M-2278524-3"  //R-M-2278524-3    demo-rewarded-yandex
    private var rewardedAdLoader: RewardedAdLoader? = null

//-------
    lateinit var lastCh: SharedPreferences
    private val save_key: String = "last_ch"
    lateinit var videoSource: Uri
    lateinit var FvCh: SharedPreferences
    lateinit var VolVis: SharedPreferences

    //private var mDataBase: DatabaseReference? = null
    private val USER_KEY = "Uri"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch)

        FullScreencall()

        handler = Handler(Looper.getMainLooper())
        lastCh = getSharedPreferences("LastChanel", MODE_PRIVATE)
        FvCh = getSharedPreferences("FvChanel", MODE_PRIVATE)
        VolVis = getSharedPreferences("VolVis", MODE_PRIVATE)


        val loader = InterstitialAdLoader(this).apply {
            setAdLoadListener(this@WatchActivity)
        }

        loader.loadAd(AdRequestConfiguration.Builder(adUnitId).build())
        interstitialAd?.setAdEventListener(eventLogger)


        val playerView = findViewById<PlayerView>(R.id.player)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        bt_fullscreen = findViewById<ImageView>(R.id.bt_fullscreen)
        val bt_lockscreen = findViewById<ImageView>(R.id.exo_lock)

        val bt_back = findViewById<ImageView>(R.id.exo_return)
        bt_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val bt_test_chanel = findViewById<ImageView>(R.id.exo_test)

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
        val bt_Match_fv = findViewById<ImageView>(R.id.exo_match_fv)
        val bt_NTV_fv = findViewById<ImageView>(R.id.exo_ntv_fv)
        val bt_TV3_fv = findViewById<ImageView>(R.id.exo_tv3_fv)
        val bt_Spas_fv = findViewById<ImageView>(R.id.exo_spas_fv)
        val bt_TNT_fv = findViewById<ImageView>(R.id.exo_tnt_fv)
        val bt_IZ_fv = findViewById<ImageView>(R.id.exo_iz_fv)
        val bt_pyatniza_fv = findViewById<ImageView>(R.id.exo_pyatniza_fv)
        val bt_vhs_fv = findViewById<ImageView>(R.id.exo_vhs_fv)
        val bt_U_fv = findViewById<ImageView>(R.id.exo_U_fv)
        val bt_Dom_fv = findViewById<ImageView>(R.id.exo_Dom_fv)
        val bt_Che_fv = findViewById<ImageView>(R.id.exo_Che_fv)
        val bt_StsLove_fv = findViewById<ImageView>(R.id.exo_sts_love_fv)
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
        val bt_Match = findViewById<ImageView>(R.id.exo_match)  //15
        val bt_NTV = findViewById<ImageView>(R.id.exo_ntv)   //16
        val bt_TV3 = findViewById<ImageView>(R.id.exo_tv3)   //17
        val bt_Spas = findViewById<ImageView>(R.id.exo_spas) //18
        val bt_TNT = findViewById<ImageView>(R.id.exo_tnt)   //19
        val bt_IZ = findViewById<ImageView>(R.id.exo_iz) //20
        val bt_pyatniza = findViewById<ImageView>(R.id.exo_pyatniza)    //21
        val bt_vhs = findViewById<ImageView>(R.id.exo_vhs)  //23
        val bt_Dom = findViewById<ImageView>(R.id.exo_Dom)      //29
        val bt_Che = findViewById<ImageView>(R.id.exo_Che)      //35
        val bt_StsLove = findViewById<ImageView>(R.id.exo_sts_love)     //36
        val bt_tvc = findViewById<ImageView>(R.id.exo_tvc)     //38
//        val bt_new1 = findViewById<ImageView>(R.id.exo_new1)     //39
//        val bt_new2 = findViewById<ImageView>(R.id.exo_new2)     //40
//        val bt_new3 = findViewById<ImageView>(R.id.exo_new3)     //41
//        val bt_new4 = findViewById<ImageView>(R.id.exo_new4)     //42
//        val bt_new5 = findViewById<ImageView>(R.id.exo_new5)     //43
//        val bt_new6 = findViewById<ImageView>(R.id.exo_new6)     //44
//        val bt_new7 = findViewById<ImageView>(R.id.exo_new7)     //45
//        val bt_new8 = findViewById<ImageView>(R.id.exo_new8)     //46
//        val bt_new9 = findViewById<ImageView>(R.id.exo_new9)     //47
//        val bt_new10 = findViewById<ImageView>(R.id.exo_new10)     //48

//        val bt_plus = findViewById<ImageView>(R.id.exo_plus)
        val bt_kinomix = findViewById<ImageView>(R.id.exo_kinomix)
        val bt_rodnoekino = findViewById<ImageView>(R.id.exo_rodnoekino)
        val bt_muzskoekino = findViewById<ImageView>(R.id.exo_muzskoekino)
        val bt_kinosemya = findViewById<ImageView>(R.id.exo_kinosemya)
        val bt_kinouzas = findViewById<ImageView>(R.id.exo_kinouzas)
        val bt_kinopremiera = findViewById<ImageView>(R.id.exo_kinopremiera)
        val bt_kinosvidanie = findViewById<ImageView>(R.id.exo_kinosvedanie)
        val bt_kinocomedy = findViewById<ImageView>(R.id.exo_kinocomedy)


        val videoSourceTV3 = Uri.parse("https://zabava-htlive.cdn.ngenix.net/hls/CH_TV3/variant.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/TV3_OTT_HD.m3u8")
        val videoSourceSpas = Uri.parse("https://spas.mediacdn.ru/cdn/spas/playlist.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Spas.m3u8")
        val videoSourceTNT = Uri.parse("https://zabava-htlive.cdn.ngenix.net/hls/CH_TNT/variant.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/TNT_OTT_HD.m3u8")
        val videoSourceIZ = Uri.parse("https://igi-hls.cdnvideo.ru/igi/igi_tcode/tracks-v1a1/mono.m3u8?hls_proxy_host=f4eb0d287702d48f6bbf6e6f56891e63")//("https://okkotv-live.cdnvideo.ru/channel/Izvestiya_HD_2.m3u8")
        val videoSourcePyatniza = Uri.parse("https://zabava-htlive.cdn.ngenix.net/hls/CH_PYATNIZZA/variant.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Pyatnizza_OTT_HD.m3u8")
        val videoSourceNTV = Uri.parse("https://cdn.ntv.ru/ntv-msk_hd/tracks-v1a1/playlist.m3u8")
        val videoSourceMatch = Uri.parse("http://zabava-htlive.cdn.ngenix.net/hls/CH_MATCHTV/variant.m3u8?version=2&checkedby:iptvcat.com")//("https://okkotv-live.cdnvideo.ru/channel/Match_OTT_HD.m3u8")
        val videoSourceSolnze = Uri.parse("https://mobdrm.mediavitrina.ru/hls-livef2/solntse/tracks-v3a1/mono.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Disney.m3u8")
        val videoSourceREN = Uri.parse("https://zabava-htlive.cdn.ngenix.net/hls/CH_RENTV/variant.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Rentv_HD_OTT.m3u8")
        val videoSourceMIR = Uri.parse("https://hls-mirtv.cdnvideo.ru/mirtv-parampublish/mirtv2_2500/playlist.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Mir_OTT.m3u8")
        val videoSourceSTS = Uri.parse("https://zabava-htlive.cdn.ngenix.net/hls/CH_STS/variant.m3u8")//("https://edge03-alm.beetv.kz/bpk-token/2an@5b5cntqu2lunc0ak5q4g14f0gzpyxi1e51ecwfaa/btv/icon/CTC_International/CTC_International.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/CTC_HD_OTT.m3u8")
        val videoSourceRUS24 = Uri.parse("https://zabava-htlive.cdn.ngenix.net/hls/CH_RUSSIA24/variant.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Russia24.m3u8")
        val videoSourceRUS1 = Uri.parse("https://zabava-htlive.cdn.ngenix.net/hls/CH_RUSSIA1/variant.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Russia1HD.m3u8")
        val videoSourceFirst = Uri.parse("https://edge4.1internet.tv/dash-live2/streams/1tv-dvr/1tvdash.mpd") // ПЕРВЫЙ КАНАЛ      https://edge4.1internet.tv/dash-live2/streams/1tv-dvr/1tvdash.mpd
        val videoSourceRUSK = Uri.parse("https://zabava-htlive.cdn.ngenix.net/hls/CH_RUSSIAK/variant.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Russia_K_SD.m3u8")
        val videoSourceStar = Uri.parse("https://tvchannelstream1.tvzvezda.ru/cdn/tvzvezda/playlist_sdhigh.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Zvezda_SD.m3u8")
        val videoSource5Ch = Uri.parse("https://zabava-htlive.cdn.ngenix.net/hls/CH_5TV/variant.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/5_OTT.m3u8")
        val videoSourceMult = Uri.parse("https://zabava-htlive.cdn.ngenix.net/hls/CH_KARUSEL/variant.m3u8")
        val videoSourcevhs = Uri.parse("https://kino-1.catcast.tv/content/38617/index.m3u8")
        val videoSourceU = Uri.parse("https://link2.rbtrack.ru/http://31.148.48.15:80/U/tracks-v1a1/mono.m3u8?&token=test")//("https://okkotv-live.cdnvideo.ru/channel/Yu_OTT.m3u8?zi/")
        val videoSourceDom = Uri.parse("https://zabava-htlive.cdn.ngenix.net/hls/CH_DOMASHNIY/variant.m3u8")
        val videoSourceChe = Uri.parse("https://zabava-htlive.cdn.ngenix.net/hls/CH_PERETZ/variant.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Che_OTT_2.m3u8?zi/")
        val videoSourceStsLove = Uri.parse("https://zabava-htlive.cdn.ngenix.net/hls/CH_STSLOVE/variant.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/CTC_Love_OTT_2.m3u8?zi/")
         val videoSourceTvc = Uri.parse("https://tvc-hls.cdnvideo.ru/tvc-res/smil:vd9221_2.smil/playlist.m3u8")
        val videoSourceRodnoe_kino = Uri.parse("https://sc.id-tv.kz/Rodnoe_kino.m3u8")
        val videoSourceMujskoe_kino = Uri.parse("https://sc.id-tv.kz/Mujskoe_kino_hd.m3u8")
        val videoSourceKinosemiya = Uri.parse("https://sc.id-tv.kz/Kinosemiya_hd.m3u8")
        val videoSourceKinouzhas = Uri.parse("https://sc.id-tv.kz/Kinouzhas_hd.m3u8")
        val videoSourceKinopremiera = Uri.parse("http://45.159.74.13:80/Kinopremera/index.m3u8")
        val videoSourceKinosvidanie = Uri.parse("https://sc.id-tv.kz/Kinosvidanie_hd.m3u8")
        val videoSourceKinomix = Uri.parse("https://sc.id-tv.kz/Kinomix_hd.m3u8")
        val videoSourceKinokomediya = Uri.parse("https://sc.id-tv.kz/Kinokomediya_hd.m3u8")
        val videoSourcetest = Uri.parse("https://edge04-alm.beetv.kz/bpk-token/2an@gs2dvquospucc13t31d12zhkbzre5ifegcddogaa/btv/SWM/FoxLife/FoxLife_1080p_5000kbps.m3u8") //TEST///////TEST////////TEST///////TEST///

        bt_fullscreen.setOnClickListener {

            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            );

            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

            if (!isFullScreen) {
                bt_fullscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_fullscreen_exit
                    )
                )
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
            } else {
                bt_fullscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_fullscreen
                    )
                )
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            isFullScreen = !isFullScreen
        }

        bt_lockscreen.setOnClickListener {
            if (!isLock) {
                bt_lockscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_lock
                    )
                )
            } else {
                bt_lockscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_lock_open
                    )
                )
            }
            isLock = !isLock
            lockScreen(isLock)
        }

        var zoom_model = arrayOf("Не растягивать", "Растянуть по горизонтали","Растянуть по вертикали" )

        val player = findViewById<PlayerView>(R.id.player)

        val spinner = findViewById(R.id.spinner) as Spinner

        val arrayAdapter = ArrayAdapter(this,R.layout.spin_close, zoom_model)
        arrayAdapter.setDropDownViewResource(R.layout.spin_style)
        spinner!!.setAdapter(arrayAdapter)


        spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(id == 0.toLong()){
                    player.setResizeMode(RESIZE_MODE_FIT)
                }else if(id == 1.toLong()){
                    player.setResizeMode(RESIZE_MODE_ZOOM)
                }else{
                    player.setResizeMode(RESIZE_MODE_FILL)
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        val ll_news = findViewById<LinearLayout>(R.id.news)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val visible = findViewById<ImageView>(R.id.exo_visible)
        val visible_vol = findViewById<ImageView>(R.id.exo_visible_volume)
        val volumes = findViewById<TextView>(R.id.volume)
        var check_vis = true
        var check_vol = true
        visible.setOnClickListener {
            check_vis = !check_vis
            if (!check_vis){
                //Picasso.get().load(str_logo[29]).into(bt_new7)
                visible.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vis))
                //visible.setBackgroundResource(R.drawable.vis)
                ll_news.visibility = View.GONE
            }else{
                //Picasso.get().load(str_logo[29]).into(bt_new7)
                visible.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.invis))
                //visible.setBackgroundResource(R.drawable.invis)
                ll_news.visibility = View.VISIBLE
            }
        }

        if (VolVis.getInt("VolVis", 1) == 0) {
            visible_vol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vis))
            seekBar.visibility = View.GONE
        } else {
            visible_vol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.invis))
            seekBar.visibility = View.VISIBLE
        }

        volumes.setOnClickListener {
            val editVol = VolVis.edit()
            check_vol = !check_vol
            if (VolVis.getInt("VolVis", 1) == 1) {
                editVol.putInt("VolVis", 0)
                editVol.apply()
                visible_vol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vis))
                seekBar.visibility = View.GONE
            } else {
                editVol.putInt("VolVis", 1)
                editVol.apply()
                visible_vol.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.invis))
                seekBar.visibility = View.VISIBLE
            }
        }

        val text_fav = findViewById<TextView>(R.id.textfav)

        var visfav = true

        val ll_fav = findViewById<LinearLayout>(R.id.ll_favorit)

        text_fav.setOnClickListener {
            if (visfav) {
                ll_fav.visibility = View.GONE
                visfav = !visfav
            } else {
                ll_fav.visibility = View.VISIBLE
                visfav = !visfav
            }
        }


//        val bt_plus_volum = findViewById<ImageView>(R.id.volum_plus)
//        val bt_minus_volum = findViewById<ImageView>(R.id.volum_minus)
//
//
//        val audioManager: AudioManager =
//            getSystemService(AUDIO_SERVICE) as AudioManager
//
//        bt_plus_volum.setOnClickListener {
//            val plus = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + 1
//            audioManager.setMediaVolume(plus)
//        }
//
//        bt_minus_volum.setOnClickListener {
//            val minus = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) - 1
//            audioManager.setMediaVolume(minus)
//        }

        initControls()

        val bt_fav = findViewById<ImageView>(R.id.exo_add_fv)
        var setChanelChoose = 2


        val editFv = FvCh.edit()

        if (FvCh.getInt(1.toString(), 0) == 1) {
            bt_ren_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(2.toString(), 0) == 1) {
            bt_1st_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(3.toString(), 0) == 1) {
            bt_r1_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(4.toString(), 0) == 1) {
            bt_r24_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(5.toString(), 0) == 1) {
            bt_sts_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(6.toString(), 0) == 1) {
            bt_mir_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(7.toString(), 0) == 1) {
            bt_mult_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(8.toString(), 0) == 1) {
            bt_5CH_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(9.toString(), 0) == 1) {
            bt_rk_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(10.toString(), 0) == 1) {
            bt_star_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(16.toString(), 0) == 1) {
            bt_NTV_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(17.toString(), 0) == 1) {
            bt_TV3_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(18.toString(), 0) == 1) {
            bt_Spas_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(19.toString(), 0) == 1) {
            bt_TNT_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(20.toString(), 0) == 1) {
            bt_IZ_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(21.toString(), 0) == 1) {
            bt_pyatniza_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(23.toString(), 0) == 1) {
            bt_vhs_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(29.toString(), 0) == 1) {
            bt_Dom_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(35.toString(), 0) == 1) {
            bt_Che_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(36.toString(), 0) == 1) {
            bt_StsLove_fv.visibility = View.VISIBLE
        }
        if (FvCh.getInt(38.toString(), 0) == 1) {
            bt_tvc_fv.visibility = View.VISIBLE
        }

        bt_fav.setOnClickListener {
            if (FvCh.getInt(setChanelChoose.toString(), 0) == 0) {
                editFv.putInt(setChanelChoose.toString(), 1)
                editFv.apply()
            } else {
                editFv.putInt(setChanelChoose.toString(), 0)
                editFv.apply()
            }

            if (FvCh.getInt(1.toString(), 0) == 1) {
                bt_ren_fv.visibility = View.VISIBLE
            } else {
                bt_ren_fv.visibility = View.GONE
            }
            if (FvCh.getInt(2.toString(), 0) == 1) {
                bt_1st_fv.visibility = View.VISIBLE
            } else {
                bt_1st_fv.visibility = View.GONE
            }
            if (FvCh.getInt(3.toString(), 0) == 1) {
                bt_r1_fv.visibility = View.VISIBLE
            } else {
                bt_r1_fv.visibility = View.GONE
            }
            if (FvCh.getInt(4.toString(), 0) == 1) {
                bt_r24_fv.visibility = View.VISIBLE
            } else {
                bt_r24_fv.visibility = View.GONE
            }
            if (FvCh.getInt(5.toString(), 0) == 1) {
                bt_sts_fv.visibility = View.VISIBLE
            } else {
                bt_sts_fv.visibility = View.GONE
            }
            if (FvCh.getInt(6.toString(), 0) == 1) {
                bt_mir_fv.visibility = View.VISIBLE
            } else {
                bt_mir_fv.visibility = View.GONE
            }
            if (FvCh.getInt(7.toString(), 0) == 1) {
                bt_mult_fv.visibility = View.VISIBLE
            } else {
                bt_mult_fv.visibility = View.GONE
            }
            if (FvCh.getInt(8.toString(), 0) == 1) {
                bt_5CH_fv.visibility = View.VISIBLE
            } else {
                bt_5CH_fv.visibility = View.GONE
            }
            if (FvCh.getInt(9.toString(), 0) == 1) {
                bt_rk_fv.visibility = View.VISIBLE
            } else {
                bt_rk_fv.visibility = View.GONE
            }
            if (FvCh.getInt(10.toString(), 0) == 1) {
                bt_star_fv.visibility = View.VISIBLE
            } else {
                bt_star_fv.visibility = View.GONE
            }
            if (FvCh.getInt(16.toString(), 0) == 1) {
                bt_NTV_fv.visibility = View.VISIBLE
            } else {
                bt_NTV_fv.visibility = View.GONE
            }
            if (FvCh.getInt(17.toString(), 0) == 1) {
                bt_TV3_fv.visibility = View.VISIBLE
            } else {
                bt_TV3_fv.visibility = View.GONE
            }
            if (FvCh.getInt(18.toString(), 0) == 1) {
                bt_Spas_fv.visibility = View.VISIBLE
            } else {
                bt_Spas_fv.visibility = View.GONE
            }
            if (FvCh.getInt(19.toString(), 0) == 1) {
                bt_TNT_fv.visibility = View.VISIBLE
            } else {
                bt_TNT_fv.visibility = View.GONE
            }
            if (FvCh.getInt(20.toString(), 0) == 1) {
                bt_IZ_fv.visibility = View.VISIBLE
            } else {
                bt_IZ_fv.visibility = View.GONE
            }
            if (FvCh.getInt(21.toString(), 0) == 1) {
                bt_pyatniza_fv.visibility = View.VISIBLE
            } else {
                bt_pyatniza_fv.visibility = View.GONE
            }
            if (FvCh.getInt(23.toString(), 0) == 1) {
                bt_vhs_fv.visibility = View.VISIBLE
            } else {
                bt_vhs_fv.visibility = View.GONE
            }
            if (FvCh.getInt(29.toString(), 0) == 1) {
                bt_Dom_fv.visibility = View.VISIBLE
            } else {
                bt_Dom_fv.visibility = View.GONE
            }
            if (FvCh.getInt(35.toString(), 0) == 1) {
                bt_Che_fv.visibility = View.VISIBLE
            } else {
                bt_Che_fv.visibility = View.GONE
            }
            if (FvCh.getInt(36.toString(), 0) == 1) {
                bt_StsLove_fv.visibility = View.VISIBLE
            } else {
                bt_StsLove_fv.visibility = View.GONE
            }
            if (FvCh.getInt(38.toString(), 0) == 1) {
                bt_tvc_fv.visibility = View.VISIBLE
            } else {
                bt_tvc_fv.visibility = View.GONE
            }
        }


        bt_test_chanel.setOnClickListener {
            setChanel(videoSourcetest)
            onSaveLast(videoSourcetest)
        }

        bt_Che_fv.setOnClickListener {
            if(checkUri(20)){
                setChanel(str_Uri[20].toUri())
                onSaveLast(str_Uri[20].toUri())
            }else {
                setChanel(videoSourceChe)//(listUri[23].toUri())
                onSaveLast(videoSourceChe)//(listUri[23].toUri())
            }
        }
        bt_StsLove_fv.setOnClickListener {
            if(checkUri(18)){
                setChanel(str_Uri[18].toUri())
                onSaveLast(str_Uri[18].toUri())
            }else {
                setChanel(videoSourceStsLove)//(listUri[11].toUri())
                onSaveLast(videoSourceStsLove)//(listUri[11].toUri())
            }
        }
        bt_Dom_fv.setOnClickListener {
            if(checkUri(21)){
                setChanel(str_Uri[21].toUri())
                onSaveLast(str_Uri[21].toUri())
            }else {
                setChanel(videoSourceDom)
                onSaveLast(videoSourceDom)
            }
        }
        bt_U_fv.setOnClickListener {
            setChanel(videoSourceU)//(listUri[22].toUri())
            onSaveLast(videoSourceU)//(listUri[22].toUri())
        }

        bt_pyatniza_fv.setOnClickListener {
            if(checkUri(11)){
                setChanel(str_Uri[11].toUri())
                onSaveLast(str_Uri[11].toUri())
            }else {
                setChanel(videoSourcePyatniza)//(listUri[17].toUri())
                onSaveLast(videoSourcePyatniza)//(listUri[17].toUri())
            }
        }

        bt_IZ_fv.setOnClickListener {
            if(checkUri(7)){
                setChanel(str_Uri[7].toUri())
                onSaveLast(str_Uri[7].toUri())
            }else {
                setChanel(videoSourceIZ)//(listUri[7].toUri())
                onSaveLast(videoSourceIZ)//(listUri[7].toUri())
            }
        }

        bt_TNT_fv.setOnClickListener {
            if(checkUri(8)){
                setChanel(str_Uri[8].toUri())
                onSaveLast(str_Uri[8].toUri())
            }else {
                setChanel(videoSourceTNT)//(listUri[19].toUri())
                onSaveLast(videoSourceTNT)//(listUri[19].toUri())
            }
        }

        bt_Spas_fv.setOnClickListener {
            if(checkUri(10)){
                setChanel(str_Uri[10].toUri())
                onSaveLast(str_Uri[10].toUri())
            }else {
                setChanel(videoSourceSpas)//(listUri[27].toUri())
                onSaveLast(videoSourceSpas)//(listUri[27].toUri())
            }
        }

        bt_TV3_fv.setOnClickListener {
            if(checkUri(9)){
                setChanel(str_Uri[9].toUri())
                onSaveLast(str_Uri[9].toUri())
            }else {
                setChanel(videoSourceTV3)//(listUri[21].toUri())
                onSaveLast(videoSourceTV3)//(listUri[21].toUri())
            }
        }

        bt_NTV_fv.setOnClickListener {
            if(checkUri(12)){
                setChanel(str_Uri[12].toUri())
                onSaveLast(str_Uri[12].toUri())
            }else {
                setChanel(videoSourceNTV)//(listUri[9].toUri())
                onSaveLast(videoSourceNTV)//(listUri[9].toUri())
            }
        }


        bt_Match_fv.setOnClickListener {
            setChanel(videoSourceMatch)//(listUri[10].toUri())
            onSaveLast(videoSourceMatch)//(listUri[10].toUri())
        }


        bt_solnze_fv.setOnClickListener {
            setChanel(videoSourceSolnze)//(listUri[31].toUri())
            onSaveLast(videoSourceSolnze)//(listUri[31].toUri())
        }

        bt_5CH_fv.setOnClickListener {
            if(checkUri(5)){
                setChanel(str_Uri[5].toUri())
                onSaveLast(str_Uri[5].toUri())
            }else {
                setChanel(videoSource5Ch)
                onSaveLast(videoSource5Ch)
            }
        }

        bt_mult_fv.setOnClickListener {
            if(checkUri(19)){
                setChanel(str_Uri[19].toUri())
                onSaveLast(str_Uri[19].toUri())
            }else {
                setChanel(videoSourceMult)
                onSaveLast(videoSourceMult)
            }
        }

        bt_rk_fv.setOnClickListener {
            if(checkUri(4)){
                setChanel(str_Uri[4].toUri())
                onSaveLast(str_Uri[4].toUri())
            }else {
                setChanel(videoSourceRUSK)//(listUri[4].toUri())
                onSaveLast(videoSourceRUSK)//(listUri[4].toUri())
            }
        }

        bt_star_fv.setOnClickListener {
            if(checkUri(16)){
                setChanel(str_Uri[16].toUri())
                onSaveLast(str_Uri[16].toUri())
            }else {
                setChanel(videoSourceStar)//(listUri[5].toUri())
                onSaveLast(videoSourceStar)//(listUri[5].toUri())
            }
        }

        bt_vhs_fv.setOnClickListener {
            if(checkUri(6)){
                setChanel(str_Uri[6].toUri())
                onSaveLast(str_Uri[6].toUri())
            }else {
                setChanel(videoSourcevhs)//(listUri[13].toUri())
                onSaveLast(videoSourcevhs)//(listUri[13].toUri())
            }
        }

        bt_mir_fv.setOnClickListener {
            if(checkUri(14)){
                setChanel(str_Uri[14].toUri())
                onSaveLast(str_Uri[14].toUri())
            }else {
                setChanel(videoSourceMIR)//(listUri[6].toUri())
                onSaveLast(videoSourceMIR)//(listUri[6].toUri())
            }
        }

        bt_ren_fv.setOnClickListener {
            if(checkUri(13)){
                setChanel(str_Uri[13].toUri())
                onSaveLast(str_Uri[13].toUri())
            }else {
                setChanel(videoSourceREN)//(listUri[12].toUri())
                onSaveLast(videoSourceREN)//(listUri[12].toUri())
            }
        }

        bt_r1_fv.setOnClickListener {
            if(checkUri(2)){
                setChanel(str_Uri[2].toUri())
                onSaveLast(str_Uri[2].toUri())
            }else {
                setChanel(videoSourceRUS1)//(listUri[2].toUri())
                onSaveLast(videoSourceRUS1)//(listUri[2].toUri())
            }
        }

        bt_r24_fv.setOnClickListener {
            if(checkUri(3)){
                setChanel(str_Uri[3].toUri())
                onSaveLast(str_Uri[3].toUri())
            }else {
                setChanel(videoSourceRUS24)//(listUri[3].toUri())
                onSaveLast(videoSourceRUS24)//(listUri[3].toUri())
            }
        }

        bt_sts_fv.setOnClickListener {
            if(checkUri(15)){
                setChanel(str_Uri[15].toUri())
                onSaveLast(str_Uri[15].toUri())
            }else {
                setChanel(videoSourceSTS)
                onSaveLast(videoSourceSTS)
            }
        }

        bt_1st_fv.setOnClickListener {
            if(checkUri(1)){
                setChanel(str_Uri[1].toUri())
                onSaveLast(str_Uri[1].toUri())
            }else {
                setChanel(videoSourceFirst)//(listUri[1].toUri())
                onSaveLast(videoSourceFirst)//(listUri[1].toUri())
            }
        }

        bt_tvc_fv.setOnClickListener {
            if(checkUri(17)){
                setChanel(str_Uri[17].toUri())
                onSaveLast(str_Uri[17].toUri())
            }else {
                setChanel(videoSourceTvc)//(listUri[8].toUri())
                onSaveLast(videoSourceTvc)//(listUri[8].toUri())
            }
        }






        bt_kinomix.setOnClickListener {
            setChanel(videoSourceKinomix)
        }
        bt_kinosemya.setOnClickListener {
            setChanel(videoSourceKinosemiya)
        }
        bt_kinouzas.setOnClickListener {
            setChanel(videoSourceKinouzhas)
        }
        bt_rodnoekino.setOnClickListener {
            setChanel(videoSourceRodnoe_kino)
        }
        bt_muzskoekino.setOnClickListener {
            setChanel(videoSourceMujskoe_kino)
        }
        bt_kinopremiera.setOnClickListener {
            setChanel(videoSourceKinopremiera)
        }
        bt_kinosvidanie.setOnClickListener {
            setChanel(videoSourceKinosvidanie)
        }
        bt_kinocomedy.setOnClickListener {
            setChanel(videoSourceKinokomediya)
        }




        bt_Che.setOnClickListener {
            if(checkUri(20)){
                setChanel(str_Uri[20].toUri())
                onSaveLast(str_Uri[20].toUri())
            }else {
                setChanel(videoSourceChe)//(listUri[23].toUri())
                onSaveLast(videoSourceChe)//(listUri[23].toUri())
            }
            setChanelChoose = 35
        }

        bt_StsLove.setOnClickListener {
            if(checkUri(18)){
                setChanel(str_Uri[18].toUri())
                onSaveLast(str_Uri[18].toUri())
            }else {
                setChanel(videoSourceStsLove)//(listUri[11].toUri())
                onSaveLast(videoSourceStsLove)//(listUri[11].toUri())
            }
            setChanelChoose = 36
        }


        bt_Dom.setOnClickListener {
            if(checkUri(21)){
                setChanel(str_Uri[21].toUri())
                onSaveLast(str_Uri[21].toUri())
            }else {
                setChanel(videoSourceDom)
                onSaveLast(videoSourceDom)
            }
            setChanelChoose = 29
        }

        bt_pyatniza.setOnClickListener {
            if(checkUri(11)){
                setChanel(str_Uri[11].toUri())
                onSaveLast(str_Uri[11].toUri())
            }else {
                setChanel(videoSourcePyatniza)//(listUri[17].toUri())
                onSaveLast(videoSourcePyatniza)//(listUri[17].toUri())
            }
            setChanelChoose = 21
        }

        bt_IZ.setOnClickListener {
            if(checkUri(7)){
                setChanel(str_Uri[7].toUri())
                onSaveLast(str_Uri[7].toUri())
            }else {
                setChanel(videoSourceIZ)//(listUri[7].toUri())
                onSaveLast(videoSourceIZ)//(listUri[7].toUri())
            }
            setChanelChoose = 20
        }

        bt_TNT.setOnClickListener {
            if(checkUri(8)){
                setChanel(str_Uri[8].toUri())
                onSaveLast(str_Uri[8].toUri())
            }else {
                setChanel(videoSourceTNT)//(listUri[19].toUri())
                onSaveLast(videoSourceTNT)//(listUri[19].toUri())
            }
            setChanelChoose = 19
        }

        bt_Spas.setOnClickListener {
            if(checkUri(10)){
                setChanel(str_Uri[10].toUri())
                onSaveLast(str_Uri[10].toUri())
            }else {
                setChanel(videoSourceSpas)//(listUri[27].toUri())
                onSaveLast(videoSourceSpas)//(listUri[27].toUri())
            }
            setChanelChoose = 18
        }

        bt_TV3.setOnClickListener {
            if(checkUri(9)){
                setChanel(str_Uri[9].toUri())
                onSaveLast(str_Uri[9].toUri())
            }else {
                setChanel(videoSourceTV3)//(listUri[21].toUri())
                onSaveLast(videoSourceTV3)//(listUri[21].toUri())
            }
            setChanelChoose = 17
        }

        bt_NTV.setOnClickListener {
            if(checkUri(12)){
                setChanel(str_Uri[12].toUri())
                onSaveLast(str_Uri[12].toUri())
            }else {
                setChanel(videoSourceNTV)//(listUri[9].toUri())
                onSaveLast(videoSourceNTV)//(listUri[9].toUri())
            }
            setChanelChoose = 16
        }

        bt_Match.setOnClickListener {
            setChanel(videoSourceMatch)//(listUri[10].toUri())
            onSaveLast(videoSourceMatch)//(listUri[10].toUri())
            setChanelChoose = 15
        }


        bt_5CH.setOnClickListener {
            if(checkUri(5)){
                setChanel(str_Uri[5].toUri())
                onSaveLast(str_Uri[5].toUri())
            }else {
                setChanel(videoSource5Ch)
                onSaveLast(videoSource5Ch)
            }
            setChanelChoose = 8
        }


        bt_mult.setOnClickListener {
            if(checkUri(19)){
                setChanel(str_Uri[19].toUri())
                onSaveLast(str_Uri[19].toUri())
            }else {
                setChanel(videoSourceMult)
                onSaveLast(videoSourceMult)
            }
            setChanelChoose = 7

        }

        bt_rk.setOnClickListener {
            if(checkUri(4)){
                setChanel(str_Uri[4].toUri())
                onSaveLast(str_Uri[4].toUri())
            }else {
                setChanel(videoSourceRUSK)//(listUri[4].toUri())
                onSaveLast(videoSourceRUSK)//(listUri[4].toUri())
            }
            setChanelChoose = 9
        }

        bt_star.setOnClickListener {
            if(checkUri(16)){
                setChanel(str_Uri[16].toUri())
                onSaveLast(str_Uri[16].toUri())
            }else {
                setChanel(videoSourceStar)//(listUri[5].toUri())
                onSaveLast(videoSourceStar)//(listUri[5].toUri())
            }
            setChanelChoose = 10
        }

        bt_vhs.setOnClickListener {
            if(checkUri(6)){
                setChanel(str_Uri[6].toUri())
                onSaveLast(str_Uri[6].toUri())
            }else {
                setChanel(videoSourcevhs)//(listUri[13].toUri())
                onSaveLast(videoSourcevhs)//(listUri[13].toUri())
            }
            setChanelChoose = 23
        }


        bt_mir.setOnClickListener {
            if(checkUri(14)){
                setChanel(str_Uri[14].toUri())
                onSaveLast(str_Uri[14].toUri())
            }else {
                setChanel(videoSourceMIR)//(listUri[6].toUri())
                onSaveLast(videoSourceMIR)//(listUri[6].toUri())
            }
            setChanelChoose = 6
        }

        bt_ren.setOnClickListener {
            if(checkUri(13)){
                setChanel(str_Uri[13].toUri())
                onSaveLast(str_Uri[13].toUri())
            }else {
                setChanel(videoSourceREN)//(listUri[12].toUri())
                onSaveLast(videoSourceREN)//(listUri[12].toUri())
            }
            setChanelChoose = 1
        }

        bt_r1.setOnClickListener {
            if(checkUri(2)){
                setChanel(str_Uri[2].toUri())
                onSaveLast(str_Uri[2].toUri())
            }else {
                setChanel(videoSourceRUS1)//(listUri[2].toUri())
                onSaveLast(videoSourceRUS1)//(listUri[2].toUri())
            }
            setChanelChoose = 3
        }

        bt_r24.setOnClickListener {
            if(checkUri(3)){
                setChanel(str_Uri[3].toUri())
                onSaveLast(str_Uri[3].toUri())
            }else {
                setChanel(videoSourceRUS24)//(listUri[3].toUri())
                onSaveLast(videoSourceRUS24)//(listUri[3].toUri())
            }
            setChanelChoose = 4
        }

        bt_sts.setOnClickListener {
            if(checkUri(15)){
                setChanel(str_Uri[15].toUri())
                onSaveLast(str_Uri[15].toUri())
            }else {
                setChanel(videoSourceSTS)
                onSaveLast(videoSourceSTS)
            }
            setChanelChoose = 5
        }

        bt_1st.setOnClickListener {
            if(checkUri(1)){
                setChanel(str_Uri[1].toUri())
                onSaveLast(str_Uri[1].toUri())
            }else {
                setChanel(videoSourceFirst)//(listUri[1].toUri())
                onSaveLast(videoSourceFirst)//(listUri[1].toUri())
            }
            setChanelChoose = 2
        }


        bt_tvc.setOnClickListener {
            if(checkUri(17)){
                setChanel(str_Uri[17].toUri())
                onSaveLast(str_Uri[17].toUri())
            }else {
                setChanel(videoSourceTvc)//(listUri[8].toUri())
                onSaveLast(videoSourceTvc)//(listUri[8].toUri())
            }
            setChanelChoose = 38
        }




//        bt_plus.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("Внимание!")
//            builder.setMessage("Сейчас будет показана реклама длительностью ~25 сек.\n" +
//                    "После ее просмотра , будут добавленны каналы:\n" +
//                    "МУЖСКОЕ КИНО\n" +
//                    "РОДНОЕ КИНО\n" +
//                    "КИНОМИКС\n" +
//                    "КИНОУЖАС\n" +
//                    "КИНОСЕМЬЯ\n" +
//                    "КИНОПРЕМЬЕРА\n" +
//                    "КИНОКОМЕДИЯ \n" +
//                    "КИНОСВИДАНИЕ")
//           // builder.setIcon(android.R.drawable.ic_dialog_alert)
//
//            builder.setPositiveButton("Смотреть") { dialogInterface, which ->
//                rewardedAdLoader = RewardedAdLoader(this).apply {
//                    setAdLoadListener(this@WatchActivity)
//                }
//                rewardedAdLoader?.loadAd(AdRequestConfiguration.Builder(adUnitIdRew).build())
//
//            }
//            builder.setNeutralButton("Отмена") { dialogInterface, which ->
//
//            }
//            val alertDialog: AlertDialog = builder.create()
//
//            alertDialog.setCancelable(false)
//            alertDialog.show()
//        }



        simpleExoPlayer = SimpleExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            //.setTrackSelector(trackSelector)
            .build()
        playerView.player = simpleExoPlayer
        playerView.keepScreenOn = true
        simpleExoPlayer.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_BUFFERING) {
                    progressBar.visibility = View.VISIBLE
                } else if (playbackState == Player.STATE_READY) {
                    progressBar.visibility = View.GONE
                }

                if (!simpleExoPlayer.playWhenReady) {
                    handler.removeCallbacks(updateProgressAction)
                } else {
                    onProgress()
                }
            }
        })

        getUri()
        onGetLast()


    }


    private fun checkUri(i: Int): Boolean {
        try {
            Log.d("Check test", str_Uri[i])
            return true
        }catch (e:IndexOutOfBoundsException){
            Log.d("Check test", "Список ссылок пуст!")
            return false
        }
    }


    fun Toast(massege:String){
        Toast.makeText(this, massege, Toast.LENGTH_SHORT).show()
    }

    private inner class RewardedAdEventLogger : RewardedAdEventListener {

        override fun onAdShown() {
        }

        override fun onAdFailedToShow(adError: AdError) {
        }

        override fun onAdDismissed() {
            destroyRewardedAd()
        }

        override fun onAdClicked() {
        }

        override fun onAdImpression(data: ImpressionData?) {
        }
        override fun onRewarded(reward: Reward) {
            val add_ch = findViewById<LinearLayout>(R.id.add_ch)
            val bt_plus = findViewById<ImageView>(R.id.exo_plus)
            add_ch.visibility = View.VISIBLE
            bt_plus.visibility = View.GONE
        }


    }

    private fun destroyInterstitial() {
        interstitialAd?.setAdEventListener(null)
        interstitialAd = null
    }

    override fun onAdLoaded(interstitialAd: InterstitialAd) {
        interstitialAd?.show(this@WatchActivity)
    }

    override fun onAdLoaded(rewardedAd: RewardedAd) {
        rewardedAd?.apply {
            setAdEventListener(eventLoggerRew)
            show(this@WatchActivity)
        }
    }

    override fun onAdFailedToLoad(adRequestError: AdRequestError) {}

    private inner class InterstitialAdEventLogger : InterstitialAdEventListener {

        override fun onAdShown() {
        }

        override fun onAdFailedToShow(adError: AdError) {
        }

        override fun onAdDismissed() {
        }

        override fun onAdClicked() {
        }

        override fun onAdImpression(data: ImpressionData?) {
        }
    }

    fun onProgress() {
        val player = simpleExoPlayer
        val position: Long = if (player == null) 0 else player.currentPosition
        handler.removeCallbacks(updateProgressAction)
        val playbackState = if (player == null) Player.STATE_IDLE else player.playbackState
        if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
            var delayMs: Long
            if (player.playWhenReady && playbackState == Player.STATE_READY) {
                delayMs = 1000 - position % 1000
                if (delayMs < 200) {
                    delayMs += 1000
                }
            } else {
                delayMs = 1000
            }

            handler.postDelayed(updateProgressAction, delayMs)
        }
    }


    private val updateProgressAction = Runnable { onProgress() }
    private fun lockScreen(lock: Boolean) {
        val sec_mid = findViewById<LinearLayout>(R.id.sec_controlvid1)
        val sec_bottom = findViewById<LinearLayout>(R.id.sec_controlvid2)
        if (lock) {
            sec_mid.visibility = View.INVISIBLE
            sec_bottom.visibility = View.INVISIBLE
        } else {
            sec_mid.visibility = View.VISIBLE
            sec_bottom.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (isLock) return
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bt_fullscreen.performClick()
        } else super.onBackPressed()

    }


    private fun FullScreencall() {
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }


    fun onSaveLast(Source: Uri) {
        val edit = lastCh.edit()
        edit.putString(save_key, Source.toString())
        edit.apply()
    }

    fun onGetLast() {
        videoSource = Uri.parse(
            lastCh.getString(
                save_key,
                "https://edge4.1internet.tv/dash-live2/streams/1tv-dvr/1tvdash.mpd"
            )
        )      //https://cdn10.1internet.tv/dash-live12/streams/1tv/1tvdash.mpd
        setChanel(videoSource)
    }

    var ads = 0
    fun setChanel(Source: Uri) {
        ads++
        chceck_ads(ads)
        simpleExoPlayer.removeMediaItem(1)
        val mediaItem = MediaItem.fromUri(Source)
        simpleExoPlayer.setMediaItem(mediaItem)
        simpleExoPlayer.prepare()
        simpleExoPlayer.play()
    }

    fun chceck_ads(ads_check: Int){
        //Toast(ads_check.toString())
        if(ads_check == 10){
            ads = 0
            val loader = InterstitialAdLoader(this).apply {
                setAdLoadListener(this@WatchActivity)
            }
            loader.loadAd(AdRequestConfiguration.Builder(adUnitId_chanell).build())
            interstitialAd?.setAdEventListener(eventLogger)
        }
    }

    override fun onStop() {
        super.onStop()
        simpleExoPlayer.stop()
    }
    private fun destroyRewardedAd() {
        rewardedAd?.setAdEventListener(null)
        rewardedAd = null
    }

    fun getUri() {
        str_Uri.clear()
        str_logo.clear()
        str_stat.clear()
        str_Uri.add("")
        str_stat.add("")
        str_logo.add("")
        val reqQueue: RequestQueue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(Request.Method.GET,apiSample, null, { res ->
            for (i in 0 until res.length()) {
                val respObj = res.getJSONObject(i)
                val id = respObj.getString("user_id")
                val name = respObj.getString("username")
                val URL = respObj.getString("user_email")
                val logo = respObj.getString("user_status")
                val stat = respObj.getString("status_view")
                str_data = str_data + "$id) $name ccылка: $URL логотип:$logo\n"
                str_Uri.add(URL)
                str_stat.add(stat)
                str_logo.add(logo)
            }
            setLogo()
        }, {err ->
            Log.d("Volley Sample Fail", err.message.toString())
        })

        reqQueue.add(request)
    }

    private fun setLogo() {

        val bt_new1 = findViewById<ImageView>(R.id.exo_new1)     //39
        val bt_new2 = findViewById<ImageView>(R.id.exo_new2)     //40
        val bt_new3 = findViewById<ImageView>(R.id.exo_new3)     //41
        val bt_new4 = findViewById<ImageView>(R.id.exo_new4)     //42
        val bt_new5 = findViewById<ImageView>(R.id.exo_new5)     //43
        val bt_new6 = findViewById<ImageView>(R.id.exo_new6)     //44
        val bt_new7 = findViewById<ImageView>(R.id.exo_new7)     //45
        val bt_new8 = findViewById<ImageView>(R.id.exo_new8)     //46
        val bt_new9 = findViewById<ImageView>(R.id.exo_new9)     //47
        val bt_new10 = findViewById<ImageView>(R.id.exo_new10)     //48

        Picasso.get().load(str_logo[23]).into(bt_new1)
        Picasso.get().load(str_logo[24]).into(bt_new2)
        Picasso.get().load(str_logo[25]).into(bt_new3)
        Picasso.get().load(str_logo[26]).into(bt_new4)
        Picasso.get().load(str_logo[27]).into(bt_new5)
        Picasso.get().load(str_logo[28]).into(bt_new6)
        Picasso.get().load(str_logo[29]).into(bt_new7)
        Picasso.get().load(str_logo[30]).into(bt_new8)
        Picasso.get().load(str_logo[31]).into(bt_new9)
        Picasso.get().load(str_logo[32]).into(bt_new10)

        if (str_stat[23].toInt() == 1){
            bt_new1.visibility = View.VISIBLE
        }
        if (str_stat[24].toInt() == 1){
            bt_new2.visibility = View.VISIBLE
        }
        if (str_stat[25].toInt() == 1){
            bt_new3.visibility = View.VISIBLE
        }
        if (str_stat[26].toInt() == 1){
            bt_new4.visibility = View.VISIBLE
        }
        if (str_stat[27].toInt() == 1){
            bt_new5.visibility = View.VISIBLE
        }
        if (str_stat[28].toInt() == 1){
            bt_new6.visibility = View.VISIBLE
        }
        if (str_stat[29].toInt() == 1){
            bt_new7.visibility = View.VISIBLE
        }
        if (str_stat[30].toInt() == 1){
            bt_new8.visibility = View.VISIBLE
        }
        if (str_stat[31].toInt() == 1){
            bt_new9.visibility = View.VISIBLE
        }
        if (str_stat[32].toInt() == 1){
            bt_new10.visibility = View.VISIBLE
        }

        val videoSourceFirst = Uri.parse("https://edge4.1internet.tv/dash-live2/streams/1tv-dvr/1tvdash.mpd") // ПЕРВЫЙ КАНАЛ

        bt_new1.setOnClickListener(){
            if(checkUri(23)){
                setChanel(str_Uri[23].toUri())
                onSaveLast(str_Uri[23].toUri())
            }else {
                setChanel(videoSourceFirst)//(listUri[11].toUri())
                onSaveLast(videoSourceFirst)//(listUri[11].toUri())
            }
        }
        bt_new2.setOnClickListener(){
            if(checkUri(24)){
                setChanel(str_Uri[24].toUri())
                onSaveLast(str_Uri[24].toUri())
            }else {
                setChanel(videoSourceFirst)//(listUri[11].toUri())
                onSaveLast(videoSourceFirst)//(listUri[11].toUri())
            }
        }
        bt_new3.setOnClickListener(){
            if(checkUri(25)){
                setChanel(str_Uri[25].toUri())
                onSaveLast(str_Uri[25].toUri())
            }else {
                setChanel(videoSourceFirst)//(listUri[11].toUri())
                onSaveLast(videoSourceFirst)//(listUri[11].toUri())
            }
        }
        bt_new4.setOnClickListener(){
            if(checkUri(26)){
                setChanel(str_Uri[26].toUri())
                onSaveLast(str_Uri[26].toUri())
            }else {
                setChanel(videoSourceFirst)//(listUri[11].toUri())
                onSaveLast(videoSourceFirst)//(listUri[11].toUri())
            }
        }
        bt_new5.setOnClickListener(){
            if(checkUri(27)){
                setChanel(str_Uri[27].toUri())
                onSaveLast(str_Uri[27].toUri())
            }else {
                setChanel(videoSourceFirst)//(listUri[11].toUri())
                onSaveLast(videoSourceFirst)//(listUri[11].toUri())
            }
        }
        bt_new6.setOnClickListener(){
            if(checkUri(28)){
                setChanel(str_Uri[28].toUri())
                onSaveLast(str_Uri[28].toUri())
            }else {
                setChanel(videoSourceFirst)//(listUri[11].toUri())
                onSaveLast(videoSourceFirst)//(listUri[11].toUri())
            }
        }
        bt_new7.setOnClickListener(){
            if(checkUri(29)){
                setChanel(str_Uri[29].toUri())
                onSaveLast(str_Uri[29].toUri())
            }else {
                setChanel(videoSourceFirst)//(listUri[11].toUri())
                onSaveLast(videoSourceFirst)//(listUri[11].toUri())
            }
        }
        bt_new8.setOnClickListener(){
            if(checkUri(30)){
                setChanel(str_Uri[30].toUri())
                onSaveLast(str_Uri[30].toUri())
            }else {
                setChanel(videoSourceFirst)//(listUri[11].toUri())
                onSaveLast(videoSourceFirst)//(listUri[11].toUri())
            }
        }
        bt_new9.setOnClickListener(){
            if(checkUri(31)){
                setChanel(str_Uri[31].toUri())
                onSaveLast(str_Uri[31].toUri())
            }else {
                setChanel(videoSourceFirst)//(listUri[11].toUri())
                onSaveLast(videoSourceFirst)//(listUri[11].toUri())
            }
        }
        bt_new10.setOnClickListener(){
            if(checkUri(32)){
                setChanel(str_Uri[32].toUri())
                onSaveLast(str_Uri[32].toUri())
            }else {
                setChanel(videoSourceFirst)//(listUri[11].toUri())
                onSaveLast(videoSourceFirst)//(listUri[11].toUri())
            }
        }


    }


    override fun onDestroy() {
        rewardedAdLoader?.setAdLoadListener(null)
        rewardedAdLoader = null
        destroyRewardedAd()
        interstitialAdLoader?.setAdLoadListener(null)
        interstitialAdLoader = null
        destroyInterstitial()
        super.onDestroy()
        simpleExoPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.pause()
    }

//    fun AudioManager.setMediaVolume(volumeIndex: Int) {
//        // Set media volume level
//        this.setStreamVolume(
//            AudioManager.STREAM_MUSIC, // Stream type
//            volumeIndex, // Volume index
//            AudioManager.FLAG_SHOW_UI// Flags
//        )
//    }

    private var volumeSeekbar: SeekBar? = null
    private var audioManager: AudioManager? = null
    private fun initControls() {
        try {
            volumeSeekbar = findViewById<View>(R.id.seekBar) as SeekBar
            audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
            volumeSeekbar!!.setMax(
                audioManager!!
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            )
            volumeSeekbar!!.setProgress(
                audioManager!!
                    .getStreamVolume(AudioManager.STREAM_MUSIC)
            )
            volumeSeekbar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(arg0: SeekBar) {}
                override fun onStartTrackingTouch(arg0: SeekBar) {}
                override fun onProgressChanged(arg0: SeekBar, progress: Int, arg2: Boolean) {
                    audioManager!!.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        progress, 0
                    )
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}