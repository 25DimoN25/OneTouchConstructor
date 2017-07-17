import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MagnetePoint extends Circle {
	public static boolean isMouseOnPoint = false;
	
	Point2D center;
	
	double minOpacity = 0;
	double maxOpacity = 0.8;

	boolean isStarted = false;
	
	public MagnetePoint(double x, double y) {
		super(5);
		setPos(x, y);
		
		setOpacity(0);
		setFill(Color.GREEN);
		
		center = new Point2D(x, y);
		
		initHightLight();
		initActions();
	}
	
	void initHightLight() {
		setOnMouseEntered(e -> {
			isMouseOnPoint = true;
			setScaleX(1.4);	
			setScaleY(1.4);
			setOpacity(maxOpacity);
		});
		
		setOnMouseExited(e -> {
			isMouseOnPoint = false;
			setScaleX(1);	
			setScaleY(1);
			setOpacity(minOpacity);
		});
	}	
	
	void initActions() {
		
		setOnMousePressed(e -> {
				Point2D coords = Main.workSpace.sceneToLocal(e.getSceneX(), e.getSceneY());
				
				if (e.getButton() == MouseButton.PRIMARY && !LineBetween.isCreated)
					LineBetween.makeStartOfLine(
							coords.getX() - e.getX()*getScaleX(), coords.getY() - e.getY()*getScaleY() );
		});
	}
	
	public void setMinOpacity(double minOpacity) {
		this.minOpacity = minOpacity;
		setOpacity(minOpacity);
	}
	
	public void setPos(double x, double y) {
		setTranslateX(x);
		setTranslateY(y);
	}
}
