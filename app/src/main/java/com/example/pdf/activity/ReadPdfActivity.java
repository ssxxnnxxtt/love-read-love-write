package com.example.pdf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.example.pdf.R;
import com.example.pdf.model.DataBox;
import com.example.pdf.model.Document;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadPdfActivity extends AppCompatActivity {
    private DataBox dataBox;
    private Document document;
    private String path = "";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_reader);
        toolbar = findViewById(R.id.read_toolbar);

        dataBox = (DataBox) getIntent().getSerializableExtra("dataBox");
        document = dataBox.getDocument();
        path = document.getDocumentUri();

        PDFView pdfView = findViewById(R.id.pdfView);
        displayFromUrl(pdfView, path);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displayFromUrl(PDFView pdfView, String pathURL){
        class RetrivePDFStream extends AsyncTask<String, Void, InputStream>{

            @Override
            protected InputStream doInBackground(String... strings) {
                InputStream inputStream = null;
                try {
                    URL uri = new URL(strings[0]);
                    HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();
                    if (urlConnection.getResponseCode() == 200) {
                        inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    }
                } catch (IOException e) { return null; }
                return inputStream;
            }

            protected void onPostExecute(InputStream inputStream) {
                pdfView.fromStream(inputStream).load();
            }
        }

        new RetrivePDFStream().execute(pathURL);
    }
}