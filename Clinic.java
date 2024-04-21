import java.sql.SQLException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Clinic extends Application {

  private Database db = new Database();
  private Stage addPatientStage = new Stage();
  private PatientList patientList;
  private AddPatient addPatient;

  public void start(Stage primaryStage) {
    // #region Create db connection
    try {
      db.connect();
    } catch (SQLException e) {
      primaryStage.close();
      System.out.println("Error connecting to database: " + e.getMessage());
    }
    // #endregion Create db connection

    // #region Create main vertical view
    VBox mainPane = new VBox();
    // #endregion Create main vertical view

    // #region Initialize AddPatient pane
    addPatient = new AddPatient(db, addPatientStage);
    // #endregion Initialize AddPatient pane

    // #region Create patient lists
    patientList = new PatientList(db, addPatient);
    mainPane.getChildren().add(patientList.getMainPane());
    VBox.setVgrow(patientList.getMainPane(), Priority.ALWAYS);
    // #endregion Create patient lists

    // #region Create control
    HBox controlBox = control(primaryStage);
    mainPane.getChildren().add(controlBox);
    // #endregion Create control

    // #region Create identification
    HBox idBox = new HBox();
    Label id = new Label("2702349666 - Yusril Muttaqien - Sistem Informasi");

    id.setOpacity(0.5);
    idBox.getChildren().add(id);
    idBox.setAlignment(Pos.CENTER);
    idBox.setPadding(new Insets(10, 10, 10, 10));
    mainPane.getChildren().add(idBox);
    // #endregion Create identification

    // #region Create cleanup on exit
    primaryStage
      .onCloseRequestProperty()
      .setValue(e -> {
        if (addPatientStage != null) {
          addPatientStage.close();
        }

        db.close();
      });
    primaryStage
      .focusedProperty()
      .addListener(e -> {
        if (primaryStage.isFocused()) {
          patientList.updateList();
        }
      });
    // #endregion Create cleanup on exit

    // #region Create the scene and show it
    Scene scene = new Scene(mainPane, 800, 800);
    primaryStage.setX(10);
    primaryStage.setY(50);
    primaryStage.setMinHeight(800);
    primaryStage.setMinWidth(800);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Pasien Klinik");
    primaryStage.show();
    // #endregion Create the scene and show it
  }

  private HBox control(Stage primaryStage) {
    HBox controlWrapper = new HBox();

    Button addPatientButton = new Button("Tambah pasien");
    addPatientButton.setOnAction(e -> {
      addPatient.refocus(null);
    });
    controlWrapper.getChildren().add(addPatientButton);

    Button exitApp = new Button("Keluar");
    exitApp.setOnAction(e -> {
      primaryStage.close();

      if (addPatientStage != null) {
        addPatientStage.close();
      }

      db.close();
    });
    controlWrapper.getChildren().add(exitApp);

    controlWrapper.setSpacing(10);
    controlWrapper.setAlignment(Pos.CENTER);
    controlWrapper.setPadding(new Insets(10, 10, 10, 10));

    return controlWrapper;
  }

  public static void main(String[] args) {
    launch(args);
  }
}
