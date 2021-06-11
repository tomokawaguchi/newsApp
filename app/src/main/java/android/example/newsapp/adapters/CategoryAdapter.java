package android.example.newsapp.adapters;

import android.example.newsapp.activities.MainActivity;
import android.example.newsapp.fragments.LifestyleFragment;
import android.example.newsapp.fragments.PoliticsFragment;
import android.example.newsapp.fragments.SportsFragment;
import android.example.newsapp.fragments.TechnologyFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CategoryAdapter extends FragmentStateAdapter {
    private final String[] tabNames = MainActivity.tabList;

    public CategoryAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PoliticsFragment();
            case 1:
                return new TechnologyFragment();
            case 2:
                return new SportsFragment();
            case 3:
                return new LifestyleFragment();
        }

        return new PoliticsFragment();
    }

    @Override
    public int getItemCount() {
        return tabNames.length;
    }
}
