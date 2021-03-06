package lin.rong.myandroidframework.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import in.srain.cube.views.pager.TabPageIndicator;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import lin.rong.myandroidframework.adapter.FragmentViewPagerAdapter;
import lin.rong.myandroidframework.fragment.LeftDrawerFragment;
import lin.rong.myandroidframework.R;
import lin.rong.myandroidframework.fragment.ViewPagerFragment;
import lin.rong.myandroidframework.viewholder.TabPageIndicatorItemViewHolder;

public class MainActivity extends ActionBarActivity {

    private final int startIndex = 0;

    private DrawerLayout mDrawerLayout;
    private PtrFrameLayout mPtrFrame;
    private ViewPager mViewPager;
    private ActionBar actionBar;
    private LeftDrawerFragment leftDrawerFragment;
    private TabPageIndicator mTabPageIndicator;

    private FragmentViewPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.view_pager_ptr_frame);
        mPtrFrame.disableWhenHorizontalMove(true);

        mTabPageIndicator = (TabPageIndicator) findViewById(R.id.view_pager_tab_indicator);
        ArrayList<ViewPagerFragment> list = new ArrayList<ViewPagerFragment>();

        for (int i = 1; i <= 8; i++) {
            list.add(ViewPagerFragment.create(i));
        }

        mPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mPtrFrame, list);

        mPtrFrame.setPtrHandler(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return mPagerAdapter.checkCanDoRefresh();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPagerAdapter.updateData();
            }

        });

        mViewPager = (ViewPager) findViewById(R.id.myViewPager);
        mViewPager.setAdapter(mPagerAdapter);

        mTabPageIndicator.setViewHolderCreator(new TabPageIndicator.ViewHolderCreator() {

            @Override
            public TabPageIndicator.ViewHolderBase createViewHolder() {
                return new TabPageIndicatorItemViewHolder();
            }

        });
        mTabPageIndicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int i) {
                switchTo(i);
            }

        });
        mTabPageIndicator.setViewPager(mViewPager);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.myDrawerLayout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        leftDrawerFragment = (LeftDrawerFragment) getFragmentManager().findFragmentById(R.id.leftDrawerFragment);
        leftDrawerFragment.init(mDrawerLayout, leftDrawerFragment);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mViewPager.setCurrentItem(startIndex);
    }

    private void switchTo(int position) {
        mPagerAdapter.switchTO(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.msg) {
            Toast.makeText(MainActivity.this, "打开消息界面", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.about) {
            Toast.makeText(MainActivity.this, getString(R.string.about_desc), Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTabPageIndicator.moveToItem(mViewPager.getCurrentItem());
    }

}
