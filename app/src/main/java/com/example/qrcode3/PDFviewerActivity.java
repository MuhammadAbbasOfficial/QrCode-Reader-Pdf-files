package com.example.qrcode3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



public class PDFviewerActivity extends AppCompatActivity {

  //  PDFView pdfView;
    //PDFView pdfView;
//    PDFView pdfView;

    PDFView pdfView;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        pd = new ProgressDialog(PDFviewerActivity.this);
                pd.setTitle("Wait...");
        pd.setMessage("Pdf is fetching...");


        pdfView = findViewById(R.id.pdfReader);


        //String url1 ="http://www.africau.edu/images/default/sample.pdf";
        String url = getIntent().getStringExtra("url");
        new pdfDownload().execute(url);

    }


        class  pdfDownload extends AsyncTask <String, Void , InputStream>
        {

            @Override
            protected void onPreExecute() {
                pd.show();
            }

            @Override
            protected InputStream doInBackground(String... strings) {
                InputStream inputStream = null;

                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    if (urlConnection.getResponseCode() == 200)
                    {
                        inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


                return inputStream;
            }

            @Override
            protected void onPostExecute(InputStream inputStream) {
                pdfView.fromStream(inputStream).onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        pd.dismiss();
                    }
                }).load();

            }
        }



}