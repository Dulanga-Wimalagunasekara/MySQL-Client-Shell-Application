package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;

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
                    "-p"+txtPassword.getText(), "-e", "exit").start();

//            Process exec = Runtime.getRuntime().exec(command);

            int i = exec.waitFor();

            if (i==0){
                System.out.println("Done!");
            }else{
                throw new RuntimeException("Something went wrong!");
            }
        } catch (Throwable e) {
            new Alert(Alert.AlertType.ERROR,"Something went wrong!",ButtonType.OK).show();
            e.printStackTrace();
        }

    }

    public void btnExitOnAction(ActionEvent actionEvent) {

    }
}
