package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuExceptionController implements Initializable {
    @FXML
    Label exceptionLabel;

    public static String error;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exceptionLabel.setText(error);
    }

    public void continueButton(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}
