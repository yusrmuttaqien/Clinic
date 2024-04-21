import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {

  private String connURL = "jdbc:mysql://localhost:3306/final_clinic";
  private String username = "root";
  private String password = "";
  private Connection conn;

  public void connect() throws SQLException {
    try {
      conn = DriverManager.getConnection(connURL, username, password);
    } catch (SQLException e) {
      throw e;
    }
  }

  public List<Patient> getAllPatients() throws SQLException {
    try {
      ResultSet result = conn
        .prepareStatement("SELECT * FROM patient")
        .executeQuery();
      List<Patient> patients = new ArrayList<Patient>();

      while (result.next()) {
        patients.add(
          new Patient(
            result.getString("name"),
            result.getString("nik"),
            result.getString("address"),
            result.getString("birth")
          )
        );
      }

      return patients;
    } catch (SQLException e) {
      throw e;
    }
  }

  public void deletePatient(String nik) throws SQLException {
    try {
      conn
        .prepareStatement("DELETE FROM patient WHERE nik = '" + nik + "'")
        .execute();
    } catch (SQLException e) {
      throw e;
    }
  }

  public void updatePatient(Patient patient) throws SQLException {
    try {
      conn
        .prepareStatement(
          "UPDATE patient SET name = '" +
          patient.getName() +
          "', address = '" +
          patient.getAddress() +
          "', birth = '" +
          patient.getBirth() +
          "' WHERE nik = '" +
          patient.getNik() +
          "'"
        )
        .execute();
    } catch (SQLException e) {
      throw e;
    }
  }

  public void addPatient(Patient patient) throws SQLException {
    try {
      conn
        .prepareStatement(
          "INSERT INTO patient (name, nik, address, birth) VALUES ('" +
          patient.getName() +
          "', '" +
          patient.getNik() +
          "', '" +
          patient.getAddress() +
          "', '" +
          patient.getBirth() +
          "')"
        )
        .execute();
    } catch (SQLException e) {
      throw e;
    }
  }

  public void close() {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        System.out.println(
          "Error closing the database connection: " + e.getMessage()
        );
      }
    }
  }
}
