package com.example.mjexco.flickrsearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mjexco.flickrsearch.adapters.SearchResultsAdapter;
import com.example.mjexco.flickrsearch.helpers.FlickrSearchHelper;
import com.example.mjexco.flickrsearch.models.FlickrSearchResponse;
import com.example.mjexco.flickrsearch.models.Photo;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView refineResultsButton;
    private EditText searchTag;
    private RecyclerView searchResultsList;
    private FlickrSearchHelper searchHelper;
    private FlickrSearchResponse searchResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        refineResultsButton = findViewById(R.id.refine_results_button);
        searchTag = findViewById(R.id.search_tag);
        Button searchButton = findViewById(R.id.search_button);

        searchResultsList = findViewById(R.id.search_results);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        searchResultsList.setLayoutManager(layoutManager);
        snapHelper.attachToRecyclerView(searchResultsList);

        searchHelper = new FlickrSearchHelper(this, new FlickrSearchHelper.FlickrSearchHelperListener() {
            @Override
            public void onSuccessResponseReceived(FlickrSearchResponse response) {
                //initialise recycler view adapter with search results
                searchResponse = response;
                if(response.getPhotos() != null && response.getPhotos().getPhoto() != null){
                    if(Integer.parseInt(response.getPhotos().getTotal()) > 0){
                        //there is at least one photo returned for the search tag
                        searchResultsList.setAdapter(new SearchResultsAdapter(getApplicationContext(),
                                response.getPhotos().getPhoto()));
                        refineResultsButton.setVisibility(View.VISIBLE);
                    } else {
                        //show no results error
                        searchHelper.showErrorMessage(getString(R.string.no_results_for_tag_text));
                    }
                } else {
                    //show error
                    searchHelper.showErrorMessage();
                }
            }

            @Override
            public void onErrorResponseReceived(String errorMessage) {
                //show error message
                searchHelper.showErrorMessage();
            }

            @Override
            public void onResultsRefined(List<Photo> newList) {
                //update adapter dataset
                if(!newList.isEmpty()){
                    SearchResultsAdapter adapter = (SearchResultsAdapter) searchResultsList.getAdapter();
                    adapter.setPhotoList(newList);
                    adapter.notifyDataSetChanged();
                } else {
                    //no photos in the results match that title
                    searchHelper.showErrorMessage(getString(R.string.no_results_text));
                }
            }
        });

        SpannableString spanStr = new SpannableString(refineResultsButton.getText().toString());
        spanStr.setSpan(new UnderlineSpan(), 0, spanStr.length(), 0);
        refineResultsButton.setText(spanStr);
        refineResultsButton.setOnClickListener(this);

        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch(id){
            case R.id.search_button: {
                //make service call to retrieve images for tag
                String tag = searchTag.getText().toString();
                refineResultsButton.setVisibility(View.INVISIBLE);
                searchHelper.getImageResults(tag);
                break;
            }
            case R.id.refine_results_button : {
                //show dialog to get title or description
                searchHelper.showDialog(this, searchResponse.getPhotos().getPhoto());
                break;
            }
        }
    }
}
