import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import comum.graficos.ListListener;
import comum.graficos.RichList;
import comum.graficos.RichStringItem;


public class Financeiro extends MIDlet implements CommandListener, ListListener{

	private static final String INSEIR = "Inserir";
	private static final String EDITAR = "Editar";
	private static final String EXCLUIR = "Exluir";
	private static final String SAIR = "Sair";
	private static final String EXPORTAR = "Exportar";
	private static final String IMPORTAR = "Importar";
	private static final String GRUPOS = "Grupos";
	private static final String MARCAR = "Marcar";
	private static final String CANCELAR = "Cancelar";
	private Vector lancamentos = new Vector();
	private int currItem;
	private EditaLancamento editaLancamento = new EditaLancamento(this);
	private RichList lista = new RichList(this, lancamentos, new RichLancamento());
	private Display display;
	private Displayable telaAtual = lista;
	private Command inserir = new Command(INSEIR, Command.OK, 0);
	private Command editar = new Command(EDITAR, Command.ITEM, 0);
	private Command excluir = new Command(EXCLUIR, Command.ITEM, 0);
	private Command sair = new Command(SAIR, Command.EXIT, 0);
	private Command exportar = new Command(EXPORTAR, Command.ITEM, 1);
	private Command importar = new Command(IMPORTAR, Command.ITEM, 1);
	private Command grupos = new Command(GRUPOS, Command.ITEM, 1);
	private Grupos telaGrupos = new Grupos();
	private Command marcar = new Command(MARCAR, Command.ITEM, 0);
	private Command cancelar = new Command(CANCELAR, Command.ITEM, 0);
	private RichList grupo;
	
	public Financeiro() {
		lista.addCommand(grupos);
		lista.addCommand(exportar);
		lista.addCommand(importar);
		lista.addCommand(excluir);
		lista.addCommand(editar);
		lista.addCommand(inserir);
		lista.addCommand(sair);
		lista.setCommandListener(this);
		telaGrupos.setCommandListener(this);
		//lista.setItems(items);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {}

	protected void pauseApp() {	}

	protected void startApp() throws MIDletStateChangeException {
		if(Fluxo.getMIDlet() == null)Fluxo.setMIDlet(this);
		if(display == null) display = Display.getDisplay(this);
		for(int i = 1; i<40;i++)lancamentos.addElement(new Lancamento("Lançamento"+i, 0, System.currentTimeMillis()));
		mostraTela(null);
	}
	
	private void novoLancamento(){
		Lancamento lancamento = new Lancamento();
		editaLancamento.setLancamento(lancamento);
		mostraTela(editaLancamento);
		lancamentos.addElement(lancamento);
		currItem = lancamentos.size()-1;
		editaLancamento.setLancamento(lancamento);
		mostraTela(editaLancamento);
	}

	private void mostraTela(Displayable tela) {
		if(tela != null) telaAtual = tela;
		display.setCurrent(telaAtual);
		if(telaAtual instanceof Canvas)((Canvas)telaAtual).repaint();
	}

	public void commandAction(Command command, Displayable tela) {
		System.out.println(tela);
		if(tela instanceof EditaLancamento) 
			editaLancamentoCommand(command, (EditaLancamento) tela);
		if(tela instanceof Grupos)
			gruposCommand(command, (Grupos) tela);
		if(command == sair) notifyDestroyed();
		else if(command == inserir) novoLancamento();
		else if(command == editar) editaLancamento(lista.getSelected());
		else if(command == excluir) excluiLancamento(lista.getSelected());
		else if(command == exportar) new Persistencia(Persistencia.EXPORTAR, lancamentos);
		else if(command == grupos) mostraTela(telaGrupos);
		else if(command == importar) {
			new Persistencia(Persistencia.IMPORTAR, lancamentos);
		} 
		lista.repaint();
		
	}
	
	private void gruposCommand(Command command, Grupos tela) {
		
		if(command == Grupos.CMD_OK){
			grupo = new RichList(this, lancamentos, new RichLancamento(new Hashtable()));
			grupo.addCommand(marcar);
			grupo.addCommand(cancelar);
			grupo.setCommandListener(this);
			grupo.setTitle(telaGrupos.getSelectedGrupo());
			mostraTela(grupo);
		} else if(command == Grupos.CMD_CANCEL){
			mostraTela(lista);
		}
	}

	private void editaLancamentoCommand(Command command, EditaLancamento tela){
		if(command == EditaLancamento.CMD_OK){
			lancamentos.setElementAt(tela.getLancamento(), currItem);
			novoLancamento();
		} else if(command == EditaLancamento.CMD_CANCEL) mostraTela(lista);
		
	}


	private void excluiLancamento(int index) {
		lancamentos.removeElementAt(index);
		lista.repaint();
	}

	private void editaLancamento(int index) {
		currItem = index;
		editaLancamento.setLancamento((Lancamento) lancamentos.elementAt(currItem));
		mostraTela(editaLancamento);
	}

	public void clickItem(RichList list, int index) {
		if(list == grupo){ 
			((RichLancamento)list.getRichItem()).toggle((Lancamento) lancamentos.elementAt(index));
			list.repaint();
		}
		if(list == lista)
			;
	}

}
