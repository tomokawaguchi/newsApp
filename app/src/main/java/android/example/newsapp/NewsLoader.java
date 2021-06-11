package android.example.newsapp;

import android.content.Context;
import android.example.newsapp.models.News;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private final String url;

    public NewsLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<News> loadInBackground() {
        if (url == null) {
            return null;
        }

        // if url is not null, fetch the data (referer to the Util class)
        List<News> list = Utils.fetchNews(url);
        return list;
    }
}
