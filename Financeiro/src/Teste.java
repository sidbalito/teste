import java.io.InputStream;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class Teste extends MIDlet {

	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
		
	}

	protected void pauseApp() {
		
	}

	protected void startApp() {
		InputStream s = getClass().getResourceAsStream("META-INF/MANIFEST.MF");
		System.out.println(new View("Grupo", "d1,d2,d3").getGrupos());
	}

}
