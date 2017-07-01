package jensklingenberg.de.jklauncher.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import jensklingenberg.de.jklauncher.ui.home.HomeScreenFragment;
import jensklingenberg.de.jklauncher.ui.widget.WidgetFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {

  @Override public Fragment getItem(int position) {
    switch (position){
      case 0:
        return HomeScreenFragment.newInstance();
      case 1:
        return WidgetFragment.newInstance();
    }
return null;
  }

  @Override public int getCount() {
    return 2;
  }

  public MainPagerAdapter(FragmentManager fragmentManager) {
    super(fragmentManager);
  }
}