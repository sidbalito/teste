package example.pim;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.pim.Contact;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMItem;
import javax.microedition.pim.PIMList;

import ListaGrafica;
import ListaListener;

public class Contatos extends MIDlet implements CommandListener, ListaListener{

	private PIM pim = PIM.getInstance();
	private ListaGrafica tela = new ListaGrafica(this);
	private Command cmdLigar = new Command("Ligar", Command.OK, 0);
	private Command cmdSair = new Command("Sair", Command.EXIT, 0);
	private Command cmdOperadoras = new Command("Operadoras", Command.ITEM, 0);
	private final Display display;
	private Vector listas = new Vector();
	private Vector icones = new Vector();

	public Contatos() {
		display = Display.getDisplay(this);
		loadIcons();
		//tela = new List("Contatos", List.IMPLICIT);
		tela.setCommandListener(this);
		tela.addCommand(cmdSair);
		tela.addCommand(cmdOperadoras);
/*		ListaGrafica grafica = new ListaGrafica(this);
		grafica.append("Teste1");
		grafica.append("Teste2");//*/
		display.setCurrent(tela);
	}

	private void loadIcons() {
		try {
			int i = 0;
			while (true) {
				icones.addElement(Image.createImage("/"+i+".jpg"));
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {

	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {
		try {
			//Operadora.loadData();
			Operadora.importa();
		} catch (Exception e) {
			Alert alerta = new Alert("Erro", e.getMessage(), null, AlertType.ERROR);
			alerta.setTimeout(Alert.FOREVER);
			display.setCurrent(alerta);//*/
			e.printStackTrace();
		}
		String[] nomesListas = pim.listPIMLists(PIM.CONTACT_LIST);
		try {
			for(int i = 0; i<nomesListas.length; i++){
				addContatos(getContactList(nomesListas[i]).items());	
			}
			tela.repaint();
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
			String nome = getFieldValue(item, Contact.FORMATTED_NAME);
			String numero = getFieldValue(item, Contact.TEL);
			
			int codOperadora = Operadora.getOperadora(numero);
			addNomeContato(nome, (Image) icones.elementAt(codOperadora<icones.size()?codOperadora:0));
		}
	}
	
	private void addNomeContato(String nome, Image icone){
		tela.append(nome, icone);
	}
	
	private String getFieldValue(PIMItem item, int field){
		return item.getString(field, 0);
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
		}else if(command == cmdOperadoras){
			ListaGrafica operadoras = new ListaGrafica(this);
			String item;
			for(Enumeration items = Operadora.getNumeros(); items.hasMoreElements();){
				item = (String)items.nextElement();
				System.out.println(item);
				int codOperadora = Operadora.getOperadora(item);
				operadoras.append(item, (Image) icones.elementAt(codOperadora <icones.size()?codOperadora:0));
			}
			display.setCurrent(operadoras);
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
					e.printStackTrace();
				}	
	}

	public void keyRepeated(int keyCode) {
		
	}

	public void keyReleased(int keyCode) {
		
	}

	public String getDetail() {
		return getFieldValue(itemAt(tela.getSelected()), Contact.TEL);
	}

}
