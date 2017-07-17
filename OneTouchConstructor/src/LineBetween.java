import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class LineBetween extends Line {
	public static final Set<LineBetween> selectedLines = new HashSet<>();
	public static final int MIN_MOVEABLE_STEP = 1; 
	public static boolean isMouseOnLine = false;
	
	public static int moveableStep = MIN_MOVEABLE_STEP;
	public static boolean isCreated;
	public static boolean moving = false;
	public static LineBetween currentLine;
	
	protected boolean isSelected;
	MagnetePoint onStrartMP;
	MagnetePoint onEndMP;
	
	int onSeneX = 0;
	int onSceneY = 0;
	int[] translateX;
	int[] translateY;
	
	public LineBetween(double xFrom, double yFrom) {
		super(xFrom, yFrom, xFrom, yFrom);	
	}
	
	
	private void initMoveableEvent() {
		
		setOnMousePressed(e -> {
			if (e.getButton() == MouseButton.PRIMARY && !LineBetween.isCreated) {
				onSeneX    = (int) e.getSceneX();
				onSceneY   = (int) e.getSceneY();
				
				if (isSelected)
					if (selectedLines.size() > 1) {	
						if (!Block.selectedBlocks.isEmpty()) {
							translateX = new int[selectedLines.size()+Block.selectedBlocks.size()];
							translateY = new int[selectedLines.size()+Block.selectedBlocks.size()];
							int i = 0;
							for (LineBetween line : selectedLines) {
								translateX[i] = (int) line.getTranslateX();
								translateY[i] = (int) line.getTranslateY();
								i++;
							}
							for (Block block : Block.selectedBlocks) {
								translateX[i] = (int) block.getTranslateX();
								translateY[i] = (int) block.getTranslateY();
								i++;
							}
						} else {
							translateX = new int[selectedLines.size()];
							translateY = new int[selectedLines.size()];
							int i = 0;
							for (LineBetween line : selectedLines) {
								translateX[i] = (int) line.getTranslateX();
								translateY[i] = (int) line.getTranslateY();
								i++;
							}
						}
					} else {
						if (!Block.selectedBlocks.isEmpty()) {
							translateX = new int[Block.selectedBlocks.size()+1];
							translateY = new int[Block.selectedBlocks.size()+1];
	
							translateX[0] = (int) getTranslateX();
							translateY[0] = (int) getTranslateY();
							
							int i = 1;
							for (Block block : Block.selectedBlocks) {
								translateX[i] = (int) block.getTranslateX();
								translateY[i] = (int) block.getTranslateY();
								i++;
							}
						} else {
							translateX = new int[1];
							translateY = new int[1];
							translateX[0] = (int) getTranslateX();
							translateY[0] = (int) getTranslateY();
						}
					}
				else {
					clearSelected();
					Block.clearSelected();
					setSelected(true);
	
					translateX = new int[1];
					translateY = new int[1];
					translateX[0] = (int) getTranslateX();
					translateY[0] = (int) getTranslateY();
				}
			
			} else if (e.getButton() == MouseButton.MIDDLE) {
				if (!isCreated) {
					splitLine(this, Funcs.toNearInt((int) (e.getX()+getTranslateX()), moveableStep),
							Funcs.toNearInt((int) (e.getY()+getTranslateY()), moveableStep), 3);
				}
			}
		});
		
		setOnMouseDragged(e -> {
			if (e.getButton() == MouseButton.PRIMARY && !LineBetween.isCreated) {
				moving = true;
				if (selectedLines.size() > 1) {
					if (!Block.selectedBlocks.isEmpty()) {
						int i = 0;
						for (LineBetween line : selectedLines) {
							line.setTranslateX(Funcs.toNearInt((int) (translateX[i] + e.getSceneX() - onSeneX), moveableStep));
							line.setTranslateY(Funcs.toNearInt((int) (translateY[i] + e.getSceneY() - onSceneY), moveableStep));
							i++;
						}
						for (Block block : Block.selectedBlocks) {
							block.setTranslateX(Funcs.toNearInt((int) (translateX[i] + e.getSceneX() - onSeneX), Block.moveableStep));
							block.setTranslateY(Funcs.toNearInt((int) (translateY[i] + e.getSceneY() - onSceneY), Block.moveableStep));
							i++;
						}
					} else {
						int i = 0;
						for (LineBetween line : selectedLines) {
							line.setTranslateX(Funcs.toNearInt((int) (translateX[i] + e.getSceneX() - onSeneX), moveableStep));
							line.setTranslateY(Funcs.toNearInt((int) (translateY[i] + e.getSceneY() - onSceneY), moveableStep));
							i++;
						}
					}
				} else {
					if (!Block.selectedBlocks.isEmpty()) {
			            setTranslateX(Funcs.toNearInt((int) (translateX[0] + e.getSceneX() - onSeneX), moveableStep));
			            setTranslateY(Funcs.toNearInt((int) (translateY[0] + e.getSceneY() - onSceneY), moveableStep));
			            
			            int i = 1;
						for (Block block : Block.selectedBlocks) {
							block.setTranslateX(Funcs.toNearInt((int) (translateX[i] + e.getSceneX() - onSeneX), Block.moveableStep));
							block.setTranslateY(Funcs.toNearInt((int) (translateY[i] + e.getSceneY() - onSceneY), Block.moveableStep));
							i++;
						}
					} else {
			            setTranslateX(Funcs.toNearInt((int) (translateX[0] + e.getSceneX() - onSeneX), moveableStep));
			            setTranslateY(Funcs.toNearInt((int) (translateY[0] + e.getSceneY() - onSceneY), moveableStep));
					}
				}
			}
		});
				
		setOnMouseReleased(e -> {
			if (moving)
				moving = false;
			else {
				clearSelected();
				Block.clearSelected();
				setSelected(true);
			}
		});
		
	}
	
	private void splitLine(LineBetween lineBetween, double xSplit, double ySplit, int accr) {
		if (Funcs.toNearInt((int) getStartX(), accr) == Funcs.toNearInt((int) getEndX(), accr)) {
			int x = (int) (getStartX()+getTranslateX());
			
			makeStartOfLine(x, getStartY()+getTranslateY())
				.setTo(x, ySplit)
				.done();
			
			makeStartOfLine(x, ySplit)
				.setTo(x, getEndY()+getTranslateY())
				.done();
			
		} else if (Funcs.toNearInt((int) getStartY(), accr) == Funcs.toNearInt((int) getEndY(), accr)) {
			int y = (int) (getStartY()+getTranslateY());
			
			makeStartOfLine(getStartX()+getTranslateX(), y)
				.setTo(xSplit, y)
				.done();
			
			makeStartOfLine(xSplit, y)
				.setTo(getEndX()+getTranslateX(), y)
				.done();

		} else {
			
			makeStartOfLine(getStartX()+getTranslateX(), getStartY()+getTranslateY())
				.setTo(xSplit, ySplit)
				.done();
			
			makeStartOfLine(xSplit, ySplit)
				.setTo(getEndX()+getTranslateX(), getEndY()+getTranslateY())
				.done();

		}
		removeLine(this);
		
	}


	private void initHightlight() {
		setOnMouseEntered(e -> {
			isMouseOnLine = true;
			if (!isSelected) {
				setEffect(new DropShadow(5, Color.BLUEVIOLET));
			}
			onStrartMP.setMinOpacity(0.2);	
			onEndMP.setMinOpacity(0.2);	
		});
		
		setOnMouseExited(e -> {
			isMouseOnLine = false;
			if (isSelected) {
				setEffect(new DropShadow(18, Color.BLUE));
			} else {
				setEffect(null);
			}
			onStrartMP.setMinOpacity(0);
			onEndMP.setMinOpacity(0);
		});
		

	}
	
	public static LineBetween makeStartOfLine(double xFrom, double yFrom) {
		isCreated = true;
		currentLine = new LineBetween(xFrom, yFrom);
		currentLine.setMouseTransparent(true);
		Main.workSpace.getElements().add(currentLine);
		return currentLine;
	}
	
	LineBetween setTo(double xTo, double yTo) {
		setEndX(xTo);
		setEndY(yTo);
		return this;
	}
	
	public void done() {
		isCreated = false;
		currentLine = null;
		
		double distance = new Point2D(getStartX(), getStartY()).distance(getEndX(), getEndY());	
		if(distance < 10) {
			Main.workSpace.getElements().remove(this);
			return;
		}
		
		setMouseTransparent(false);
		setCursor(Cursor.HAND);
		
		initMoveableEvent();
		initHightlight();
		
		onStrartMP = new MagnetePoint(getStartX(), getStartY());
		onEndMP = new MagnetePoint(getEndX(), getEndY());
		Main.workSpace.getElements().addAll(onStrartMP, onEndMP);
		
		translateXProperty().addListener((obs, oVal, nVal) -> {
			onStrartMP.setTranslateX(nVal.doubleValue()+getStartX());
			onEndMP.setTranslateX(nVal.doubleValue()+getEndX());
		});
		translateYProperty().addListener((obs, oVal, nVal) -> {
			onStrartMP.setTranslateY(nVal.doubleValue()+getStartY());
			onEndMP.setTranslateY(nVal.doubleValue()+getEndY());
		});
			
	};

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
		
		if (isSelected) {
			setStrokeWidth(2);
			setEffect(new DropShadow(18, Color.BLUE));
			selectedLines.add(this);
		} else {
			setStrokeWidth(1);
			setEffect(null);
			selectedLines.remove(this);
		}
	}
	
	public static void removeLine(LineBetween line) {
		if (line.isSelected)
			line.setSelected(false);
		Main.workSpace.getElements().remove(line.onStrartMP);
		Main.workSpace.getElements().remove(line.onEndMP);
		Main.workSpace.getElements().remove(line);
	}
	
	public static void clearSelected() {
		List<LineBetween> lines = new ArrayList<>(selectedLines);
		for (LineBetween line : lines) {
			line.setSelected(false);
		}
		lines.clear();
		lines = null;
	}
}
