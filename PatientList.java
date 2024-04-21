import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PatientList {

  private VBox mainPane = new VBox();
  private TableView<Patient> tableview = new TableView<Patient>();
  private Database db;
  private AddPatient addPatient;

  public PatientList(Database db, AddPatient addPatient) {
    this.db = db;
    this.addPatient = addPatient;

    render();
    updateList();
  }

  private void render() {
    // #region fields
    TableColumn<Patient, String> nameColumn = new TableColumn<Patient, String>(
      "Nama"
    );
    nameColumn.setCellValueFactory(
      new PropertyValueFactory<Patient, String>("name")
    );
    tableview.getColumns().add(nameColumn);

    TableColumn<Patient, String> nikColumn = new TableColumn<Patient, String>(
      "NIK"
    );
    nikColumn.setCellValueFactory(
      new PropertyValueFactory<Patient, String>("nik")
    );
    tableview.getColumns().add(nikColumn);

    TableColumn<Patient, String> addressColumn = new TableColumn<Patient, String>(
      "Alamat"
    );
    addressColumn.setCellValueFactory(
      new PropertyValueFactory<Patient, String>("address")
    );
    tableview.getColumns().add(addressColumn);

    TableColumn<Patient, Date> birthColumn = new TableColumn<Patient, Date>(
      "Tanggal lahir"
    );
    birthColumn.setCellValueFactory(
      new PropertyValueFactory<Patient, Date>("formattedBirth")
    );
    tableview.getColumns().add(birthColumn);

    mainPane.getChildren().add(tableview);
    VBox.setVgrow(tableview, Priority.ALWAYS);
    // #endregion fields

    // #region action
    HBox controlWrapper = new HBox();

    Button update = new Button("Perbarui data");
    update.disableProperty().set(true);
    update.setOnAction(e -> {
      Patient activePatient = tableview.getFocusModel().getFocusedItem();

      addPatient.refocus(activePatient);
    });
    controlWrapper.getChildren().add(update);

    Button delete = new Button("Hapus data");
    delete.disableProperty().set(true);
    delete.setOnAction(e -> {
      String activeNik = tableview.getFocusModel().getFocusedItem().getNik();

      try {
        db.deletePatient(activeNik);
        rerenderButtons(update, delete, true);
        updateList();
      } catch (SQLException v) {
        // TODO: Use notification pane
        System.out.println("Error when deleting data: " + v.getMessage());
      }
    });
    controlWrapper.getChildren().add(delete);

    tableview
      .onMouseClickedProperty()
      .setValue(e -> rerenderButtons(update, delete, false));
    tableview
      .onKeyPressedProperty()
      .setValue(e -> rerenderButtons(update, delete, false));

    controlWrapper.setSpacing(10);
    controlWrapper.setAlignment(Pos.CENTER);
    controlWrapper.setPadding(new Insets(10, 10, 10, 10));
    mainPane.getChildren().add(controlWrapper);
    // #endregion action

    tableview.setPlaceholder(new Label("Tidak ada data"));
    tableview.setColumnResizePolicy(
      TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
    );
  }

  public void rerenderButtons(Button update, Button delete, boolean isReset) {
    if (isReset) {
      update.setText("Perbarui data");
      update.disableProperty().set(true);

      delete.setText("Hapus data");
      delete.disableProperty().set(true);

      return;
    }
    Patient active = tableview.getFocusModel().getFocusedItem();

    if (active == null) {
      return;
    }

    String activeNik = active.getNik();

    update.setText("Perbarui data NIK: " + activeNik);
    update.disableProperty().set(false);

    delete.setText("Hapus data NIK: " + activeNik);
    delete.disableProperty().set(false);
  }

  public void updateList() {
    tableview.getItems().clear();

    try {
      List<Patient> patients = db.getAllPatients();
      patients.forEach(e -> tableview.getItems().add(e));
    } catch (SQLException e) {
      System.out.println("Error when retriving data: " + e.getMessage());
    }
  }

  public VBox getMainPane() {
    return mainPane;
  }

  public TableView<Patient> getTableview() {
    return tableview;
  }
}
