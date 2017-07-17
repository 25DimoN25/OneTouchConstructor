import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class Elements {
	static final String LINE_SEPARATOR_SYM = "�"; 
	
	static void addFontTextFields(ToolBar toolBar) {
		ComboBox<String> fonts;
		ComboBox<String> fontsSize;
		
		fonts = new ComboBox<String>(FXCollections.observableArrayList(Font.getFontNames()));
		fonts.setPrefWidth(180);
		fonts.setValue(Main.fontName);
		
		fontsSize = new ComboBox<>(FXCollections.observableArrayList(
				"2", "4", "8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72"));
		fontsSize.setPrefWidth(70);
		fontsSize.setEditable(true);
		fontsSize.getEditor().setTextFormatter(new TextFormatter<>(c -> {
			if (c.getControlNewText().matches("([1-6][0-9]|7[0-2]|[1-9])?(\\.\\d{0,3})?")) return c;
			else return null;
		}));
		fontsSize.setValue(Main.fontSize.toString());

		EventRegisters.registerFontChangeFields(fonts, fontsSize);
		
		toolBar.getItems().addAll(fonts, fontsSize);
	}
	
	static void addGridButton(ToolBar toolBar) {

		ToggleButton tbGrid = new ToggleButton();
		tbGrid.setSelected(true);
		tbGrid.setTooltip(new Tooltip("���/���� �����"));
		tbGrid.setGraphic(Funcs.checkedPNG("grid"));
		
		ToggleButton tbMagnete = new ToggleButton();
		tbMagnete.setSelected(true);
		tbMagnete.setTooltip(new Tooltip("��������������� � �����"));
		tbMagnete.setGraphic(Funcs.checkedPNG("magnet"));
		
		Slider slider = new Slider(10, 120, 5);
		slider.setShowTickMarks(true);
		slider.setSnapToTicks(true);
		slider.setMajorTickUnit(10);
		slider.setMinorTickCount(1);
		
		EventRegisters.registerSliderActivity(slider, tbMagnete);
		EventRegisters.registerGridAndMagneticButton(tbGrid, tbMagnete, slider);
		
		toolBar.getItems().addAll(tbGrid, tbMagnete, slider);
	}
	
	static TextField addTextEditTextField(ToolBar toolBar) {
		
		TextField text = new TextField();
		text.setDisable(true);
		
		text.setPromptText("����� �� �����..");
		
		text.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				int pos = text.getCaretPosition();
				text.setText(new StringBuilder(text.getText()).insert(pos, LINE_SEPARATOR_SYM).toString());
				text.positionCaret(pos+1);
			}
		});
		
		EventRegisters.registerTextFieldTextEdit(text);
		
		toolBar.getItems().add(text);
		
		return text;
	}
	
	static void addMenu(MenuBar menuBar) {
	
		Menu file = new Menu("_����");
		MenuItem clear = new MenuItem("_��������", Funcs.checkedPNG("clean"));
		EventRegisters.registerClearAll(clear);
		MenuItem open = new MenuItem("�������");
		MenuItem save = new MenuItem("���������");
		MenuItem saveAs = new MenuItem("��������� ���..");
		MenuItem exit = new MenuItem("�����");
		EventRegisters.registerExit(exit);
		file.getItems().addAll(clear, new SeparatorMenuItem(),
				open, save, saveAs, new SeparatorMenuItem(), exit);

		Menu options = new Menu("�����");
		MenuItem option1 = new MenuItem("����� ����� ����");
		MenuItem option2 = new MenuItem("����� ����� ���");
		MenuItem option3 = new MenuItem("����� ����� ��� �-�");
		options.getItems().addAll(option1, option2, option3);
//		options.setDisable(true);

		Menu help = new Menu("����_��");
		MenuItem about = new MenuItem("� ��������_�");
		help.getItems().add(about);

				
		final AboutDialog aboutDialog = new AboutDialog();
		about.setOnAction(e -> aboutDialog.showAndWait());


		menuBar.getMenus().addAll(file, options, help);
	}
	
	static void addVerticalToolBarSeparator(ToolBar toolBar) {
		toolBar.getItems().add(new Separator(Orientation.VERTICAL));
	}
	
	static void addBlocks(Pane blocksPane) {
		blocksPane.getChildren().add(new ActionBlock(Main.DEF_BLOCK_WIDTH, Main.DEF_BLOCK_HEIGHT, BlockType.MENU));
		blocksPane.getChildren().add(new ConditionBlock(Main.DEF_BLOCK_WIDTH, Main.DEF_BLOCK_HEIGHT, BlockType.MENU));
		blocksPane.getChildren().add(new BeginEndBlock(Main.DEF_BLOCK_WIDTH, Main.DEF_BLOCK_HEIGHT, BlockType.MENU));
		blocksPane.getChildren().add(new CycleBlock(Main.DEF_BLOCK_WIDTH, Main.DEF_BLOCK_HEIGHT, BlockType.MENU));

		blocksPane.getChildren().add(new FunctionalBlock(Main.DEF_BLOCK_WIDTH, Main.DEF_BLOCK_HEIGHT, BlockType.MENU));
		blocksPane.getChildren().add(new InOutBlock(Main.DEF_BLOCK_WIDTH, Main.DEF_BLOCK_HEIGHT, BlockType.MENU));
		blocksPane.getChildren().add(new PrintBlock(Main.DEF_BLOCK_WIDTH, Main.DEF_BLOCK_HEIGHT, BlockType.MENU));
		blocksPane.getChildren().add(new CycleBeginBlock(Main.DEF_BLOCK_WIDTH, Main.DEF_BLOCK_HEIGHT, BlockType.MENU));
		blocksPane.getChildren().add(new CycleEndBlock(Main.DEF_BLOCK_WIDTH, Main.DEF_BLOCK_HEIGHT, BlockType.MENU));		
	}

}
