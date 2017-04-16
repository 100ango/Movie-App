package sani.ango.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import sani.ango.popularmoviesapp.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.ListItemClickListener{

    private final static String SORT_ORDER = "pref_sort_order";
    private List<Movie> movies;
    private RecyclerView mMovieList;
    private MovieAdapter adapter;
    private String mSortOrder;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        preferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mSortOrder = preferences.getString(SORT_ORDER, "popular");

        movies = new ArrayList<>();
        mMovieList = (RecyclerView) findViewById(R.id.rv_movies_list);

        int orientation = getActivity().getConfiguration.orientation;
            
        if(orientation == Configuration.ORIENTATION_PORTRAIT)
            mMovieList.setLayoutManager(new GridLayoutManager(this, 2));
        else
            mMovieList.setLayoutManager(new GridLayoutManager(this, 3));
            
        mMovieList.setHasFixedSize(true);

        if (isNetworkAvailable())
            downloadMovieDetails();
        else
            setNoConnectionLayout();
    }

    private void setNoConnectionLayout() {
        ((findViewById(R.id.rv_no_conn))).setVisibility(View.VISIBLE);
        ((findViewById(R.id.rv_refresh))).setVisibility(View.VISIBLE);
    }

    public void refresh(View view){
        if (isNetworkAvailable())
        {
            ((findViewById(R.id.rv_no_conn))).setVisibility(View.INVISIBLE);
            ((findViewById(R.id.rv_refresh))).setVisibility(View.INVISIBLE);
            downloadMovieDetails();
        }
    }

    private void downloadMovieDetails() {
        URL apiURL = NetworkUtils.buildUrl(mSortOrder, this);
        new InternetConnectionTask().execute(apiURL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings)
        {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("movieDetails", movies.get(position));
        startActivity(intent);
    }

    private class InternetConnectionTask extends AsyncTask<URL, Void, JSONObject>{

        @Override
        public void onPreExecute(){
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(URL... params) {

            String r = "";
            try {
                r = NetworkUtils.getResponseFromHttpUrl(params[0]);


            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                return new JSONObject(r);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(JSONObject jsonObject){
            parseJSONObject(jsonObject);
            mProgressBar.setVisibility(View.INVISIBLE);
            adapter = new MovieAdapter(movies, MainActivity.this);
            mMovieList.setAdapter(adapter);
        }
    }

    private void parseJSONObject(JSONObject jsonObject) {
        movies.clear();
        try {
            JSONArray list = jsonObject.getJSONArray("results");

            for(int i = 0; i < list.length(); i++)
            {
                JSONObject details = list.getJSONObject(i);
                String title = details.getString("original_title");
                String poster = details.getString("poster_path");
                String summary = details.getString("overview");
                double ratings = details.getDouble("vote_average");
                String date = details.getString("release_date");
                movies.add(new Movie(title, ratings, summary, date, poster));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager con = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = con.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.isConnected();
    }


    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(
                        SharedPreferences sharedPreferences, String key) {
                    if (key.equals(SORT_ORDER))
                    {
                        mSortOrder = sharedPreferences.getString(SORT_ORDER, "popular");
                        if (isNetworkAvailable())
                            downloadMovieDetails();
                    }
                }
            };
}
