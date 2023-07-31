package com.hellokhdev.sovary.rustv.tvprogram;



import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.newdev.beta.rustv.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;


public class tvprogramm extends AppCompatActivity {


    private Thread setSecondThread;
    private Runnable runnable;
    private Document doc;
    RecyclerView recyclerView;
    ArrayList< String > Chanel_name = new ArrayList<>();
    ArrayList< String > event = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_prog);
        FullScreencall();
        TextView wait = findViewById(R.id.wait);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        for (int j = 0;j<25;j++) {
            init(j);
        }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    HelperAdapter helperAdapter = new HelperAdapter(Chanel_name,event,tvprogramm.this);
                    recyclerView.setAdapter(helperAdapter);
                    wait.setText("");
                }
            }, 3000);

    }
    private void init(int i){
        runnable = new Runnable() {
            @Override
            public void run() {
                getWeb(i);
            }
        };
        setSecondThread = new Thread(runnable);
        setSecondThread.start();
    }

    private void getWeb(int i){
        String url = "https://programma-peredach.com/moskva/";
        String Events = "";
        try {
            doc = Jsoup.connect(url).get();
            Elements selections = doc.getElementsByClass("channel");
            Element selection = selections.get(i);
            Element Chanel = selection.getElementsByClass("title").first();
            Elements eventtime = selection.getElementsByClass("proTime");
            Elements eventname = selection.getElementsByClass("proTitle");
            Events = "";
            for (int j = 0;j<eventname.size();j++) {
                 Events = Events + "\n" + eventtime.get(j).text() + "  " + eventname.get(j).text();
            }

            Chanel_name.add(Chanel.text());
            event.add(Events);
            Log.d("test_tv","Канал: " + Chanel.text() + Events);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void FullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}

