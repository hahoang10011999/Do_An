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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Contect.VideoContect;
import com.example.myapplication.Fragment.FullScreen;
import com.example.myapplication.Fragment.PhimHai;
import com.example.myapplication.Fragment.PhimLe;
import com.example.myapplication.Fragment.TrangChu;
import com.example.myapplication.Fragment.VideoDaXem;
import com.example.myapplication.SQL.SQLHelper;
import com.example.myapplication.SQL.SQLHelperList;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

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
    String urlApi = Define.hotVideo;
    String urlPL = Define.phimLe;
    List<String> suggestions;
    List<VideoContect> contects;
    List<VideoContect> list;
    List<VideoContect> videoContects;
    String[] strings;
    SQLHelper sqlHelper;
    SQLHelperList sqlHelperList;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    PutVideoList putVideoList;
    int dem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkInternet = findViewById(R.id.checkInternet);
        toolbar = findViewById(R.id.toolBar);
        layoutLogo = findViewById(R.id.Logo);
        frameLayout = findViewById(R.id.container);
        sqlHelper = new SQLHelper(getBaseContext());
        sqlHelperList = new SQLHelperList(getBaseContext());
        list = sqlHelperList.getAllProductAdvanced();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        editor = sharedPreferences.edit();
        putVideoList = new PutVideoList(getBaseContext());

        searchView = (MaterialSearchView) findViewById(R.id.searchView);
        setSupportActionBar(toolbar);

        suggestions = new ArrayList<>();
        contects = new ArrayList<>();
        new DogetData(urlApi).execute();
        new DogetData1(urlPL).execute();



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

    public class DogetData extends AsyncTask<Void, Void, Void> {
        String result = "";
        String urlAPI;

        public DogetData(String urlAPI) {
            this.urlAPI = urlAPI;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(urlAPI);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                int byteCharacter;
                while ((byteCharacter = inputStream.read()) != -1) {
                    result += (char) byteCharacter;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONArray jsonArray = new JSONArray(result);
                int length = jsonArray.length();
                VideoContect contect;
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString(Define.title);
                    String avatar = jsonObject.getString(Define.avatar);
                    String date = jsonObject.getString(Define.date_create);
                    String linkVideo = jsonObject.getString(Define.file_mp4);
                    String id = jsonObject.getString(Define.id);
                    contect = new VideoContect(title, date, avatar, linkVideo, id);
                    contects.add(contect);
                    suggestions.add(title);
                }
                strings = suggestions.toArray(new String[suggestions.size()]);
                searchViewCode();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class DogetData1 extends AsyncTask<Void, Void, Void> {
        String result = "";
        String urlPL;

        public DogetData1(String url) {
            this.urlPL = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(urlPL);
                URLConnection connection = url.openConnection();
                InputStream is = connection.getInputStream();
                int byteCharacter;
                while ((byteCharacter = is.read()) != -1) {
                    result += (char) byteCharacter;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONArray jsonArray = new JSONArray(result);
                int length = jsonArray.length();
                VideoContect contect;
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString(Define.title);
                    String avatar = jsonObject.getString(Define.avatar);
                    String date = jsonObject.getString(Define.date_create);
                    String linkVideo = jsonObject.getString(Define.file_mp4);
                    String id = jsonObject.getString(Define.id);
                    contect = new VideoContect(title, date, avatar, linkVideo, id);
                    contects.add(contect);
                    suggestions.add(title);
                }
                strings = suggestions.toArray(new String[suggestions.size()]);
                searchViewCode();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

        searchView.setSuggestions(strings);
        searchView.setEllipsize(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                for(int i = 0 ; i <contects.size();i++){
                    if(query.equals(contects.get(i).getName()) == true){
                        if (list != null) {
                            sqlHelperList.delAllProduct();
                        }
                        putVideoList.onPutVideo(contects, contects.get(i), sqlHelperList);
                        putVideoList.onPutVideoHistory(videoContects, contects.get(i), sqlHelper, dem);

                        editor.putString(Define.file_mp4, contects.get(i).getUrl());
                        editor.putString(Define.date_create, contects.get(i).getDate());
                        editor.putString(Define.title, contects.get(i).getName());
                        editor.putString(Define.id,contects.get(i).getId());
                        editor.putString(Define.avatar,contects.get(i).getImg());
                        editor.commit();

                        Intent intent = new Intent(getBaseContext(), FullScreen.class);
                        startActivity(intent);
                    }
                }
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
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
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
