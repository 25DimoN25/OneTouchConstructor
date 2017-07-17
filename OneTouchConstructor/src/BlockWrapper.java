import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class BlockWrapper extends Stage {
	private final double scaleFactor = 1.2;
	private Scene scene;
	
	public BlockWrapper(Block node, Window owner) {
		initStyle(StageStyle.TRANSPARENT);	
		initOwner(owner);
		
		node.scale(scaleFactor);
		node.setFocusTraversable(false);
				
		Pane pane = new BorderPane(node);
		pane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.0)");
		
		scene = new Scene(pane, node.getPrefWidth()*scaleFactor, node.getPrefHeight()*scaleFactor);
		scene.setFill(Color.TRANSPARENT);
		
		setAlwaysOnTop(true);
		setOpacity(0.8);
		setScene(scene);
		sizeToScene();
	}
	
	public void move(double x, double y) {
		setX(x-scene.getWidth()/2);
		setY(y-scene.getHeight()/2);
	}
}
