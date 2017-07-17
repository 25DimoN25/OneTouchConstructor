import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CycleBeginBlock extends Block {

	public CycleBeginBlock(double width, double height, BlockType type) {
		super(width, height, type);
	}

	@Override
	public void drawBlock(GraphicsContext g) {
		g.setFill(Color.WHITE);
		g.fillPolygon(
				new double[]{w/6, w-w/6, w, 0},
				new double[]{h/4, h/4, h/2+h/4, h/2+h/4}, 4); 
		
		g.setStroke(Color.BLACK);	
		g.strokePolygon(
				new double[]{w/6, w-w/6, w, 0},
				new double[]{h/4, h/4, h/2+h/4, h/2+h/4}, 4); 
	}

	@Override
	public void registerMagnetePoints(ArrayList<MagnetePoint> points) {
		points.add(new MagnetePoint(w/2, 0));
		points.add(new MagnetePoint(w/2, h));
	}

}
