import java.net.URL;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;

public class Funcs {
	static ImageView checkedPNG(String name) {
		URL res = Elements.class.getResource("res/"+name+".png");
		if (res != null) 
			return new ImageView(res.toExternalForm());
		else {
			System.err.println("Image "+name+".png not found.");
//			Platform.exit();
			return null;
		}
	}
	
	static ToggleButton getToggleButton(ToggleGroup toggleGroup, String accessibleHelp) {
		return toggleGroup.getToggles().stream()
				.map(e -> (ToggleButton)e)
				.filter(e -> e.getAccessibleHelp().equals("pick"))
				.findFirst().get();
	}
	
	//число приблизить к ближайшему кратному
	static int toNearInt(int number, int divider) {
        int div = number/divider;
        int mod = number % divider;
        return  div*divider + (mod > divider/2 ? divider : 0);
	}
}
