package com.example.mjexco.flickrsearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mjexco.flickrsearch.R;
import com.example.mjexco.flickrsearch.models.Photo;

import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder> {
    private List<Photo> photoList;
    private Context context;

    public SearchResultsAdapter(Context context, List<Photo> photoList){
        this.photoList = photoList;
        this.context = context;
    }

    @Override
    public SearchResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);

        return new SearchResultsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultsViewHolder holder, int position) {
        ImageView imageView = holder.imageView;
        Glide.with(context)
                //retrieve photo url and load in imageview
                .load(getPhotoUrl(photoList.get(position)))
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        //return number of items in data set
        return photoList.size();
    }

    /**
     * Construct the url of the given photo
     * @param photo given pgoto
     * @return photo url
     */
    private String getPhotoUrl(Photo photo){
        String url =
        context.getString(R.string.photo_url, photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret());
        //Log.d("PHOTO URL", url);
        return  url;

    }

    /**
     * Update data set
     * @param newPhotoList new data set
     */
    public void setPhotoList(List<Photo> newPhotoList){
        photoList = newPhotoList;
        notifyDataSetChanged();
    }

    /**
     * View holder class to hold recycler view components
     */
    class SearchResultsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        SearchResultsViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
