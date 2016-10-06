/*
 * Copyright (C) 2016 Erik Cox
 */

package rocks.ecox.popularmovies;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Arrays;

public class MainActivityFragment extends Fragment {

    private MoviePosterAdapter movieAdapter;

    Movie[] movies = {
            new Movie("694", "The Shining", "/9fgh3Ns1iRzlQNYuJyK0ARQZU7w.jpg", "http://image.tmdb.org/t/p/w185/9fgh3Ns1iRzlQNYuJyK0ARQZU7w.jpg", "http://image.tmdb.org/t/p/w92/9fgh3Ns1iRzlQNYuJyK0ARQZU7w.jpg", "327826800000", "7.85", "Jack Torrance accepts a caretaker job at the Overlook Hotel, where he, along with his wife Wendy and their son Danny, must live isolated from the rest of the world for the winter. But they aren't prepared for the madness that lurks with"),
            new Movie("278", "The Shawshank Redemption", "/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg", "http://image.tmdb.org/t/p/w185/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg", "http://image.tmdb.org/t/p/w92/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg", "779180400000", "8.33", "Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope."),
            new Movie("244786", "Whiplash", "/lIv1QinFqz4dlp5U4lQ6HaiskOZ.jpg", "http://image.tmdb.org/t/p/w185/lIv1QinFqz4dlp5U4lQ6HaiskOZ.jpg", "http://image.tmdb.org/t/p/w92/lIv1QinFqz4dlp5U4lQ6HaiskOZ.jpg", "1412924400000", "8.27", "Under the direction of a ruthless instructor, a talented young drummer begins to pursue perfection at any cost, even his humanity."),
            new Movie("238", "The Godfather", "/d4KNaTrltq6bpkFS01pYtyXa09m.jpg", "http://image.tmdb.org/t/p/w185/d4KNaTrltq6bpkFS01pYtyXa09m.jpg", "http://image.tmdb.org/t/p/w92/d4KNaTrltq6bpkFS01pYtyXa09m.jpg", "69494400000", "8.27", "The story spans the years from 1945 to 1955 and chronicles the fictional Italian-American Corleone crime family. When organized crime family patriarch Vito Corleone barely survives an attempt on his life, his youngest son, Michael, steps in to take care of the would-be killers, launching a campaign of bloody revenge."),
            new Movie("129", "千と千尋の神隠し", "/ynXoOxmDHNQ4UAy0oU6avW71HVW.jpg", "http://image.tmdb.org/t/p/w185/ynXoOxmDHNQ4UAy0oU6avW71HVW.jpg", "http://image.tmdb.org/t/p/w92/ynXoOxmDHNQ4UAy0oU6avW71HVW.jpg", "995612400000", "8.13", "Spirited Away is an Oscar winning Japanese animated film about a ten year old girl who wanders away from her parents along a path that leads to a world ruled by strange and unusual monster-like animals. Her parents have been changed into pigs along with others inside a bathhouse full of these creatures. Will she ever see the world how it once was?"),
            new Movie("157336", "Interstellar", "/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg", "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg", "http://image.tmdb.org/t/p/w92/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg", "1415174400000", "8.1", "Interstellar chronicles the adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage."),
            new Movie("424", "Schindler's List", "/yPisjyLweCl1tbgwgtzBCNCBle.jpg", "http://image.tmdb.org/t/p/w185/yPisjyLweCl1tbgwgtzBCNCBle.jpg", "http://image.tmdb.org/t/p/w92/yPisjyLweCl1tbgwgtzBCNCBle.jpg", "754560000000", "8.08", "Told from the perspective of businessman Oskar Schindler who saved over a thousand Jewish lives from the Nazis while they worked as slaves in his factory. Schindler’s List is based on a true story, illustrated in black and white and controversially filmed in many original locations."),
            new Movie("77338", "Intouchables", "/4mFsNQwbD0F237Tx7gAPotd0nbJ.jpg", "http://image.tmdb.org/t/p/w185/4mFsNQwbD0F237Tx7gAPotd0nbJ.jpg", "http://image.tmdb.org/t/p/w92/4mFsNQwbD0F237Tx7gAPotd0nbJ.jpg", "1320217200000", "8.08", "A true story of two men who should never have met - a quadriplegic aristocrat who was injured in a paragliding accident and a young man from the projects."),
            new Movie("240", "The Godfather: Part II", "/tHbMIIF51rguMNSastqoQwR0sBs.jpg", "http://image.tmdb.org/t/p/w185/tHbMIIF51rguMNSastqoQwR0sBs.jpg", "http://image.tmdb.org/t/p/w92/tHbMIIF51rguMNSastqoQwR0sBs.jpg", "156758400000", "8.08", "The continuing saga of the Corleone crime family tells the story of a young Vito Corleone growing up in Sicily and in 1910s New York; and follows Michael Corleone in the 1950s as he attempts to expand the family business into Las Vegas, Hollywood and Cuba."),
            new Movie("155", "The Dark Knight", "/1hRoyzDtpgMU7Dz4JF22RANzQO7.jpg", "http://image.tmdb.org/t/p/w185/1hRoyzDtpgMU7Dz4JF22RANzQO7.jpg", "http://image.tmdb.org/t/p/w92/1hRoyzDtpgMU7Dz4JF22RANzQO7.jpg", "1216191600000", "8.06", "Batman raises the stakes in his war on crime. With the help of Lt. Jim Gordon and District Attorney Harvey Dent, Batman sets out to dismantle the remaining criminal organizations that plague the streets. The partnership proves to be effective, but they soon find themselves prey to a reign of chaos unleashed by a rising criminal mastermind known to the terrified citizens of Gotham as the Joker.")
    };

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        movieAdapter = new MoviePosterAdapter(getActivity(), Arrays.asList(movies));

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        gridView.setAdapter(movieAdapter);

        return rootView;
    }
}