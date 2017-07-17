import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CycleEndBlock extends Block {

	public CycleEndBlock(double width, double height, BlockType type) {
		super(width, height, type);
	}

	@Override
	public void drawBlock(GraphicsContext g) {
		g.setFill(Color.WHITE);
		g.fillPolygon(
				new double[]{w, w-w/6, w/6, 0},
				new double[]{h/2-h/4, h-h/4, h-h/4, h/2-h/4}, 4); 
		
		g.setStroke(Color.BLACK);	
		g.strokePolygon(
				new double[]{w, w-w/6, w/6, 0},
				new double[]{h/2-h/4, h-h/4, h-h/4, h/2-h/4}, 4);  
	}

	@Override
	public void registerMagnetePoints(ArrayList<MagnetePoint> points) {
		points.add(new MagnetePoint(w/2, 0));
		points.add(new MagnetePoint(w/2, h));
	}

}
