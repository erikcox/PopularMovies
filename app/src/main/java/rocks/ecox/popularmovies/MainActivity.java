package rocks.ecox.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Movie> movies;
    private RecyclerView rvPosters;
    private MoviePosterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute("popular"); // popular or top_rated for now
//        movieTask.execute("top_rated"); // top_rated isn't working, test url in browser
        getItems();

        // Initialize recycler view
        rvPosters = (RecyclerView) findViewById(R.id.rvPoster);
        rvPosters.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new MoviePosterAdapter(MainActivity.this, movies);
        rvPosters.setAdapter(adapter);
    }

    // Get items from DB
    private void getItems() {
        movies = (ArrayList) Movie.getAll();
    }

}
