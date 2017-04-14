/*In the name of Allah*/
package sani.ango.popularmoviesapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends Activity{
    private TextView originalTitle;
    private TextView overview;
    private TextView releasedDate;
    private TextView voteAverage;
    private ImageView poster;

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.details_activity);

        originalTitle = (TextView) findViewById(R.id.title);
        overview  = (TextView) findViewById(R.id.overview);
        releasedDate  = (TextView) findViewById(R.id.date);
        voteAverage  = (TextView) findViewById(R.id.ratings);
        poster  = (ImageView) findViewById(R.id.poster);

        Intent intent = getIntent();
        Movie moveDetails =  intent.getParcelableExtra("movieDetails");

        setDetails(moveDetails);
    }

    private void setDetails(Movie details) {
        originalTitle.setText(details.getMovieTitle());
        overview.setText(details.getOverview());

        releasedDate.setText(details.getReleasedDate());
        voteAverage.setText(String.valueOf(details.getViewerRatings()));

        Picasso.with(getBaseContext()).load(details.getPosterImage())
                .networkPolicy(NetworkPolicy.OFFLINE).resize(300, 300).into(poster);
    }
}
