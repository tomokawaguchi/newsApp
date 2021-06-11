package android.example.newsapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.example.newsapp.NewsLoader;
import android.example.newsapp.R;
import android.example.newsapp.adapters.NewsAdapter;
import android.example.newsapp.models.News;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LifestyleFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<News> newsList;
    private LoaderManager loaderManager;
    private TextView emptyTextView;
    private TextView noInternetTextView;
    private ProgressBar spinner;

    private static final int NEWS_LOADER_ID = 4;

    // Constructor
    public LifestyleFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lifestyle_fragment, container, false);

        // Obtaining obj by ID
        recyclerView = rootView.findViewById(R.id.recycler_view);
        emptyTextView = rootView.findViewById(R.id.empty_text_view);
        noInternetTextView = rootView.findViewById(R.id.no_internet_text_view);
        spinner = rootView.findViewById(R.id.spinner_progress_bar);

        // Instantiate the list
        newsList = new ArrayList<>();

        //Check the state of network connectivity by using connectivity manager
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // If network connection exist, start fetching data
        if (networkInfo != null && networkInfo.isConnected()) {
            if (newsList.isEmpty()) {
                spinner.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

            // Instantiate LoaderManager
            loaderManager = LoaderManager.getInstance(this);
            //Initiate the Loader by passing unique ID and place of where the callback is called
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // If there is no network connection, the no-internet text view should be visible instead
            spinner.setVisibility(View.GONE);
            noInternetTextView.setVisibility(View.VISIBLE);
        }

        // Setting RecyclerView with custom adapter
        adapter = new NewsAdapter(getContext(), newsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        // Setting the onItemClickListener so that we can set an intent to open a web link
        adapter.setOnItemClickListener(new NewsAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                // Obtaining the web URL based on the given position of the clicked item
                String clickedItemWebUrl = newsList.get(position).getUrl();
                // Set the intent with obtained URL detail and initiate it
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(clickedItemWebUrl));
                startActivity(intent);
            }
        });

        return rootView;
    }

    @NonNull
    @Override
    public NewsLoader onCreateLoader(int id, @Nullable Bundle args) {
        // Referencing the following url
        // https://content.guardianapis.com/lifeandstyle?api-key=996b4b09-b72c-4e8e-9cf4-d3380adbe39c&show-fields=thumbnail&show-tags=contributor

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").authority("content.guardianapis.com")
                .appendPath("lifeandstyle")
                .appendQueryParameter("api-key", "996b4b09-b72c-4e8e-9cf4-d3380adbe39c")
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("show-fields", "thumbnail");

        // Instantiate the new custom loader
        return new NewsLoader(requireContext(), builder.build().toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        // clear the previous data
        newsList.clear();
        // If there is a valid list, add the data and notify the adapter
        if (data != null && !data.isEmpty()) {
            newsList.addAll(data);
            adapter.notifyDataSetChanged();

            //Until the data is updated, the spinner is showing up
            // Swap the visibility between spinner and recyclerView
            spinner.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

        } else {
            // After attempting fetching data, it somewhat returned empty. Then show the empty textView
            spinner.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        newsList.clear();
    }
}