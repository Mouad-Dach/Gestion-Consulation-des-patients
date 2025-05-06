package net.dach.gestiondespatients;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.io.File;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/net/dach/gestiondespatients/views/main-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 800, 600);


        root.setStyle("-fx-font-family: 'Segoe UI', Arial, sans-serif; -fx-background-color: #f5f5f5;");


        try {

            URL cssResource = getClass().getResource("/net/dach/gestiondespatients/styles/application.css");

            if (cssResource != null) {
                scene.getStylesheets().add(cssResource.toExternalForm());
                System.out.println("CSS loaded from resources: " + cssResource);
            } else {

                File cssFile = new File("src/main/resources/net/dach/gestiondespatients/styles/application.css");
                if (cssFile.exists()) {
                    scene.getStylesheets().add(cssFile.toURI().toURL().toExternalForm());
                    System.out.println("CSS loaded from file system: " + cssFile.getAbsolutePath());
                } else {
                    System.err.println("CSS file not found in resources or file system");
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading CSS: " + e.getMessage());

        }

        primaryStage.setTitle("Gestion Cabinet Médical");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
