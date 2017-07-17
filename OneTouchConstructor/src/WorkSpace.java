import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineJoin;

public class WorkSpace extends StackPane {

	public static boolean isPickDisabled;
	
	private double x, y;
	private int bgStepSize = 30;
	
	private Canvas bg;
	private Pane workSpacePane;
	
	private double w;
	private double h;
	
	private GraphicsContext g;
	
	public WorkSpace(double w, double h) {
		this.w = w;
		this.h = h;
		
		setPrefSize(w, h);
		
		workSpacePane = new Pane();
		workSpacePane.setPrefSize(w, h);
		workSpacePane.setStyle("-fx-background-color: transparent");
		setAlignment(workSpacePane, Pos.TOP_LEFT);
		
		bg = new Canvas(w, h);
		g = bg.getGraphicsContext2D();
		setAlignment(bg, Pos.TOP_LEFT);	
		redrawBG(Main.DEF_BG_STEPSIZE);
		
		initSelectingEventAndLineMoving();

		super.getChildren().addAll(bg, workSpacePane);
	}
	
	public void initSelectingEventAndLineMoving() {
		final Rectangle pick = new Rectangle();
		pick.setFill(Color.rgb(180, 180, 180, 0.3));
		pick.setStroke(Color.BLUE);
		pick.setStrokeLineJoin(StrokeLineJoin.ROUND);

		setOnMousePressed(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				x = e.getX();
				y = e.getY();
					
				if (Block.isMouseOnBlock || MagnetePoint.isMouseOnPoint || LineBetween.isMouseOnLine) {
					isPickDisabled = true;
					return;
				}
				
				isPickDisabled = false;
				
				getElements().add(pick);	
	
		    	pick.setWidth(0);
				pick.setHeight(0);
				pick.setX(x);
				pick.setY(y);
					

			}
		});
		
		setOnMouseDragged(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				
				if (LineBetween.isCreated) {
					if (Math.abs(e.getX()-LineBetween.currentLine.getStartX()) < Math.abs(e.getY()-LineBetween.currentLine.getStartY()))
						LineBetween.currentLine.setTo(
								LineBetween.currentLine.getStartX(), 
								Funcs.toNearInt((int) e.getY(), LineBetween.moveableStep));
					else
						LineBetween.currentLine.setTo(
								Funcs.toNearInt((int) e.getX(), LineBetween.moveableStep),
								LineBetween.currentLine.getStartY());
				} else {
				
					if (!isPickDisabled) {
						if (e.getX() - x >= 0 && e.getY() - y >= 0) {
							pick.setX(x);
							pick.setY(y);
							pick.setWidth(e.getX() - x);
							pick.setHeight(e.getY() - y);
						} else if (e.getX() - x < 0 && e.getY() - y >= 0) {
							pick.setX(e.getX());
							pick.setY(y);
							pick.setWidth(x - e.getX());
							pick.setHeight(e.getY() - y);
						} else if (e.getX() - x >= 0 && e.getY() - y < 0) {
							pick.setX(x);
							pick.setY(e.getY());
							pick.setWidth(e.getX() - x);
							pick.setHeight(y - e.getY());
						} else if (e.getX() - x < 0 && e.getY() - y < 0) {
							pick.setX(e.getX());
							pick.setY(e.getY());
							pick.setWidth(x - e.getX());
							pick.setHeight(y - e.getY());
						}
			
						selectInPick(pick);
					}
				}
			}
		});
		
		setOnMouseReleased(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				
				if (LineBetween.isCreated) {
					LineBetween.currentLine.done();
				} else {		
					if (!isPickDisabled) {
						selectInPick(pick);
						if (Block.selectedBlocks.isEmpty())
							Main.configTextField(true, null, false);
						else
							Main.textField.setDisable(false);
						
						getElements().remove(pick);
					}
				}
				
			}
		});
	}
	
	private void selectInPick(Rectangle pick) {
		for (Node block : getElements()) {
			if (block instanceof Block) {
				if (pick.getBoundsInParent().intersects(((Block) block).getBoundsInParent())) {
					((Block) block).setSelected(true);
				} else {
					((Block) block).setSelected(false);
				}
			} else if (block instanceof LineBetween) {
				if (pick.getBoundsInParent().intersects(((LineBetween) block).getBoundsInParent())) {
					((LineBetween) block).setSelected(true);
				} else {
					((LineBetween) block).setSelected(false);
				}
			}
		}
	}
	
	public void setBgStepSize(int bgStepSize) {
		this.bgStepSize = bgStepSize;
		redrawBG(bgStepSize);
	}

	public int getBgStepSize() {return bgStepSize;}
	
	public ObservableList<Node> getElements() {return workSpacePane.getChildren();}
	
	public void setBGVisible(boolean enabled) {
		if (enabled) {
			redrawBG(bgStepSize);
		} else {
			clearBG();
		}
	}
	
	public void redrawBG(int size) {
		clearBG();
		
		g.setStroke(Color.LIGHTGRAY);
		
		for (int tempX = 0; tempX <= w; tempX += size)
			g.strokeLine(tempX, 0, tempX, h);
		for (int tempY = 0; tempY <= h; tempY += size)
			g.strokeLine(0, tempY, w, tempY);

	}
	
	public void clearBG() {
		g.setFill(Color.WHITE);
		g.fillRect(0, 0, w, h);
	}

	public Pane getPane() {return workSpacePane;}
	
	public Canvas getBG() {return bg;}
}
