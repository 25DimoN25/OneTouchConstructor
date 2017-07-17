

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class BeginEndBlock extends Block{

	public BeginEndBlock(double width, double height, BlockType type) {
		super(width, height/4, type);
	}

	@Override
	public void drawBlock(GraphicsContext g) {
		g.setFill(Color.WHITE);		
		g.fillOval(0, 0, h, h);
		g.fillOval(w-h, 0, h, h);
		g.fillRect(h/2, 0, w-h, h);
		
		g.setStroke(Color.BLACK);
		g.strokeLine(h/2, 0, w-h/2, 0);
		g.strokeLine(h/2, h, w-h/2, h);
		g.strokeArc(0, 0, h, h, 90, 180, ArcType.OPEN);
		g.strokeArc(w-h, 0, h, h, -90, 180, ArcType.OPEN);

		
	}

	@Override
	public void registerMagnetePoints(ArrayList<MagnetePoint> points) {
		points.add(new MagnetePoint(w/2, 0));
		points.add(new MagnetePoint(w/2, h));

	}

	@Override
	public Block setNewWidth(double width) {
		setNewSize(width, width/4);
		return this;
	}
}
