package jensklingenberg.de.jklauncher.ui.home;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import java.util.ArrayList;
import java.util.List;
import jensklingenberg.de.jklauncher.MainActivity;
import jensklingenberg.de.jklauncher.R;

/**
 * Created by jens on 1/7/17.
 */

public class HomeScreenFragment extends Fragment {

  private static final String TAG = MainActivity.class.getName();
  @BindView(R.id.rvApps) RecyclerView rvApps;
  @BindView(R.id.mainLayout) ConstraintLayout mainLayout;
  Unbinder unbinder;
  AppRecyclerAdapter appRecyclerAdapter;

  public static HomeScreenFragment newInstance() {
    Bundle args = new Bundle();
    HomeScreenFragment fragment = new HomeScreenFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public static List<ApplicationInfo> getAllInstalledApplications(Context context) {
    List<ApplicationInfo> installedApps =
        context.getPackageManager().getInstalledApplications(PackageManager.PERMISSION_GRANTED);
    List<ApplicationInfo> launchableInstalledApps = new ArrayList<ApplicationInfo>();
    for (int i = 0; i < installedApps.size(); i++) {
      if (context.getPackageManager().getLaunchIntentForPackage(installedApps.get(i).packageName)
          != null) {
        launchableInstalledApps.add(installedApps.get(i));
      }
    }
    return launchableInstalledApps;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.homescreen_frag, container, false);

    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initRvApps();
  }

  private void initRvApps() {
    List<ApplicationInfo> packages = getAllInstalledApplications(getContext());
    appRecyclerAdapter = new AppRecyclerAdapter(getContext(), packages);
    StaggeredGridLayoutManager gridLayoutManager =
        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    rvApps.setLayoutManager(gridLayoutManager);
    rvApps.setAdapter(appRecyclerAdapter);
  }
}
