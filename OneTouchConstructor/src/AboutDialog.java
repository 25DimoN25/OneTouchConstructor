import java.awt.Toolkit;

import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AboutDialog extends Stage {
	public AboutDialog() {
		Pane pane = new Pane();
		Scene scene = new Scene(pane, 480, 200);
		setScene(scene);
		setTitle("О программе..");
		centerOnScreen();
		initModality(Modality.APPLICATION_MODAL);
		setResizable(false);
		
		Text text1 = new Text(15, 50, "Simple Touch Constructor v0.9b");
		text1.setFont(Font.font(null, FontWeight.BOLD, 30));
		text1.setEffect(new DropShadow(5, Color.RED));
		
		Text text2 = new Text(20, 100, "специально для");
		text2.setFont(Font.font(null, FontWeight.NORMAL, 14));
		
		Text text3 = new Text(20, 130, "ХАКАТОН 2015");
		text3.setFont(Font.font(null, FontWeight.NORMAL, 25));
		text3.setEffect(new DropShadow(5, Color.RED));
		
		Text text4 = new Text(280, 180, "Авторы:\nСалтыков Д.К, Дворникова А.П.");
		text4.setFont(Font.font(null, FontWeight.NORMAL, 14));
	
		
		pane.getChildren().addAll(text1, text2, text3, text4);
	}
	
	@Override
	public void showAndWait() {
		Toolkit.getDefaultToolkit().beep();
		super.showAndWait();
	}
	
}
