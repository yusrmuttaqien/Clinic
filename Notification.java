import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Notification extends Application {

  private Stage primaryStage = new Stage();
  private NotificationCallback callback;
  private boolean isConfirmation;
  private String windowTitle;
  private String title;
  private String desc;

  public void start(Stage primaryStage) {
    VBox mainPane = new VBox();

    Text title = new Text(this.title);
    title.setWrappingWidth(300 - (20 * 2));
    title.textAlignmentProperty().set(TextAlignment.CENTER);
    title.styleProperty().set("-fx-font-weight: bold; -fx-font-size: 16px;");
    mainPane.getChildren().add(title);

    Text desc = new Text(this.desc);
    desc.setWrappingWidth(300 - (20 * 2));
    desc.textAlignmentProperty().set(TextAlignment.CENTER);
    mainPane.getChildren().add(desc);

    // #region Create control
    VBox controlWrapper = new VBox();

    if (isConfirmation) {
      Button next = new Button("Oke");
      next.setPrefWidth(300 - (20 * 2));
      next.setOnAction(e -> {
        callback.callback();
        primaryStage.close();
      });
      controlWrapper.getChildren().add(next);
    }

    Button close = new Button("Tutup");
    close.setPrefWidth(300 - (20 * 2));
    close.setOnAction(e -> {
      if (!isConfirmation && callback != null) {
        callback.callback();
      }

      primaryStage.close();
    });
    controlWrapper.getChildren().add(close);

    controlWrapper.setSpacing(10);
    controlWrapper.setPadding(new Insets(20, 0, 0, 0));

    mainPane.getChildren().add(controlWrapper);
    // #endregion Create control

    mainPane.setPadding(new Insets(50, 20, 20, 20));
    mainPane.setAlignment(Pos.CENTER);
    mainPane.setSpacing(10);
    mainPane.autosize();
    mainPane.setMaxWidth(300);
    mainPane.setMinWidth(300);
    Scene scene = new Scene(mainPane);

    primaryStage.setResizable(false);
    primaryStage.setScene(scene);
    primaryStage.setTitle(windowTitle);
    primaryStage
      .onCloseRequestProperty()
      .setValue(e -> {
        if (!isConfirmation && callback != null) {
          callback.callback();
        }
      });

    if (primaryStage.isShowing()) {
      primaryStage.requestFocus();
    } else {
      primaryStage.show();
    }
  }

  public void refocus(
    String windowTitle,
    String title,
    String desc,
    NotificationCallback callback,
    boolean isConfirmation
  ) {
    this.desc = desc;
    this.title = title;
    this.callback = callback;
    this.windowTitle = windowTitle;
    this.isConfirmation = isConfirmation;

    start(primaryStage);
  }

  public void refocus(
    String windowTitle,
    String title,
    String desc,
    NotificationCallback callback
  ) {
    this.desc = desc;
    this.title = title;
    this.callback = callback;
    this.windowTitle = windowTitle;
    this.isConfirmation = false;

    start(primaryStage);
  }

  public void refocus(String windowTitle, String title, String desc) {
    this.desc = desc;
    this.title = title;
    this.callback = null;
    this.windowTitle = windowTitle;
    this.isConfirmation = false;

    start(primaryStage);
  }

  public Stage getStage() {
    return primaryStage;
  }
}
