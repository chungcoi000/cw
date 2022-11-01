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

import entities.TripExpenseEntity;

public class TripExpenseAdapter extends ArrayAdapter<TripExpenseEntity> {
  private final Context mContext;
  private final int mResources;

  public TripExpenseAdapter(@NonNull Context context, int resource, @NonNull ArrayList<TripExpenseEntity> objects) {
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

    TextView type = convertView.findViewById(R.id.txtExpenseType);
    TextView time = convertView.findViewById(R.id.txtExpenseTime);
    TextView amount = convertView.findViewById(R.id.txtExpenseAmount);

    type.setText(getItem(position).getExpenseType());
    time.setText("Time: " + getItem(position).getExpenseTime());
    amount.setText("Amount: " + getItem(position).getExpenseAmount() + " VND");

    return convertView;
  }
}
