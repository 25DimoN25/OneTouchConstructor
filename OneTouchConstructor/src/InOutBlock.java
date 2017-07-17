import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class InOutBlock extends Block {

	public InOutBlock(double width, double height, BlockType type) {
		super(width, height, type);
	}

	@Override
	public void drawBlock(GraphicsContext g) {
		g.setFill(Color.WHITE);
		g.fillPolygon(
				new double[]{w/4, w, 3*w/4, 0},
				new double[]{0, 0, h, h}, 4
				);
		
		g.setStroke(Color.BLACK);
		g.strokePolygon(
				new double[]{w/4, w, 3*w/4, 0},
				new double[]{0, 0, h, h}, 4
				);
	}

	@Override
	public void registerMagnetePoints(ArrayList<MagnetePoint> points) {
		points.add(new MagnetePoint(w/2, 0));
		points.add(new MagnetePoint(w/2, h));
	}

}
