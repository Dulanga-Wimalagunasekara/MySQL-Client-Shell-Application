package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class LoginFormController {

    public void initialize(){
        txtHost.setText("localhost");
        txtPort.setText("3306");
        txtUsername.requestFocus();
    }

    public TextField txtHost;
    public TextField txtPort;
    public TextField txtUsername;
    public TextField txtPassword;

    public void btnConnectOnAction(ActionEvent actionEvent) {
        if (txtHost.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Host can not be empty", ButtonType.OK).show();
            txtHost.requestFocus();
            return;
        }else if (txtPort.getText().isEmpty() || !txtPort.getText().matches("\\d+")){
            new Alert(Alert.AlertType.ERROR,"Invalid Port", ButtonType.OK).show();
            txtPort.requestFocus();
            return;
        }else if (txtUsername.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Username can not be empty", ButtonType.OK).show();
            txtUsername.requestFocus();
            return;
        }else if (txtPassword.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Password can not be empty", ButtonType.OK).show();
            txtPassword.requestFocus();
            return;
        }

        try {
//            String command = String.format("mysql -h %s --port %s -u %s -p%s -e exit",
//                    txtHost.getText(),
//                    txtPort.getText(),
//                    txtUsername.getText(),
//                    txtPassword.getText());

            Process exec = new ProcessBuilder("mysql", "-h", txtHost.getText(), "--port", txtPort.getText(), "-u", txtUsername.getText(),
                    "-p", "-e", "exit").start();

//            Process exec = Runtime.getRuntime().exec(command);

            exec.getOutputStream().write(txtPassword.getText().getBytes());
            exec.getOutputStream().close();

            int i = exec.waitFor();

            if (i==0){
                FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/CommandForm.fxml"));
                AnchorPane load = fxmlLoader.load();
                CommandFormController controller = fxmlLoader.getController();
                controller.initData(txtHost.getText(),txtPort.getText(),txtUsername.getText(),txtPassword.getText());
                Stage stage = new Stage();
                stage.setScene(new Scene(load));
                stage.sizeToScene();
                stage.centerOnScreen();
                stage.setResizable(false);
                stage.show();
            }else{
                String error = readStream(exec.getErrorStream());
                Alert alert = new Alert(Alert.AlertType.ERROR, error, ButtonType.OK);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.show();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

        private String readStream(InputStream errorStream) throws IOException {
            byte[] buffer = new byte[errorStream.available()];
            errorStream.read(buffer);
            errorStream.close();
            return new String(buffer);
        }


    public void btnExitOnAction(ActionEvent actionEvent) {

    }
}
