package com.example.myapplication;


import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.myapplication.Fragment.PhimHai;
import com.example.myapplication.Fragment.PhimLe;
import com.example.myapplication.Fragment.TrangChu;
import com.example.myapplication.Fragment.VideoDaXem;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    DrawerLayout drawerLayout;
    Disposable disposable;
    LinearLayout checkInternet;
    Toolbar toolbar;
    MaterialSearchView searchView;
    Handler handler = new Handler();
    RelativeLayout layoutLogo;
    FrameLayout frameLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkInternet = findViewById(R.id.checkInternet);
        toolbar = findViewById(R.id.toolBar);
        layoutLogo = findViewById(R.id.Logo);
        frameLayout = findViewById(R.id.container);
        setSupportActionBar(toolbar);

        searchViewCode();

        drawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.nav_View);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bottomHome:
                        getFragment(TrangChu.newInstance());
                        checkInternet.setVisibility(View.VISIBLE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                checkInternet.setVisibility(View.GONE);
                            }
                        }, 5000);
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




    public void onResume() {
        super.onResume();
        disposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isConnected) throws Exception {
                        if (isConnected == false) {
                            loadDialog();
                        } else {
                            getFragment(TrangChu.newInstance());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    checkInternet.setVisibility(View.GONE);
                                }
                            }, 5000);

                        }

                    }
                });
    }

    public void loadDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Lost Internet");
        dialog.setMessage("Checking again");
        dialog.setPositiveButton("Go to Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }
    private void searchViewCode() {
        searchView = (MaterialSearchView) findViewById(R.id.searchView);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setEllipsize(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                layoutLogo.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                frameLayout.setVisibility(View.GONE);
            }

            @Override
            public void onSearchViewClosed() {
                layoutLogo.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else{
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.home:
                getFragment(TrangChu.newInstance());
                break;
            case R.id.mPhimLe:
                getFragment(PhimLe.newInstance());
                break;
            case R.id.mPhimHai:
                getFragment(PhimHai.newInstance());
                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
