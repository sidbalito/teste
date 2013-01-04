import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
//Midlet's User interface API
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
//import javax.microedition.midlet.*;

public class Teste extends MIDlet
			implements CommandListener {

	private Form helloForm;
	private Command exitCommand;

/**
* Midlet constructor
* */

  public Teste() {

  	exitCommand = new Command("Exit", Command.EXIT, 1);

     	helloForm = new Form("Device Specs");

  	// Use System.getProperty() to query device configurations
  	helloForm.append("Platform : "
		+ System.getProperty("microedition.platform") + "\n");
  	helloForm.append("Encoding : "
		+ System.getProperty("microedition.encoding") + "\n");
  	helloForm.append("Configuration : "
		+ System.getProperty("microedition.configuration") + "\n");
  	helloForm.append("Profiles : "
		+ System.getProperty("microedition.profiles") + "\n");
  	helloForm.append("Locale : "
		+ System.getProperty("microedition.locale") + "\n");

  	// Use java.lang's Runtime class to query device memory
  	long total = Runtime.getRuntime().totalMemory();
  	long livre = Runtime.getRuntime().freeMemory();
  	long percentual = (livre*100)/total;
  	helloForm.append("Total Memory: "
		+ total  + "\n");
  	helloForm.append("Free Memory: "
		+ livre  + " " +
				"("+percentual+"%)\n");

  	// Forcibly call garbage collector
  	Runtime.getRuntime().gc();

  	helloForm.addCommand(exitCommand);
  	helloForm.setCommandListener(this);

  }

  /**
   * The paused state changed to active state.
   */

  protected void startApp() {
      Display.getDisplay(this).setCurrent(helloForm);
  }

  /**
   * The MIDlet frees as much resources as it can.
   */

  protected void pauseApp() {}


  protected void destroyApp(boolean bool) {}
  /**
   * Midlet event handling
   */
  public void commandAction(Command cmd, Displayable disp) {
      if (cmd == exitCommand) {
          destroyApp(false);
          notifyDestroyed();
      }
  }
}
