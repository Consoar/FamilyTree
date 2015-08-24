package bos.whu.familytree.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Picasso;

import bos.whu.familytree.R;
import bos.whu.familytree.model.PersonBean;
import bos.whu.familytree.support.views.CircularImageView;
import bos.whu.familytree.ui.fragments.HomeFragment;


public class DetailsActivity extends ActionBarActivity implements ObservableScrollViewCallbacks {
    private RelativeLayout mPersonDetailHeader;
    private View mToolbarView;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    private View mFatherViewBtn,mTreeViewBtn;
    private TextView mName,mYear;
    private CircularImageView headImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_details);
        final PersonBean person = HomeFragment.mPersonMap.get(getIntent().getExtras().get("pid"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailsActivity.this.finish();
            }
        });
        headImageView = (CircularImageView) findViewById(R.id.headImageView);
        if (person.getPhotourl()!=null) {
            Picasso.with(this).load(person.getPhotourl()).into(headImageView);
        } else {
            if (person.getSex()==1)
                headImageView.setImageResource(R.drawable.male_sill);
            else if (person.getSex()==2)
                headImageView.setImageResource(R.drawable.female_sill);
            else
                headImageView.setImageResource(R.drawable.unknown_sill);
        }
        mName = (TextView)findViewById(R.id.person_name);
        mYear = (TextView)findViewById(R.id.person_year);
        mName.setText(person.getFullname());
        mYear.setText(person.getBirthday());

        mFatherViewBtn = (View)findViewById(R.id.father_link_btn);
        mFatherViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(DetailsActivity.this, FatherViewActivity.class);
                intent.putExtra("pid",String.valueOf(person.getPid()));
                DetailsActivity.this.startActivity(intent);
            }
        });

        mTreeViewBtn = (View)findViewById(R.id.tree_link_btn);
        mTreeViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(DetailsActivity.this, TreeViewActivity.class);
                intent.putExtra("pid",String.valueOf(person.getPid()));
                DetailsActivity.this.startActivity(intent);
            }
        });

        mPersonDetailHeader = (RelativeLayout) findViewById(R.id.person_detail_header);
        mToolbarView = findViewById(R.id.toolbar);
        mToolbarView.setBackgroundColor(
                ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.theme_dialer_primary)));


        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.drawer_menu_width);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.theme_dialer_primary);
        float alpha = 1 - (float) Math.max(0, mParallaxImageHeight - scrollY) / mParallaxImageHeight;
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        final RelativeLayout mPersonDetailHeader = this.mPersonDetailHeader;
        ViewHelper.setTranslationY(mPersonDetailHeader, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }
}
