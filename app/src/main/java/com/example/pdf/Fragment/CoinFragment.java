package com.example.pdf.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pdf.R;
import com.example.pdf.Transaction;
import com.example.pdf.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CoinFragment extends Fragment {
    private Context context;
    private View view;
    private TextView coinText;

    private Button fiftyButton;
    private Button hundredButton;
    private Button twoHundredButton;
    private Button fiveHundredButton;
    private Button thousandButton;

    private User user;
    private long currCoin;
    private long neededCoin;
    private Transaction a;
    private TextView dateTextView;

    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_coin, container, false);
        this.context = getActivity();

        Intent i = getActivity().getIntent();
        user = (User) i.getSerializableExtra("userObj");

        Web3j web3 = Web3j.build(new HttpService("https://goerli.infura.io/v3/286934f4831e45b087469c4da88cb0f9"));
        Credentials credentials = Credentials.create(user.getKey());
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        a = Transaction.load(user.getContractAddress(), web3, credentials, contractGasProvider);
        displayCoin();
        displayTimeStamp();
        progressButton();

        return view;
    }

    private void displayCoin(){
        coinText = view.findViewById(R.id.coin);
        databaseReference = FirebaseDatabase.getInstance().getReference(String.format("%s/coin", user.getUserName()));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currCoin = (Long) snapshot.getValue();
                Log.i("1", String.valueOf(currCoin));
                coinText.setText(String.valueOf(currCoin));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayTimeStamp(){
        ProgressDialog beginDialog = new ProgressDialog(context);
        beginDialog.setTitle("Connect to blockchain");
        beginDialog.setMessage("Please wait...");
        beginDialog.show();

        dateTextView = view.findViewById(R.id.update_date);

        a.getDate().flowable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BigInteger>() {
            @Override
            public void accept(BigInteger bigInteger) throws Exception {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String recentlyDate  = dateFormat.format(bigInteger);

                dateTextView.setText(recentlyDate);
                beginDialog.dismiss();
            }
        });
    }

    private void progressButton(){
        fiftyButton = view.findViewById(R.id.buy_fiffy_button);
        hundredButton = view.findViewById(R.id.buy_hundred_button);
        twoHundredButton = view.findViewById(R.id.buy_two_hundred_button);
        fiveHundredButton = view.findViewById(R.id.buy_five_hundred_button);
        thousandButton = view.findViewById(R.id.buy_thousand_button);

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setCancelable(false);
        dialog.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        buyCoin(neededCoin);
                    }
                })
                .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                        dialog.cancel();
                    }
                });

        fiftyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setTitle("Buy 50 coins and pay gas");
                dialog.setMessage("Are you want to buy 50 coins and pay gas for buy this coins?" );
                neededCoin = 50;
                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });

        hundredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setTitle("Buy 100 coins and pay gas");
                dialog.setMessage("Are you want to buy 100 coins and pay gas for buy this coins?" );
                neededCoin = 100;
                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });

        twoHundredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setTitle("Buy 200 coins and pay gas");
                dialog.setMessage("Are you want to buy 200 coins and pay gas for buy this coins?" );
                neededCoin = 200;
                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });

        fiveHundredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setTitle("Buy 500 coins and pay gas");
                dialog.setMessage("Are you want to buy 500 coins and pay gas for buy this coins?" );
                neededCoin = 500;
                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });

        thousandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setTitle("Buy 1000 coins and pay gas");
                dialog.setMessage("Are you want to buy 1000 coins and pay gas for buy this coins?" );
                neededCoin = 1000;
                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });
    }

    private void buyCoin(long coin){
        long totalCoin = currCoin + coin;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String formattedNow = now.format(formatter);
        Timestamp timestamp = Timestamp.valueOf(formattedNow);

        long t = timestamp.getTime();
        BigInteger finalT = BigInteger.valueOf(t);
        BigInteger finalCoin = BigInteger.valueOf(coin);

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Connect to blockchain");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        /*a.store(finalT, finalCoin).flowable().subscribeOn(Schedulers.io()).subscribe(new Consumer<TransactionReceipt>() {
            @Override
            public void accept(TransactionReceipt transactionReceipt) throws Exception {
                Log.i("1", "Success");
                databaseReference.setValue(totalCoin);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String recentlyDate  = dateFormat.format(finalT);
                dateTextView.setText(recentlyDate);
                progressDialog.dismiss();
            }
        });*/

        a.store(finalT, finalCoin).flowable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<TransactionReceipt>() {
            @Override
            public void accept(TransactionReceipt transactionReceipt) throws Exception {
                Log.i("1", "Success");
                databaseReference.setValue(totalCoin);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String recentlyDate  = dateFormat.format(finalT);
                dateTextView.setText(recentlyDate);
                progressDialog.dismiss();
            }
        });

        neededCoin = 0;
    }
}
