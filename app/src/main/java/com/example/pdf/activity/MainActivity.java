package com.example.pdf.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.pdf.Fragment.CoinFragment;
import com.example.pdf.Fragment.CollectionsFragment;
import com.example.pdf.Fragment.CreationFragment;
import com.example.pdf.Fragment.HomepageFragment;
import com.example.pdf.R;
import com.example.pdf.model.User;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = new User("user1");
        user.setKey("7a27ac79f44d5a917207108668ac527e5b394c390047399dc61eae0888057cba");
        user.setContractAddress("0x4cb4490F8eaF6fB84f1a24589197c5E079657C6C");
        getIntent().putExtra("userObj", user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomepageFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_homepage);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_homepage:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomepageFragment()).commit();
                break;
            case R.id.nav_collection:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CollectionsFragment()).commit();
                break;
            case R.id.nav_create:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CreationFragment()).commit();
                break;
            case R.id.nav_coin:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CoinFragment()).commit();
                break;
        }
        return true;
    }
}