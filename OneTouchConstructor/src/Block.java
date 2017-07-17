import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

enum BlockType {
	MENU, WORKSPACE, FREE;
}

public abstract class Block extends StackPane {
	public static final Set<Block> selectedBlocks = new HashSet<>();
	public static final int MIN_MOVEABLE_STEP = 1; 
	
	public static int moveableStep = MIN_MOVEABLE_STEP;
	public static boolean isMouseOnBlock = false;
	public static boolean moving = false;
	
	
	protected double w;
	protected double h;
	protected boolean isSelected;

	private ArrayList<MagnetePoint> mPoints = new ArrayList<>();
	
	int onSeneX = 0;
	int onSceneY = 0;
	int[] translateX;
	int[] translateY;
	 
	 Canvas block;
	 Text textOnBlock;
	 Pane magnetePane;
	
	public Block(double width, double height, BlockType type) {
		this.w = width;
		this.h = height;
		
		block = new Canvas(w, h);
		textOnBlock = new Text("");
		textOnBlock.setWrappingWidth(width);
		textOnBlock.setTextAlignment(TextAlignment.CENTER);
		textOnBlock.setFont(new Font(Main.fontName, Main.fontSize));
		magnetePane = new Pane();
		
		setNewWidth(w);
		
		setCursor(Cursor.HAND);
		
		StackPane.setAlignment(block, Pos.CENTER);
		StackPane.setAlignment(textOnBlock, Pos.CENTER);
		StackPane.setAlignment(magnetePane, Pos.CENTER);
		
		drawBlock(block.getGraphicsContext2D());

		setEffect(new DropShadow(5, Color.BLACK));		

		if (type == BlockType.MENU)
			initClickableEvent();
		else if (type == BlockType.WORKSPACE) {
			initMoveableEvent();
			initHightlight();
			registerMagnetePoints(mPoints);
			magnetePane.getChildren().addAll(mPoints);
		}
		
		getChildren().addAll(block, textOnBlock, magnetePane);
	}
	
	public abstract void drawBlock(GraphicsContext g);
	
	public abstract void registerMagnetePoints(ArrayList<MagnetePoint> points);
	
	public void redrawBlock() {
		GraphicsContext g = block.getGraphicsContext2D();
		g.clearRect(0, 0, w, h);
		drawBlock(g);
	}
	
	//pressed, released, dragged
	private void initClickableEvent() {
		ScaleTransition scale = new ScaleTransition(Duration.millis(100), this);
		
		BlockWrapper blockWrapper = new BlockWrapper(
				newInstance(this, getPrefWidth(), getPrefHeight(), BlockType.FREE),
				Main.primaryStage);
		
		setOnMousePressed(e -> {
			scale.stop();
			scale.setFromX(1);
			scale.setFromY(1);
			scale.setByX(0.2);
			scale.setByY(0.2);
			scale.play();
			blockWrapper.move(e.getScreenX(), e.getScreenY());
			blockWrapper.show();
		});
		
		setOnMouseReleased(e -> {
			scale.stop();
			scale.setFromX(1.2);
			scale.setFromY(1.2);
			scale.setByX(-0.2);
			scale.setByY(-0.2);
			scale.play();
			blockWrapper.close();
			
			Point2D point = Main.workSpace.sceneToLocal(e.getSceneX(), e.getSceneY());
			
			if (point.getX() > 0 && point.getY() > 0 && point.getX() < Main.DEF_WIDTH*1.5 && point.getY() < Main.DEF_HEIGHT*1.5) {
				Block newBlock = Block.newInstance(this, Main.DEF_BLOCK_WIDTH, Main.DEF_BLOCK_HEIGHT, BlockType.WORKSPACE);
				newBlock.setLocation(
						Funcs.toNearInt((int) (point.getX() - Main.DEF_BLOCK_WIDTH /2), moveableStep),
						Funcs.toNearInt((int) (point.getY() - Main.DEF_BLOCK_HEIGHT/2), moveableStep));
				Main.workSpace.getElements().add(newBlock);
				
				clearSelected();
				newBlock.setSelected(true);
				
				Main.configTextField(false, newBlock.getText().replaceAll("\n", Elements.LINE_SEPARATOR_SYM), true);		
			}
		});
		
		setOnMouseDragged(e -> {
			blockWrapper.move(e.getScreenX(), e.getScreenY());
		});
	}
	
	//dragged, pressed, clicked
	private void initMoveableEvent() {
				
		setOnMousePressed(e -> {
			if (e.getButton() == MouseButton.PRIMARY && !LineBetween.isCreated) {
				onSeneX    = (int) e.getSceneX();
				onSceneY   = (int) e.getSceneY();
				
				if (isSelected)
					if (selectedBlocks.size() > 1) {	
						if (!LineBetween.selectedLines.isEmpty()) {
							translateX = new int[selectedBlocks.size()+LineBetween.selectedLines.size()];
							translateY = new int[selectedBlocks.size()+LineBetween.selectedLines.size()];
							int i = 0;
							for (Block block : selectedBlocks) {
								translateX[i] = (int) block.getTranslateX();
								translateY[i] = (int) block.getTranslateY();
								i++;
							}
							for (LineBetween line : LineBetween.selectedLines) {
								translateX[i] = (int) line.getTranslateX();
								translateY[i] = (int) line.getTranslateY();
								i++;
							}
						} else {
							translateX = new int[selectedBlocks.size()];
							translateY = new int[selectedBlocks.size()];
							int i = 0;
							for (Block block : selectedBlocks) {
								translateX[i] = (int) block.getTranslateX();
								translateY[i] = (int) block.getTranslateY();
								i++;
							}
						}
					} else {
						if (!LineBetween.selectedLines.isEmpty()) {
							translateX = new int[LineBetween.selectedLines.size()+1];
							translateY = new int[LineBetween.selectedLines.size()+1];
	
							translateX[0] = (int) getTranslateX();
							translateY[0] = (int) getTranslateY();
							
							int i = 1;
							for (LineBetween line : LineBetween.selectedLines) {
								translateX[i] = (int) line.getTranslateX();
								translateY[i] = (int) line.getTranslateY();
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
					LineBetween.clearSelected();
					setSelected(true);
					Main.configTextField(false, getText().replaceAll("\n", Elements.LINE_SEPARATOR_SYM), false);
					
					translateX = new int[1];
					translateY = new int[1];
					translateX[0] = (int) getTranslateX();
					translateY[0] = (int) getTranslateY();
				}
			}
		});
		
		setOnMouseDragged(e -> {
			if (e.getButton() == MouseButton.PRIMARY  && !LineBetween.isCreated) {
				moving = true;
				if (selectedBlocks.size() > 1) {
					if (!LineBetween.selectedLines.isEmpty()) {
						int i = 0;
						for (Block block : selectedBlocks) {
							block.setTranslateX(Funcs.toNearInt((int) (translateX[i] + e.getSceneX() - onSeneX), moveableStep));
							block.setTranslateY(Funcs.toNearInt((int) (translateY[i] + e.getSceneY() - onSceneY), moveableStep));
							i++;
						}
						for (LineBetween line : LineBetween.selectedLines) {
							line.setTranslateX(Funcs.toNearInt((int) (translateX[i] + e.getSceneX() - onSeneX), LineBetween.moveableStep));
							line.setTranslateY(Funcs.toNearInt((int) (translateY[i] + e.getSceneY() - onSceneY), LineBetween.moveableStep));
							i++;
						}
					} else {
						int i = 0;
						for (Block block : selectedBlocks) {
							block.setTranslateX(Funcs.toNearInt((int) (translateX[i] + e.getSceneX() - onSeneX), moveableStep));
							block.setTranslateY(Funcs.toNearInt((int) (translateY[i] + e.getSceneY() - onSceneY), moveableStep));
							i++;
						}
					}
				} else {
					if (!LineBetween.selectedLines.isEmpty()) {
			            setTranslateX(Funcs.toNearInt((int) (translateX[0] + e.getSceneX() - onSeneX), moveableStep));
			            setTranslateY(Funcs.toNearInt((int) (translateY[0] + e.getSceneY() - onSceneY), moveableStep));
			            
			            int i = 1;
						for (LineBetween line : LineBetween.selectedLines) {
							line.setTranslateX(Funcs.toNearInt((int) (translateX[i] + e.getSceneX() - onSeneX), LineBetween.moveableStep));
							line.setTranslateY(Funcs.toNearInt((int) (translateY[i] + e.getSceneY() - onSceneY), LineBetween.moveableStep));
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
				LineBetween.clearSelected();
				setSelected(true);
				Main.configTextField(false, getText().replaceAll("\n", Elements.LINE_SEPARATOR_SYM), false);
			}
		});
		
		setOnMouseClicked(e -> { if (e.getClickCount() > 1) Main.configTextField(false, null, true); });
	}
	
	private void initHightlight() {
		
		EventHandler<MouseEvent> mouseEnter = e -> {
			isMouseOnBlock = true;
			if (!isSelected) {
				setEffect(new DropShadow(5, Color.BLUEVIOLET));
			}
			mPoints.forEach(points -> points.setMinOpacity(0.2));
		};
		
		EventHandler<MouseEvent> mouseExit = e -> {
			isMouseOnBlock = false;
			if (isSelected) {
				setEffect(new DropShadow(18, Color.BLUE));
			} else {
				setEffect(new DropShadow(5, Color.BLACK));
			}
			mPoints.forEach(points -> points.setMinOpacity(0));
		};
		
		setOnMouseEntered(mouseEnter);
		setOnMouseExited(mouseExit);
	}
	
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
		
		if (isSelected) {
			setEffect(new DropShadow(18, Color.BLUE));
			selectedBlocks.add(this);
		} else {
			setEffect(new DropShadow(5, Color.BLACK));
			selectedBlocks.remove(this);
		}
	}
	
	public static void clearSelected() {
		List<Block> blocks = new ArrayList<Block>(selectedBlocks);
		for (Block block : blocks) {
			block.setSelected(false);
		}
		blocks.clear();
		blocks = null;
	}
	
	protected void setNewSize(double width, double height) {
		setPrefSize(width, height);
		block.setWidth(width);
		block.setHeight(height);

		this.w = width;
		this.h = height;
		
		textOnBlock.setWrappingWidth(width);
		
		redrawBlock();
	}
	
	public void scale(double percent) {
		setNewSize(w*percent, h*percent);
	}
	
	public Block setNewWidth(double width) {setNewSize(width, width/2); return this;}
	
	public double getCurrentWidth() {return w;}
	
	public Block setText(String text) {textOnBlock.setText(text); return this;}
	
	public String getText() {return textOnBlock.getText();}
	
	public void setLocation(double x, double y) {
		setTranslateX(x);
		setTranslateY(y);
	}
			
	public static Block newInstance(Block block, double width, double height, BlockType type) {
		try {
			Constructor<?> newBlock = block.getClass().getConstructor(double.class, double.class, BlockType.class);
			return (Block) newBlock.newInstance(width, height, type);
		} catch (Exception e) {
			System.err.println("Ошибка создания блока: " + e);
		}
		return null;
	}
}
