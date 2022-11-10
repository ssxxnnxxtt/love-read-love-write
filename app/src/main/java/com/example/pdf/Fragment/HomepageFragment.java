package com.example.pdf.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pdf.OnPdfFileSelectListener;
import com.example.pdf.PdfAdapter;
import com.example.pdf.R;
import com.example.pdf.activity.DetailActivity;
import com.example.pdf.activity.ReadPdfActivity;
import com.example.pdf.model.Document;
import com.example.pdf.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomepageFragment extends Fragment {
    private View view;
    private List<Document> documents;
    private Context context;
    private PdfAdapter pdfAdapter;
    private RecyclerView recyclerView;
    private User user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepage, container, false);
        this.context = getActivity();

        Intent i = getActivity().getIntent();
        user = (User) i.getSerializableExtra("userObj");

        displayPdf();
        return view;
    }

    private void displayPdf(){
        recyclerView = view.findViewById(R.id.homepage_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));

        documents = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (!ds.getKey().matches(user.getUserName())){
                        if (ds.child("create").exists()) {
                            for (DataSnapshot createDs: ds.child("create").getChildren()) {
                                Document doc = createDs.getValue(com.example.pdf.model.Document.class);
                                documents.add(doc);
                            }
                        }
                    }
                }

                OnPdfFileSelectListener onDetail = new OnPdfFileSelectListener() {
                    @Override
                    public void onPdfSelected(Document document) {
                        startActivity(new Intent(context, DetailActivity.class)
                                .putExtra("docObj", document));
                    }
                };

                pdfAdapter = new PdfAdapter(context, documents, onDetail);
                recyclerView.setAdapter(pdfAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
