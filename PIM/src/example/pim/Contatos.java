package example.pim;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.ConnectionNotFoundException;
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

import ListaGrafica;
import ListaListener;

public class Contatos extends MIDlet implements CommandListener, ListaListener, PickerListener{

	private PIM pim = PIM.getInstance();
	private ListaGrafica contatos = new ListaGrafica(this);
	private Command cmdSair = new Command("Sair", Command.EXIT, 0);
	private Command cmdChamar = new Command("Chamar", Command.ITEM, 0);
	private Command cmdImportar = new Command("Importar", Command.ITEM, 0);
	private Command cmdExportar = new Command("Exportar", Command.ITEM, 0);
	private Command cmdEditar = new Command("Editar", Command.ITEM, 0);
	private Command cmdOperadoras = new Command("Operadoras", Command.ITEM, 0);
	private final Display display;
	private Vector listas = new Vector();
	private Vector icones = new Vector();
	private int corTelaContatos = 0x800000;
	private int corLetraContatos = 0xFFFFFF;

	public Contatos() {
		display = Display.getDisplay(this);
	}

	private void loadIcons() {
		try {
			int i = 0;
			while (true) {
				icones.addElement(Image.createImage("/"+i+".png"));
				i++;
			}
		} catch (IOException e) {
			System.out.println("Ultimo ícone");
		}
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {
		if(icones.size()<1)inicializa();
		display.setCurrent(contatos);
	}
	
	private void inicializa() {
		display.setCurrent(new Splash("Carregando..."));
		loadIcons();
		contatos.setCommandListener(this);
		contatos.addCommand(cmdSair);
		contatos.addCommand(cmdChamar);
		contatos.addCommand(cmdImportar);
		contatos.addCommand(cmdExportar);
		//contatos.setOrdered(true);
		contatos.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
		contatos.setColor(corTelaContatos , corLetraContatos );
		try {
			Operadora.loadData();
		} catch (Exception e) {
			System.out.println("Erro ao carregar dados");
		}
		String[] nomesListas = pim.listPIMLists(PIM.CONTACT_LIST);
//		Enumeration items;
		//ListsMerger.setContatos(this, nomesListas.length);
		Enumeration[] enums = new Enumeration[nomesListas.length];
		try {
			//while(items.hasMoreElements())System.out.println(getString(items.nextElement()));
			//System.out.println(pim.openPIMList(PIM.CONTACT_LIST, pim.READ_ONLY, pim.listPIMLists(PIM.CONTACT_LIST)[0]).items().nextElement());//enums[0].hasMoreElements());
			for(int i = 0; i<nomesListas.length; i++){
				//new Thread(new ListsMerger(getContactList(nomesListas[i]))).start();
				enums[i] = getContactList(nomesListas[i]).items();	
			}
//			while(enums[0].hasMoreElements())System.out.println(getString(enums[0].nextElement()));
			mergeEnumerations(enums);
			//contatos.repaint();
		} catch (PIMException e) {
			System.out.println("Erro ao abrir PIMList");
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
	}
	
	private String getFieldValue(PIMItem item, int field){
		if(item == null) return "";
		//if(field == Contact.TEL) System.out.println("Tel "+item.countValues(field));//getString(field, 0));
		if (item.countValues(field) == 0) return "";
		//System.out.println("num: "+item.getString(field, 0));
		return item.getString(field, 0);
	}
	
	public void commandAction(Command command, Displayable d) {
		if(command == cmdChamar)
			try {
				platformRequest("tel:"+getFieldValue(itemAt(contatos.getSelected()), Contact.TEL));
			} catch (Exception e) {
				System.out.println("Erro ao efetuar chamada");;
			}
		else if(command == cmdSair){
			new Thread(new Runnable(){
				
				public void run() {
					try {
						Operadora.storeData();
						notifyDestroyed();
				} catch (Exception e) {
					System.out.println("Erro ao armazenar dados");
				}
					
				}
			}).start();
		}else if(command == cmdImportar){
			try {
				Operadora.importa();
			} catch (Exception e) {
				System.out.println("Erro ao importar");
			}
		}else if(command == cmdExportar){
			try {
				Operadora.exporta();
			} catch (IOException e) {
				System.out.println("Erro ao exportar");
			}
		}else if(command == cmdEditar){
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
		if(listas.size()<=selectedIndex) return null;
		return (PIMItem) listas.elementAt(selectedIndex);	
	}

	public void keyPressed(int keyCode) {
			if(keyCode == -5){
				String numero = getFieldValue(itemAt(contatos.getSelected()), Contact.TEL);
			String nome = getFieldValue(itemAt(contatos.getSelected()), Contact.FORMATTED_NAME);

			ImagePicker ip = new ImagePicker(this, Operadora.getOperadora(numero), icones);
			ip.setTexts(nome, numero);
			display.setCurrent(ip);
			
			}else if(keyCode == -10)
				try {
					platformRequest("tel:"+getFieldValue(itemAt(contatos.getSelected()), Contact.TEL));
				} catch (ConnectionNotFoundException e) {
					System.out.println("Erro ao efetuar chamada");
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
//		int numCandidates = len==1?1:0;
		int numEnums = len;
//		int x = 1;
		Object[] items = new Object[len];
		while(true){
			//System.out.println(i+": "+candidates[i]);
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
//					numCandidates++;
				}
			}
			candidate = i;
			//System.out.println(enums[i].hasMoreElements());
			next = compare(next, candidate, candidates);
			//System.out.println(numCandidates);
			i++;
			
			if( i >= len){
				//add(candidates[next], (items[next]));
				addContato(items[next]);
				candidates[next] = null;
//				numCandidates--;
				next = -1;
				i = 0;
				//hasCandidates = false;
			}
			//System.out.println(isAllNull(candidates));
		}//*/
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
		//System.out.println(compare);
		if(compare>0)return candidate;
		return next;
	}

	public boolean hasMoreStrings() {
		return false;
	}


}
