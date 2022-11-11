package com.example.pdf.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pdf.R;
import com.example.pdf.model.DataBox;
import com.example.pdf.model.Document;
import com.example.pdf.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {
    private DataBox dataBox;
    private User buyUser;
    private Document document;
    private TextView documentNameTextView;
    private ImageView documentImageView;
    private TextView documentReleaseDateTextView;
    private TextView documentAuthorTextView;
    private TextView documentDescriptionTextView;
    private TextView documentPriceTextView;
    private Toolbar toolbar;

    private long currCoin;

    private DatabaseReference coinReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dataBox = (DataBox) getIntent().getSerializableExtra("dataBox");
        document = dataBox.getDocument();
        buyUser = dataBox.getUser();

        toolbar = findViewById(R.id.detail_toolbar);
        documentNameTextView = findViewById(R.id.detail_doc_name);
        documentImageView = findViewById(R.id.detail_doc_image);
        documentReleaseDateTextView = findViewById(R.id.detail_doc_release_date);
        documentAuthorTextView = findViewById(R.id.detail_doc_author);
        documentDescriptionTextView = findViewById(R.id.detail_doc_description);
        documentPriceTextView = findViewById(R.id.detail_doc_price);

        coinReference = FirebaseDatabase.getInstance().getReference(String.format("%s/coin", buyUser.getUserName()));
        coinReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currCoin = (Long) snapshot.getValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        documentPriceTextView.setText(Double.toString(document.getDocumentPrice())+ " coins");
    }

    public void buy(View view){
        if (currCoin < document.getDocumentPrice()){

        }

        double r = currCoin - document.getDocumentPrice();
        Log.i("1", "This is: "+ r);
    }
}