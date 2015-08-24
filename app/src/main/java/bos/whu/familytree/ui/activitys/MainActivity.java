package bos.whu.familytree.ui.activitys;

import android.os.Bundle;

import bos.whu.familytree.R;
import bos.whu.familytree.support.constant.FragmentNames;
import bos.whu.familytree.ui.activitys.base.BaseActivity;
import bos.whu.familytree.ui.fragments.HomeFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.screen_default_container, new HomeFragment(), FragmentNames.FRAGMENT_HOME)
                    .commit();
        }
    }

    @Override
    protected int setLayoutResourceIdentifier() {
        return R.layout.activity_home;
    }

    @Override
    protected int getTitleToolBar() {
        return R.string.app_name;
    }
}
