import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Patient {

  private String name;
  private String nik; // using string because int could be requires big int
  private String address;
  private String birth;

  public Patient(String name, String nik, String address, String birth) {
    this.name = name;
    this.nik = nik;
    this.address = address;
    this.birth = birth;
  }

  public String getName() {
    return name;
  }

  public String getNik() {
    return nik;
  }

  public int getNumericNik() {
    return Integer.parseInt(nik);
  }

  public String getAddress() {
    return address;
  }

  public String getBirth() {
    return birth;
  }

  public String getFormattedBirth() {
    LocalDate date = LocalDate.parse(birth);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MMM-dd");

    return formatter.format(date).toString();
  }
}
