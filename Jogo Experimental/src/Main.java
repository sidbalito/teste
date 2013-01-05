import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class Main extends MIDlet implements Jogo, MenuListener {
	public static final int MENU = 0;
	public static final int TELA = 1;

	Menu menu = new Menu(this);
	Screen tela = new Screen(this);
	private Item novo = new Item("Novo");
	private Item continuar = new Item("Continuar");
	private Item sair = new Item("Sair");
	private Item record = new Item("Record: " + Controle.getPontosMax());
	private boolean iniciado;

	public Main() {
		// System.out.println("Record: "+Controle.getPontosMax());
		novo.setSelecionado();
		record.setBloqueado();
		continuar.oculta();
		menu.addItem(novo);
		menu.addItem(continuar);
		menu.addItem(sair);
		menu.addItem(record);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {

	}

	protected void pauseApp() {
		pause();
	}

	private void setDisplay(Displayable displayable) {
		Display.getDisplay(this).setCurrent(displayable);
	}

	protected void startApp() throws MIDletStateChangeException {
		setDisplay(menu);
	}

	public void pause() {
		setDisplay(menu);
		Relogio.pausa();
	}

	/*
	 * public void setDisplay(int cod){ switch (cod){ case MENU:
	 * setDisplay(menu); break; case TELA: setDisplay(tela); break; } }//
	 */

	public void novo() {
		tela.novoJogo();
		continuar.mostra();
		setDisplay(tela);
	}

	public void pausa() {
		Relogio.pausa();
		record.atualiza("Record: " + Controle.getPontosMax());
		setDisplay(menu);
	}

	public void continua() {
		setDisplay(tela);
	}

	public void encerra() {
		notifyDestroyed();
	}

	public void onItem(Item item) {
		if (item == novo) {
			novo();
		} else if (item == continuar) {
			continua();
		} else if (item == sair) {
			encerra();
		}
	}

	public boolean iniciado() {
		return iniciado;
	}
}
