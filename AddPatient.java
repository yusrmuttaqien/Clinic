import java.sql.SQLException;
import java.time.LocalDate;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddPatient extends Application {

  private Patient activePatient = null;
  private Stage primaryStage;
  private double labelWidth = 100;
  private Database db;

  public AddPatient(Database db, Stage primaryStage) {
    this.db = db;
    this.primaryStage = primaryStage;
  }

  public void start(Stage primaryStage) {
    VBox mainPane = new VBox();

    // #region Name field
    HBox nameBox = new HBox();
    Label nameLabel = new Label("Nama :");
    TextField nameField = new TextField();
    nameBox.getChildren().add(nameLabel);
    nameBox.getChildren().add(nameField);
    nameLabel.setPrefWidth(labelWidth);
    mainPane.getChildren().add(nameBox);
    // #endregion Name field

    // #region Address field
    HBox addressBox = new HBox();
    Label addressLabel = new Label("Alamat :");
    TextArea addressField = new TextArea();
    addressBox.getChildren().add(addressLabel);
    addressBox.getChildren().add(addressField);
    addressLabel.setPrefWidth(labelWidth);
    mainPane.getChildren().add(addressBox);
    // #endregion Address field

    // #region NIK field
    HBox nikBox = new HBox();
    Label nikLabel = new Label("NIK :");
    TextField nikField = new TextField();
    nikBox.getChildren().add(nikLabel);
    nikBox.getChildren().add(nikField);
    nikLabel.setPrefWidth(labelWidth);
    mainPane.getChildren().add(nikBox);
    // #endregion NIK field

    // #region Birth field
    HBox birthBox = new HBox();
    Label birthLabel = new Label("Tanggal lahir :");
    DatePicker birthField = new DatePicker();
    birthBox.getChildren().add(birthLabel);
    birthBox.getChildren().add(birthField);
    birthLabel.setPrefWidth(labelWidth);
    mainPane.getChildren().add(birthBox);
    // #endregion Birth field

    // #region Create control
    HBox controlWrapper = new HBox();

    if (activePatient == null) {
      Button saveStay = new Button("Simpan");
      saveStay.setOnAction(e -> {
        Patient patient = parseForm(
          nameField,
          nikField,
          addressField,
          birthField
        );

        if (patient != null) {
          try {
            commit(patient);
          } catch (Exception ex) {
            // TODO: Use notification pane
            System.out.println("Error saving patient: " + ex.getMessage());
          }
        }
      });
      controlWrapper.getChildren().add(saveStay);
    }

    Button saveExit = new Button("Simpan & Tutup");
    saveExit.setOnAction(e -> {
      Patient patient = parseForm(
        nameField,
        nikField,
        addressField,
        birthField
      );

      if (patient != null) {
        try {
          commit(patient);
          primaryStage.close();
        } catch (Exception ex) {
          // TODO: Use notification pane
          System.out.println("Error saving patient: " + ex.getMessage());
        }
      }
    });
    controlWrapper.getChildren().add(saveExit);

    controlWrapper.setSpacing(10);
    controlWrapper.setPadding(new Insets(10, 0, 0, 0));
    controlWrapper.setAlignment(Pos.CENTER_RIGHT);

    mainPane.getChildren().add(controlWrapper);
    // #endregion Create control

    if (activePatient != null) {
      LocalDate birthDate = LocalDate.parse(activePatient.getBirth());

      nameField.setText(activePatient.getName());
      addressField.setText(activePatient.getAddress());
      nikField.setText(activePatient.getNik());
      nikField.disableProperty().set(true);
      birthField.setValue(birthDate);
    }

    mainPane.setPadding(new Insets(10));
    mainPane.setSpacing(10);
    mainPane.autosize();
    Scene scene = new Scene(mainPane);

    primaryStage.setResizable(false);
    primaryStage.setX(60);
    primaryStage.setY(100);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Tambah pasien");
    primaryStage.show();
  }

  public Patient parseForm(
    TextField nameField,
    TextField nikField,
    TextArea addressField,
    DatePicker birthField
  ) {
    try {
      return new Patient(
        nameField.getText(),
        nikField.getText(),
        addressField.getText(),
        birthField.getValue().toString()
      );
    } catch (Exception e) {
      // TODO: Use notification pane
      System.out.println("Error parsing form: " + e.getMessage());
      return null;
    }
  }

  public void commit(Patient patient) throws SQLException {
    if (activePatient != null) {
      try {
        db.updatePatient(patient);
      } catch (SQLException e) {
        throw e;
      }
    } else {
      try {
        db.addPatient(patient);
      } catch (SQLException e) {
        throw e;
      }
    }
  }

  public void refocus(Patient patient) {
    if (
      activePatient != null &&
      patient != null &&
      activePatient.getNik() != patient.getNik()
    ) {
      if (primaryStage.isShowing()) {
        primaryStage.close();
      }

      activePatient = patient;
      start(primaryStage);
    } else {
      if (primaryStage.isShowing()) {
        primaryStage.requestFocus();
      } else {
        activePatient = patient;
        start(primaryStage);
      }
    }
  }
}
