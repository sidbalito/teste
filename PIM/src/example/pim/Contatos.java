package example.pim;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.pim.Contact;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMItem;
import javax.microedition.pim.PIMList;

import comum.Alerta;
import comum.KeyBoard;
import comum.graficos.*;

public class Contatos extends MIDlet implements CommandListener, ListaListener, PickerListener{

	private PIM pim = PIM.getInstance();
	private ListaGrafica contatos = new ListaGrafica(this);
	private Command cmdSair = new Command("Sair", Command.ITEM, 2);
	private Command cmdChamar = new Command("Chamar", Command.OK, 1);
	private Command cmdImportar = new Command("Importar", Command.ITEM, 2);
	private Command cmdExportar = new Command("Exportar", Command.ITEM, 2);
	private Command cmdEditar = new Command("Editar", Command.ITEM, 2);
	private Command cmdOperadoras = new Command("Operadoras", Command.ITEM, 2);
	private final Display display;
	private final Vector listas = new Vector();
	private Vector icones = new Vector();
	private Vector logos = new Vector();
	private int corTelaContatos = 0x800000;
	private int corLetraContatos = 0xFFFFFF;
	private int[] atalhos = new int[26];
	private int repeat;
	private int lastKey;
	private Splash splash;

	public Contatos() {
		display = Display.getDisplay(this);
		for(int i = 0; i < atalhos.length; i++)atalhos[i] = Integer.MAX_VALUE;
	}

	private void loadIcons() {
		int i = 0;
		String pequeno, grande;
		try {
			while (true) {
				pequeno = "/"+i+".png";
				grande = "/G"+i+".png";
				icones.addElement(Image.createImage(pequeno));
				logos.addElement(Image.createImage(grande));
				i++;
			}
		} catch (Exception e) {
			System.out.println("Ultimo ícone carregado.");
		}
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {
		try {
			if(icones.size()<1)inicializa();
			splash.setSubtext("Inicializado...");
			/*
			long ms = System.currentTimeMillis()+2000;
			while(ms>System.currentTimeMillis());
			//*/
			display.setCurrent(contatos);		
		} catch (Exception e) {
			showError("Erro desconhecido: \n"+e.getMessage()+"\n"+e.getClass());
		}
	}
	
	private void inicializa() {
		loadIcons();
		splash = new Splash("Carregando...", logos);
		splash.setSubtext("Preparando aplicação...");
		display.setCurrent(splash);
		/*
		long ms = System.currentTimeMillis()+2000;
		while(ms>System.currentTimeMillis());
		//*/
		configuraContatos();
		try {
			Operadora.loadData();
		} catch (Exception e) {
			System.out.println(e.getClass());
			showError("Erro ao carregar dados.\n"+e.getMessage()+"\n"+e.getClass());
		}
		splash.setSubtext("Lista de contatos...");
		populateList();
	}
	
	private void populateList(){
		String[] nomesListas = pim.listPIMLists(PIM.CONTACT_LIST);
		Enumeration[] enums = new Enumeration[nomesListas.length];
		try {
			for(int i = 0; i<nomesListas.length; i++){
				enums[i] = getContactList(nomesListas[i]).items();	
			}
			mergeEnumerations(enums);
		} catch (PIMException e) {
			showError("Erro ao abrir PIMList.\n"+e.getMessage()+"\n"+e.getClass());
		}
	}

	private PIMList getContactList(String name) throws PIMException{
		return pim.openPIMList(PIM.CONTACT_LIST, PIM.READ_ONLY, name);
	}
	
	private void addContato(Object item){
				listas.addElement(item);
				String nome = getString(item);
				add(nome, item);
	}
	
	public String getString(Object item){
		return getFieldValue((PIMItem) item, Contact.FORMATTED_NAME);
	}
	
	public void add(String nome, Object item){
		String numero = getFieldValue((PIMItem)item, Contact.TEL);
		int codOperadora = Operadora.getOperadora(numero);
		addNomeContato(nome, getImage(codOperadora));//*/
	}
	
	public Image getImage(int index){
		return (Image) icones.elementAt(index<icones.size()?index:0);
	}
	
	public void addNomeContato(String nome, Image icone){
		contatos.append(nome, icone);
		if(nome.length()>0)	setAtalho(nome.charAt(0), contatos.size());
	}
	
	private void setAtalho(char c, int pos) {
		int index = Character.toUpperCase(c) - 'A';
		if(atalhos[index] > pos)	atalhos[index] = pos;
	}

	private int getAtalho(char c) {
		if(c < 'A' | c > 'z' | (c > 'Z' & c < 'a')) return contatos.getSelected();
		int index = Character.toUpperCase(c) - 'A';
		int pos = atalhos[index]-1;
		if(pos > contatos.size()) return contatos.getSelected();
		return pos;
	}

	private String getFieldValue(PIMItem item, int field){
		if(item == null) return "";
		if (item.countValues(field) == 0) return "";
		return item.getString(field, 0);
	}
	
	public void commandAction(Command command, Displayable d) {
		if(command == cmdChamar)
			try {
				platformRequest("tel:"+getFieldValue(itemAt(contatos.getSelected()), Contact.TEL));
			} catch (Exception e) {
				showError("Erro ao efetuar chamada.\n"+e.getMessage()+"\n"+e.getClass());
			}
		else if(command == cmdSair){
			new Thread(new Runnable(){
				
				public void run() {
					try {
						Operadora.storeData();
						notifyDestroyed();
				} catch (Exception e) {
					showError("Erro ao armazenar dados..\n"+e.getMessage()+"\n"+e.getClass());
				}
					
				}
			}).start();
		}else if(command == cmdImportar){
			contatos = new ListaGrafica(this);
			display.setCurrent(new Splash("Carregando..."));
			new Thread(new Runnable() {
				public void run() {
					try {
						Operadora.importa();
						configuraContatos();
						display.setCurrent(contatos);
					} catch (Exception e) {
						showError("Erro ao importar.\n"+e.getMessage()+"\n"+e.getClass());
					}						
				}
			}).start();

		}else if(command == cmdExportar){
			new Thread(new Runnable() {
				public void run() {
					try {
						Operadora.exporta();
					} catch (IOException e) {
						showError("Erro ao exportar.\n"+e.getMessage()+"\n"+e.getClass());
					}
				}
			}).start();
		}else if(command == cmdEditar){
		}else if(command == cmdOperadoras){
			ListaGrafica operadoras = new ListaGrafica(this);
			String item;
			for(Enumeration items = Operadora.getNumeros(); items.hasMoreElements();){
				item = (String)items.nextElement();
				int codOperadora = Operadora.getOperadora(item);
				operadoras.append(item, (Image) icones.elementAt(codOperadora <icones.size()?codOperadora:0));
			}
			display.setCurrent(operadoras);
		}
	}

	private PIMItem itemAt(int selectedIndex) {
		if(listas.size()<=selectedIndex) return null;
		return (PIMItem) listas.elementAt(selectedIndex);	
	}

	public void keyPressed(int keyCode) {
		if(keyCode == KeyBoard.FIRE_KEY){
			String numero = getFieldValue(itemAt(contatos.getSelected()), Contact.TEL);
			String nome = getFieldValue(itemAt(contatos.getSelected()), Contact.FORMATTED_NAME);
			ImagePicker ip = new ImagePicker(this, Operadora.getOperadora(numero), logos);
			ip.setTexts(nome, numero);
			display.setCurrent(ip);
		} else {	
			if(lastKey != keyCode){
				lastKey = keyCode;
				repeat = 0;
			} else repeat++;
			char c = KeyBoard.getAlphaChar(keyCode, repeat, true);
			if(c == -1) return;
			contatos.setTopSelected(getAtalho(c));
			contatos.repaint();
		}
	}

	public void keyRepeated(int keyCode) {
	}

	public void keyReleased(int keyCode) {
		
	}

	public String getDetail(int index) {
		return getFieldValue(itemAt(index), Contact.TEL);
	}

	public void setPicked(int selected) {
		Operadora.setOperadora(getFieldValue(itemAt(contatos.getSelected()), Contact.TEL), selected);
		contatos.setImage(contatos.getSelected(), (Image)icones.elementAt(selected));
		contatos.getSelected();
		display.setCurrent(contatos);
	}

	void mergeEnumerations(Enumeration[] pEnums){
		int len = pEnums.length;
		Enumeration[] enums = new Enumeration[len];
		String[] candidates = new String[len];
		for(int i = 0; i<len;i++) enums[i] = pEnums[i];
		int i = 0;
		int next = -1;
		int candidate;
		int numEnums = len;
		Object[] items = new Object[len];
		while(true){
			if(i<numEnums){
				if(candidates[i] == null){
					if(!enums[i].hasMoreElements()){
						if(isAllNull(candidates))break;
						numEnums--;						
						if(numEnums > 0) enums[i]=enums[numEnums];
						continue;
					}
					items[i] = enums[i].nextElement();
					candidates[i] = getString(items[i]);
				}
			}
			candidate = i;
			next = compare(next, candidate, candidates);
			i++;
			
			if( i >= len){
				addContato(items[next]);
				candidates[next] = null;
				next = -1;
				i = 0;
			}
		}
	}

	private boolean isAllNull(String[] candidates) {
		for(int i = 0; i < candidates.length; i++) if(candidates[i] != null) return false;
		return true;
	}

	public int compare(int next, int candidate, String[] candidates){
		int len = candidates.length;
		if(next < 0 | next >= len){
			if(candidate < 0 | candidate >= len) return -1;
			return candidate;
		}
		if(candidates[candidate] == null) return next;
		int compare = candidates[next].compareTo(candidates[candidate]);
		if(compare>0)return candidate;
		return next;
	}

	public boolean hasMoreStrings() {
		return false;
	}

	private void showError(String text){
		System.out.println("Erro: "+text);
		display.setCurrent(new Alerta("Erro", text));
		//*/
		long ms = System.currentTimeMillis()+10000;
		while(ms>System.currentTimeMillis());
		//*/
	}
	
	private void configuraContatos(){
		contatos.setCommandListener(this);
		contatos.addCommand(cmdSair);
		contatos.addCommand(cmdChamar);/*
		contatos.addCommand(cmdImportar);
		contatos.addCommand(cmdExportar);//*/
		contatos.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
		contatos.setColor(corTelaContatos , corLetraContatos );

	}
	
}
