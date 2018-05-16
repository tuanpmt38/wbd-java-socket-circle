import java.io.*;
import java.net.*;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class Server extends Application {
    //override start method in the Application class
    public void start(Stage primaryStage) {
        //textarea for displaying contents
        TextArea ta = new TextArea();

        //create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(() -> {
            try {
                //create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() ->
                        ta.appendText("Server started at " + new Date() + '\n')
                );

                //listen for a connection request
                Socket socket = serverSocket.accept();

                //create data input and output streams
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

                while (true) {

                    //receive radius from client
                    double radius = inputFromClient.readDouble();
                    //compute area
                    double area = radius * radius * Math.PI;
                    //send area back to the client
                    outputToClient.writeDouble(area);

                    Platform.runLater(() -> {
                                ta.appendText("Radius received from client: " + radius + '\n');
                                ta.appendText("Area is " + area + '\n');
                            }

                    );
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        ).start();
    }
}
