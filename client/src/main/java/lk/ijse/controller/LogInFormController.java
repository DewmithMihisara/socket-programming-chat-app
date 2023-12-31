package lk.ijse.controller;

import animatefx.animation.Shake;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class LogInFormController {
@FXML
private Line line;
    @FXML
    private Button logInBtn;
    @FXML
    private TextField usrNameTxt;
    public static String usrName;
    static Socket socket;
    DataOutputStream dataOutputStream;
    Shake shake;
    Stage stage;
    double x,y =0;
    @FXML
    void logInBtnOnAction() throws IOException {
        usrName=usrNameTxt.getText();
        if(usrName.equals("")){
            shakeLine();
        }else {
            defaultLine();

            socket = new Socket("localhost", 3991);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("/usrLog//!-> "+usrName);
            dataOutputStream.flush();
            Parent anchorPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/clientForm.fxml")));
            anchorPane.setOnMousePressed(event -> { x = event.getSceneX();y = event.getSceneY(); });
            anchorPane.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });

            stage = new Stage();
            stage.setScene(new Scene(anchorPane));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle(usrName);
            stage.setResizable(false);
            stage.show();

            usrNameTxt.setText("");
        }
    }

    @FXML
    void typingValidationOnKeyTyped(KeyEvent event) {
        spaceBlockOnKeyTyped(event);
        handleKeyTyped();
    }
    @FXML
    void mouseClickOnAction(MouseEvent event) {
        defaultLine();
        if (shake !=null){
            shake.stop();
        }
    }
    @FXML
    void usrNameTxtOnAction(ActionEvent event) {
        logInBtn.fire();
    }
    public static void clsStg() throws IOException {
        if (socket!=null){
            socket.close();
        }
    }
    void shakeLine(){
        line.setStroke(Color.RED);
        shake=new Shake(line);
        shake.setOnFinished(actionEvent -> {
            defaultLine();
        });
        shake.play();
    }
    void defaultLine(){
        line.setStroke(Color.BLACK);
    }
    void spaceBlockOnKeyTyped(KeyEvent event) {
        if (" ".equals(event.getCharacter())) {
            String trimmedText = usrNameTxt.getText().trim();
            usrNameTxt.setText(trimmedText);
            usrNameTxt.positionCaret(trimmedText.length());
        }
    }
    private void handleKeyTyped() {
        String inputText = usrNameTxt.getText();
        if (inputText.length() > 15) {
            String limitedText = inputText.substring(0, 15);
            usrNameTxt.setText(limitedText);
            usrNameTxt.positionCaret(limitedText.length());
        }
    }
}
