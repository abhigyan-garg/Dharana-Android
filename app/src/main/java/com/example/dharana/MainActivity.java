package com.example.dharana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private BottomNavigationView bottomNavigationView;
    private MenuItem previousMenuItem;
    private GuruMantraFragment guruMantraFragment;
    private ChakraFragment chakraFragment;
    private KriyaFragment kriyaFragment;
    private boolean bottomMarginSet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.linearLayout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        ViewCompat.setOnApplyWindowInsetsListener(linearLayout, (v, insets) -> {
            if(!bottomMarginSet) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bottomNavigationView.getLayoutParams();
                params.bottomMargin = Math.round(insets.getSystemWindowInsetBottom() - 6*getResources().getDisplayMetrics().density);
                bottomNavigationView.setLayoutParams(params);
                bottomMarginSet = true;
            }
            return insets.consumeSystemWindowInsets();
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(previousMenuItem != null)
                    previousMenuItem.setChecked(false);

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                previousMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        kriyaFragment.switchedFrom();
                        chakraFragment.switchedFrom();
                        guruMantraFragment.switchedTo();
                        break;

                    case 1:
                        guruMantraFragment.switchedFrom();
                        chakraFragment.switchedFrom();
                        kriyaFragment.switchedTo();
                        break;
                    case 2:
                        kriyaFragment.switchedFrom();
                        guruMantraFragment.switchedFrom();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.guru);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.guru:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.kriya:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.chakra:
                    viewPager.setCurrentItem(2);
                    break;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.guru);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        guruMantraFragment = new GuruMantraFragment();
        kriyaFragment = new KriyaFragment();
        chakraFragment = new ChakraFragment();
        adapter.addFragment(guruMantraFragment);
        adapter.addFragment(kriyaFragment);
        adapter.addFragment(chakraFragment);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        switch(viewPager.getCurrentItem()) {
            case 0:
                guruMantraFragment.switchedFrom();
                break;
            case 1:
                kriyaFragment.switchedFrom();
                break;
            case 2:
                chakraFragment.switchedFrom();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch(viewPager.getCurrentItem()) {
            case 0:
                if(guruMantraFragment != null)
                    guruMantraFragment.switchedTo();
                break;
            case 1:
                if(kriyaFragment != null)
                    kriyaFragment.switchedTo();
                break;
        }
    }
}