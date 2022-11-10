package com.example.pdf;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PdfViewHolder extends RecyclerView.ViewHolder {
    public TextView pdfName;
    public CardView cardView;
    public ImageView imageView;

    public PdfViewHolder(@NonNull View itemView) {
        super(itemView);

        pdfName = itemView.findViewById(R.id.pdfName);
        cardView = itemView.findViewById(R.id.cardView);
        imageView = itemView.findViewById(R.id.imageView);
    }
}
