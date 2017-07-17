
import java.awt.Toolkit;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
	
	
	public static WorkSpace workSpace;
	
	public static TextField textField;
	public static Stage primaryStage; //доступность только для initOwner

	public static Double fontSize = Font.getDefault().getSize();
	public static String fontName = Font.getDefault().getName();

	
	static final double DEF_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth()/1.5;
	static final double DEF_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight()/1.5;
	static final double DEF_BLOCK_WIDTH = 120;
	static final double DEF_BLOCK_HEIGHT = 60;
	static final int DEF_BG_STEPSIZE = 30;
	
	
	public void start(Stage primaryStage) throws Exception {
		Main.primaryStage = primaryStage;
		
		BorderPane mainPane = new BorderPane();	
		VBox menuAndToolBoxPane = new VBox();
		
		//TOP (menu and toolbox)
		ToolBar toolBar = new ToolBar();
		Elements.addGridButton(toolBar);
		Elements.addVerticalToolBarSeparator(toolBar);
		textField = Elements.addTextEditTextField(toolBar);
		Elements.addVerticalToolBarSeparator(toolBar);
		Elements.addFontTextFields(toolBar);
		
		MenuBar menuBar = new MenuBar();
		Elements.addMenu(menuBar);
		
		
		//LEFT (elements pane)
		TilePane elementsPane = new TilePane(0, 10);
		elementsPane.setPrefColumns(1);
		elementsPane.setPadding(new Insets(5));
		Elements.addBlocks(elementsPane);
		
		ScrollPane elementsPaneScrolls = new ScrollPane(elementsPane);
		elementsPaneScrolls.setMinWidth(0);
		elementsPaneScrolls.setMaxWidth(DEF_BLOCK_WIDTH*3);
		elementsPaneScrolls.setHbarPolicy(ScrollBarPolicy.NEVER);
		elementsPaneScrolls.setVbarPolicy(ScrollBarPolicy.ALWAYS);
			
		
		//RIGHT (workspace, selecting)
		workSpace = new WorkSpace(DEF_WIDTH*1.5, DEF_HEIGHT*1.5);	
		EventRegisters.registerDeleteButton(mainPane);
		
		//CENTER (union workspace and elements pane)
		SplitPane splitPane = new SplitPane(elementsPaneScrolls, new ScrollPane(workSpace));
		SplitPane.setResizableWithParent(elementsPaneScrolls, false);
		EventRegisters.registerBlockInMenuAutoSize(elementsPane.getChildren(), splitPane.getDividers().get(0), primaryStage);
		
		
		//ADDING
		menuAndToolBoxPane.getChildren().addAll(menuBar, toolBar);
		mainPane.setTop(menuAndToolBoxPane);
		mainPane.setCenter(splitPane);
		
		Scene scene = new Scene(mainPane, DEF_WIDTH, DEF_HEIGHT);
		primaryStage.setScene(scene);
		primaryStage.setMinWidth(600);
		primaryStage.setMinHeight(300);
		primaryStage.setTitle("OneTouch Constructor v2.3" + " ---- C:\\users\\admin\\desktop\\New Scheme.blch*");
		ImageView icon = Funcs.checkedPNG("icon");
		primaryStage.getIcons().add(icon!=null?icon.getImage():null);
		primaryStage.sizeToScene();
		primaryStage.show();
		
		splitPane.setDividerPositions((DEF_BLOCK_WIDTH+10+19)/DEF_WIDTH); //10 - insets, 19 - scrollwidth	
		
	}
	
	public static void configTextField(boolean isDisable, String text, boolean isGetFocus) {
		textField.setDisable(isDisable);
		
		if (text != null)
			textField.setText(text);
		else if (isDisable)
			textField.setText("");
		
		
		if (isGetFocus) {
			textField.requestFocus();
			textField.positionCaret(Main.textField.getText().length());
		}
	
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
