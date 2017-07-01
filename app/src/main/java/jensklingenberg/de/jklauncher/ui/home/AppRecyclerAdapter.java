package jensklingenberg.de.jklauncher.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import jensklingenberg.de.jklauncher.R;

/**
 * Created by jens on 26/6/17.
 */
public class AppRecyclerAdapter extends RecyclerView.Adapter<AppRecyclerAdapter.ViewHolder> {

  private static final String TAG = AppRecyclerAdapter.class.getSimpleName();

  private Context context;
  private List<ApplicationInfo> list;

  public AppRecyclerAdapter(Context context, List<ApplicationInfo> list) {
    this.context = context;
    sortAlphabetical(context, list);
    this.list = list;
  }

  private void sortAlphabetical(final Context context, List<ApplicationInfo> list) {
    Collections.sort(list, new Comparator<ApplicationInfo>() {
      @Override public int compare(ApplicationInfo lhs, ApplicationInfo rhs) {
        PackageManager pm = context.getPackageManager();
        String app1Name = pm.getApplicationLabel(lhs).toString().toLowerCase();
        String app2Name = pm.getApplicationLabel(rhs).toString().toLowerCase();

        return app1Name.compareTo(app2Name);
      }
    });
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    final ApplicationInfo item = list.get(position);
    PackageManager pm = context.getPackageManager();

    holder.tvAppName.setText(pm.getApplicationLabel(item));
    final Intent appStartIntent = pm.getLaunchIntentForPackage(item.packageName);
    Drawable d = null;
    try {
      d = context.getPackageManager().getApplicationIcon(item.packageName);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    holder.ivAppIcon.setBackground(d);
    holder.llPackage.setOnClickListener(v -> {

      if (null != appStartIntent) {
        context.startActivity(appStartIntent);
      }
    });
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    View view = inflater.inflate(R.layout.list_item, parent, false);
    ButterKnife.bind(this, view);

    ViewHolder viewHolder = new ViewHolder(view);

    return viewHolder;
  }

  @Override public int getItemCount() {
    return list.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    // Todo Butterknife bindings

    @BindView(R.id.tvAppName) TextView tvAppName;
    @BindView(R.id.ivAppIcon) ImageView ivAppIcon;
    @BindView(R.id.llPackage) LinearLayout llPackage;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}