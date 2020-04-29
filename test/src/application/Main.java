package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class Main extends Application {
	int waittime = 0; // set the time between loop, in milliseconds.
    MediaView mediaView = new MediaView();
    MediaPlayer mp;
    
    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Drag a file and DROP IT!.");
        Label dropped = new Label("File path:");
        VBox dragTarget = new VBox();
        dragTarget.getChildren().addAll(label,dropped);
        dragTarget.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != dragTarget
                        && event.getDragboard().hasFiles()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        dragTarget.setOnDragDropped(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                	
                    dropped.setText("File path: "+db.getFiles().toString());
                    mp = new MediaPlayer(new Media(db.getUrl()));
					mediaView = new MediaView(mp);
					success = true;
					while(true){
					mp.play();
					try {
						mp.wait(waittime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
                }
                /* let the source know whether the string was successfully 
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            }
        });


        StackPane root = new StackPane();
        root.getChildren().add(dragTarget);
        root.getChildren().add(mediaView);

        Scene scene = new Scene(root, 500, 500);

        primaryStage.setTitle("Drag Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}