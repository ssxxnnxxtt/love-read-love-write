package com.example.pdf.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.pdf.activity.ReadPdfActivity;
import com.example.pdf.model.DataBox;
import com.example.pdf.model.Document;
import com.example.pdf.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CollectionsFragment extends Fragment {
    private PdfAdapter pdfAdapter;
    private User user;
    private RecyclerView recyclerView;
    private View view;
    private Context context;

    private List<Document> documents;
    private List<DataBox> dataBoxes;

    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_collections, container, false);
        this.context = getActivity();

        Intent i = getActivity().getIntent();
        user = (User) i.getSerializableExtra("userObj");

        displayPdf();

        return view;
    }

    private void displayPdf(){
        recyclerView = view.findViewById(R.id.collection_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));

        databaseReference = FirebaseDatabase.getInstance().getReference(String.format("%s/collection", user.getUserName()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                documents = new ArrayList<>();
                dataBoxes = new ArrayList<>();

                for (DataSnapshot ds: snapshot.getChildren()){
                    Document doc = ds.getValue(com.example.pdf.model.Document.class);

                    DataBox dataBox = new DataBox();
                    dataBox.setUser(user);
                    dataBox.setDocument(doc);
                    dataBoxes.add(dataBox);
                    documents.add(doc);
                }

                OnPdfFileSelectListener onPdf = new OnPdfFileSelectListener() {
                    @Override
                    public void onPdfSelected(DataBox dataBox) {
                        startActivity(new Intent(context, ReadPdfActivity.class)
                                .putExtra("dataBox", dataBox));
                    }
                };

                pdfAdapter = new PdfAdapter(context, dataBoxes, documents, onPdf);
                recyclerView.setAdapter(pdfAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
