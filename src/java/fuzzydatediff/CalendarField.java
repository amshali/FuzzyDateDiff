package fuzzydatediff;

public enum CalendarField {

  YEAR("year"), 
  MONTH("month"), 
  WEEK("week"), 
  DAY("day"), 
  HOUR("hour"), 
  MIN("minute"), 
  SEC("second"),
  NOW("now");

  private CalendarField(String name) {
    this.name = name;
  }

  public final String name;

}
