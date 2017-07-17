
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane.Divider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class EventRegisters {
	
	//Нажатие кнопки выхода в меню
	static void registerExit(MenuItem item) {
		item.setOnAction(e -> Platform.exit());
	}
	
	//Нажатие кнопки отчистить в меню
	static void registerClearAll(MenuItem menuItem) {
		menuItem.setOnAction(e -> {
			Main.configTextField(true, null, false);
			Main.workSpace.getElements().clear();
		});
	}
	
	//Изменение размеров блоков при перемещении ребра у SplitPane
	static void registerBlockInMenuAutoSize(ObservableList<Node> blocks, Divider divider, Stage parent) {
		divider.positionProperty().addListener((obs, oldPos, newPos) -> {
				for (Node node : blocks) {
					if (node instanceof Block) {
						Block block = (Block) node;
						block.setNewWidth(newPos.doubleValue()*parent.getWidth()-19-10-newPos.doubleValue()*19);
					}
				}
		});
	}

	//слайдер изменения сетки
	static void registerSliderActivity(Slider slider, ToggleButton tbMagnete) {
		slider.setValue(Main.DEF_BG_STEPSIZE);
		
		if (tbMagnete.isSelected()) {
			Block.moveableStep = Main.DEF_BG_STEPSIZE;
			LineBetween.moveableStep = Main.DEF_BG_STEPSIZE;
		}
		
		slider.valueProperty().addListener((obs, oValue, nValue) -> {
			Main.workSpace.setBgStepSize(nValue.intValue());
			if (tbMagnete.isSelected()) {
				Block.moveableStep = nValue.intValue();
				LineBetween.moveableStep = nValue.intValue();
			}
		});
	}
	
	//кнопка включения отключения сетки, деактивация слайдера + кнопка привязки к сетке
	static void registerGridAndMagneticButton(ToggleButton grid, ToggleButton magnete, Slider slider) {
		grid.setOnAction(e -> {
			slider.setDisable(!grid.isSelected());
			magnete.setDisable(!grid.isSelected());
			Main.workSpace.setBGVisible(grid.isSelected());
			
			Block.moveableStep = grid.isSelected() && magnete.isSelected() ? (int)slider.getValue() : Block.MIN_MOVEABLE_STEP;
			LineBetween.moveableStep = grid.isSelected() && magnete.isSelected() ? (int)slider.getValue() : LineBetween.MIN_MOVEABLE_STEP;
		});
		
		magnete.setOnAction(e -> {
			Block.moveableStep = magnete.isSelected() ? (int)slider.getValue() : Block.MIN_MOVEABLE_STEP;
			LineBetween.moveableStep = magnete.isSelected() ? (int)slider.getValue() : LineBetween.MIN_MOVEABLE_STEP;
		});
	}
	
	//удаление выделенных на DEL
	static void registerDeleteButton(Pane pane) {
		pane.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.DELETE) {
				Main.workSpace.getElements().removeAll(Block.selectedBlocks);
				Main.workSpace.getElements().removeAll(LineBetween.selectedLines);
				Block.clearSelected();
				LineBetween.clearSelected();
				Main.configTextField(true, null, false);
			} else if (e.getCode() == KeyCode.ESCAPE && !Block.moving && !LineBetween.moving) {	
				Block.clearSelected();
				LineBetween.clearSelected();
				Main.configTextField(true, null, false);
			}
		});
	}

	//изменение текста на блоках
	static void registerTextFieldTextEdit(TextField textField) {
		textField.textProperty().addListener((obs, oldText, newText) -> {
			for (Block block : Block.selectedBlocks) {
				block.setText(newText.replaceAll(Elements.LINE_SEPARATOR_SYM, "\n"));
			}
		});
	}


	static void registerFontChangeFields(ComboBox<String> fontsName, ComboBox<String> fontsSize) {
		fontsName.setOnAction(e -> {
			Main.fontName = fontsName.getValue();
			Block.selectedBlocks.forEach(block -> block.textOnBlock.setFont(new Font(Main.fontName, Main.fontSize)));
		});
		
		fontsSize.setOnAction(e -> {
			Main.fontSize = Double.parseDouble(fontsSize.getValue());
			Block.selectedBlocks.forEach(block -> block.textOnBlock.setFont(new Font(Main.fontName, Main.fontSize)));
		});
	}	
}
