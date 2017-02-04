package com.appliepi.android.test;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    private String htmlPageUrl = "http://www.sunrint.hs.kr/18602/subMenu.do";
    private TextView textviewHtmlDocument;
    private String htmlContentInStringFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textviewHtmlDocument = (TextView)findViewById(R.id.textView);
        textviewHtmlDocument.setMovementMethod(new ScrollingMovementMethod());

        Button htmlTitleButton = (Button)findViewById(R.id.button);
        htmlTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
            }
        });
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(htmlPageUrl).get();
                Elements links = doc.select("#contents_195696");

//                for (Element link : links) {
                htmlContentInStringFormat += links.html().trim() + "\n";
//                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("asdf", htmlContentInStringFormat);

            String pureText = getText(htmlContentInStringFormat);
            textviewHtmlDocument.setText(pureText);
        }

        private String getText(String content){
            Pattern SCRIPTS = Pattern.compile("<(no)?script[^>]*>.*?</(no)?script>", Pattern.DOTALL);
            Pattern STYLE = Pattern.compile("<style[^>]*>.*</style>", Pattern.DOTALL);
            Pattern TAGS = Pattern.compile("<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>");
            Pattern nTAGS = Pattern.compile("<\\w+\\s+[^<]*\\s*>");
            Pattern ENTITY_REFS = Pattern.compile("&[^;]+;");
            Pattern WHITESPACE = Pattern.compile("\\s\\s+");

            Matcher m;

            m = SCRIPTS.matcher(content);
            content = m.replaceAll("");
            m = STYLE.matcher(content);
            content = m.replaceAll("");
            m = TAGS.matcher(content);
            content = m.replaceAll("");
            m = nTAGS.matcher(content);
            content = m.replaceAll("");
            m = ENTITY_REFS.matcher(content);
            content = m.replaceAll("");
            m = WHITESPACE.matcher(content);
            content = m.replaceAll(" ");

            return content;
        }
    }



}
