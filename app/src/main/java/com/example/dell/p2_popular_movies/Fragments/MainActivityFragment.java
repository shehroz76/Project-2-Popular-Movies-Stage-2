package com.example.dell.p2_popular_movies.Fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.dell.p2_popular_movies.Adapters.PosterAdapter;
import com.example.dell.p2_popular_movies.R;
import com.example.dell.p2_popular_movies.Sync.MovieDataLoader;
import com.example.dell.p2_popular_movies.Sync.MoviesSyncAdapter;
import com.example.dell.p2_popular_movies.Utilites.utility;
import com.example.dell.p2_popular_movies.data.MoviesContract;

/**
 * Created by DELL on 8/13/2016.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private PosterAdapter mGridViewMoviesAdapter;
    private static final int FORECAST_LOADER = 0;
    private boolean mTwoPane;

    public static final String[] MOVIE_COLUMNS = {
            MoviesContract.MovieEntry.TABLE_NAME + "." + MoviesContract.MovieEntry._ID,
            MoviesContract.MovieEntry.COLUMN_MOVIE_ID,
            MoviesContract.MovieEntry.COLUMN_IS_ADULT,
            MoviesContract.MovieEntry.COLUMN_BACK_DROP_PATH,
            MoviesContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE,
            MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            MoviesContract.MovieEntry.COLUMN_OVERVIEW,
            MoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.MovieEntry.COLUMN_POPULARITY,
            MoviesContract.MovieEntry.COLUMN_TITLE,
            MoviesContract.MovieEntry.COLUMN_IS_VIDEO,
            MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MoviesContract.MovieEntry.COLUMN_VOTE_COUNT,
            MoviesContract.MovieEntry.COLUMN_RUNTIME,
            MoviesContract.MovieEntry.COLUMN_STATUS,
            MoviesContract.MovieEntry.COLUMN_DATE
    };

    public static final int COL_MOVIE_PK_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_IS_ADULT   = 2;
    public static final int COL_BACK_DROP_PATH = 3;
    public static final int COL_ORIGINAL_LANGUAGE = 4;
    public static final int COL_ORIGINAL_TITLE = 5;
    public static final int COL_OVERVIEW = 6;
    public static final int COL_RELEASE_DATE = 7;
    public static final int COL_POSTER_PATH = 8;
    public static final int COL_POPULARITY = 9;
    public static final int COL_TITLE = 10;
    public static final int COL_IS_VIDEO = 11;
    public static final int COL_VOTE_AVERAGE = 12;
    public static final int COL_VOTE_COUNT = 13;
    public static final int COL_RUNTIME = 14;
    public static final int COL_STATUS = 15;
    public static final int COL_DATE = 16;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGridViewMoviesAdapter = new PosterAdapter(getActivity(), null, 0);

        // initialize the GridView
        GridView gridView = (GridView) rootView.findViewById(R.id.gridView);
        TextView emptyTextView = (TextView) rootView.findViewById(R.id.gridView_empty);
        emptyTextView.setText(getString(R.string.label_text_view_no_movies_as_favorite));
        gridView.setEmptyView(emptyTextView);
        gridView.setAdapter(mGridViewMoviesAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                    long id) {
                final Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);

                String backDropPath = cursor.getString(COL_BACK_DROP_PATH);
                String posterPath = cursor.getString(COL_POSTER_PATH);
                if (backDropPath == null || backDropPath.equals("null")) {
                    if (posterPath != null && !posterPath.equals("null")) {
                        backDropPath = posterPath;
                    }
                }

                if (posterPath == null || posterPath.equals("null")) {
                    if (backDropPath != null && !backDropPath.equals("null")) {
                        posterPath = backDropPath;
                    }
                }


                String data[] = {backDropPath, posterPath, cursor.getString(COL_RELEASE_DATE).toString(),
                        Double.toString(cursor.getDouble(COL_RUNTIME)), Double.toString(cursor.getDouble(COL_VOTE_AVERAGE)), cursor.getString(COL_OVERVIEW),
                        cursor.getString(COL_ORIGINAL_TITLE), Integer.toString(cursor.getInt(COL_MOVIE_ID)), Boolean.toString(mTwoPane)};
                ((Callback) getActivity()).onItemSelected(data);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public void onSortOrderChanged( ) {
        MoviesSyncAdapter.syncImmediately(getActivity());
        getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrderSelected = prefs.getString(getActivity().getString(R.string.pref_sort_order_key), null);

        String sortOrder = MoviesContract.MovieEntry.COLUMN_POPULARITY + " DESC";

        if(sortOrderSelected != null && sortOrderSelected.equals(getActivity().getString(R.string.pref_sort_order_vote_average))) {
            sortOrder = MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE + " DESC";
        }

        Uri weatherForLocationUri = MoviesContract.MovieEntry.buildMovieUri();

        return new CursorLoader(getActivity(),
                weatherForLocationUri,
                MOVIE_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mGridViewMoviesAdapter.swapCursor(cursor);
        updateEmptyView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mGridViewMoviesAdapter.swapCursor(null);
    }

    public interface Callback {
        /**
         * MainActivityFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String data[]);
    }

    public void setTwoPane(boolean mTwoPane) {
        this.mTwoPane = mTwoPane;
    }

    private void updateEmptyView() {
        if (mGridViewMoviesAdapter.getCount() == 0) {
            TextView emptyTextView = (TextView) getView().findViewById(R.id.gridView_empty);
            if (null != emptyTextView) {
                int message = R.string.empty_movies_list;
                @MovieDataLoader.MovieStatus int status = utility.getMovieStatus(getActivity());
                switch (status) {
                    case MovieDataLoader.MOVIE_STATUS_SERVER_DOWN:
                        message = R.string.empty_movies_list_server_down;
                        break;
                    case MovieDataLoader.MOVIE_STATUS_SERVER_INVALID:
                        message = R.string.empty_movies_list_server_error;
                        break;
                    default:
                        String [] favoriteMovieIds = utility.loadFavoriteMovieIds(getActivity());
                        if ((favoriteMovieIds == null || favoriteMovieIds.length <= 0) &&
                                utility.getPreferredSortOrder(getActivity()).equals(getActivity().getString(R.string.pref_sort_order_favorite))) {
                            message = R.string.label_text_view_no_movies_as_favorite;
                        }
                        else if (!utility.isNetworkConnected(getActivity())) {
                            message = R.string.empty_movies_list_no_network;
                        }
                        break;
                }
                emptyTextView.setText(message);
            }
        }
    }
}
