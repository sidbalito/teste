import java.util.Date;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.Item;



public class EditaLancamento extends Form{

	private static final String DESCRICAO = "Descrição";
	private static final String VAZIO = "";
	private static final String VALOR = "Valor";
	private static final String DATA = "Data";
	private static final String OK = "OK";
	private static final String CANCEL = "Cancel";
	private static final String TITLE = "Financeiro";
	
	private TextField descricao = new TextField(DESCRICAO, VAZIO, 100, 0);
	private MoneyField valor = new MoneyField(VALOR, VAZIO, 8, 0);
	private DateField data = new DateField(DATA, DateField.DATE);//*/
	public static final Command CMD_OK =  new Command(OK, Command.OK, 0);
	public static final Command CMD_CANCEL = new Command(CANCEL, Command.CANCEL, 1);

	public EditaLancamento(CommandListener listener) {
		super(TITLE);
		append(descricao);
		append(valor);
		append(data);
		addCommand(CMD_OK);
		addCommand(CMD_CANCEL);
		setCommandListener(listener);
		setItemStateListener(new ItemStateListener() {
			
			public void itemStateChanged(Item item) {
				if(item instanceof FormatableField){
					((FormatableField) item).format();
				}
			}


		});

	}

	public void setLancamento(String descricao, float valor, long data) {
		this.descricao.setString(descricao);
		this.valor.setString(valor);
		this.data.setDate(new Date(data));
	}

	public Lancamento getLancamento() {
		return new Lancamento(descricao.getString(), valor.getValue(),
				data.getDate().getTime());
	}

	public void setLancamento(Lancamento lancamento) {
		setLancamento(lancamento.getDescricao(), lancamento.getValor(), lancamento.getData());
	}

}

interface FormatableField{
	public void format();
}

class MoneyField extends TextField implements FormatableField{

	public MoneyField(String label, String text, int maxSize, int constraints) {
		super(label, text, maxSize, constraints);
	}
	
	public float getValue(){
		StringBuffer text = new StringBuffer(getString());
		text.setCharAt(getString().indexOf(','), '.');
		System.out.println(text);
		return Float.parseFloat(text.toString());
	}

	public void setString(float valor) {
		String val = Float.toString(valor);
		StringBuffer text = new StringBuffer(val);
		text.setCharAt(val.indexOf('.'), ',');
		super.setString(text.toString());
		//format();
	}

	public void format() {
		StringBuffer sb = new StringBuffer();
		String text = getString();
		int i, len = text.length();
		for(i = 0; i < 3-len; i++) sb.append(0);
		int lastZero = -1;
		for(i = 0; i < len; i++){
			char ch = text.charAt(i);
			if(ch == ',') continue;
			if(lastZero<0 & ch != '0')lastZero = i;
			sb.append(ch);
		}
		lastZero = Math.min(lastZero, sb.length()-3);
		if(lastZero >= 0)sb.delete(0, lastZero);
		sb.insert(sb.length()-2, ',');
		setString(sb.toString());
		
	}
	
}

