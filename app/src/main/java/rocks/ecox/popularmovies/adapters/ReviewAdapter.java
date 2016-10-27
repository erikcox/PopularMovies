package rocks.ecox.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import rocks.ecox.popularmovies.R;
import rocks.ecox.popularmovies.models.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private static Context mContext;
    private ArrayList<Review> reviewsList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView author;
        private TextView review;
        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            this.author = (TextView) itemView.findViewById(R.id.tvAuthorName);
            this.review = (TextView) itemView.findViewById(R.id.tvReviewContent);
            this.context = context;
        }

    }

    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        super();
        mContext = context;
        this.reviewsList = reviews;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_review, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, final int position) {
        // Get the data model based on the position
        final Review reviewAttributes = reviewsList.get(position);

        holder.author.setText(reviewAttributes.getAuthor());
        holder.review.setText(reviewAttributes.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

}
