import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class Jogo extends MIDlet {
	Menu tela = new Menu(this);

	public Jogo() {
		// TODO Auto-generated constructor stub
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		Display.getDisplay(this).setCurrent(tela);
	}
	
	/**
	 * Método chamado quando o usuário seleciona um item do menu 
	 * @param selecionado
	 */
	public void onItem(Item selecionado) {
				
	}

}
