package rocks.ecox.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

import rocks.ecox.popularmovies.R;

import static rocks.ecox.popularmovies.BuildConfig.YOUTUBE_API_KEY;

public class TrailerArrayAdapter extends ArrayAdapter<String> {
    public TrailerArrayAdapter(Context context, List<String> trailers) {
        super(context, android.R.layout.simple_list_item_1, trailers);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String trailerKey = this.getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_trailer, parent, false);
        }

        YouTubePlayerView player = (YouTubePlayerView) convertView.findViewById(R.id.player);

        // Try to set the reviews first, then figure out how to extend YouTubePlayer with a fragment
        // set trailer adapter here
        if (trailerKey != null) {
            // Setup YouTube
            player.initialize(YOUTUBE_API_KEY,
                    new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer youTubePlayer, boolean b) {

                            youTubePlayer.cueVideo(trailerKey);
                        }
                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    });
        }

        return convertView;
    }
}
