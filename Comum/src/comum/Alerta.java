package comum;
import javax.microedition.lcdui.Alert;


public class Alerta extends Alert{

	public Alerta(String title, String text) {
		super(title);
		this.setString(text);
		this.setTimeout(Alert.FOREVER);		
	}
	

}
