package com.example.pdf.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.pdf.R;
import com.example.pdf.model.Document;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {
    private Document document;
    private TextView documentNameTextView;
    private ImageView documentImageView;
    private TextView documentReleaseDateTextView;
    private TextView documentAuthorTextView;
    private TextView documentDescriptionTextView;
    private TextView documentPriceTextView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        document = document = (Document) getIntent().getSerializableExtra("docObj");
        toolbar = findViewById(R.id.detail_toolbar);

        documentNameTextView = findViewById(R.id.detail_doc_name);
        documentImageView = findViewById(R.id.detail_doc_image);
        documentReleaseDateTextView = findViewById(R.id.detail_doc_release_date);
        documentAuthorTextView = findViewById(R.id.detail_doc_author);
        documentDescriptionTextView = findViewById(R.id.detail_doc_description);
        documentPriceTextView = findViewById(R.id.detail_doc_price);

        displayDetail();

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

    private void displayDetail(){
        documentNameTextView.setText(document.getDocumentName());
        Picasso.with(this).load(document.getDocumentImage()).into(documentImageView);
        documentReleaseDateTextView.setText(document.getDocumentRelease());
        documentAuthorTextView.setText(document.getDocumentAuthor());
        documentDescriptionTextView.setText(document.getDocumentDescription());
        documentPriceTextView.setText(Double.toString(document.getDocumentPrice())+ " Baht.");
    }
}