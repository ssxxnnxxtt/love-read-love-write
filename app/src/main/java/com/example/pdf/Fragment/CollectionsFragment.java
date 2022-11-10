package com.example.pdf.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pdf.R;
import com.example.pdf.model.User;

public class CollectionsFragment extends Fragment {
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Intent i = getActivity().getIntent();
        user = (User) i.getSerializableExtra("userObj");
        System.out.println(user.getUserName());

        return inflater.inflate(R.layout.fragment_collections, container, false);
    }
}
