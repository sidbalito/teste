import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import comum.graficos.ListListener;
import comum.graficos.RichList;
import comum.graficos.RichStringItem;


public class Financeiro extends MIDlet implements CommandListener, ListListener{

	private static final String CANCELAR = "Cancelar";
	private static final String EDITAR = "Editar";
	private static final String EXCLUIR = "Exluir";
	private static final String EXPORTAR = "Exportar";
		
	private static final int GRUPO = 2;
	private static final String IMPORTAR = "Importar";
	private static final String INSERIR = "Inserir";
	private static final int LANCAMENTO = 1;
	private static final String LANCAMENTOS = "Lançamentos";
	private static final String MARCAR = "Marcar";
	private static final int OPCAO = 0;
	
	private static final String PADRAO = "Padrão";
	private static final String SAIR = "Sair";
	private static final String SELECIONAR = "Selecionar";
	private static final int VISAO = 3;
	private static final String VISOES = "Visões";
	static final String GRUPOS = "Grupos";
	private static final String OPCOES = "Opções";
	
	private Command cancelar = new Command(CANCELAR, Command.ITEM, 0);
	private Command cmdEditar = new Command(EDITAR, Command.ITEM, 0);
	private Command cmdExcluir = new Command(EXCLUIR, Command.ITEM, 0);
	private Command cmdExportar = new Command(EXPORTAR, Command.ITEM, 1);
	private Command cmdGrupos = new Command(GRUPOS, Command.ITEM, 1);
	private Command cmdImportar = new Command(IMPORTAR, Command.ITEM, 1);
	private Command cmdInserir = new Command(INSERIR, Command.OK, 0);
	private Command cmdOpcoes = new Command(OPCOES, Command.ITEM, 0);
	private Command cmdSair = new Command(SAIR, Command.EXIT, 0);
	private Command cmdSelecionar =  new Command(SELECIONAR, Command.ITEM, 0);
	private Command cmdVisoes = new Command(VISOES, Command.ITEM, 0);
	private int currItem;
	private Display display;
	private EditaLancamento editaLancamento = new EditaLancamento(this);
	private RichList grupo;
	private Vector grupos = new Vector();
	private Vector lancamentos = new Vector();
	private RichList lista;
	private Command marcar = new Command(MARCAR, Command.ITEM, 0);
	private int modo = OPCAO;
	private Vector opcoes = new Vector();
	private Displayable telaAtual = lista;
	private RichList telaGrupos;// = new Grupos(grupos);
	private Vector view;
	private Vector viewNames = new Vector();
	private Hashtable views = new Hashtable();
	private boolean inserindo;
	
	public Financeiro() {
		load();
		createView(PADRAO);
		opcoes.addElement(LANCAMENTOS);
		opcoes.addElement(VISOES);
		opcoes.addElement(GRUPOS);
		//lista.setItems(items);
	}

	private void load() {
		new Thread(new Runnable() {
			
			public void run() {
				try {
					new Persist().load(LANCAMENTOS, lancamentos, new Lancamento());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private String valor(float literal) {
		StringBuffer valor = new StringBuffer(Float.toString(literal));
		int len = valor.length();
		int excesso = len-valor.toString().indexOf('.')-3;
		if(excesso > 0)valor.setLength(len-excesso);
		if(excesso < 0)valor.append('0');
		return valor.toString();
	}

	public void clickItem(RichList list, int index) {
		/*
		if(list == grupo){ 
			((RichLancamento)list.getRichItem()).toggle((Lancamento) lancamentos.elementAt(index));
			list.repaint();
		}//*/
		if(list instanceof RichList) commandAction(cmdSelecionar, list);
	}

	public void commandAction(Command command, Displayable tela) {
		if(command == cmdSair)
			try {
				destroyApp(false);
			} catch (MIDletStateChangeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else if(command == cmdExportar) exporta();
		else if(command == cmdImportar) importa();
		else if(command == cmdVisoes) listaVisoes();
		else if(command == cmdOpcoes) listaOpcoes();
		
		switch (modo) {
		case OPCAO: opcoesCommand(command, tela);break;
		case LANCAMENTO: lancamentosCommand(command, tela);break;
		case GRUPO: gruposCommand(command, tela);break;
		case VISAO: viewsCommand(command, tela);break;
		}
		if(tela instanceof EditaLancamento) 
			editaLancamentoCommand(command, (EditaLancamento) tela);
		lista.repaint();
		
	}

	private void createView(String name) {
		views.put(name, new Vector());
		viewNames.addElement(name);
	}

	private Grupo editaGrupo(Grupo grupo) {
		return grupo;
	}

	private void editaLancamento(int index) {
		currItem = index;
		editaLancamento.setLancamento((Lancamento) lancamentos.elementAt(currItem));
		mostraTela(editaLancamento);
	}

	private void editaLancamentoCommand(Command command, EditaLancamento tela){
		if(command == EditaLancamento.CMD_OK){
			if(inserindo){
				lancamentos.addElement(tela.getLancamento());
			}else lancamentos.setElementAt(tela.getLancamento(), currItem);
			novoLancamento();
		} else if(command == EditaLancamento.CMD_CANCEL) {
			inserindo = false;
			mostraTela(lista);
		}
		
	}
	
	private String editaNome() {
		return "Novo";
	}

	private void excluiLancamento(int index) {
		lancamentos.removeElementAt(index);
		lista.repaint();
	}

	private void exporta() {
		new Persistencia().executa(lancamentos, Persistencia.EXPORTAR, LANCAMENTOS, new Lancamento(), lista);
	}
	
	private void gruposCommand(Command command, Displayable tela) {		
		if(command == cmdSelecionar){
			grupo = new RichList(this, lancamentos, new RichLancamento(new Hashtable()));
			grupo.addCommand(marcar);
			grupo.addCommand(cancelar);
			grupo.setTitle(grupos.elementAt(0).toString());
			mostraTela(grupo);
		} else if(command == cancelar){
			mostraTela(lista);
		} else if (command ==  cmdExcluir){
			grupos.removeElementAt(telaGrupos.getSelected());
		} else if (command ==  cmdInserir){
			grupos.addElement(editaGrupo(new Grupo(editaNome(), new Vector())));
		}
	}

	private void importa() {
		new Persistencia().executa(lancamentos, Persistencia.IMPORTAR, LANCAMENTOS, new Lancamento(), lista);
		lista.setItems(lancamentos);
	}

	private void lancamentosCommand(Command command, Displayable tela) {
		if(command == cmdInserir) novoLancamento();
		else if(command == cmdEditar) editaLancamento(lista.getSelected());
		else if(command == cmdExcluir) excluiLancamento(lista.getSelected());
		
	}

	private void listaGrupos() {
		modo = GRUPO;
		//if(view == null) view = new Vector();
		if(view != null) grupos = view;
		telaGrupos = new RichList(this, grupos, new RichStringItem());
		telaGrupos.addCommand(cmdInserir);
		telaGrupos.addCommand(cmdOpcoes);
		mostraTela(telaGrupos);		
	}
	
	private void listaLancamentos() {
		modo = LANCAMENTO;
		lista = new RichList(this, lancamentos, new RichLancamento());
		lista.addCommand(cmdExportar);
		lista.addCommand(cmdImportar);
		lista.addCommand(cmdExcluir);
		lista.addCommand(cmdEditar);
		lista.addCommand(cmdInserir);
		lista.addCommand(cmdOpcoes);
		mostraTela(lista);
	}

	private void listaOpcoes() {
		modo = OPCAO;
		lista =  new RichList(this, opcoes, new RichStringItem());
		lista.addCommand(cmdSelecionar);
		lista.addCommand(cmdSair);
		mostraTela(lista);
	}

	private void listaVisoes() {
		view = null;
		modo = VISAO;
		lista =  new RichList(this, viewNames, new RichStringItem());
		lista.addCommand(cmdInserir);
		lista.addCommand(cmdExcluir);
		lista.addCommand(cmdGrupos);
		lista.addCommand(cmdOpcoes);
		mostraTela(lista);
	}
	
	private void mostraTela(Displayable tela) {
		if(tela != null) telaAtual = tela;
		if(telaAtual == null | display == null) return;
		display.setCurrent(telaAtual);
		telaAtual.setCommandListener(this);
		if(telaAtual instanceof Canvas)((Canvas)telaAtual).repaint();
	}
	
	private void novoLancamento(){
		inserindo = true;
		Lancamento lancamento = new Lancamento();
		editaLancamento.setLancamento(lancamento);/*
		mostraTela(editaLancamento);
		lancamentos.addElement(lancamento);
		currItem = lancamentos.size()-1;//*/
		editaLancamento.setLancamento(lancamento);
		mostraTela(editaLancamento);
	}

	private void opcoesCommand(Command command, Displayable tela) {
		
		if(command == cmdSelecionar){
			switch (lista.getSelected()) {
			case 0:	listaLancamentos();break;
			case 1:	listaVisoes();break;
			case 2:	listaGrupos();break;
			}
		}
	}

	private void removeView(int selected) {
		String viewName = (String) viewNames.elementAt(selected);
		viewNames.removeElementAt(selected);
		views.remove(viewName);
	}

	private void viewsCommand(Command command, Displayable tela) {
		if(command == cmdInserir){
			createView(editaNome());
		} else if(command == cmdGrupos){
			view =  (Vector) views.get(lista.getItem((lista.getSelected())));
			listaGrupos();
		} else if(command == cmdExcluir){
			removeView(lista.getSelected());
		}
	}

	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
		new Thread(new Runnable() {
			
			public void run() {
				try {
					Persist persist = new Persist();
					persist.store(LANCAMENTOS, lancamentos);
					persist.store(VISOES, viewNames);
					persist.store(GRUPOS, grupos);
					notifyDestroyed();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	protected void pauseApp() {	}

	protected void startApp() throws MIDletStateChangeException {
		if(Fluxo.getMIDlet() == null)Fluxo.setMIDlet(this);
		if(display == null) display = Display.getDisplay(this);
		if(telaAtual == null)listaOpcoes();
		else mostraTela(null);
	}

}
