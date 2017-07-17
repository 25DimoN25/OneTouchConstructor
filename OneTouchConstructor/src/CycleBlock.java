import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CycleBlock extends Block {

	public CycleBlock(double width, double height, BlockType type) {
		super(width, height, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawBlock(GraphicsContext g) {
		g.setFill(Color.WHITE);
		g.fillPolygon(
				new double[]{w/6, w-w/6, w, w-w/6, w/6, 0},
				new double[]{0, 0, h/2, h, h, h/2}, 6);
		g.setStroke(Color.BLACK);	
		g.strokePolygon(
				new double[]{w/6, w-w/6, w, w-w/6, w/6, 0},
				new double[]{0, 0, h/2, h, h, h/2}, 6); 
	}

	@Override
	public void registerMagnetePoints(ArrayList<MagnetePoint> points) {
		points.add(new MagnetePoint(0, h/2));
		points.add(new MagnetePoint(w, h/2));
		points.add(new MagnetePoint(w/2, 0));
		points.add(new MagnetePoint(w/2, h));
	}

}
