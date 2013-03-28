import java.util.Calendar;
import java.util.Date;

public class Lancamento implements Serializable {
	private static final char DATE_SEPARATOR = '/';
	private static final char SEPARATOR = ';';
	private static final char PONTO = '.';
	private static final char VIRGULA = ',';
	private static final char ZERO = '0';
	private float valor;
	private long data;
	private String descricao;
	public Lancamento(String descricao, float valor, long data) {
		this.setDescricao(descricao);
		this.setValor(valor);
		this.setData(data);
	}
	
	public Lancamento() {
		this("", 0, System.currentTimeMillis());
	}

	public Lancamento(String param) {
		fromString(param);
	}

	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
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
		StringBuffer sb = new StringBuffer();
		sb.append(getDescricao());
		sb.append(';');
		sb.append(getValor());
		sb.append(';');
		sb.append(getData());
		return sb.toString();
	}
	
	public Serializable fromString(String s){
		int inicio = 0, fim = s.indexOf(SEPARATOR);
		descricao = s.substring(inicio, fim);
		inicio = fim+1;
		fim = s.indexOf(SEPARATOR, inicio);
		valor = Float.parseFloat(s.substring(inicio, fim));//Integer.parseInt(s.substring(inicio, fim));
		inicio = fim+1;
		data = Long.parseLong(s.substring(inicio));	
		return new Lancamento(descricao, valor, data);
	}
	
	public String printValor(){/*
		StringBuffer sb = new StringBuffer(Float.toString(valor));
		while(sb.length()<3)sb.insert(0, '0');
		sb.insert(sb.length()-2, ',');
		return sb.toString();//*/
		StringBuffer sb = new StringBuffer(Float.toString(valor));
		int len = sb.length();
		int ponto = sb.toString().indexOf(PONTO);
		int excesso = len-ponto-3;
		if(excesso > 0)sb.setLength(len-excesso);
		if(excesso < 0)sb.append(ZERO);
		sb.setCharAt(ponto, VIRGULA);
		return sb.toString();
	}
	
	public String printData(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(this.data));
		StringBuffer sb = new StringBuffer();
		sb.append(calendar.get(Calendar.DAY_OF_MONTH));
		sb.append(DATE_SEPARATOR);
		sb.append(calendar.get(Calendar.MONTH)+1);
		sb.append(DATE_SEPARATOR);
		sb.append(calendar.get(Calendar.YEAR));
		return sb.toString();
	}
}
