import java.util.Calendar;
import java.util.Date;

public class Lancamento {
	private static final char DATE_SEPARATOR = '/';
	private int valor;
	private long data;
	private String descricao;
	public Lancamento(String descricao, int valor, long data) {
		this.setDescricao(descricao);
		this.setValor(valor);
		this.setData(data);
	}
	
	public Lancamento() {
		this("", 0, System.currentTimeMillis());
	}

	public int getValor() {
		return valor;
	}
	public void setValor(int valor) {
		this.valor = valor;
	}
	public long getData() {
		return data;
	}
	public void setData(long data) {
		this.data = data;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String toString() {
		return descricao;
	}
	
	public String printValor(){
		StringBuffer sb = new StringBuffer(Integer.toString(valor));
		while(sb.length()<3)sb.insert(0, '0');
		sb.insert(sb.length()-2, ',');
		return sb.toString();
	}
	
	public String printData(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(this.data));
		StringBuffer sb = new StringBuffer();
		sb.append(calendar.get(Calendar.DAY_OF_MONTH));
		sb.append(DATE_SEPARATOR);
		sb.append(calendar.get(Calendar.MONTH));
		sb.append(DATE_SEPARATOR);
		sb.append(calendar.get(Calendar.YEAR));
		return sb.toString();
	}
}
