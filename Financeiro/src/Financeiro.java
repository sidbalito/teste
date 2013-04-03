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
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import comum.StringsHashtable;
import comum.StringsVector;
import comum.graficos.ListListener;
import comum.graficos.RichItem;
import comum.graficos.RichList;
import comum.graficos.RichStringItem;


public class Financeiro extends MIDlet implements CommandListener, ListListener{

	private static final String PERIODO_PREFIX = "periodo.";
	private static final String VISAO_PREFIX = "visao.";
	private static final int NOME_VISAO = 7;
	private static final int NOME_GRUPO = 6;
	private static final int GRUPO_LANCAMENTO = 5;
	private static final int UNICO_REC = 3;
	private static final String CANCELAR = "Cancelar";
	private static final String EDITAR = "Editar";
	private static final String EXCLUIR = "Excluir";
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
	private static Object VAZIO = new Object();
	
	
	private static final int OPCAO = 0;			//Lista de opções
	private static final int LANCAMENTO = 1;	//Lista de lançamentos
	private static final int GRUPO = 2;			//Lista de grupos
	private static final int VISAO = 3;			//Lista de visões
	private static final int PERIODO = 4;
	private static final String SOMAR = "Somar";
	
	/*
	 * Comandos disponíveis
	 */
	//private Command cancelar = new Command(GRUPOS, Command.ITEM, 0);
	private Command cmdEditar = new Command(EDITAR, Command.ITEM, 0);
	private Command cmdExcluir = new Command(EXCLUIR, Command.ITEM, 0);
	private Command cmdExportar = new Command(EXPORTAR, Command.ITEM, 1);
	private Command cmdImportar = new Command(IMPORTAR, Command.ITEM, 1);
	private Command cmdInserir = new Command(INSERIR, Command.OK, 0);
	private Command cmdOpcoes = new Command(OPCOES, Command.ITEM, 0);
	private Command cmdSair = new Command(SAIR, Command.EXIT, 0);
	private Command cmdSelecionar =  new Command(SELECIONAR, Command.ITEM, 0);
	private Command cmdVisoes = new Command(VISOES, Command.ITEM, 0);
	private Command cmdMarcar = new Command(MARCAR, Command.ITEM, 0);
	private Command cmdOk = new Command(OK, Command.ITEM, 0);
	private Command cmdCancelar = new Command(CANCELAR, Command.ITEM, 0);
	private Command cmdSomar = new Command(SOMAR, Command.ITEM, 0);
	private Command cmdGrupos = new Command(GRUPOS, Command.ITEM, 0);

	
	private int currItem;							//Lançamento atual
	private Display display;						//Componente display
	private EditaLancamento editaLancamento;		//Tela de edição de lançamentos
	private RichList grupo;							//Lista de lançamentos associados ao grupo atual
	private Vector grupos  = new Vector();			//Lista de grupos da visão atual
	private Vector lancamentos = new Vector();		//Lista de lançamentos
	private RichList lista;							//
	private int modo = OPCAO;						//
	private Vector opcoes = new Vector();			//
	private Displayable telaAtual = lista;			//
	private RichList telaGrupos;					// = new Grupos(grupos);
//	private Vector view = new Vector();							//
	//private Vector viewNames = new Vector();		//Lista com nomes das visões
	private Hashtable views = new Hashtable();		//Lista de visões com os respectivos grupos
	private Hashtable gruposVisao;					//
	private boolean inserindo;						//
	private Hashtable viewTable = new Hashtable();	//
	private RichLancamento rich;					//
	private String grupoAtual, visaoAutal;						//
	private long data = System.currentTimeMillis();	//
	private Hashtable descricoes;					//
	private Vector listaPeriodos = new Vector();					//
	private boolean destroying = false;
	private Persist persist = new Persist();
	/**
	 * 
	 */
	public Financeiro() {
		load();
		opcoes.addElement(LANCAMENTOS);
		opcoes.addElement(VISOES);
		opcoes.addElement(GRUPOS);
		opcoes.addElement(PERIODOS);
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
	/**/
	private void carrega() throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		//listaPeriodos = new StringsVector(persist.load(DADOS, PERIODOS_REC)).getVector(); //Carrega lista de períodos disponíveis
		/*viewNames = new StringsVector(persist.load(DADOS, VISOES_REC)).getVector();  //Carrega lista de visões disponíveis
		if(!(viewNames.size()>0))
		visaoAutal = (String) views.keys();//*/
		String[] registros = RecordStore.listRecordStores();
		if(registros != null)
			for(int i = 0; i < registros.length; i++)prefixados(registros[i]);
		visaoAutal = PADRAO;
		//novoGrupo("G1");
		//viewTable = new StringsHashtable(persist.load(visaoAutal, UNICO_REC)).getHashtable();
		//viewNames = values(views);
//		views.put(visaoAutal, view);
		//System.out.println(view);
		//viewTable = new StringsHashtable(string)
		//persist.load(LANCAMENTOS, lancamentos, new Lancamento());
	}

	private void prefixados(String prefixado) {
		if(prefixado.startsWith(VISAO_PREFIX)) createView(semPrefixo(prefixado, VISAO_PREFIX));
		else if(prefixado.startsWith(PERIODO_PREFIX)) addPeriodo(semPrefixo(prefixado, PERIODO_PREFIX));
	}

	private String semPrefixo(String string, String prefix) {
		return string.substring(prefix.length());
	}

	/**
	 * 
	 */
	public void clickItem(RichList list, int index) {
		System.out.println("ClickItem.Modo: "+modo);
		//if(list instanceof RichList) commandAction(cmdSelecionar, list);
		switch(modo){
		case LANCAMENTO: commandAction(cmdEditar, telaAtual); break;
		case GRUPO: commandAction(cmdSelecionar, telaAtual); break;
		case GRUPO_LANCAMENTO: commandAction(cmdMarcar, telaAtual); break;
		case PERIODO:
		case VISAO:
		case OPCAO: commandAction(cmdSelecionar, list); break;
		}
	}

	/**
	 * 	
	 */
	public void commandAction(Command command, Displayable tela) {
		//System.out.println("CommandAction");
		if(command == cmdSair)
			try {
				destroyApp(false);
			} catch (MIDletStateChangeException e) {
				e.printStackTrace();
			}
		else if(command == cmdExportar) exporta();
		else if(command == cmdImportar) importa();
		else if(command == cmdVisoes) listaVisoes();
		else if(command == cmdOpcoes) listaOpcoes();
		else if(command == cmdGrupos)listaGrupos();
//		else if(command == cmdOk) novoGrupo(((EditaNome)tela).getNome());
		
		switch (modo) {
		case GRUPO_LANCAMENTO: grupoLancamentosCommand(command, tela);break;
		case OPCAO: opcoesCommand(command, tela);break;
		case LANCAMENTO: lancamentosCommand(command, tela);break;
		case GRUPO: gruposCommand(command, tela);break;
		case VISAO: try {
				viewsCommand(command, tela);
			} catch (Exception e) {
				e.printStackTrace();
			}break;
		case PERIODO: periodoCommand(command, tela);break;
		case NOME_VISAO:
		case NOME_GRUPO:{
			nomeCommand(command, tela);
		}break;
		}
		if(tela instanceof EditaLancamento) 
			editaLancamentoCommand(command, (EditaLancamento) tela);
		mostraTela(telaAtual);
		
	}
	
	private void grupoLancamentosCommand(Command command, Displayable tela) {
		if(command == cmdCancelar)listaGrupos();
		else if(command == cmdMarcar)marcaItem();
		
	}

	private void nomeCommand(Command command, Displayable tela) {
		switch(modo){
		case NOME_GRUPO:
			if(command == cmdCancelar)listaGrupos();
			else if(command == cmdOk){
				String nome = ((EditaNome)tela).getNome();
				System.out.println(grupos);
				if(inserindo) novoGrupo(nome);
				else grupos.setElementAt(nome, telaGrupos.getSelected());
			}break;
		case NOME_VISAO:
			if(command == cmdCancelar)listaVisoes();
			else if(command == cmdOk){
				String nome = ((EditaNome)tela).getNome();
				String nomeAntigo = (String) lista.getItem((lista.getSelected()));
				if(inserindo) createView(nome);
				else renameView(nomeAntigo, nome);
				listaVisoes();
			}break;
		}
	}

	/**
	 * 
	 * @param command
	 * @param tela
	 */
	private void periodoCommand(Command command, Displayable tela){
		if(command == cmdSelecionar){
			String periodo = listaPeriodos.elementAt(lista.getSelected()).toString();
			try {
				armazenaLancamentos();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
//				armazenaLancamentos();
				lancamentos = new StringsVector(persist.load(prefixo(periodo, PERIODO), UNICO_REC)).getVector();
				converteLancamentos();
				listaLancamentos();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void converteLancamentos() {
		for(int i = 0; i<lancamentos.size(); i++){
			Object lancamento = lancamentos.elementAt(i);
			if(lancamento instanceof Lancamento) continue;
			lancamentos.setElementAt(new Lancamento(lancamento.toString()), i);
		}
	}

	/**
	 * Cria um novo grupo
	 * @param nome o nome do grupo a ser criado
	 */
	private void novoGrupo(String nome) {
//		grupos.addElement(nome);
//		System.out.println(grupos);
//		System.out.println("Financeiro.novoGrupo" );
		viewTable.put("", nome);
		listaGrupos();
	}

	/**
	 * 
	 */
	private void marcaItem() {
		//System.out.println("MarcaItem: "+grupoAtual);
		rich.toggle((Lancamento) lancamentos.elementAt(grupo.getSelected()), grupoAtual);
		System.out.println(viewTable);
		mostraTela(telaAtual);
	}

	private void createView(String name) {
		if(name == null) return;
		if(!views.containsKey(name))views.put(name, VAZIO);
		if(visaoAutal != null & viewTable.size()>0){
			try {
				armazenaVisao(visaoAutal);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		viewTable.clear();
		visaoAutal = name;
	}
	
	private void renameView(String name, String newName){
		if(name == null|newName == null) return;
		views.put(newName, views.get(name));
		views.remove(name);
		listaVisoes();
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
				addPeriodo(Util.mesAno(data));
 				lancamentos.addElement(lancamento);
				novoLancamento();
			}else {
				lancamentos.setElementAt(tela.getLancamento(), currItem);
				currItem++;
				if(currItem < (lancamentos.size()))editaLancamento(currItem);
				else novoLancamento();
			}
		} else if(command == EditaLancamento.CMD_CANCEL) {
			inserindo = false;
			mostraTela(lista);
		}
		
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
			System.out.println("GruposCommand"+grupos);
		
		if(command == cmdSelecionar){
			rich = new RichLancamento(viewTable);
			grupo = new RichList(this, lancamentos, rich);
			grupo.addCommand(cmdMarcar);
			grupo.addCommand(cmdOpcoes);
			grupo.addCommand(cmdCancelar);
			grupoAtual = grupos.elementAt(telaGrupos.getSelected()).toString();
			RichLancamento.setGrupo(grupoAtual);
			grupo.setTitle(grupoAtual);
			//telas.push(telaAtual);
			mostraTela(grupo);
			modo = GRUPO_LANCAMENTO;
		} else if(command == cmdCancelar){
			listaGrupos();
			modo = GRUPO;
		} else if (command ==  cmdExcluir){
			grupos.removeElementAt(telaGrupos.getSelected());
		} else if (command ==  cmdInserir){
			renomeia(true, NOME_GRUPO);
		} else if (command == cmdSomar){
			listaGrupos(true);
		} else if (command == cmdEditar){
			renomeia(false, NOME_GRUPO);
		}
	}

	private void listaGrupos() {
		listaGrupos(false);		
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
	 * @param pInserir 
	 * @param modo 
	 */
	private void renomeia(boolean pInserir, int modo) {
		inserindo = pInserir;
		this.modo = modo;
		EditaNome edita = new EditaNome();
		edita.addCommand(cmdOk);
		edita.addCommand(cmdCancelar);
		edita.setCommandListener(this);
		mostraTela(edita);
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
	 * @param somar 
	 */
	private void listaGrupos(boolean somar) {
		modo = GRUPO;
		//if(view == null) view = new Vector();
//		viewTable = new StringsHashtable("{L1=G1, L2=G2}");
		System.out.println(viewTable);
		grupos = new HashtableVector(viewTable, HashtableVector.VALUES);
		System.out.println(grupos);
		RichItem rich;
		if(somar)
			rich = new RichGrupo(lancamentos, viewTable);
		else
			rich = new RichStringItem();
		telaGrupos = new RichList(this, grupos, rich);
		telaGrupos.addCommand(cmdInserir);
		telaGrupos.addCommand(cmdOpcoes);
		telaGrupos.addCommand(cmdSomar);
		telaGrupos.addCommand(cmdEditar);
		telaGrupos.addCommand(cmdVisoes);
		telaGrupos.addCommand(cmdExcluir);
		System.out.println("ListaGrupos.modo: "+modo);
		mostraTela(telaGrupos);		
	}
	
	/**
	 *Lista os lançamentos de um período dentro ou fora de um grupo.
	 */
	private void listaLancamentos() {
		modo = LANCAMENTO;
		//System.out.println("Lançamentos: "+lancamentos);
		lista = new RichList(this, lancamentos, new RichLancamento());
		lista.addCommand(cmdExportar);
		lista.addCommand(cmdImportar);
		lista.addCommand(cmdExcluir);
		lista.addCommand(cmdEditar);
		lista.addCommand(cmdInserir);
		lista.addCommand(cmdGrupos );
		lista.addCommand(cmdOpcoes);
		mostraTela(lista);
	}

	private void listaPeriodos() {
		modo = PERIODO;
		//System.out.println(listaPeriodos);
		lista = new RichList(this, listaPeriodos, new RichStringItem());
		lista.addCommand(cmdSelecionar);
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
//		view = null;
		modo = VISAO;
		//System.out.println("listaVisoes: "+viewNames);
		Vector viewNames = new HashtableVector(views, HashtableVector.KEYS);
		if(viewNames.size() == 0)createView(PADRAO);
		lista =  new RichList(this, viewNames, new RichStringItem());
		lista.addCommand(cmdInserir);
		lista.addCommand(cmdExcluir);
		lista.addCommand(cmdSelecionar);
		lista.addCommand(cmdEditar);
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
		//System.out.println(telaAtual);
		telaAtual.setCommandListener(this);
		if(telaAtual instanceof Canvas)((Canvas)telaAtual).repaint();
		System.out.println("MostraTela.modo: "+modo);
	}
	
	/**
	 * Insere um novo lançamento
	 */
	private void novoLancamento(){
		inserindo = true;
		Lancamento lancamento = new Lancamento("", 0, data);
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
			case 3: listaPeriodos();break;
			}
		}
	}

	/**
	 * Remove uma visão da lista de visões
	 * @param selected visão que deve ser removida
	 * @throws RecordStoreException 
	 * @throws RecordStoreNotOpenException 
	 */
	private void removeView(int selected) throws RecordStoreNotOpenException, RecordStoreException {
		String viewName = (String) lista.getItem(selected);
		if(visaoAutal == viewName)visaoAutal = null;
		lista.removeItem(selected);
		views.remove(viewName);
		persist.remove(prefixo(viewName, VISAO));
	}

	/**
	 * Executa comandos associados à lista de visões
	 * @param command comando a ser executado
	 * @param tela tela atual
	 * @throws RecordStoreException 
	 * @throws RecordStoreNotOpenException 
	 */
	private void viewsCommand(Command command, Displayable tela) throws RecordStoreNotOpenException, RecordStoreException {
		System.out.println("viewsCommand");
		if(command == cmdInserir){
			renomeia(true, NOME_VISAO);
			//createView(editaNome(VISOES));
		} else if(command == cmdSelecionar){
			if(visaoAutal != null & viewTable.size()>0){
				armazenaVisao(visaoAutal);
			}
			visaoAutal = (String) lista.getItem((lista.getSelected()));
			String string = persist.load(prefixo(visaoAutal, VISAO), UNICO_REC);
			viewTable = new StringsHashtable(string);
			listaGrupos();
		} else if(command == cmdExcluir){
			removeView(lista.getSelected());
		} else if(command == cmdEditar){
			renomeia(false, NOME_VISAO);
			
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
//			persist.store(DADOS, VISOES_REC, viewNames.toString());
			armazenaVisao(visaoAutal);
			armazenaLancamentos();
//			persist.store(DADOS, PERIODOS_REC, listaPeriodos.toString());
			//persist.store(LANCAMENTOS, lancamentos);
			storeHash(persist, gruposVisao, VISOES);
			storeHash(persist, descricoes, GRUPOS);
			persist.store(GRUPOS, grupos);
			//storeVector(PERIODOS, listaPeriodos, persist);
	}

	private void armazenaVisao(String visao) throws RecordStoreFullException,
			RecordStoreNotFoundException, RecordStoreException {
		System.out.println("ArmazenaVisão: "+viewTable);
		if(visao == null) return;
		persist.store(prefixo(visao, VISAO), UNICO_REC, viewTable.toString());
	}
	
	private void armazenaLancamentos() throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		Hashtable periodos = new Hashtable();
		for(int i = 0; i < lancamentos.size(); i++){
			Lancamento lancamento = (Lancamento) lancamentos.elementAt(i);
			String periodo = Util.mesAno(lancamento.getData());
			Vector vector = (Vector) periodos.get(periodo);
			if(vector == null){
				vector = new Vector(); 
				periodos.put(periodo, vector);
			}
			vector.addElement(lancamento);
		}
		Enumeration keys = periodos.keys();
		while (keys.hasMoreElements()) {
			String periodo = keys.nextElement().toString();
			System.out.println(periodos.get(periodo));
			persist.store(prefixo(periodo, PERIODO), UNICO_REC, periodos.get(periodo).toString());
			//System.out.println(persist.load(periodo, UNICO_REC));
			addPeriodo(periodo);
		}
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
				String key = (String) keys.nextElement();
				persist.writeRecord(section, key);
				Vector vector = (Vector) hash.get(key);
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
	 * Adiciona o mês à lista de períodos, sem duplicidade
	 * @param periodo
	 */
	private void addPeriodo(String periodo){
		if(periodo == null)return;
		if(!listaPeriodos.contains(periodo))listaPeriodos.addElement(periodo);
	}
	
	private String prefixo(String nome, int codPrefixo){
		StringBuffer prefixo = new StringBuffer();
		prefixo.append(prefixo(codPrefixo));
		
		return prefixo.append(nome).toString();
	}

	private String prefixo(int codPrefixo) {
		switch(codPrefixo){
		case VISAO: return VISAO_PREFIX;
		case PERIODO: return PERIODO_PREFIX;
		}
		return "";
	}
}