package rocks.ecox.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import rocks.ecox.popularmovies.R;
import rocks.ecox.popularmovies.models.Trailer;

import static rocks.ecox.popularmovies.R.drawable.poster_thumbnail_placeholder;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private static Context mContext;
    private ArrayList<Trailer> trailersList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.ivTrailerThumb);
            mTextView = (TextView) itemView.findViewById(R.id.tvTrailerTitle);
        }

    }

    public TrailerAdapter(Context context, ArrayList<Trailer> trailers) {
        super();
        mContext = context;
        this.trailersList = trailers;
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
        Trailer trailerAttributes = trailersList.get(position);
        String trailerThumb = (String) "http://img.youtube.com/vi/" + trailerAttributes.getKey() + "/0.jpg";

        holder.mTextView.setText(trailerAttributes.getTitle());
//        holder.mImageView.setImageResource(poster_thumbnail_placeholder);
        Picasso.with(getContext())
                .load(trailerThumb)
                .error(poster_thumbnail_placeholder)
                .placeholder(poster_thumbnail_placeholder)
                .into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        return trailersList.size();
    }

}
