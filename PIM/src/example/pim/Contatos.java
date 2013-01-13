package example.pim;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.pim.Contact;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMItem;
import javax.microedition.pim.PIMList;

import KeyListener;
import ListaGrafica;

public class Contatos extends MIDlet implements CommandListener, KeyListener{

	private PIM pim = PIM.getInstance();
	private ListaGrafica tela = new ListaGrafica(this);
	private Command cmdLigar = new Command("Ligar", Command.OK, 0);
	private Command cmdSair = new Command("Sair", Command.EXIT, 0);
	private final Display display;
	private Vector listas = new Vector();

	public Contatos() {
		//tela = new List("Contatos", List.IMPLICIT);
		tela.setCommandListener(this);
		tela.addCommand(cmdSair);
		display = Display.getDisplay(this);
/*		ListaGrafica grafica = new ListaGrafica(this);
		grafica.append("Teste1");
		grafica.append("Teste2");//*/
		display.setCurrent(tela);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {

	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {
		String[] nomesListas = pim.listPIMLists(PIM.CONTACT_LIST);
		Alert alerta = new Alert("Info", "Há "+nomesListas.length+" lista(s)", null, AlertType.INFO);
		alerta.setTimeout(Alert.FOREVER);
		display.setCurrent(alerta);
		try {
			for(int i = 0; i<nomesListas.length; i++){
				addContatos(getContactList(nomesListas[i]).items());	
			}
		} catch (PIMException e) {
			e.printStackTrace();
		}
	}
	
	private PIMList getContactList(String name) throws PIMException{
		return pim.openPIMList(PIM.CONTACT_LIST, PIM.READ_ONLY, name);
	}
	
	private void addContatos(Enumeration items) throws PIMException{
			for(; items.hasMoreElements();){
				PIMItem item = (PIMItem) items.nextElement();
				listas.addElement(item);
				addNomeContato(getFieldValue(item, Contact.FORMATTED_NAME)+" - " + 
						getFieldValue(item, Contact.TEL));
			}
		
	}
	
	private void addNomeContato(String nome){
		tela.append(nome);
	}
	
	private String getFieldValue(PIMItem item, int field){
		return item.getString(field, 0);
	}
	
	private String getFieldLabel(PIMItem item, int field){
		return item.getPIMList().getFieldLabel(field);
	}

	public void commandAction(Command command, Displayable d) {
		if(command == cmdLigar)
			try {
				platformRequest("sim2:"+getFieldValue(itemAt(tela.getSelected()), Contact.TEL));
			} catch (Exception e) {
				e.printStackTrace();
			}
		else if(command == cmdSair){
			notifyDestroyed();
		}
	}

	private PIMItem itemAt(int selectedIndex) {
		return (PIMItem) listas.elementAt(selectedIndex);	
	}

	public void keyPressed(int keyCode) {
			if(keyCode == -5)
				try {
					platformRequest("tel:"+getFieldValue(itemAt(tela.getSelected()), Contact.TEL));
				} catch (ConnectionNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
	}

	public void keyRepeated(int keyCode) {
		
	}

	public void keyReleased(int keyCode) {
		
	}

}
