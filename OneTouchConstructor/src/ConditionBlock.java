import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ConditionBlock extends Block {

	public ConditionBlock(double width, double height, BlockType type) {
		super(width, height, type);
	}

	@Override
	public void drawBlock(GraphicsContext g)  {
		g.setFill(Color.WHITE);
		g.fillPolygon(
				new double[]{0, w/2, w, w/2},
				new double[]{h/2, 0, h/2, h}, 
				4);
		
		g.setStroke(Color.BLACK);
		g.strokePolygon(
				new double[]{0, w/2, w, w/2},
				new double[]{h/2, 0, h/2, h}, 
				4);
	}

	@Override
	public void registerMagnetePoints(ArrayList<MagnetePoint> points) {
		points.add(new MagnetePoint(0, h/2));
		points.add(new MagnetePoint(w, h/2));
		points.add(new MagnetePoint(w/2, 0));
		points.add(new MagnetePoint(w/2, h));
	}

}
