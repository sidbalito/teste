package jbackup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;

import auxiliar.Util;

public class ZIP {
	private static final int BUFFER_SIZE = 2;
	private static final long ZERO = 0;
	private byte[] buffer = new byte[BUFFER_SIZE];
	private String tempFileName = "TEMP.TMP";
	private HashMap<Long, Long> longs = new HashMap<Long, Long>();
	private HashMap<byte[], String> digests = new HashMap<byte[], String>();
	private long fileLen;
	private int contador;
	private int contaBits;
	private long contaLongs;
	private long mask;
	private long time;
	private int marca;
	private int novos;
	private int bytesLidos;



	public void processStreams() throws NoSuchAlgorithmException, IOException{
		//File[] files = new File("\\\\C:\\temp\\teste\\").listFiles();
		//processStream(new File("\\\\C:\\Documents and Settings\\Usuario\\Configurações locais\\Temporary Internet Files\\Content.IE5\\PN13IAJ6\\lockbox3[1].cab"));
		//processStream(new File("\\\\C:\\Temp\\~FB.tmp"));	
		processStream(new File("\\\\C:\\Temp\\bet365pokerpt.cab"));	
	}
	
	private void processStream(File file) throws NoSuchAlgorithmException, IOException {
		InputStream in = new FileInputStream(file);
		OutputStream out = tempFile();
		fileLen = file.length();
		time = System.currentTimeMillis();
		System.out.println("Tamanho do arquivo: " +fileLen);
		//o primeiro arquivo com determinado tamanho será gravado imediatamente (tamanho diferente = conteúdo diferente)`
		readBuffered(in);
		out.close();
	}


	private void readFile(InputStream in) throws IOException {
		long value = 0;
		while(in.available()>0){
			value = in.read()| (in.read() << 8);
			incShorts(value);
		}
		getLongs();
	}

	final static int BUFF_LEN = 0x100000;
	private void readBuffered(InputStream in) throws IOException {
		long value = 0; 
		byte[] buff =  new byte[BUFF_LEN];
		while(in.available()>0){
			int lidos = Math.min(in.read(buff), buff.length);
			//contaLongs = longs.size();
			//System.out.println(lidos+" bytes lidos; "+contaLongs+" valores");
			for(int i = 0; i<lidos;i+=2){
				value = buff[i]|buff[i+1]<<8;
				bytesLidos += 2;
				incShorts(value);
				if(longs.size() == 256 & novos == 0){
					System.out.println(longs.size()+" valores ("+novos+" novos) "+(bytesLidos-marca)+" bytes lidos");
					marca = bytesLidos;
					//contaLongs = 0;
					novos = 1;
					//contaLongs = longs.size();
					//System.out.println(((lidos+i)-marca)+"; "+(contaLongs)+"");;
					 //longs = new HashMap<Long, Long>();
				}
			}
				//System.out.println(value);
		}
		getLongs();
	}


	private void getLongs() {
		long numLongs = 0; numLongs = numLongs*0;
		long numRanges = 0;
		long min = Long.MAX_VALUE, max = 0, med = 0;
		for (Iterator<Long> iterator = longs.keySet().iterator(); iterator.hasNext();) {
			Long range = (Long) iterator.next();
			long value = longs.get(range);
			numLongs += value;
			numRanges++;
			min = Math.min(min, value);
			max = Math.max(max, value);
			med += value;
			contador++;
			//System.out.println(value);//Long.toBinaryString(range)+": "+value + " " + ((float)100*value/(fileLen/8)));
		}
		System.out.println("Tempo: " +(System.currentTimeMillis()-time)/1000+" segundos");
		System.out.println("Tabela: " + numRanges + " valores");//etores: " + numLongs + " tamanho: " + ((numRanges*16+numLongs*14)/8));
		System.out.println("Min: "+min+" max: "+max+" média: "+med/contador);
		System.out.println("Longs: "+contador);
	}


	private boolean firstDigest(byte[] digest, String path) {
		System.out.println(Util.bytesToHex(digest)+path);
		String paths = digests.get(digest);
		if(paths == null) paths = new String();
		StringBuffer sb = new StringBuffer(paths);
		sb.append(path);
		return false;
	}


	private OutputStream tempFile() throws FileNotFoundException {
		return new FileOutputStream(new File(tempFileName));
	}


	private byte[] digestFile(DigestInputStream din) throws IOException {
		return digestFile(din, null);
	}


	private void renFile(byte[] digest) {
		
	}

	/**
	 * Lê conteúdo do arquivo para verificar o digest. Se out não for nulo simultâneamente envia os bytes lidos para a saída
	 * @param din stream de entrada com capacidade de gerar digest do conteúdo
	 * @param out stream de saída para gravação simultânea à leitura
	 * @return digest do conteúdo do arquivo lido
	 * @throws IOException
	 */
	private byte[] digestFile(DigestInputStream din, OutputStream out) throws IOException {
		while(din.available()>0){
			din.read(buffer);
			if(out != null)out.write(buffer);
		}
		return din.getMessageDigest().digest();
	}


	private void incLong(long value) {
		//long range = value & ~MASK;
		//System.out.println(value);
		long bit = value;
		Long count = longs.get(bit);
		if((bit & mask) == 0){
			contaBits++;
			if(contaBits==8) {
				System.out.println(contaLongs+"Longs");
				contaBits = 0;
				contaLongs = 0;
				mask = 0;
			}
		}
		if(count == null) {
			count = ZERO;
		}
		count++;
		longs.put(bit, count);
	}
	
	private void incShorts(long value) {
		Long count = longs.get(value);
		if(count == null)count = 0L;
		count++;
		longs.put(value, count );
	}
	
	public void fazNada() throws Exception{
		boolean bool = true;
		if(bool) return;
		getLongs();
		readFile(null);
		digestFile(null);
		firstDigest(null, null);
		incLong(0);
		renFile(null);
		
	}
}