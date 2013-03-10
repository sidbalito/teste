import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

import comum.graficos.RichList;


public class Persistencia implements Runnable{
	private static final String EXTENSAO = ".csv";
	public static final int EXPORTAR = 0;
	public static final int IMPORTAR = 1;
	private static final String ENCODING = "ASCII";
	private long fileLen;
	private int modo;
	private Vector items;
	private String url;
	private String arquivo;
	private Serializable serializable;
	private RichList lista;

	
	private FileConnection openFile() throws IOException{
		Enumeration e = FileSystemRegistry.listRoots();
        String root = (String)e.nextElement();
        url = "file://localhost/"+root +arquivo+EXTENSAO;
        FileConnection fc = (FileConnection)Connector.open(url, Connector.READ_WRITE);
        if(!fc.exists())fc.create();
        fileLen = fc.fileSize();//*/
         return fc;
		
	}

	private void saveData(){
		String s = saveLancamentos().toString();
		OutputStreamWriter output;
		try {
			output = new  OutputStreamWriter(openFile().openDataOutputStream(), ENCODING) ;
			output.write(s);
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	private StringBuffer saveLancamentos() {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<items.size();i++){
			Lancamento lancamento = (Lancamento) items.elementAt(i);
			sb.append(lancamento.toString());
			/*
			sb.append(lancamento.getDescricao());
			sb.append(';');
			sb.append(lancamento.getValor());
			sb.append(';');
			sb.append(lancamento.getData());//*/
			sb.append('\n');
		}
		return sb;
	}

	private void loadData() {
		try {
			InputStreamReader input = new InputStreamReader(openFile().openInputStream(), ENCODING);
			char[] bytes = new char[(int) fileLen];
			input.read(bytes);
			int inicio = 0;
			for(int i = 0; i < bytes.length; i++){
				if(bytes[i] == '\n'){
					int fim = i;
					items.addElement(serializable.fromString(new String(bytes, inicio, fim-inicio)));//loadLancamentos(bytes);
					inicio = fim+1; 
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadLancamentos(char[] bytes) {
		//TODO: utilidade
			StringBuffer sb = new StringBuffer();
			String descricao = null;
			int valor = 0;
			long data = 0;
			int campo = 0;
			for(int i = 0; i<bytes.length;i++){
				if(bytes[i] == ';'|bytes[i]=='\n'){
					switch(campo){
					case 0: descricao = sb.toString();break;
					case 1: valor = Integer.parseInt(sb.toString());break;
					case 2: data = Long.parseLong(sb.toString());break;
					}
					sb.setLength(0);
					campo++;
					if(campo == 3){
						items.addElement(new Lancamento(descricao, valor, data));
						campo = 0;
					}
					continue;
				}
				sb.append(bytes[i]);
			}
	}

	public void executa(Vector items, int modo, String section, Serializable serializable, RichList lista) {
		this.modo = modo;
		this.items = items;
		this.arquivo = section;
		this.lista = lista;
		this.serializable = serializable;
		new Thread(this).start();
	}

	public void run() {
		switch(modo){
		case EXPORTAR: saveData();break; 
		case IMPORTAR: loadData();break; 
		}
		lista.repaint();
	}
}
