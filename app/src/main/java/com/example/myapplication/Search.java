package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.example.myapplication.Contect.Suggestion;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {
    private List<Suggestion> mSuggestions =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initData();

        final FloatingSearchView searchView= (FloatingSearchView) findViewById(R.id.floating_search_view);

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    searchView.clearSuggestions();
                } else {
                    searchView.showProgress();
                    searchView.swapSuggestions(getSuggestion(newQuery));
                    searchView.hideProgress();
                }
            }
        });
        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                searchView.showProgress();
                searchView.swapSuggestions(getSuggestion(searchView.getQuery()));
                searchView.hideProgress();
            }

            @Override
            public void onFocusCleared() {

            }
        });
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                Suggestion suggestion= (Suggestion) searchSuggestion;
                Toast.makeText(getApplicationContext(),"Ban vua chon "+suggestion.getBody(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchAction(String currentQuery) {

            }
        });
        searchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
            @Override
            public void onMenuOpened() {
                Toast.makeText(getBaseContext(),"open",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMenuClosed() {
                Toast.makeText(getBaseContext(),"close",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void initData(){
        mSuggestions.add(new Suggestion("Ha Noi"));
        mSuggestions.add(new Suggestion("Ha nam"));
        mSuggestions.add(new Suggestion("Da nang"));
        mSuggestions.add(new Suggestion("Dong nai"));
        mSuggestions.add(new Suggestion("Phú Tho"));
        mSuggestions.add(new Suggestion("Quang ngai"));
        mSuggestions.add(new Suggestion("Thanh hoa"));
        mSuggestions.add(new Suggestion("Hue"));
    }

    private List<Suggestion> getSuggestion(String query){
        List<Suggestion> suggestions=new ArrayList<>();
        for(Suggestion suggestion:mSuggestions){
            if(suggestion.getBody().toLowerCase().contains(query.toLowerCase())){
                suggestions.add(suggestion);
            }
        }
        return suggestions;
    }
}
