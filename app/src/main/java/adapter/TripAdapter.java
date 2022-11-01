package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.m_expense.R;

import java.util.ArrayList;

import entities.TripEntity;

public class TripAdapter extends ArrayAdapter<TripEntity> {
  private final Context mContext;
  private final int mResources;

  public TripAdapter(@NonNull Context context, int resource, @NonNull ArrayList<TripEntity> objects) {
    super(context, resource, objects);
    this.mContext = context;
    this.mResources = resource;
  }

  @SuppressLint({"SetTextI18n", "ViewHolder"})
  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    LayoutInflater layoutInflater = LayoutInflater.from(mContext);

    convertView = layoutInflater.inflate(mResources, parent, false);

    TextView txtName = convertView.findViewById(R.id.txtName2);
    TextView txtDestination = convertView.findViewById(R.id.viewTripName);
    TextView txtDate = convertView.findViewById(R.id.txtDate);
    TextView txtDuration = convertView.findViewById(R.id.txtDuration);

    txtName.setText(getItem(position).getItemName());
    txtDestination.setText("Destination: " + getItem(position).getDestination());
    txtDate.setText("Date: " + getItem(position).getDate());
    txtDuration.setText("Duration: " + getItem(position).getDuration() + " day(s)");

    return convertView;
  }
}
