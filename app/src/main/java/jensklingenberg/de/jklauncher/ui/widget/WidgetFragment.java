package jensklingenberg.de.jklauncher.ui.widget;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jensklingenberg.de.jklauncher.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by jens on 1/7/17.
 */

public class WidgetFragment extends Fragment {

  private static final String TAG = "WidgetFragment";
  @BindView(R.id.frmWidget) FrameLayout frameLayout;
  Unbinder unbinder;
  AppWidgetManager mAppWidgetManager;
  AppWidgetHost mAppWidgetHost;

  private int APPWIDGET_HOST_ID = 1;
  private int REQUEST_PICK_APPWIDGET = 2;
  private int REQUEST_CREATE_APPWIDGET = 3;
  @BindView(R.id.btnAddWidget) Button btnAddWidget;

  public static WidgetFragment newInstance() {
    Bundle args = new Bundle();
    WidgetFragment fragment = new WidgetFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.widget_frag, container, false);

    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    initWidgetHost();
    btnAddWidget.setOnClickListener(v -> selectWidget());
  }

  private void initWidgetHost() {
    mAppWidgetManager = AppWidgetManager.getInstance(getContext().getApplicationContext());
    mAppWidgetHost = new AppWidgetHost(getContext().getApplicationContext(), APPWIDGET_HOST_ID);
  }

  /**
   * Launches the menu to select the widget. The selected widget will be on
   * the result of the activity.
   */
  void selectWidget() {
    int appWidgetId = this.mAppWidgetHost.allocateAppWidgetId();
    Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
    pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET);
  }


  /**
   * If the user has selected an widget, the result will be in the 'data' when
   * this function is called.
   */
  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      if (requestCode == REQUEST_PICK_APPWIDGET) {
        configureWidget(data);
      } else if (requestCode == REQUEST_CREATE_APPWIDGET) {
        createWidget(data);
      }
    } else if (resultCode == RESULT_CANCELED && data != null) {
      int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
      if (appWidgetId != -1) {
        mAppWidgetHost.deleteAppWidgetId(appWidgetId);
      }
    }
  }

  /**
   * Checks if the widget needs any configuration. If it needs, launches the
   * configuration activity.
   */
  private void configureWidget(Intent data) {
    Bundle extras = data.getExtras();
    int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
    AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);
    if (appWidgetInfo.configure != null) {
      Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
      intent.setComponent(appWidgetInfo.configure);
      intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
      startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
    } else {
      createWidget(data);
    }
  }

  /**
   * Creates the widget and adds to our view layout.
   */
  public void createWidget(Intent data) {
    Bundle extras = data.getExtras();
    int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
    AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);

    AppWidgetHostView hostView =
        mAppWidgetHost.createView(getContext().getApplicationContext(), appWidgetId, appWidgetInfo);
    hostView.setAppWidget(appWidgetId, appWidgetInfo);
    frameLayout.removeAllViews();
    frameLayout.addView(hostView);

    Log.i(TAG, "The widget size is: " + appWidgetInfo.minWidth + "*" + appWidgetInfo.minHeight);
  }

  /**
   * Registers the AppWidgetHost to listen for updates to any widgets this app
   * has.
   */
  @Override public void onStart() {
    super.onStart();
    mAppWidgetHost.startListening();
  }

  /**
   * Stop listen for updates for our widgets (saving battery).
   */
  @Override public void onStop() {
    super.onStop();
    mAppWidgetHost.stopListening();
  }
}
