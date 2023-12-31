package com.hellokhdev.sovary.rustv.watch




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
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.*
import com.google.android.exoplayer2.ui.PlayerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hellokhdev.sovary.rustv.DB.DB
import com.hellokhdev.sovary.rustv.main.MainActivity
import com.newdev.beta.rustv.R
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

//-------
    private val adUnitId = "R-M-2278524-2" //demo-interstitial-yandex    R-M-2278524-2
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

    private var mDataBase: DatabaseReference? = null
    private val USER_KEY = "Uri"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch)

        FullScreencall()
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        handler = Handler(Looper.getMainLooper())
        lastCh = getSharedPreferences("LastChanel", MODE_PRIVATE)
        FvCh = getSharedPreferences("FvChanel", MODE_PRIVATE)


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
        val bt_tv1000_act_fv = findViewById<ImageView>(R.id.exo_tv1000_act_fv)
        val bt_tv1000_fv = findViewById<ImageView>(R.id.exo_tv1000_fv)
        val bt_tv1000_rus_fv = findViewById<ImageView>(R.id.exo_tv1000rus_fv)
        val bt_Match_fv = findViewById<ImageView>(R.id.exo_match_fv)
        val bt_NTV_fv = findViewById<ImageView>(R.id.exo_ntv_fv)
        val bt_TV3_fv = findViewById<ImageView>(R.id.exo_tv3_fv)
        val bt_Spas_fv = findViewById<ImageView>(R.id.exo_spas_fv)
        val bt_TNT_fv = findViewById<ImageView>(R.id.exo_tnt_fv)
        val bt_IZ_fv = findViewById<ImageView>(R.id.exo_iz_fv)
        val bt_pyatniza_fv = findViewById<ImageView>(R.id.exo_pyatniza_fv)
        val bt_MuzTv_fv = findViewById<ImageView>(R.id.exo_muztv_fv)
        val bt_vhs_fv = findViewById<ImageView>(R.id.exo_vhs_fv)
        val bt_pobeda_fv = findViewById<ImageView>(R.id.exo_pobeda_fv)
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
        val bt_oruzie_fv = findViewById<ImageView>(R.id.exo_oruzie_fv)
        val bt_tvc_fv = findViewById<ImageView>(R.id.exo_tvc_fv)
        val bt_VIP_Comedy_fv = findViewById<ImageView>(R.id.exo_VIP_Comedy_fv)
        val bt_VIP_Serial_fv = findViewById<ImageView>(R.id.exo_VIP_Serial_fv)
        val bt_VIP_Premiere_fv = findViewById<ImageView>(R.id.exo_Premiere_fv)
        val bt_VIP_Megahit_fv = findViewById<ImageView>(R.id.exo_Megahit_fv)
        val bt_DomKino_fv = findViewById<ImageView>(R.id.exo_DomKino_fv)
        val bt_zolcol_fv = findViewById<ImageView>(R.id.exo_zolcol_fv)
        val bt_hollywood_fv = findViewById<ImageView>(R.id.exo_hollywood_fv)
        val bt_myplanet_fv = findViewById<ImageView>(R.id.exo_myplanet_fv)
        val bt_solov_fv = findViewById<ImageView>(R.id.exo_solov_fv)
        val bt_Terra_fv = findViewById<ImageView>(R.id.exo_terra_fv)
        val bt_Auto24_fv = findViewById<ImageView>(R.id.exo_auto24_fv)
        val bt_Autoplus_fv = findViewById<ImageView>(R.id.exo_autoplus_fv)

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
        val bt_NTV = findViewById<ImageView>(R.id.exo_ntv)   //16
        val bt_TV3 = findViewById<ImageView>(R.id.exo_tv3)   //17
        val bt_Spas = findViewById<ImageView>(R.id.exo_spas) //18
        val bt_TNT = findViewById<ImageView>(R.id.exo_tnt)   //19
        val bt_IZ = findViewById<ImageView>(R.id.exo_iz) //20
        val bt_pyatniza = findViewById<ImageView>(R.id.exo_pyatniza)    //21
        val bt_MuzTv = findViewById<ImageView>(R.id.exo_muztv)   //22
        val bt_vhs = findViewById<ImageView>(R.id.exo_vhs)  //23
        val bt_pobeda = findViewById<ImageView>(R.id.exo_pobeda)    //24
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
        val bt_oruzie = findViewById<ImageView>(R.id.exo_oruzie)     //37
        val bt_tvc = findViewById<ImageView>(R.id.exo_tvc)     //38
        val bt_VIP_Comedy = findViewById<ImageView>(R.id.exo_VIP_Comedy)     //39
        val bt_VIP_Serial = findViewById<ImageView>(R.id.exo_VIP_Serial)     //40
        val bt_VIP_Premiere = findViewById<ImageView>(R.id.exo_Premiere)     //41
        val bt_VIP_Megahit = findViewById<ImageView>(R.id.exo_Megahit)     //42
        val bt_DomKino = findViewById<ImageView>(R.id.exo_DomKino)     //43
        val bt_zolcol = findViewById<ImageView>(R.id.exo_zolcol)     //44
        val bt_hollywood = findViewById<ImageView>(R.id.exo_hollywood)     //45
        val bt_myplanet = findViewById<ImageView>(R.id.exo_myplanet)     //46
        val bt_solov = findViewById<ImageView>(R.id.exo_solov)     //47
        val bt_terra = findViewById<ImageView>(R.id.exo_terra)     //48
        val bt_auto24 = findViewById<ImageView>(R.id.exo_auto24)     //49
        val bt_autoplus = findViewById<ImageView>(R.id.exo_autoplus)     //50

        val bt_plus = findViewById<ImageView>(R.id.exo_plus)
        val bt_kinomix = findViewById<ImageView>(R.id.exo_kinomix)
        val bt_rodnoekino = findViewById<ImageView>(R.id.exo_rodnoekino)
        val bt_muzskoekino = findViewById<ImageView>(R.id.exo_muzskoekino)
        val bt_kinosemya = findViewById<ImageView>(R.id.exo_kinosemya)
        val bt_kinouzas = findViewById<ImageView>(R.id.exo_kinouzas)
        val bt_kinopremiera = findViewById<ImageView>(R.id.exo_kinopremiera)
        val bt_kinosvidanie = findViewById<ImageView>(R.id.exo_kinosvedanie)
        val bt_kinocomedy = findViewById<ImageView>(R.id.exo_kinocomedy)


        val videoSourceTV3 = Uri.parse("https://s16.federal.tv:8082/fed/tvtri-hq.stream/playlist.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/TV3_OTT_HD.m3u8")
        val videoSourceSpas = Uri.parse("https://spas.mediacdn.ru/cdn/spas/playlist.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Spas.m3u8")
        val videoSourceTNT = Uri.parse("https://ok.ru/video/854539312735")//("https://okkotv-live.cdnvideo.ru/channel/TNT_OTT_HD.m3u8")
        val videoSourceIZ = Uri.parse("https://igi-hls.cdnvideo.ru/igi/igi_tcode/tracks-v1a1/mono.m3u8?hls_proxy_host=f4eb0d287702d48f6bbf6e6f56891e63")//("https://okkotv-live.cdnvideo.ru/channel/Izvestiya_HD_2.m3u8")
        val videoSourcePyatniza = Uri.parse("https://s16.federal.tv:8082/fed/pyatnicatv-hq.stream/playlist.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Pyatnizza_OTT_HD.m3u8")
        val videoSourceMuztv = Uri.parse("https://s17.federal.tv:8082/fed/muztvtv.stream/playlist.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/MuzTV.m3u8")
        val videoSourceNTV = Uri.parse("https://cdn.ntv.ru/ntv-msk_hd/tracks-v1a1/playlist.m3u8")
        val videoSourceMatch = Uri.parse("https://s16.federal.tv:8082/fed/matchtv-hq.stream/chunks.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Match_OTT_HD.m3u8")
        val videoSourceTV1000Rus = Uri.parse("https://tbs01-edge11.Itdc.ge/tv1000rukino/tracks-v1a1/mono.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/TV1000_Rus_Kino_HD/1080p.m3u8")
        val videoSourceTV1000 = Uri.parse("https://bl.uma.media/live/485542/HLS/4614144_3/2/1/playlist.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/TV1000_HD.m3u8")
        val videoSourceSolnze = Uri.parse("https://mobdrm.mediavitrina.ru/hls-livef2/solntse/tracks-v3a1/mono.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Disney.m3u8")
        val videoSourceREN = Uri.parse("https://tbs01-edge11.itdc.ge/rentv/tracks-v1a1/mono.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Rentv_HD_OTT.m3u8")
        val videoSourceMIR = Uri.parse("https://hls-mirtv.cdnvideo.ru/mirtv-parampublish/mirtv2_2500/playlist.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Mir_OTT.m3u8")
        val videoSourceSTS = Uri.parse("https://edge03-alm.beetv.kz/bpk-token/2an@utkdodeyknynqkt2lqwq5vg2ga3u0q1aujrdvvaa/btv/icon/CTC_International/CTC_International.m3u8")//("https://edge03-alm.beetv.kz/bpk-token/2an@5b5cntqu2lunc0ak5q4g14f0gzpyxi1e51ecwfaa/btv/icon/CTC_International/CTC_International.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/CTC_HD_OTT.m3u8")
        val videoSourceRUS24 = Uri.parse("https://zabava-htlive.cdn.ngenix.net/hls/CH_RUSSIA24/variant.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Russia24.m3u8")
        val videoSourceRUS1 = Uri.parse("https://zabava-htlive.cdn.ngenix.net/hls/CH_RUSSIA1/variant.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Russia1HD.m3u8")
        val videoSourceFirst = Uri.parse("https://edge4.1internet.tv/dash-live2/streams/1tv-dvr/1tvdash.mpd") // ПЕРВЫЙ КАНАЛ      https://edge4.1internet.tv/dash-live2/streams/1tv-dvr/1tvdash.mpd
        val videoSourceRUSK = Uri.parse("https://s17.federal.tv:8082/fed/rossiyak.stream/chunks.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Russia_K_SD.m3u8")
        val videoSourceStar = Uri.parse("https://tvchannelstream1.tvzvezda.ru/cdn/tvzvezda/playlist_sdhigh.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Zvezda_SD.m3u8")
        val videoSourceTV1000ACT = Uri.parse("http://dmitry-tv.ml/hls/CH_TV1000ACTIONHD.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/TV1000_Action_HD/480p.m3u8")
        val videoSource5Ch = Uri.parse("https://edge01-alm.beetv.kz/bpk-token/2an@kt3by42ewha1tbt5grwm41dq3r0mpawhk3r0peaa/btv/SWM/SWM_5kanal/SWM_5kanal.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/5_OTT.m3u8")
        val videoSourceMult = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Multilandia_HD.m3u8")
        val videoSourcevhs = Uri.parse("https://s17.federal.tv:8082/fed/kkino.stream/chunks.m3u8")
        val videoSourcePobeda = Uri.parse("https://edge04-alm.beetv.kz/bpk-token/2an@geswrcf3i0gkusqha4e0qendf0brunuigunwkuba/btv/SWM/Pobeda/Pobeda_576p_2000kbps.m3u8")
        val videoSourcered = Uri.parse("https://cdn01.poster-abh.ru/red/tracks-v1a1/mono.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Sony_ET/480p.m3u8")
        val videoSourceblack = Uri.parse("https://cdn01.poster-abh.ru/black/tracks-v1a1/mono.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Sony_Turbo/480p.m3u8")
        val videoSourcehistory = Uri.parse("https://tbs01-edge11.Itdc.ge/viasathist/tracks-v1a1/mono.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Viasat_History_ad_HD/1080p.m3u8")
        val videoSourceU = Uri.parse("https://link2.rbtrack.ru/http://31.148.48.15:80/U/tracks-v1a1/mono.m3u8?&token=test")//("https://okkotv-live.cdnvideo.ru/channel/Yu_OTT.m3u8?zi/")
        val videoSourceDom = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/Dom_HD_OTT.m3u8?zi/")
        val videoSourceStart_world = Uri.parse("https://cdn.ru03.spr24.net:4443/5771/tracks-v1a1/mono.m3u8?token=Ti8qDtheX2PUkt")//("https://edge01-alm.beetv.kz/bpk-token/2an@ld33l2okuxprlwt5cdzp401xy30ip5cxlnrg2gca/btv/SWM/start_world_hd/start_world_hd.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/START_World_HD.m3u8?zi/")
        val videoSourceScifi = Uri.parse("https://cdn01.poster-abh.ru/sci-fi/tracks-v1a1/mono.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Sony_SciFi.m3u8?zi/")
        val videoSourceViasNat = Uri.parse("")//("https://okkotv-live.cdnvideo.ru/channel/Viasat_Nature_ad_HD.m3u8?zi/")
        val videoSourceViasExp = Uri.parse("https://tbs01-edge11.Itdc.ge/viasatexp/tracks-v1a1/mono.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Viasat_Explore_HD.m3u8?zi/")
        val videoSourceturist = Uri.parse("https://livetv.mylifeisgood.ml/channels/glazamiturista")
        val videoSourceChe = Uri.parse("https://link2.rbtrack.ru/http://31.148.48.15/Che/tracks-v1a1/mono.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/Che_OTT_2.m3u8?zi/")
        val videoSourceStsLove = Uri.parse("https://link2.rbtrack.ru/http://31.148.48.15:80/STS_Love/tracks-v1a1/mono.m3u8?&token=test")//("https://okkotv-live.cdnvideo.ru/channel/CTC_Love_OTT_2.m3u8?zi/")
        val videoSourceOruzie = Uri.parse("https://tbs01-edge11.itdc.ge/oruzhie/tracks-v1a1/mono.m3u8")
        val videoSourceTvc = Uri.parse("https://tvc-hls.cdnvideo.ru/tvc-res/smil:vd9221_2.smil/playlist.m3u8")
        val videoSourceVIP_Comedy = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/VIP_Comedy_HD.m3u8")
        val videoSourceVIP_Serial = Uri.parse("https://okkotv-live.cdnvideo.ru/channel/VIP_Serial_HD.m3u8")
        val videoSourceVIP_Premiere = Uri.parse("http://myott.top/stream/202.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/VIP_Premiere_HD.m3u8")
        val videoSourceVIP_Megahit = Uri.parse("https://bl.uma.media/live/485537/HLS/4614144_3/2/1/playlist.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/VIP_Megahit_HD.m3u8")
        val videoSourceRodnoe_kino = Uri.parse("https://sc.id-tv.kz/Rodnoe_kino.m3u8")
        val videoSourceMujskoe_kino = Uri.parse("https://sc.id-tv.kz/Mujskoe_kino_hd.m3u8")
        val videoSourceKinosemiya = Uri.parse("https://sc.id-tv.kz/Kinosemiya_hd.m3u8")
        val videoSourceKinouzhas = Uri.parse("https://sc.id-tv.kz/Kinouzhas_hd.m3u8")
        val videoSourceKinopremiera = Uri.parse("https://sc.id-tv.kz/Kinopremiera_hd.m3u8")
        val videoSourceKinosvidanie = Uri.parse("https://sc.id-tv.kz/Kinosvidanie_hd.m3u8")
        val videoSourceKinomix = Uri.parse("https://sc.id-tv.kz/Kinomix_hd.m3u8")
        val videoSourceKinokomediya = Uri.parse("https://sc.id-tv.kz/Kinokomediya_hd.m3u8")
        val videoSourceDomKino = Uri.parse("https://edge02-alm.beetv.kz/bpk-token/2an@ntjksdb4gjjoud4uzpem4tuehc1mss2inr0rfzba/btv/SWM/Dom_Kino/Dom_Kino_576p_2000kbps.m3u8")   //https://sc.id-tv.kz/domkino_hd.m3u8
        val videoSourceZolCol = Uri.parse("https://s17.federal.tv:8082/fed/sovkinotv.stream/playlist.m3u8")
        val videoSourceHollywood = Uri.parse("https://edge04-alm.beetv.kz/bpk-token/2an@cofusovvvqk0ve2bu5csekif30qlbvulc40ujyba/btv/SWM/Hollywood/Hollywood_576p_2000kbps.m3u8")
        val videoSourceMyPlanet = Uri.parse("https://edge02-alm.beetv.kz/bpk-token/2an@zbgicdpryajm2qxsehef1mqeggoqbsyyzdvtvzda/btv/SWM/PlanetaHD/PlanetaHD.m3u8")
        val videoSourceDomPremium = Uri.parse("https://edge02-alm.beetv.kz/bpk-token/2an@zrmhggbas3yyush54vge1zommcoe1rrjzt52r2ba/btv/SWM/Dom_kino_Prem/Dom_kino_Prem_1080p_5000kbps.m3u8")
        val videoSourcetest = Uri.parse("https://edge04-alm.beetv.kz/bpk-token/2an@gs2dvquospucc13t31d12zhkbzre5ifegcddogaa/btv/SWM/FoxLife/FoxLife_1080p_5000kbps.m3u8") //TEST///////TEST////////TEST///////TEST///
        val videoSourceAutoplus = Uri.parse("https://tbs01-edge11.itdc.ge/autoplus/tracks-v1a1/mono.m3u8")  //  АВТО ПЛЮС
        val videoSourceAuto24 = Uri.parse("https://tbs01-edge11.itdc.ge/auto24/tracks-v1a1/mono.m3u8") //    АВТО 24
        val videoSourceTerra = Uri.parse("http://kino-1.catcast.tv/content/38617/index.m3u8")//("https://okkotv-live.cdnvideo.ru/channel/NGC_HD/1080p.m3u8") // Terra

        bt_fullscreen.setOnClickListener {

            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            );

            if (Build.VERSION.SDK_INT >= 19) {
                window.decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            }

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

        getDataFromDB()

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


        val player = findViewById<PlayerView>(R.id.player)
        val zoom1 = findViewById<ImageView>(R.id.zoom1)
        val zoom2 = findViewById<ImageView>(R.id.zoom2)
        val zoom3 = findViewById<ImageView>(R.id.zoom3)

        zoom1.setOnClickListener {
            player.setResizeMode(RESIZE_MODE_FILL)
        }
        zoom2.setOnClickListener {
            player.setResizeMode(RESIZE_MODE_ZOOM)
        }
        zoom3.setOnClickListener {
            player.setResizeMode(RESIZE_MODE_FIT)
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


        val bt_plus_volum = findViewById<ImageView>(R.id.volum_plus)
        val bt_minus_volum = findViewById<ImageView>(R.id.volum_minus)
        val bt_qual_HD = findViewById<ImageView>(R.id.qual_HD)
        val bt_qual_auto = findViewById<ImageView>(R.id.qual_auto)


        val audioManager: AudioManager =
            getSystemService(AUDIO_SERVICE) as AudioManager


        bt_plus_volum.setOnClickListener {
            val plus = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + 1
            audioManager.setMediaVolume(plus)
        }

        bt_minus_volum.setOnClickListener {
            val minus = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) - 1
            audioManager.setMediaVolume(minus)
        }

        bt_qual_HD.setOnClickListener {
            startHd()
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
            onGetLast()
        }

        bt_qual_auto.setOnClickListener {
            startauto()
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
            onGetLast()
        }

        val bt_fav = findViewById<ImageView>(R.id.exo_add_fv)
        var setChanelChoose = 2


        val editFv = FvCh.edit()

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
        if (FvCh.getInt(11.toString(), 0) == 1) {
            bt_solnze_fv.visibility = View.VISIBLE
        } else {
            bt_solnze_fv.visibility = View.GONE
        }
        if (FvCh.getInt(12.toString(), 0) == 1) {
            bt_tv1000_act_fv.visibility = View.VISIBLE
        } else {
            bt_tv1000_act_fv.visibility = View.GONE
        }
        if (FvCh.getInt(13.toString(), 0) == 1) {
            bt_tv1000_fv.visibility = View.VISIBLE
        } else {
            bt_tv1000_fv.visibility = View.GONE
        }
        if (FvCh.getInt(14.toString(), 0) == 1) {
            bt_tv1000_rus_fv.visibility = View.VISIBLE
        } else {
            bt_tv1000_rus_fv.visibility = View.GONE
        }
        if (FvCh.getInt(15.toString(), 0) == 1) {
            bt_Match_fv.visibility = View.VISIBLE
        } else {
            bt_Match_fv.visibility = View.GONE
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
        if (FvCh.getInt(22.toString(), 0) == 1) {
            bt_MuzTv_fv.visibility = View.VISIBLE
        } else {
            bt_MuzTv_fv.visibility = View.GONE
        }
        if (FvCh.getInt(23.toString(), 0) == 1) {
            bt_vhs_fv.visibility = View.VISIBLE
        } else {
            bt_vhs_fv.visibility = View.GONE
        }
        if (FvCh.getInt(24.toString(), 0) == 1) {
            bt_pobeda_fv.visibility = View.VISIBLE
        } else {
            bt_pobeda_fv.visibility = View.GONE
        }
        if (FvCh.getInt(25.toString(), 0) == 1) {
            bt_red_fv.visibility = View.VISIBLE
        } else {
            bt_red_fv.visibility = View.GONE
        }
        if (FvCh.getInt(26.toString(), 0) == 1) {
            bt_black_fv.visibility = View.VISIBLE
        } else {
            bt_black_fv.visibility = View.GONE
        }
        if (FvCh.getInt(27.toString(), 0) == 1) {
            bt_history_fv.visibility = View.VISIBLE
        } else {
            bt_history_fv.visibility = View.GONE
        }
        if (FvCh.getInt(28.toString(), 0) == 1) {
            bt_U_fv.visibility = View.VISIBLE
        } else {
            bt_U_fv.visibility = View.GONE
        }
        if (FvCh.getInt(29.toString(), 0) == 1) {
            bt_Dom_fv.visibility = View.VISIBLE
        } else {
            bt_Dom_fv.visibility = View.GONE
        }
        if (FvCh.getInt(30.toString(), 0) == 1) {
            bt_turist_fv.visibility = View.VISIBLE
        } else {
            bt_turist_fv.visibility = View.GONE
        }
        if (FvCh.getInt(31.toString(), 0) == 1) {
            bt_ViasExp_fv.visibility = View.VISIBLE
        } else {
            bt_ViasExp_fv.visibility = View.GONE
        }
        if (FvCh.getInt(32.toString(), 0) == 1) {
            bt_ViasNat_fv.visibility = View.VISIBLE
        } else {
            bt_ViasNat_fv.visibility = View.GONE
        }
        if (FvCh.getInt(33.toString(), 0) == 1) {
            bt_Scifi_fv.visibility = View.VISIBLE
        } else {
            bt_Scifi_fv.visibility = View.GONE
        }
        if (FvCh.getInt(34.toString(), 0) == 1) {
            bt_Start_world_fv.visibility = View.VISIBLE
        } else {
            bt_Start_world_fv.visibility = View.GONE
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
        if (FvCh.getInt(37.toString(), 0) == 1) {
            bt_oruzie_fv.visibility = View.VISIBLE
        } else {
            bt_oruzie_fv.visibility = View.GONE
        }
        if (FvCh.getInt(38.toString(), 0) == 1) {
            bt_tvc_fv.visibility = View.VISIBLE
        } else {
            bt_tvc_fv.visibility = View.GONE
        }
        if (FvCh.getInt(39.toString(), 0) == 1) {
            bt_VIP_Comedy_fv.visibility = View.VISIBLE
        } else {
            bt_VIP_Comedy_fv.visibility = View.GONE
        }
        if (FvCh.getInt(40.toString(), 0) == 1) {
            bt_VIP_Serial_fv.visibility = View.VISIBLE
        } else {
            bt_VIP_Serial_fv.visibility = View.GONE
        }
        if (FvCh.getInt(41.toString(), 0) == 1) {
            bt_VIP_Premiere_fv.visibility = View.VISIBLE
        } else {
            bt_VIP_Premiere_fv.visibility = View.GONE
        }
        if (FvCh.getInt(42.toString(), 0) == 1) {
            bt_VIP_Megahit_fv.visibility = View.VISIBLE
        } else {
            bt_VIP_Megahit_fv.visibility = View.GONE
        }
        if (FvCh.getInt(43.toString(), 0) == 1) {
            bt_DomKino_fv.visibility = View.VISIBLE
        } else {
            bt_DomKino_fv.visibility = View.GONE
        }
        if (FvCh.getInt(44.toString(), 0) == 1) {
            bt_zolcol_fv.visibility = View.VISIBLE
        } else {
            bt_zolcol_fv.visibility = View.GONE
        }
        if (FvCh.getInt(45.toString(), 0) == 1) {
            bt_hollywood_fv.visibility = View.VISIBLE
        } else {
            bt_hollywood_fv.visibility = View.GONE
        }
        if (FvCh.getInt(46.toString(), 0) == 1) {
            bt_myplanet_fv.visibility = View.VISIBLE
        } else {
            bt_myplanet_fv.visibility = View.GONE
        }
        if (FvCh.getInt(47.toString(), 0) == 1) {
            bt_solov_fv.visibility = View.VISIBLE
        } else {
            bt_solov_fv.visibility = View.GONE
        }
        if (FvCh.getInt(48.toString(), 0) == 1) {
            bt_Terra_fv.visibility = View.VISIBLE
        } else {
            bt_Terra_fv.visibility = View.GONE
        }
        if (FvCh.getInt(49.toString(), 0) == 1) {
            bt_Auto24_fv.visibility = View.VISIBLE
        } else {
            bt_Auto24_fv.visibility = View.GONE
        }
        if (FvCh.getInt(50.toString(), 0) == 1) {
            bt_Autoplus_fv.visibility = View.VISIBLE
        } else {
            bt_Autoplus_fv.visibility = View.GONE
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
            if (FvCh.getInt(11.toString(), 0) == 1) {
                bt_solnze_fv.visibility = View.VISIBLE
            } else {
                bt_solnze_fv.visibility = View.GONE
            }
            if (FvCh.getInt(12.toString(), 0) == 1) {
                bt_tv1000_act_fv.visibility = View.VISIBLE
            } else {
                bt_tv1000_act_fv.visibility = View.GONE
            }
            if (FvCh.getInt(13.toString(), 0) == 1) {
                bt_tv1000_fv.visibility = View.VISIBLE
            } else {
                bt_tv1000_fv.visibility = View.GONE
            }
            if (FvCh.getInt(14.toString(), 0) == 1) {
                bt_tv1000_rus_fv.visibility = View.VISIBLE
            } else {
                bt_tv1000_rus_fv.visibility = View.GONE
            }
            if (FvCh.getInt(15.toString(), 0) == 1) {
                bt_Match_fv.visibility = View.VISIBLE
            } else {
                bt_Match_fv.visibility = View.GONE
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
            if (FvCh.getInt(22.toString(), 0) == 1) {
                bt_MuzTv_fv.visibility = View.VISIBLE
            } else {
                bt_MuzTv_fv.visibility = View.GONE
            }
            if (FvCh.getInt(23.toString(), 0) == 1) {
                bt_vhs_fv.visibility = View.VISIBLE
            } else {
                bt_vhs_fv.visibility = View.GONE
            }
            if (FvCh.getInt(24.toString(), 0) == 1) {
                bt_pobeda_fv.visibility = View.VISIBLE
            } else {
                bt_pobeda_fv.visibility = View.GONE
            }
            if (FvCh.getInt(25.toString(), 0) == 1) {
                bt_red_fv.visibility = View.VISIBLE
            } else {
                bt_red_fv.visibility = View.GONE
            }
            if (FvCh.getInt(26.toString(), 0) == 1) {
                bt_black_fv.visibility = View.VISIBLE
            } else {
                bt_black_fv.visibility = View.GONE
            }
            if (FvCh.getInt(27.toString(), 0) == 1) {
                bt_history_fv.visibility = View.VISIBLE
            } else {
                bt_history_fv.visibility = View.GONE
            }
            if (FvCh.getInt(28.toString(), 0) == 1) {
                bt_U_fv.visibility = View.VISIBLE
            } else {
                bt_U_fv.visibility = View.GONE
            }
            if (FvCh.getInt(29.toString(), 0) == 1) {
                bt_Dom_fv.visibility = View.VISIBLE
            } else {
                bt_Dom_fv.visibility = View.GONE
            }
            if (FvCh.getInt(30.toString(), 0) == 1) {
                bt_turist_fv.visibility = View.VISIBLE
            } else {
                bt_turist_fv.visibility = View.GONE
            }
            if (FvCh.getInt(31.toString(), 0) == 1) {
                bt_ViasExp_fv.visibility = View.VISIBLE
            } else {
                bt_ViasExp_fv.visibility = View.GONE
            }
            if (FvCh.getInt(32.toString(), 0) == 1) {
                bt_ViasNat_fv.visibility = View.VISIBLE
            } else {
                bt_ViasNat_fv.visibility = View.GONE
            }
            if (FvCh.getInt(33.toString(), 0) == 1) {
                bt_Scifi_fv.visibility = View.VISIBLE
            } else {
                bt_Scifi_fv.visibility = View.GONE
            }
            if (FvCh.getInt(34.toString(), 0) == 1) {
                bt_Start_world_fv.visibility = View.VISIBLE
            } else {
                bt_Start_world_fv.visibility = View.GONE
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
            if (FvCh.getInt(37.toString(), 0) == 1) {
                bt_oruzie_fv.visibility = View.VISIBLE
            } else {
                bt_oruzie_fv.visibility = View.GONE
            }
            if (FvCh.getInt(38.toString(), 0) == 1) {
                bt_tvc_fv.visibility = View.VISIBLE
            } else {
                bt_tvc_fv.visibility = View.GONE
            }
            if (FvCh.getInt(39.toString(), 0) == 1) {
                bt_VIP_Comedy_fv.visibility = View.VISIBLE
            } else {
                bt_VIP_Comedy_fv.visibility = View.GONE
            }
            if (FvCh.getInt(40.toString(), 0) == 1) {
                bt_VIP_Serial_fv.visibility = View.VISIBLE
            } else {
                bt_VIP_Serial_fv.visibility = View.GONE
            }
            if (FvCh.getInt(41.toString(), 0) == 1) {
                bt_VIP_Premiere_fv.visibility = View.VISIBLE
            } else {
                bt_VIP_Premiere_fv.visibility = View.GONE
            }
            if (FvCh.getInt(42.toString(), 0) == 1) {
                bt_VIP_Megahit_fv.visibility = View.VISIBLE
            } else {
                bt_VIP_Megahit_fv.visibility = View.GONE
            }
            if (FvCh.getInt(43.toString(), 0) == 1) {
                bt_DomKino_fv.visibility = View.VISIBLE
            } else {
                bt_DomKino_fv.visibility = View.GONE
            }
            if (FvCh.getInt(44.toString(), 0) == 1) {
                bt_zolcol_fv.visibility = View.VISIBLE
            } else {
                bt_zolcol_fv.visibility = View.GONE
            }
            if (FvCh.getInt(45.toString(), 0) == 1) {
                bt_hollywood_fv.visibility = View.VISIBLE
            } else {
                bt_hollywood_fv.visibility = View.GONE
            }
            if (FvCh.getInt(46.toString(), 0) == 1) {
                bt_myplanet_fv.visibility = View.VISIBLE
            } else {
                bt_myplanet_fv.visibility = View.GONE
            }
            if (FvCh.getInt(47.toString(), 0) == 1) {
                bt_solov_fv.visibility = View.VISIBLE
            } else {
                bt_solov_fv.visibility = View.GONE
            }
            if (FvCh.getInt(48.toString(), 0) == 1) {
                bt_Terra_fv.visibility = View.VISIBLE
            } else {
                bt_Terra_fv.visibility = View.GONE
            }
            if (FvCh.getInt(49.toString(), 0) == 1) {
                bt_Auto24_fv.visibility = View.VISIBLE
            } else {
                bt_Auto24_fv.visibility = View.GONE
            }
            if (FvCh.getInt(50.toString(), 0) == 1) {
                bt_Autoplus_fv.visibility = View.VISIBLE
            } else {
                bt_Autoplus_fv.visibility = View.GONE
            }

        }


        bt_test_chanel.setOnClickListener {
            setChanel(videoSourcetest)
            onSaveLast(videoSourcetest)
        }

        bt_myplanet_fv.setOnClickListener {
            setChanel(videoSourceMyPlanet)
            onSaveLast(videoSourceMyPlanet)
        }

        bt_solov_fv.setOnClickListener {
            setChanel(listUri[32].toUri())
            onSaveLast(listUri[32].toUri())
        }

        bt_Terra_fv.setOnClickListener {
            setChanel(videoSourceTerra)
            onSaveLast(videoSourceTerra)
        }

        bt_Auto24_fv.setOnClickListener {
            setChanel(listUri[29].toUri())
            onSaveLast(listUri[29].toUri())
        }

        bt_Autoplus_fv.setOnClickListener {
            setChanel(listUri[30].toUri())
            onSaveLast(listUri[30].toUri())
        }

        bt_zolcol_fv.setOnClickListener {
            setChanel(listUri[28].toUri())
            onSaveLast(listUri[28].toUri())
        }

        bt_hollywood_fv.setOnClickListener {
            setChanel(videoSourceHollywood)
            onSaveLast(videoSourceHollywood)
        }

        bt_black_fv.setOnClickListener {
            setChanel(listUri[16].toUri())
            onSaveLast(listUri[16].toUri())
        }

        bt_Che_fv.setOnClickListener {
            setChanel(listUri[23].toUri())
            onSaveLast(listUri[23].toUri())
        }

        bt_Start_world_fv.setOnClickListener {
            setChanel(videoSourceStart_world)
            onSaveLast(videoSourceStart_world)
        }
        bt_StsLove_fv.setOnClickListener {
            setChanel(listUri[11].toUri())
            onSaveLast(listUri[11].toUri())
        }
        bt_ViasNat_fv.setOnClickListener {
            setChanel(videoSourceViasNat)
            onSaveLast(videoSourceViasNat)
        }
        bt_ViasExp_fv.setOnClickListener {
            setChanel(listUri[26].toUri())
            onSaveLast(listUri[26].toUri())
        }
        bt_Scifi_fv.setOnClickListener {
            setChanel(listUri[24].toUri())
            onSaveLast(listUri[24].toUri())
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
            setChanel(listUri[15].toUri())
            onSaveLast(listUri[15].toUri())
        }
        bt_pobeda_fv.setOnClickListener {
            setChanel(videoSourcePobeda)
            onSaveLast(videoSourcePobeda)
        }
        bt_U_fv.setOnClickListener {
            setChanel(listUri[22].toUri())
            onSaveLast(listUri[22].toUri())
        }
        bt_history_fv.setOnClickListener {
            setChanel(listUri[25].toUri())
            onSaveLast(listUri[25].toUri())
        }

        bt_pyatniza_fv.setOnClickListener {
            setChanel(listUri[17].toUri())
            onSaveLast(listUri[17].toUri())
        }

        bt_MuzTv_fv.setOnClickListener {
            setChanel(listUri[18].toUri())
            onSaveLast(listUri[18].toUri())
        }

        bt_IZ_fv.setOnClickListener {
            setChanel(listUri[7].toUri())
            onSaveLast(listUri[7].toUri())
        }

        bt_TNT_fv.setOnClickListener {
            setChanel(listUri[19].toUri())
            onSaveLast(listUri[19].toUri())
        }

        bt_Spas_fv.setOnClickListener {
            setChanel(listUri[27].toUri())
            onSaveLast(listUri[27].toUri())
        }

        bt_TV3_fv.setOnClickListener {
            setChanel(listUri[21].toUri())
            onSaveLast(listUri[21].toUri())
        }

        bt_NTV_fv.setOnClickListener {
            setChanel(listUri[9].toUri())
            onSaveLast(listUri[9].toUri())
        }

        bt_tv1000_fv.setOnClickListener {
            setChanel(videoSourceTV1000)
            onSaveLast(videoSourceTV1000)
        }

        bt_Match_fv.setOnClickListener {
            setChanel(listUri[10].toUri())
            onSaveLast(listUri[10].toUri())
        }

        bt_tv1000_rus_fv.setOnClickListener {
            setChanel(listUri[20].toUri())
            onSaveLast(listUri[20].toUri())
        }

        bt_solnze_fv.setOnClickListener {
            setChanel(listUri[31].toUri())
            onSaveLast(listUri[31].toUri())
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
            setChanel(listUri[4].toUri())
            onSaveLast(listUri[4].toUri())
        }

        bt_star_fv.setOnClickListener {
            setChanel(listUri[5].toUri())
            onSaveLast(listUri[5].toUri())
        }

        bt_vhs_fv.setOnClickListener {
            setChanel(listUri[13].toUri())
            onSaveLast(listUri[13].toUri())
        }

        bt_tv1000_act_fv.setOnClickListener {
            setChanel(videoSourceTV1000ACT)
            onSaveLast(videoSourceTV1000ACT)
        }

        bt_mir_fv.setOnClickListener {
            setChanel(listUri[6].toUri())
            onSaveLast(listUri[6].toUri())
        }

        bt_ren_fv.setOnClickListener {
            setChanel(listUri[12].toUri())
            onSaveLast(listUri[12].toUri())
        }

        bt_r1_fv.setOnClickListener {
            setChanel(listUri[2].toUri())
            onSaveLast(listUri[2].toUri())
        }

        bt_r24_fv.setOnClickListener {
            setChanel(listUri[3].toUri())
            onSaveLast(listUri[3].toUri())
        }

        bt_sts_fv.setOnClickListener {
            setChanel(videoSourceSTS)
            onSaveLast(videoSourceSTS)
        }

        bt_1st_fv.setOnClickListener {
            setChanel(listUri[1].toUri())
            onSaveLast(listUri[1].toUri())
        }
        bt_oruzie_fv.setOnClickListener {
            setChanel(listUri[14].toUri())
            onSaveLast(listUri[14].toUri())
        }
        bt_tvc_fv.setOnClickListener {
            setChanel(listUri[8].toUri())
            onSaveLast(listUri[8].toUri())
        }

        bt_VIP_Premiere_fv.setOnClickListener {
            setChanel(videoSourceVIP_Premiere)
            onSaveLast(videoSourceVIP_Premiere)
        }

        bt_VIP_Megahit_fv.setOnClickListener {
            setChanel(videoSourceVIP_Megahit)
            onSaveLast(videoSourceVIP_Megahit)
        }

        bt_VIP_Serial_fv.setOnClickListener {
            setChanel(videoSourceVIP_Serial)
            onSaveLast(videoSourceVIP_Serial)
        }

        bt_VIP_Comedy_fv.setOnClickListener {
            setChanel(videoSourceVIP_Comedy)
            onSaveLast(videoSourceVIP_Comedy)
        }

        bt_DomKino_fv.setOnClickListener {
            setChanel(videoSourceDomKino)
            onSaveLast(videoSourceDomKino)
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




        bt_terra.setOnClickListener {
            setChanel(videoSourceTerra)
            onSaveLast(videoSourceTerra)
            setChanelChoose = 48
        }

        bt_auto24.setOnClickListener {
            setChanel(listUri[29].toUri())
            onSaveLast(listUri[29].toUri())
            setChanelChoose = 49
        }

        bt_autoplus.setOnClickListener {
            setChanel(listUri[30].toUri())
            onSaveLast(listUri[30].toUri())
            setChanelChoose = 50
        }

        bt_myplanet.setOnClickListener {
            setChanel(videoSourceMyPlanet)
            onSaveLast(videoSourceMyPlanet)
            setChanelChoose = 46
        }

        bt_hollywood.setOnClickListener {
            setChanel(videoSourceHollywood)
            onSaveLast(videoSourceHollywood)
            setChanelChoose = 45
        }

        bt_zolcol.setOnClickListener {
            setChanel(listUri[28].toUri())
            onSaveLast(listUri[28].toUri())
            setChanelChoose = 44
        }

        bt_VIP_Premiere.setOnClickListener {
            setChanel(videoSourceVIP_Premiere)
            onSaveLast(videoSourceVIP_Premiere)
            setChanelChoose = 41
        }


        bt_DomKino.setOnClickListener {
            setChanel(videoSourceDomKino)
            onSaveLast(videoSourceDomKino)
            setChanelChoose = 43
        }

        bt_VIP_Megahit.setOnClickListener {
            setChanel(videoSourceVIP_Megahit)
            onSaveLast(videoSourceVIP_Megahit)
            setChanelChoose = 42
        }

        bt_VIP_Serial.setOnClickListener {
            setChanel(videoSourceVIP_Serial)
            onSaveLast(videoSourceVIP_Serial)
            setChanelChoose = 40
        }

        bt_VIP_Comedy.setOnClickListener {
            setChanel(videoSourceVIP_Comedy)
            onSaveLast(videoSourceVIP_Comedy)
            setChanelChoose = 39
        }

        bt_black.setOnClickListener {
            setChanel(listUri[16].toUri())
            onSaveLast(listUri[16].toUri())
            setChanelChoose = 26
        }

        bt_Che.setOnClickListener {
            setChanel(listUri[23].toUri())
            onSaveLast(listUri[23].toUri())
            setChanelChoose = 35
        }

        bt_Start_world.setOnClickListener {
            setChanel(videoSourceStart_world)
            onSaveLast(videoSourceStart_world)
            setChanelChoose = 34
        }
        bt_StsLove.setOnClickListener {
            setChanel(listUri[11].toUri())
            onSaveLast(listUri[11].toUri())
            setChanelChoose = 36
        }
        bt_ViasNat.setOnClickListener {
            setChanel(videoSourceViasNat)
            onSaveLast(videoSourceViasNat)
            setChanelChoose = 32
        }
        bt_ViasExp.setOnClickListener {
            setChanel(listUri[26].toUri())
            onSaveLast(listUri[26].toUri())
            setChanelChoose = 31
        }
        bt_Scifi.setOnClickListener {
            setChanel(listUri[24].toUri())
            onSaveLast(listUri[24].toUri())
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
            setChanel(listUri[15].toUri())
            onSaveLast(listUri[15].toUri())
            setChanelChoose = 25
        }
        bt_pobeda.setOnClickListener {
            setChanel(videoSourcePobeda)
            onSaveLast(videoSourcePobeda)
            setChanelChoose = 24
        }
        bt_U.setOnClickListener {
            setChanel(listUri[22].toUri())
            onSaveLast(listUri[22].toUri())
            setChanelChoose = 28
        }
        bt_history.setOnClickListener {
            setChanel(listUri[25].toUri())
            onSaveLast(listUri[25].toUri())
            setChanelChoose = 27
        }

        bt_pyatniza.setOnClickListener {
            setChanel(listUri[17].toUri())
            onSaveLast(listUri[17].toUri())
            setChanelChoose = 21
        }

        bt_MuzTv.setOnClickListener {
            setChanel(listUri[18].toUri())
            onSaveLast(listUri[18].toUri())
            setChanelChoose = 22
        }

        bt_IZ.setOnClickListener {
            setChanel(listUri[7].toUri())
            onSaveLast(listUri[7].toUri())
            setChanelChoose = 20
        }

        bt_TNT.setOnClickListener {
            setChanel(listUri[19].toUri())
            onSaveLast(listUri[19].toUri())
            setChanelChoose = 19
        }

        bt_Spas.setOnClickListener {
            setChanel(listUri[27].toUri())
            onSaveLast(listUri[27].toUri())
            setChanelChoose = 18
        }

        bt_TV3.setOnClickListener {
            setChanel(listUri[21].toUri())
            onSaveLast(listUri[21].toUri())
            setChanelChoose = 17
        }

        bt_NTV.setOnClickListener {
            setChanel(listUri[9].toUri())
            onSaveLast(listUri[9].toUri())
            setChanelChoose = 16
        }

        bt_tv1000.setOnClickListener {
            setChanel(videoSourceTV1000)
            onSaveLast(videoSourceTV1000)
            setChanelChoose = 13
        }

        bt_Match.setOnClickListener {
            setChanel(listUri[10].toUri())
            onSaveLast(listUri[10].toUri())
            setChanelChoose = 15
        }

        bt_tv1000_rus.setOnClickListener {
            setChanel(listUri[20].toUri())
            onSaveLast(listUri[20].toUri())
            setChanelChoose = 14
        }

        bt_solnze.setOnClickListener {
            setChanel(listUri[31].toUri())
            onSaveLast(listUri[31].toUri())
            setChanelChoose = 11

        }

        bt_5CH.setOnClickListener {
            setChanel(videoSource5Ch)
            onSaveLast(videoSource5Ch)
            setChanelChoose = 8
        }

        bt_solov.setOnClickListener {
            setChanel(listUri[32].toUri())
            onSaveLast(listUri[32].toUri())
            setChanelChoose = 47
        }

        bt_mult.setOnClickListener {
            setChanel(videoSourceMult)
            onSaveLast(videoSourceMult)
            setChanelChoose = 7

        }

        bt_rk.setOnClickListener {
            setChanel(listUri[4].toUri())
            onSaveLast(listUri[4].toUri())
            setChanelChoose = 9
        }

        bt_star.setOnClickListener {
            setChanel(listUri[5].toUri())
            onSaveLast(listUri[5].toUri())
            setChanelChoose = 10
        }

        bt_vhs.setOnClickListener {
            setChanel(listUri[13].toUri())
            onSaveLast(listUri[13].toUri())
            setChanelChoose = 23
        }

        bt_tv1000_act.setOnClickListener {
            setChanel(videoSourceTV1000ACT)
            onSaveLast(videoSourceTV1000ACT)
            setChanelChoose = 12
        }

        bt_mir.setOnClickListener {
            setChanel(listUri[6].toUri())
            onSaveLast(listUri[6].toUri())
            setChanelChoose = 6
        }

        bt_ren.setOnClickListener {
            setChanel(listUri[12].toUri())
            onSaveLast(listUri[12].toUri())
            setChanelChoose = 1
        }

        bt_r1.setOnClickListener {
            setChanel(listUri[2].toUri())
            onSaveLast(listUri[2].toUri())
            setChanelChoose = 3
        }

        bt_r24.setOnClickListener {
            setChanel(listUri[3].toUri())
            onSaveLast(listUri[3].toUri())
            setChanelChoose = 4
        }

        bt_sts.setOnClickListener {
            setChanel(videoSourceSTS)
            onSaveLast(videoSourceSTS)
            setChanelChoose = 5
        }

        bt_1st.setOnClickListener {
            //onClickSave()
            //getDataFromDB()
            setChanel(listUri[1].toUri())
            onSaveLast(listUri[1].toUri())
            setChanelChoose = 2
        }

        bt_oruzie.setOnClickListener {
            setChanel(listUri[14].toUri())
            onSaveLast(listUri[14].toUri())
            setChanelChoose = 37
        }

        bt_tvc.setOnClickListener {
            setChanel(listUri[8].toUri())
            onSaveLast(listUri[8].toUri())
            setChanelChoose = 38
        }

        


        bt_plus.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Внимание!")
            builder.setMessage("Сейчас будет показана реклама длительностью ~25 сек.\n" +
                    "После ее просмотра , будут добавленны каналы:\n" +
                    "МУЖСКОЕ КИНО\n" +
                    "РОДНОЕ КИНО\n" +
                    "КИНОМИКС\n" +
                    "КИНОУЖАС\n" +
                    "КИНОСЕМЬЯ\n" +
                    "КИНОПРЕМЬЕРА\n" +
                    "КИНОКОМЕДИЯ \n" +
                    "КИНОСВИДАНИЕ")
           // builder.setIcon(android.R.drawable.ic_dialog_alert)

            builder.setPositiveButton("Смотреть") { dialogInterface, which ->
                rewardedAdLoader = RewardedAdLoader(this).apply {
                    setAdLoadListener(this@WatchActivity)
                }
                rewardedAdLoader?.loadAd(AdRequestConfiguration.Builder(adUnitIdRew).build())

            }
            builder.setNeutralButton("Отмена") { dialogInterface, which ->

            }
            val alertDialog: AlertDialog = builder.create()

            alertDialog.setCancelable(false)
            alertDialog.show()
        }



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

        //videoSource = Uri.parse("https://cdn10.1internet.tv/dash-live12/streams/1tv/1tvdash.mpd") //https://cdn10.1internet.tv/dash-live12/streams/1tv/1tvdash.mpd

        onGetLast()


    }


    fun onClickSave() {
        val uriString = "https://edge4.1internet.tv/dash-live2/streams/1tv-dvr/1tvdash.mpd"
        val id = "Россия 1"
        val newUser = DB(id,uriString)
        mDataBase!!.push().setValue(newUser)
        Toast("Сохранено")
    }

    fun Toast(massege:String){
        Toast.makeText(this, massege, Toast.LENGTH_SHORT).show()
    }
    val listUri = mutableListOf("")
    fun getDataFromDB() {
        listUri.clear()
        listUri.add("")
        val vListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val user: DB = ds.getValue(DB::class.java)!!
                    listUri.add(user.uriString.toString())
                }
//                val first = listUri[1]
//                setChanel(first.toUri())
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        mDataBase!!.addValueEventListener(vListener)
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

//    private fun loadRewarded() {
//            destroyRewarded()
//            createRewarded()
//        rewardedAd?.loadAd(AdRequest.Builder().build())
//    }
//
//    private fun createRewarded() {
//        rewardedAd = RewardedAd(this).apply {
//            setAdUnitId(adUnitIdRew)
//            setRewardedAdEventListener(eventLoggerRew)
//        }
//    }
//
//    private fun destroyRewarded() {
//        rewardedAd?.destroy()
//        rewardedAd = null
//    }
//
//
//    private inner class RewardedAdEventLogger : RewardedAdEventListener {
//
//        override fun onAdLoaded() {
//           rewardedAd?.show()
//        }
//
//        override fun onAdFailedToLoad(error: AdRequestError) {
//        }
//
//        override fun onAdShown() {
//        }
//
//        override fun onAdDismissed() {
//        }
//
//        override fun onRewarded(reward: Reward) {
//            val add_ch = findViewById<LinearLayout>(R.id.add_ch)
//            val bt_plus = findViewById<ImageView>(R.id.exo_plus)
//            add_ch.visibility = View.VISIBLE
//            bt_plus.visibility = View.GONE
//        }
//
//        override fun onAdClicked() {
//        }
//
//        override fun onLeftApplication() {
//        }
//
//        override fun onReturnedToApplication() {
//        }
//
//        override fun onImpression(data: ImpressionData?) {
//        }
//    }

    fun startHd() {
        simpleExoPlayer.stop()
        simpleExoPlayer = SimpleExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            //.setTrackSelector(trackSelector)
            .build()

    }

    fun startauto() {
        val trackSelector = DefaultTrackSelector(this)
        trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd())
        simpleExoPlayer.stop()
        simpleExoPlayer = SimpleExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .setTrackSelector(trackSelector)
            .build()
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

    fun setChanel(Source: Uri) {
        simpleExoPlayer.removeMediaItem(1)
        val mediaItem = MediaItem.fromUri(Source)
        simpleExoPlayer.setMediaItem(mediaItem)
        simpleExoPlayer.prepare()
        simpleExoPlayer.play()
    }

    override fun onStop() {
        super.onStop()
        simpleExoPlayer.stop()
    }
    private fun destroyRewardedAd() {
        // don't forget to clean up event listener to null?
        rewardedAd?.setAdEventListener(null)
        rewardedAd = null
    }
    override fun onDestroy() {
        rewardedAdLoader?.setAdLoadListener(null)
        rewardedAdLoader = null
        destroyRewardedAd()
        interstitialAdLoader?.setAdLoadListener(null)
        interstitialAdLoader = null
        destroyInterstitial()
        //destroyRewarded()
        super.onDestroy()
        simpleExoPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.pause()
    }

    fun AudioManager.setMediaVolume(volumeIndex: Int) {
        // Set media volume level
        this.setStreamVolume(
            AudioManager.STREAM_MUSIC, // Stream type
            volumeIndex, // Volume index
            AudioManager.FLAG_SHOW_UI// Flags
        )
    }


}