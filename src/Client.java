import java.io.*;
import java.net.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Client extends Application { //IO streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    //override the start method in the Application Class
    public void start(Stage primaryStage) {
        //panel to hold the label and text field
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5, 5, 5, 5));
        paneForTextField.setStyle("-fx-border-color: green");
        paneForTextField.setLeft(new Label("Enter a radius: "));

        TextField tf = new TextField();
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField.setCenter(tf);

        BorderPane mainPain = new BorderPane();

        //textarea to display contents
        TextArea ta = new TextArea();
        mainPain.setCenter(new ScrollPane(ta));
        mainPain.setTop(paneForTextField);

        //create a scene and place it in the stage
        Scene scene = new Scene(mainPain, 450, 200);
        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.show();

        try {
            //create the socket to connect to the server
            Socket socket = new Socket("localhost", 8000);
            //create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());
            //create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());

        } catch (IOException ex) {

            ta.appendText(ex.toString() + '\n');
        }

        tf.setOnAction(e -> {
                    try {
                        //get the radius from the text field
                        double radius = Double.parseDouble(tf.getText().trim());

                        //send radius to the server
                        toServer.writeDouble(radius);
                        toServer.flush();

                        //get area from the server
                        double area = fromServer.readDouble();

                        //display to the text area
                        ta.appendText("Radius is " + radius + '\n');
                        ta.appendText("Area received from the server is " + area + '\n');

                    } catch (IOException ex) {
                        System.err.println(ex);
                    }
                }

        );
    }
}
