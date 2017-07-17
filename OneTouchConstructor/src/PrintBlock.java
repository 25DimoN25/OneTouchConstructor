import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class PrintBlock extends Block {

	public PrintBlock(double width, double height, BlockType type) {
		super(width, height, type);
	}

	@Override
	public void drawBlock(GraphicsContext g) {
		g.setFill(Color.WHITE);
		g.fillPolygon(
				new double[]{0, h/2, h/2},
				new double[]{h/2, 0, h}, 3); 
		g.fillRect(h/2, 0, w-h, h);
		g.fillArc(w-h, 0, h, h, 270, 180, ArcType.OPEN);
		
		g.setStroke(Color.BLACK);
		g.strokePolyline(
				new double[]{h/2, 0, h/2},
				new double[]{h, h/2, 0}, 3);

		g.strokeLine(h/2, 0, w-h/2, 0);
		g.strokeLine(h/2, h, w-h/2, h);
		g.strokeArc(w-h, 0, h, h, 270, 180, ArcType.OPEN);
	}

	@Override
	public void registerMagnetePoints(ArrayList<MagnetePoint> points) {
		points.add(new MagnetePoint(w/2, 0));
	}

}
