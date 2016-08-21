package com.example.dell.p2_popular_movies.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.dell.p2_popular_movies.Fragments.MainActivityFragment;
import com.example.dell.p2_popular_movies.R;
import com.example.dell.p2_popular_movies.Utilites.utility;
import com.squareup.picasso.Picasso;

/**
 * Created by DELL on 8/13/2016.
 */
public class PosterAdapter extends
        CursorAdapter {


    public PosterAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.gridview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String posterPath = cursor.getString(MainActivityFragment.COL_POSTER_PATH);

        if (posterPath == null) {
            posterPath = cursor.getString(MainActivityFragment.COL_BACK_DROP_PATH);
        }

        Picasso.with(context).load(utility.getImageURL(posterPath)).error(R.drawable.no_image_available).into(viewHolder.imageView);
    }


    public static class ViewHolder {
        public final ImageView imageView;

        public ViewHolder(View vi) {
            imageView = (ImageView) vi.findViewById(R.id.imageView);
        }
    }
}
