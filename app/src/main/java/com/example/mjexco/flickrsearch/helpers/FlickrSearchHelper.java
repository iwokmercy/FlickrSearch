package com.example.mjexco.flickrsearch.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mjexco.flickrsearch.R;
import com.example.mjexco.flickrsearch.backend.HttpClient;
import com.example.mjexco.flickrsearch.models.FlickrSearchResponse;
import com.example.mjexco.flickrsearch.models.Photo;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FlickrSearchHelper {

    /*
    Interface implemented by view class to get notified whenever an update is ready
     */
    public interface FlickrSearchHelperListener{
        void onSuccessResponseReceived(FlickrSearchResponse response);
        void onErrorResponseReceived(String errorMessage);
        void onResultsRefined(List<Photo> newList);
    }

    private ProgressDialog progressBar;
    private Context context;

    private FlickrSearchHelperListener searchHelperListener;

    public FlickrSearchHelper(@NonNull Context context, @NonNull FlickrSearchHelperListener listener){
        searchHelperListener = listener;
        this.context = context;
        progressBar = new ProgressDialog(context);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setMessage(context.getString(R.string.loading_text));
    }

    /**
     * Makes the sevice call to retrieve images associated with given tag
     * @param tag search tag
     */
    public void getImageResults(String tag){
        showProgressBar(true);
        HttpClient.get("rest/", getRequestParams(tag), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
                response = StringUtils.replace(response, "jsonFlickrApi(", "");
                response = StringUtils.removeEnd(response, ")");
                Log.d("SERVICE RESPONSE", response);
                Gson gson = new Gson();
                FlickrSearchResponse jsonResponse = gson.fromJson(response, FlickrSearchResponse.class);
                if(jsonResponse.getStat().equals("fail")){
                    //api returned an error
                    searchHelperListener.onErrorResponseReceived(jsonResponse.getMessage());
                } else {
                    //api returned data
                    searchHelperListener.onSuccessResponseReceived(jsonResponse);
                }
                showProgressBar(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("SERVICE FAILURE", error.getMessage());
                searchHelperListener.onErrorResponseReceived(error.getMessage());
                showProgressBar(false);
            }
        });

    }

    /**
     * Builds service request parameter object
     * @param tag tag request value
     * @return request parameter object
     */
    private RequestParams getRequestParams(String tag){
        RequestParams params = new RequestParams();
        params.put(Constants.method_label, Constants.search_method);
        params.put(Constants.api_key_label, Constants.api_key);
        params.put(Constants.tags_label, tag);
        params.put(Constants.content_type_label, Constants.content_type);
        params.put(Constants.media_label, Constants.media);
        params.put(Constants.format_label, Constants.format);
        params.put(Constants.jsonCallback_label, Constants.noJsonCallback);
        return params;
    }

    /**
     * Show dialog to get refining value from user
     * @param activity context to use to show dialog
     * @param photoList current search result
     */
    public void showDialog(Activity activity, final List<Photo> photoList) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.refine_results_dialog_layout, null);
        dialogBuilder.setView(dialogView);

        final EditText text = dialogView.findViewById(R.id.title_text);

        dialogBuilder.setTitle(R.string.dialog_title);
        dialogBuilder.setMessage(R.string.dialog_message);
        dialogBuilder.setPositiveButton(R.string.dialog_button_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                refineResults(photoList, text.getText().toString());
            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel_dialog_button_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    /**
     * Finds photos that match user search value, and returns them
     * @param photoList current search result to traverse for matches
     * @param title search parameter
     */
    private void refineResults(List<Photo> photoList, String title){
        List<Photo> newPhotoList = new ArrayList<>();
        for(int i=0; i<photoList.size(); i++){
            if(StringUtils.lowerCase(photoList.get(i).getTitle()).contains(StringUtils.lowerCase(title))){
                newPhotoList.add(photoList.get(i));
            }
        }
        searchHelperListener.onResultsRefined(newPhotoList);
    }

    /**
     * Shows or dismisses an indeterminate progress bar shown when data is loading
     * @param show true to show, false to dismiss
     */
    private void showProgressBar(boolean show){
        if(show){
            progressBar.show();
        } else {
            progressBar.dismiss();
        }
    }

    /*
    Show generic error message in case of unrecoverable error
     */
    public void showErrorMessage(){
        Toast.makeText(context, R.string.generic_error_message, Toast.LENGTH_LONG).show();
    }

    /*
    Show custom error message in case of unrecoverable error
     */
    public void showErrorMessage(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
