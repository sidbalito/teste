import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;


public class Persistencia implements Runnable{
	private static final String ARQUIVO = "lancamentos.csv";
	public static final int EXPORTAR = 0;
	public static final int IMPORTAR = 1;
	private long fileLen;
	private int modo;
	private Vector lancamentos;
	private String url;

	public Persistencia(int modo, Vector lancamentos) {
		this.modo = modo;
		this.lancamentos = lancamentos;
		new Thread(this).start();
	}

	private void saveData(Vector lancamentos){
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<lancamentos.size()-1;i++){
			Lancamento lancamento = (Lancamento) lancamentos.elementAt(i);
			sb.append(lancamento.getDescricao());
			sb.append(';');
			sb.append(lancamento.getValor());
			sb.append(';');
			sb.append(lancamento.getData());
			sb.append('\n');
		}
		OutputStream output;
		try {
			output = openFile().openDataOutputStream();
			output.write(sb.toString().getBytes());
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private FileConnection openFile() throws IOException{
		Enumeration e = FileSystemRegistry.listRoots();
        String root = (String)e.nextElement();
        url = "file://localhost/"+root +ARQUIVO;
        FileConnection fc = (FileConnection)Connector.open(url, Connector.READ_WRITE);
        if(!fc.exists())fc.create();
        fileLen = fc.fileSize();//*/
        return fc;
		
	}


	private void loadData(Vector lancamentos) {
		try {
			InputStream input = openFile().openInputStream();
			byte[] bytes = new byte[(int) fileLen];
			input.read(bytes);
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
						lancamentos.addElement(new Lancamento(descricao, valor, data));
						campo = 0;
					}
					continue;
				}
				sb.append((char)bytes[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void executa() {
		switch(modo){
		case EXPORTAR: saveData(lancamentos);break; 
		case IMPORTAR: loadData(lancamentos);break; 
		}
		
	}

	public void run() {
		executa();
	}
}
