package com.example.m_expense;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import entities.TripEntity;

public class MainActivity extends AppCompatActivity {
  EditText inputName, inputDestination, inputDate, inputDescription, inputDuration, inputContact;
  Button saveBtn, viewBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    DatabaseHelper dbHelper = new DatabaseHelper(this);

    inputName = findViewById(R.id.tripName);
    inputDestination = findViewById(R.id.tripDestination);
    inputDate = findViewById(R.id.tripDate);
    inputDuration = findViewById(R.id.tripDuration);
    inputDescription = findViewById(R.id.tripDescription);
    inputContact = findViewById(R.id.contact);
    RadioGroup radioGroup = findViewById(R.id.radioGroup2);

    inputDate.setOnFocusChangeListener((view, b) -> {
      if (b) {
        MyDatePicker dlg = new MyDatePicker();
        dlg.setExamDate(inputDate);
        dlg.show(getSupportFragmentManager(), "dateTimePicker");
      }
    });

    saveBtn = findViewById(R.id.saveBtn);
    saveBtn.setOnClickListener(view -> {
      int selectedId = radioGroup.getCheckedRadioButtonId();
      RadioButton radioBtn = findViewById(selectedId);
      String risk = radioBtn.getText().toString();
      String name = inputName.getText().toString();
      String destination = inputDestination.getText().toString();
      String date = inputDate.getText().toString();
      String contact = inputContact.getText().toString();
      String duration = inputDuration.getText().toString();
      String description = inputDescription.getText().toString();

      if (TextUtils.isEmpty(name)) {
        inputName.setError("Trip name is required!");
        return;
      }
      if (TextUtils.isEmpty(destination)) {
        inputDestination.setError("Trip destination is required!");
        return;
      }

      if (TextUtils.isEmpty(date)) {
        inputDate.setError("Trip date is required!");
        return;
      }

      if (TextUtils.isEmpty(duration)) {
        inputDuration.setError("Trip duration is required!");
        return;
      }

      if (TextUtils.isEmpty(contact)) {
        inputContact.setError("Contact is required!");
        return;
      }

      AlertDialog.Builder alert = new AlertDialog.Builder(this);
      alert.setIcon(android.R.drawable.ic_input_add);
      alert.setTitle("Confirm information");
      alert.setMessage("Do you want to add this trip?");

      View dialog = getLayoutInflater().inflate(R.layout.confirm_layout, null);

      final TextView confirmName = dialog.findViewById(R.id.confirmName);
      final TextView confirmDestination = dialog.findViewById(R.id.confirmDestination);
      final TextView confirmDate = dialog.findViewById(R.id.confirmDate);
      final TextView confirmDuration = dialog.findViewById(R.id.confirmDuration);
      final TextView confirmContact = dialog.findViewById(R.id.confirmContact);
      final TextView confirmRisk = dialog.findViewById(R.id.confirmRisk);
      final TextView confirmDescription = dialog.findViewById(R.id.confirmDescription);

      confirmName.setText(String.format("Trip name: %s", name));
      confirmDestination.setText(String.format("Trip destination: %s", destination));
      confirmDate.setText(String.format("Trip date: %s", date));
      confirmDuration.setText(String.format("Trip duration: %s", duration));
      confirmContact.setText(String.format("Trip contact: %s", contact));
      confirmRisk.setText(String.format("Trip risk: %s", risk));
      confirmDescription.setText(String.format("Trip description: %s", description));

      alert.setView(dialog);

      alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          dbHelper.insertTrips(name, destination, date, duration, contact, risk, description);
          inputName.setText("");
          inputDate.setText("");
          inputDuration.setText("");
          inputDestination.setText("");
          radioGroup.clearCheck();
          radioGroup.check(R.id.radioButton2);
          inputDescription.setText("");
          Toast.makeText(MainActivity.this, "Trip was saved!", Toast.LENGTH_SHORT).show();
        }
      });
      alert.setNegativeButton("Cancel", null);
      alert.show();
    });

    viewBtn = findViewById(R.id.viewBtn);
    viewBtn.setOnClickListener(view -> {
      ArrayList<TripEntity> tripEntities = (ArrayList<TripEntity>) dbHelper.getTrips();
      if (tripEntities.isEmpty()) {
        Toast.makeText(this, "There is no trip in trip list. Please add more trip!", Toast.LENGTH_LONG).show();
        return;
      } else {
        Intent intent = new Intent(MainActivity.this, TripList.class);
        startActivity(intent);
      }
    });
  }

  public static class MyDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    EditText examDate;

    public EditText getExamDate() {
      return examDate;
    }

    public void setExamDate(EditText examDate) {
      this.examDate = examDate;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
      final Calendar c = Calendar.getInstance();
      int year = c.get(Calendar.YEAR);
      int month = c.get(Calendar.MONTH);
      int day = c.get(Calendar.DAY_OF_MONTH);
      // Create a new instance of DatePickerDialog and return it
      return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
      int month = i1 + 1;
      String dateValue = i2 + "/" + month + "/" + i;
      examDate.setText(dateValue);
    }
  }
}

