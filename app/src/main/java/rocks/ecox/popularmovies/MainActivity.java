package rocks.ecox.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "RecyclerView";
    private List<Movie> movies;
    private RecyclerView rvPosters;
    private MoviePosterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize recycler view
        rvPosters = (RecyclerView) findViewById(R.id.rvPoster);
        rvPosters.setLayoutManager(new GridLayoutManager(this, 2));

        // TODO: Remove mock data
        movies = new ArrayList<Movie>();
        movies.add(new Movie("Chinatown", "https://images-na.ssl-images-amazon.com/images/M/MV5BN2YyNDE5NzItMjAwNC00MGQxLTllNjktZGIzMWFkZjA3OWQ0XkEyXkFqcGdeQXVyNDk3NzU2MTQ@._V1_UY268_CR2,0,182,268_AL_.jpg"));
        movies.add(new Movie("The Two Jakes", "https://images-na.ssl-images-amazon.com/images/M/MV5BOTM3MDU5MjQwNV5BMl5BanBnXkFtZTgwMTY1NTk4NjE@._V1_UX182_CR0,0,182,268_AL_.jpg"));
        movies.add(new Movie("The Shining", "https://images-na.ssl-images-amazon.com/images/M/MV5BODMxMjE3NTA4Ml5BMl5BanBnXkFtZTgwNDc0NTIxMDE@._V1_UY268_CR0,0,182,268_AL_.jpg"));
        movies.add(new Movie("The Departed", "https://images-na.ssl-images-amazon.com/images/M/MV5BMTI1MTY2OTIxNV5BMl5BanBnXkFtZTYwNjQ4NjY3._V1_UX182_CR0,0,182,268_AL_.jpg"));
        movies.add(new Movie("As Good As It Gets", "https://images-na.ssl-images-amazon.com/images/M/MV5BNWMxZTgzMWEtMTU0Zi00NDc5LWFkZjctMzUxNDIyNzZiMmNjXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_UX182_CR0,0,182,268_AL_.jpg"));

        adapter = new MoviePosterAdapter(MainActivity.this, movies);
        rvPosters.setAdapter(adapter);

    }
}
