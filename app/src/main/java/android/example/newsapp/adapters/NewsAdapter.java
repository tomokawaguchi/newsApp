package android.example.newsapp.adapters;

import android.content.Context;
import android.example.newsapp.R;
import android.example.newsapp.models.News;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private final Context context;
    private final List<News> newsList;
    private final LayoutInflater inflater;
    private onItemClickListener listener;

    // Constructor
    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
        inflater = LayoutInflater.from(context);
    }

    // Setting up the onItemClickListener interface
    public interface onItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // This method allows parent to access
    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    // Setting up the ViewHolder Subclass
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTV;
        private final TextView authorTV;
        private final TextView dateTV;
        private final ImageView thumbnailIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.title_text_view);
            authorTV = itemView.findViewById(R.id.author_text_view);
            dateTV = itemView.findViewById(R.id.date_text_view);
            thumbnailIV = itemView.findViewById(R.id.thumbnail_image_view);

            // Setting the onClick listener along with onItemClickListener on VH
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listener.onItemClick(itemView, position);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        News currentNews = newsList.get(position);

        // Binding data with each view
        // If there is an author name, it is separated with " | "
        String separator = context.getString(R.string.separator);
        // Both data obtained below are identical as per Utils data JSON parsing
        String currentTitle = currentNews.getTitle();
        // If there is an author name in the title,remove it
        if (currentTitle.contains(separator)) {
            currentTitle = currentTitle.substring(0, currentTitle.indexOf(separator));
        }

        TextView titleView = holder.titleTV;
        titleView.setText(currentTitle);

        // If there is no author name, set the default "Guardian"
        // If there are multiple authors, showcase them with "&" in between
        TextView authorView = holder.authorTV;
        String author = "";
        ArrayList currentAuthor = currentNews.getAuthor();
        int numOfAuthors = currentAuthor.size();
        if (numOfAuthors == 0) {
            author = context.getString(R.string.default_author_guardian);
        } else if (numOfAuthors == 1) {
            author = (String) currentAuthor.get(0);
        } else {
            author = (String) currentAuthor.get(0);
            for (int i = 0; i < numOfAuthors; i++) {
                author += context.getString(R.string.author_and_symbol) + currentAuthor.get(i);
            }
        }

        authorView.setText(author);

        TextView dateView = holder.dateTV;
        String obtainedDateData = currentNews.getDate();
        // Obtaining substring form of "2021-05-27"
        String dateString = obtainedDateData.substring(0, 10);
        dateView.setText(dateString);

        ImageView thumbnailView = holder.thumbnailIV;
        Drawable thumbnailDrawable = currentNews.getImageUrl();
        thumbnailView.setImageDrawable(thumbnailDrawable);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
