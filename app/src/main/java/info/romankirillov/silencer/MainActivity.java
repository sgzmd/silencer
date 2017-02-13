package info.romankirillov.silencer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private MyFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Log.d(TAG, "starting MainActivity.OnCreate");

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        this.pagerAdapter =
                new MyFragmentPagerAdapter(
                        getSupportFragmentManager());
        this.viewPager = (ViewPager) findViewById(R.id.pager);

        viewPager.setAdapter(pagerAdapter);

//        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        tabLayout.addTab(tabLayout.newTab().setText("Silence now"));
//        tabLayout.addTab(tabLayout.newTab().setText("Schedule"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("Main", "Pos: " + position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        viewPager.setCurrentItem(0);
        initialiseNotification();
    }


    private void initialiseNotification() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean enableNotification = sharedPreferences
                .getBoolean(SettingsActivity.KEY_ENABLE_STICKY_NOTIF, true);

        if (!sharedPreferences.contains(SettingsActivity.KEY_ENABLE_STICKY_NOTIF)) {
            sharedPreferences.edit().putBoolean(SettingsActivity.KEY_ENABLE_STICKY_NOTIF, true);
        }

        if (enableNotification) {
            NotificationHelper.createOrUpdateNotification(this);
        }
    }


    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new SilenceFragment();
            } else {
                return new SilenceFragment();
            }
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Silence";
        }
    }
}
