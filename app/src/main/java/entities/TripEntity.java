package entities;

import androidx.annotation.NonNull;

public class TripEntity {

  public TripEntity(int trip_id, String name, String destination, String date, String duration, String risk, String description) {
    this.trip_id = trip_id;
    this.name = name;
    this.destination = destination;
    this.date = date;
    this.duration = duration;
    this.description = description;
    this.risk = risk;
  }

  public int getTrip_id() {
    return trip_id;
  }

  public void setTrip_id(int trip_id) {
    this.trip_id = trip_id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getRisk() {
    return risk;
  }

  public void setRisk(String risk) {
    this.risk = risk;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public String getItemName() {
    return trip_id + " - " + name;
  }

  @NonNull
  @Override
  public String toString() {
    return trip_id + "-" + name + "-" + destination + "-" + date + "-" + duration;
  }

  protected int trip_id;
  protected String name;
  protected String destination;
  protected String date;
  protected String duration;
  protected String risk;
  protected String description;
}
