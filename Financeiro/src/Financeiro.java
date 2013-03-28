import java.util.Enumeration;
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
	private static final String IMPORTAR = "Importar";
	private static final String INSERIR = "Inserir";
	private static final String LANCAMENTOS = "Lançamentos";
	private static final String PERIODOS = "Periodos";
	private static final String MARCAR = "Marcar";
	private static final String PADRAO = "Padrão";
	private static final String SAIR = "Sair";
	private static final String SELECIONAR = "Selecionar";
	private static final String VISOES = "Visões";
	static final String GRUPOS = "Grupos";
	private static final String OPCOES = "Opções";
	private static final String OK = "Ok";
	
	//Códigos de listas
	private static final int OPCAO = 0;			//Lista de opções
	private static final int LANCAMENTO = 1;	//Lista de lançamentos
	private static final int GRUPO = 2;			//Lista de grupos
	private static final int VISAO = 3;			//Lista de visões
	
	/*
	 * Comandos disponíveis
	 */
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
	private Command cmdMarcar = new Command(MARCAR, Command.ITEM, 0);
	private Command cmdOk = new Command(OK, Command.ITEM, 0);
	private Command cmdCancelar = new Command(CANCELAR, Command.ITEM, 0);

	
	private int currItem;							//Lançamento atual
	private Display display;						//Componente display
	private EditaLancamento editaLancamento;		//Tela de edição de lançamentos
	private RichList grupo;							//Lista de lançamentos associados ao grupo atual
	private Vector grupos = new Vector();			//Lista de grupos da visão atual
	private Vector lancamentos = new Vector();		//Lista de lançamentos
	private RichList lista;							//
	private int modo = OPCAO;						//
	private Vector opcoes = new Vector();			//
	private Displayable telaAtual = lista;			//
	private RichList telaGrupos;					// = new Grupos(grupos);
	private Vector view;							//
	private Vector viewNames = new Vector();		//Lista com nomes das visões
	private Hashtable views = new Hashtable();		//Lista de visões com os respectivos grupos
	private Hashtable gruposVisao;					//
	private boolean inserindo;						//
	private Hashtable viewTable = new Hashtable();	//
	private RichLancamento rich;					//
	private String grupoAtual;						//
	private long data = System.currentTimeMillis();	//
	private Hashtable descricoes;					//
	private Vector listaPeriodos;					//
	private boolean destroying = false;
	
	/**
	 * 
	 */
	public Financeiro() {
		load();
		createView(PADRAO);
		opcoes.addElement(LANCAMENTOS);
		opcoes.addElement(VISOES);
		opcoes.addElement(GRUPOS);
		//lista.setItems(items);
	}

	/**
	 * 
	 */
	private void load() {
		new Thread(new Runnable() {
			
			public void run() {
				try {
					carrega();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Carrega dados do sistema de registro do celular 
	 * @throws RecordStoreFullException
	 * @throws RecordStoreNotFoundException
	 * @throws RecordStoreException
	 **/
	private void carrega() throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		Persist persist = new Persist();
		persist.load(LANCAMENTOS, lancamentos, new Lancamento());
		viewNames = loadVector(VISOES, persist);
		criaPeriodos();
		listaPeriodos = loadVector(PERIODOS, persist);
		System.out.println(listaPeriodos);
	}
	
	/**
	 * 
	 **/
	private void criaPeriodos() {
		listaPeriodos = new Vector();
//		addPeriodo(Util.mesAno(System.currentTimeMillis()));
//		addPeriodo(Util.mesAno(System.currentTimeMillis()));
		//listaPeriodos.addElement(Util.mesAno(System.currentTimeMillis()));
		try {
			storeVector(PERIODOS, listaPeriodos, new Persist());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clickItem(RichList list, int index) {
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
		else if(command == cmdMarcar)marcaItem();
		else if(command == cmdExportar) exporta();
		else if(command == cmdImportar) importa();
		else if(command == cmdVisoes) listaVisoes();
		else if(command == cmdOpcoes) listaOpcoes();
		else if(command == cmdOk)novoGrupo(((EditaNome)tela).getNome());
		
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

	/**
	 * Cria um novo grupo
	 * @param nome o nome do grupo a ser criado
	 */
	private void novoGrupo(String nome) {
		grupos.addElement(nome);
		System.out.println("Financeiro.novoGrupo");
		mostraTela(telaAtual);
	}

	/**
	 * 
	 */
	private void marcaItem() {
		rich.toggle((Lancamento) lancamentos.elementAt(grupo.getSelected()), grupoAtual);
		mostraTela(telaAtual);
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
				Lancamento lancamento = tela.getLancamento();
				data = lancamento.getData();
 				lancamentos.addElement(lancamento);
			}else lancamentos.setElementAt(tela.getLancamento(), currItem);
			novoLancamento();
		} else if(command == EditaLancamento.CMD_CANCEL) {
			inserindo = false;
			mostraTela(lista);
		}
		
	}
	
	/**
	 * Exibe tela de edição de nome
	 * @param titulo título da tela
	 * @return nome digitado pelo usuario
	 */
	private String editaNome(String titulo) {
		return "Nome";
	}

	/**
	 * Exclui um lançamento
	 * @param index indíce do lançamento
	 */
	private void excluiLancamento(int index) {
		lancamentos.removeElementAt(index);
		lista.repaint();
	}

	/**
	 * Exporta lançamentos para o arquivo
	 */
	private void exporta() {
		new Persistencia().executa(lancamentos, Persistencia.EXPORTAR, LANCAMENTOS, new Lancamento(), lista);
	}
	
	/**
	 * Executa comandos associados à lista de grupos
	 * @param command comando a ser executado
	 * @param tela tela atual
	 */
	private void gruposCommand(Command command, Displayable tela) {		
		if(command == cmdSelecionar){
			rich = new RichLancamento(viewTable );
			grupo = new RichList(this, lancamentos, rich);
			grupo.addCommand(cmdMarcar);
			grupo.addCommand(cancelar);
			grupoAtual = grupos.elementAt(telaGrupos.getSelected()).toString();
			grupo.setTitle(grupoAtual);
			mostraTela(grupo);
		} else if(command == cancelar){
			mostraTela(lista);
		} else if (command ==  cmdExcluir){
			grupos.removeElementAt(telaGrupos.getSelected());
		} else if (command ==  cmdInserir){
			insereGrupo();
		}
	}

	/**
	 * Importa lançamentos do arquivo
	 */
	private void importa() {
		new Persistencia().executa(lancamentos, Persistencia.IMPORTAR, LANCAMENTOS, new Lancamento(), lista);
		lista.setItems(lancamentos);
	}

	/**
	 * Insere um grupo na visão
	 */
	private void insereGrupo() {
		EditaNome edita = new EditaNome();
		edita.addCommand(cmdOk);
		edita.addCommand(cmdCancelar);
		edita.setCommandListener(this);
		display.setCurrent(edita);
	}

	/**
	 * Executa comandos associados à lista de lançamentos 
	 * @param command comando que será executado
	 * @param tela tela atual
	 */
	private void lancamentosCommand(Command command, Displayable tela) {
		if(command == cmdInserir) novoLancamento();
		else if(command == cmdEditar) editaLancamento(lista.getSelected());
		else if(command == cmdExcluir) excluiLancamento(lista.getSelected());
		
	}

	/**
	 * Lista os grupos dentro ou fora de uma visão
	 */
	private void listaGrupos() {
		modo = GRUPO;
		//if(view == null) view = new Vector();
		if(view != null) grupos = view;
		telaGrupos = new RichList(this, grupos, new RichStringItem());
		telaGrupos.addCommand(cmdInserir);
		telaGrupos.addCommand(cmdOpcoes);
		mostraTela(telaGrupos);		
	}
	
	/**
	 *Lista os lançamentos de um período dentro ou fora de um grupo.
	 */
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

	/**
	 * Lista as opções disponíveis
	 */
	private void listaOpcoes() {
		modo = OPCAO;
		lista =  new RichList(this, opcoes, new RichStringItem());
		lista.addCommand(cmdSelecionar);
		lista.addCommand(cmdSair);
		mostraTela(lista);
	}

	/**
	 * Lista as visões disponíveis
	 */
	private void listaVisoes() {
		view = null;
		modo = VISAO;
		lista =  new RichList(this, viewNames, new RichStringItem());
		lista.addCommand(cmdInserir);
		lista.addCommand(cmdExcluir);
		lista.addCommand(cmdSelecionar);
		lista.addCommand(cmdOpcoes);
		mostraTela(lista);
	}
	
	/**
	 * Mostra a tela
	 * @param tela tela a ser mostrada
	 */
	private void mostraTela(Displayable tela) {
		if(tela != null) telaAtual = tela;
		if(telaAtual == null | display == null) return;
		display.setCurrent(telaAtual);
		System.out.println(telaAtual);
		telaAtual.setCommandListener(this);
		if(telaAtual instanceof Canvas)((Canvas)telaAtual).repaint();
	}
	
	/**
	 * Insere um novo lançamento
	 */
	private void novoLancamento(){
		inserindo = true;
		Lancamento lancamento = new Lancamento("", 0, data );
		editaLancamento.setLancamento(lancamento);/*
		mostraTela(editaLancamento);
		lancamentos.addElement(lancamento);
		currItem = lancamentos.size()-1;
		editaLancamento.setLancamento(lancamento);//*/
		mostraTela(editaLancamento);
	}

	/**
	 * Executa comandos associados à lista de opções
	 * @param command comando que será executado
	 * @param tela rela atual
	 */
	private void opcoesCommand(Command command, Displayable tela) {
		
		if(command == cmdSelecionar){
			switch (lista.getSelected()) {
			case 0:	listaLancamentos();break;
			case 1:	listaVisoes();break;
			case 2:	listaGrupos();break;
			}
		}
	}

	/**
	 * Remove uma visão da lista de visões
	 * @param selected visão que deve ser removida
	 */
	private void removeView(int selected) {
		String viewName = (String) viewNames.elementAt(selected);
		viewNames.removeElementAt(selected);
		views.remove(viewName);
	}

	/**
	 * Executa comandos associados à lista de visões
	 * @param command comando a ser executado
	 * @param tela tela atual
	 */
	private void viewsCommand(Command command, Displayable tela) {
		if(command == cmdInserir){
			createView(editaNome(VISOES));
		} else if(command == cmdSelecionar){
			view =  (Vector) views.get(lista.getItem((lista.getSelected())));
			listaGrupos();
		} else if(command == cmdExcluir){
			removeView(lista.getSelected());
		}
	}

	/**
	 * 
	 */
	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
		if(destroying)return;
		destroying = true;
		new Thread(new Runnable() {
			
			public void run() {
				try {
					armazena();
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					notifyDestroyed();
				}
			}

		}).start();
	}

	/**
	 * Eexecuta rotina de armazenamento.
	 * @throws RecordStoreFullException
	 * @throws RecordStoreNotFoundException
	 * @throws RecordStoreException
	 */
	private void armazena() throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
			Persist persist = new Persist();
			persist.store(LANCAMENTOS, lancamentos);
			storeHash(persist, gruposVisao, VISOES);
			storeHash(persist, descricoes, GRUPOS);
			persist.store(GRUPOS, grupos);
			storeVector(PERIODOS, listaPeriodos, persist);
	}
	
	/**
	 * Armazena um Hashtable na camada de persistêcia
	 * @param persist instância da classe Persist
	 * @param hash instância da classe Hashtable a ser armazenada
	 * @param section nome da seção onde será armazenado o Hashtable
	 * @throws RecordStoreFullException
	 * @throws RecordStoreNotFoundException
	 * @throws RecordStoreException
	 */
	private void storeHash(Persist persist, Hashtable hash, String section) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
			if(hash == null)return;
			Enumeration keys = hash.keys();
			while (keys.hasMoreElements()) {
				String visao = (String) keys.nextElement();
				persist.writeRecord(section, visao);
				Vector vector = (Vector) hash.get(visao);
				StringBuffer sb = new StringBuffer();
				for(int i = 0; i < vector.size(); i++){
					sb.append(vector.elementAt(i));
					sb.append(';');
				}
				persist.writeRecord(section, sb.toString());
			}
	}
	
	protected void pauseApp() {	}

	/**
	 * 
	 */
	protected void startApp() throws MIDletStateChangeException {
		if(Fluxo.getMIDlet() == null)Fluxo.setMIDlet(this);
		if(display == null) display = Display.getDisplay(this);
		editaLancamento = new EditaLancamento(this, display);
		if(telaAtual == null)listaOpcoes();
		else mostraTela(null);
	}
	
	/**
	 * Armazena um vetor na camada de persistência (Classe Persist)
	 * @param section nome da seção
	 * @param vector vetor que será armazenado
	 * @param persist instância da classe Persist
	 * @throws RecordStoreFullException
	 * @throws RecordStoreNotFoundException
	 * @throws RecordStoreException
	 */
	private void storeVector( String section, Vector vector,Persist persist) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		for(int i = 0; i < vector.size(); i++){
			//System.out.println("Storing "+vector.elementAt(i));
			persist.writeRecord(section, vector.elementAt(i).toString());
		}		
	}
	
	/**
	 * Carrega um vetor através da camada de persistência (Classe Persist)
	 * @param section nome da seção
	 * @param persist instância da classe Persist
	 * @return
	 * @throws RecordStoreFullException
	 * @throws RecordStoreNotFoundException
	 * @throws RecordStoreException
	 */
	private Vector loadVector(String section, Persist persist) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		int numRec = persist.numRecords(section);
		Vector vector = new Vector();
//		System.out.println(numRec);
		if(numRec > 0)
			for(int i = 1; i <= numRec; i += 2) vector.addElement(persist.readRecord(i, section));
//		System.out.println("Vector: "+vector.size());
		return vector;		
	}
	
	/**
	 * Adiciona o mês à lista de períodos, sem duplicidade
	 * @param periodo
	 */
	private void addPeriodo(String periodo){
		if(!listaPeriodos.contains(periodo))listaPeriodos.addElement(periodo);
	}
}
