package rocks.ecox.popularmovies.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;

import rocks.ecox.popularmovies.R;
import rocks.ecox.popularmovies.models.Trailer;

import static rocks.ecox.popularmovies.BuildConfig.YOUTUBE_API_KEY;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private static Context mContext;
    private ArrayList<Trailer> trailersList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private YouTubeThumbnailView mPlayer;
        private Context context;
//        protected RelativeLayout relativeLayoutOverYouTubeThumbnailView;
//        YouTubeThumbnailView youTubeThumbnailView;
//        protected ImageView playButton;


        public ViewHolder(View itemView) {
            super(itemView);
            this.mPlayer = (YouTubeThumbnailView) itemView.findViewById(R.id.ytThumbnail);
            this.context = context;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = getContext();
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if (trailersList.get(position).getKey() != null){
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context, YOUTUBE_API_KEY, trailersList.get(position).getKey());
                    context.startActivity(intent);
                }
            }
        }
    }

    public TrailerAdapter(Context context, ArrayList<Trailer> trailers) {
        super();
        mContext = context;
        this.trailersList = trailers;
        setHasStableIds(true);
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_trailer, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder holder, final int position) {
        // Get the data model based on the position
        final Trailer trailerAttributes = trailersList.get(position);
        String trailerThumb = (String) "http://img.youtube.com/vi/" + trailerAttributes.getKey() + "/0.jpg";

        final YouTubeThumbnailView youTubeThumbnailView = (YouTubeThumbnailView) holder.mPlayer;
        youTubeThumbnailView.initialize(YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(trailerAttributes.getKey());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return trailersList.size();
    }

}
