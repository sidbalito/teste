import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;

import comum.graficos.RichList;
import comum.graficos.RichStringItem;

public class Grupos extends RichList{
	private static final String OK = "Ok";
	private static final String CANCEL = "Cancelar";
	private static final String INSERIR = "Inserir";
	private static final String ALTERAR = "Alterar";
	private static final String EXCLUIR = "Excluir";
	
	public static final Command CMD_OK = new Command(OK, Command.OK, 0);
	public static final Command CMD_CANCEL = new Command(CANCEL, Command.CANCEL, 0);
	public static final Command CMD_EXCLUIR = new Command(EXCLUIR, Command.ITEM, 0);  
	public static final Command CMD_ALTERAR = new Command(ALTERAR, Command.ITEM, 0);  
	public static final Command CMD_INSERIR = new Command(INSERIR, Command.ITEM, 0);
	
	private static Hashtable tabela = new Hashtable();
	
	public Grupos(Vector grupos) {
		super(null, grupos, new RichStringItem());
		setColor(Cores.AZUL_ESCURO, Cores.BRANCO);
		new Persistencia().executa(grupos, Persistencia.IMPORTAR, Financeiro.GRUPOS, new Grupo(), null);
		addCommand(CMD_OK);
		addCommand(CMD_CANCEL);
		addCommand(CMD_INSERIR);
		addCommand(CMD_EXCLUIR);
		addCommand(CMD_ALTERAR);
	}

	public String getSelectedGrupo() {
		return items.elementAt(getSelected()).toString();
	}
	
	
}
