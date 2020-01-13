package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.Fragment.PhimHai;
import com.example.myapplication.Fragment.PhimLe;
import com.example.myapplication.Fragment.TrangChu;
import com.example.myapplication.Fragment.VideoDaXem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    DrawerLayout drawerLayout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        drawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.nav_View);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bottomHome:
                        getFragment(TrangChu.newInstance());
                        break;
                    case R.id.bottomPhimHai:
                        getFragment(PhimHai.newInstance());
                        break;
                    case R.id.bottomPhimLe:
                        getFragment(PhimLe.newInstance());
                        break;
                    case R.id.LichSu:
                        getFragment(VideoDaXem.newInstance());
                        break;
                }
                return true;
            }
        });
        toggle.syncState();



      getFragment(TrangChu.newInstance());

    }

    public void getFragment(Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getFragment: " + e.getMessage());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.home:
                getFragment(TrangChu.newInstance());
                break;
            case R.id.mPhimHai:
                getFragment(PhimHai.newInstance());
                break;
            case R.id.mPhimLe:
                getFragment(PhimLe.newInstance());
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
