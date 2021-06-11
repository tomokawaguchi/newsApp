package android.example.newsapp.activities;

import android.content.res.Resources;
import android.example.newsapp.R;
import android.example.newsapp.adapters.CategoryAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private CategoryAdapter categoryAdapter;

    public static String[] tabList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtaining views by Id
        tabLayout = findViewById(R.id.tabs_layout);
        viewPager = findViewById(R.id.view_pager2);

        // Obtaining tab names from String file
        Resources res = getResources();
        tabList = new String[]{
                res.getString(R.string.tab_politics),
                res.getString(R.string.tab_technology),
                res.getString(R.string.tab_sports),
                res.getString(R.string.tab_lifestyle)
        };

        // Instantiate an adapter to handle the category switch
        categoryAdapter = new CategoryAdapter(this);
        viewPager.setAdapter(categoryAdapter);

        // Setting up the TabLayout with ViewPager2 by using TabLayoutMediator
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                // Setting up the tab names
                tab.setText(tabList[position]);
            }
        }).attach();
    }

    /**
     * Setting up the back button behaviour so that it goes back to the previous step
     */
    @Override
    public void onBackPressed() {
        // Obtaining the current item position
        int currentItemPosition = viewPager.getCurrentItem();
        // If user is currently at the first page, we can let the system to handle back button
        // Otherwise the back button should lead to the previous page
        if (currentItemPosition == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(currentItemPosition - 1);
        }
    }
}
