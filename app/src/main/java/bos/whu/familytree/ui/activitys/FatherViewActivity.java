package bos.whu.familytree.ui.activitys;


import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import bos.whu.familytree.R;
import bos.whu.familytree.model.PersonBean;
import bos.whu.familytree.support.views.FatherView;
import bos.whu.familytree.support.views.TreeNode;
import bos.whu.familytree.support.views.TreeView;
import bos.whu.familytree.ui.fragments.HomeFragment;

public class FatherViewActivity extends ActionBarActivity {
private PersonBean person;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fatherview);
    person = HomeFragment.mPersonMap.get(getIntent().getExtras().get("pid"));
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        FatherViewActivity.this.finish();
      }
    });
    initViews();
  }

  private void initViews() {
    FatherView tv = (FatherView) findViewById(R.id.activity_fatherview_fatherview);
    tv.setRootNode(new TreeNode(this,person));
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

}
