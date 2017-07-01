package jensklingenberg.de.jklauncher;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import jensklingenberg.de.jklauncher.ui.MainPagerAdapter;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  @BindView(R.id.vpPager) ViewPager vpPager;
  @BindView(R.id.mainLayout) ConstraintLayout mainLayout;

  MainPagerAdapter mainPagerAdapter;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
    vpPager.setAdapter(mainPagerAdapter);
  }
}
