import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FunctionalBlock extends Block {

	public FunctionalBlock(double width, double height, BlockType type) {
		super(width, height, type);
	}

	@Override
	public void drawBlock(GraphicsContext g) {
		g.setFill(Color.WHITE);
		g.fillRect(0, 0, w, h);
		
		g.setStroke(Color.BLACK);
		g.strokeRect(0, 0, w, h);
		
		g.strokeLine(w/8, 0, w/8, h);
		g.strokeLine(7*w/8, 0, 7*w/8, h);
	}
	
	@Override
	public void registerMagnetePoints(ArrayList<MagnetePoint> points) {
		points.add(new MagnetePoint(w/2, 0));
		points.add(new MagnetePoint(w/2, h));
		points.add(new MagnetePoint(0, h/2));
		points.add(new MagnetePoint(w, h/2));
	}
}
