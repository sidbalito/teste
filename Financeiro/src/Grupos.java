import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;

import comum.graficos.RichList;
import comum.graficos.RichStringItem;

public class Grupos extends RichList{
	private static final String OK = "Ok";
	public static final Command CMD_OK = new Command(OK, Command.OK, 0);
	private static final String CANCEL = "Cancelar";
	public static final Command CMD_CANCEL = new Command(CANCEL, Command.CANCEL, 0);
	private static final int KEY_FIRE = -5;
	private static Vector grupos = new Vector();
	private static Hashtable tabela = new Hashtable();
	
	public Grupos() {
		super(null, grupos, new RichStringItem());
		setColor(Cores.AZUL_ESCURO, Cores.BRANCO);
		for(int i = 0;i<5;i++) grupos.addElement("Teste"+i);
		addCommand(CMD_OK);
		addCommand(CMD_CANCEL);
	}

	public String getSelectedGrupo() {
		return grupos.elementAt(getSelected()).toString();
	}
	
	
}
