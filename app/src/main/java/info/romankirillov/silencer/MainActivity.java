package info.romankirillov.silencer;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {

    private MyFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        this.pagerAdapter =
                new MyFragmentPagerAdapter(
                        getSupportFragmentManager());
        this.viewPager = (ViewPager) findViewById(R.id.pager);

        viewPager.setAdapter(pagerAdapter);

    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return new SilenceFragment();
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
