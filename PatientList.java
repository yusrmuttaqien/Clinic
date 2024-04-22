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

  private TableView<Patient> tableview = new TableView<Patient>();
  private VBox mainPane = new VBox();
  private Notification notification;
  private AddPatient addPatient;
  private Database db;

  private Button update;
  private Button delete;

  public PatientList(
    Database db,
    AddPatient addPatient,
    Notification notification
  ) {
    this.db = db;
    this.addPatient = addPatient;
    this.notification = notification;

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

    update = new Button("Perbarui data");
    update.disableProperty().set(true);
    update.setOnAction(e -> {
      Patient activePatient = tableview.getFocusModel().getFocusedItem();

      if (notification.getStage().isShowing()) {
        notification.getStage().requestFocus();
      } else {
        addPatient.refocus(activePatient);
      }
    });
    controlWrapper.getChildren().add(update);

    delete = new Button("Hapus data");
    delete.disableProperty().set(true);
    delete.setOnAction(e -> {
      String activeNik = tableview.getFocusModel().getFocusedItem().getNik();

      if (addPatient.getStage().isShowing()) {
        addPatient.getStage().requestFocus();
      } else {
        notification.refocus(
          "Daftar Pasien",
          "Konfirmasi hapus data",
          "Apakah anda yakin ingin menghapus data dengan NIK: " +
          activeNik +
          "?",
          () -> {
            try {
              db.deletePatient(activeNik);
              rerenderButtons(true);
              updateList();
            } catch (SQLException v) {
              notification.refocus(
                "Daftar Pasien",
                "Gagal menghapus data",
                "Data dengan NIK: " +
                activeNik +
                " gagal dihapus dari database. Error: " +
                v.getMessage()
              );
            }
          },
          true
        );
      }
    });
    controlWrapper.getChildren().add(delete);

    tableview.onMouseClickedProperty().setValue(e -> rerenderButtons(false));
    tableview.onKeyPressedProperty().setValue(e -> rerenderButtons(false));

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

  public void rerenderButtons(boolean isReset) {
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
      rerenderButtons(true);
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
