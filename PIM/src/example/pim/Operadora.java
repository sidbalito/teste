package example.pim;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

public class Operadora {
	private static final String TABELA = "TABELA";
	private static Hashtable tabela = new Hashtable();
	private static RecordStore rs; 
	
	/**
	 * Define a operadora para um determinado número
	 * @param numero é o número do telefone
	 * @param codOperadora é o código da operadora
	 */
	public static void setOperadora(String numero, int codOperadora){
		if(tabela.get(numero) == null)
			tabela.put(numero, new Integer(codOperadora));
		else {
			tabela.put(numero, new Integer(codOperadora));
		}
	}
	
	public static int getOperadora(String numero){
		return operadora(numero).intValue();
	}
	
	public static Integer operadora(String numero){
		Integer codOperadora = (Integer) tabela.get(numero);
		if(codOperadora == null) return new Integer(0);
		//System.out.println(numero+" operadora: "+codOperadora.byteValue());
		return codOperadora;
	}

	public static void storeData() throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		
		rs.closeRecordStore();
		RecordStore.deleteRecordStore(TABELA);
		rs = null;
		//*/
		openRecordStore();
		Integer codOperadora = null; 
		String numero = null;
		byte[] registro;
		for(Enumeration items = tabela.keys(); items.hasMoreElements();){
			numero = (String) items.nextElement();
			codOperadora = operadora(numero);
			registro = (codOperadora+";"+numero).getBytes(); 
			System.out.println("Storing: "+new String(registro));
			rs.addRecord(registro, 0, registro.length);
		}
	}
	
	public static void loadData() throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		openRecordStore();
		System.out.println("RecordStore: "+rs.getNumRecords());
		int i = 0;
		String registro;
		for(i = 1; i <= rs.getNumRecords();i++){
			registro = new String(rs.getRecord(i));
			System.out.println("Loading: "+registro);
			int len = registro.length();
			int codOperadora = registro.charAt(0)-0x30;
			String numero = registro.substring(2);
			System.out.println(codOperadora + ";" + numero);
			setOperadora(numero, codOperadora);//*/
		}
	}
	
	public static void importa() throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException, IOException{
		FileConnection fc = openFile();
        if (!fc.exists()) {
            return;
        }
        InputStream fis = fc.openInputStream();
        long len = fc.fileSize();
        byte[] buff = new byte[(int) len];
		fis.read(buff);
		int campo = 0;
		int codOperadora = 0; 
		StringBuffer numero = new StringBuffer(); 
	}

	public static Enumeration getNumeros() {
		return tabela.keys();
	}

	public static void exporta() throws IOException {
		FileConnection fc = openFile();
		OutputStream fos = fc.openOutputStream();
	    StringBuffer buff = new StringBuffer();
		Integer codOperadora = null; 
		String numero = null;
		for(Enumeration items = tabela.keys(); items.hasMoreElements();){
			numero = (String) items.nextElement();
			codOperadora = operadora(numero);
			buff.append(codOperadora+";"+numero+"\n");
		}	    
		fos.write(buff.toString().getBytes());
		fos.flush();
	}
	
	private static FileConnection openFile() throws IOException{
		Enumeration e = FileSystemRegistry.listRoots();
        String root = (String)e.nextElement();
        return (FileConnection)Connector.open("file://localhost/"+root +"numeros.csv" );
		
	}

	private void fromBytes(byte[] buff){
		StringBuffer numero = new StringBuffer();
		int codOperadora = 0, campo = 0;
		for(int i = 0; i<buff.length;i++){
			char c = (char) buff[i];
			if(c == '\n' | c == '\r'){
				if(numero.length()<=0)continue;
				//System.out.println(numero.toString()+" operadora: "+ codOperadora);
				setOperadora(numero.toString(), codOperadora);
				numero = new StringBuffer();
				campo = 0;
				continue;
			}
			if(c == ';'){
				campo++;
				continue;
			}
			if(campo == 0){
				codOperadora = (c<'0'|c>'9')?0:(int)c-'0';
				continue;
			}
			if(campo == 1){
				numero.append(c);
			}
		}
		
	}

	private static void openRecordStore() throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		if(rs != null) {
			return;
		}
		rs = RecordStore.openRecordStore(TABELA, true);
	}
}
