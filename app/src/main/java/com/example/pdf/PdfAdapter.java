package com.example.pdf;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pdf.model.Document;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfViewHolder> {
    private Context context;
    private List<Document> documents;

    private OnPdfFileSelectListener listener;

    public PdfAdapter(Context context, List<Document> documents, OnPdfFileSelectListener listener) {
        this.context = context;
        this.documents = documents;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PdfViewHolder(LayoutInflater.from(context).inflate(R.layout.pdf_element, parent, false));
    }

    public static InputStream getInputStreamByUri(Context context, Uri uri) {
        try {
            return context.getContentResolver().openInputStream(uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        int pos = position;
        Picasso.with(context).load(documents.get(pos).getDocumentImage()).into(holder.imageView);
        holder.pdfName.setText(documents.get(pos).getDocumentName());
        holder.pdfName.setSelected(true);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPdfSelected(documents.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }
}
