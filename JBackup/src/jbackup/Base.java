package jbackup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import auxiliar.Util;

public class Base {
	private static final String ALGORITM = "MD5";
	private static final int BUFFER_SIZE = 1024;
	private static final long BIT = 1;
	private static final long MASK = 0x3F;
	private static final long ZERO = 0;
	private byte[] buffer = new byte[BUFFER_SIZE];
	private String tempFileName = "TEMP.TMP";
	private HashMap<Long, Long> filesLens = new HashMap<Long, Long>();
	private HashMap<byte[], String> digests = new HashMap<byte[], String>();
	public Base(String[] strings) throws NoSuchAlgorithmException, IOException {
	}
	

	public void processStreams() throws NoSuchAlgorithmException, IOException{
		File[] files = new File("\\\\C:\\Documents and Settings\\Usuario\\Meus documentos\\SMS\\16-11-2012").listFiles();
		for(int i = 0; i<files.length; i++){
			processStream(files[i]);
		}
		
	}
	
	private void processStream(File file) throws NoSuchAlgorithmException, IOException {
		InputStream in = new FileInputStream(file);
		OutputStream out = tempFile();
		byte[] digest = null;
		DigestInputStream din = new DigestInputStream(in , MessageDigest.getInstance(ALGORITM));
		long fileLen = file.length();
		//o primeiro arquivo com determinado tamanho ser� gravado imediatamente (tamanho diferente = conte�do diferente)
		if(firstLen(fileLen)){
			digest = digestFile(din, out);
			renFile(digest);
		} else {
			//TODO: a convers�o de byte[] para String � via new String(byte[])
			digest = digestFile(din);
			String fileName = file.getAbsolutePath();
			if(firstDigest(digest, fileName )) {
				//obs.: din e in s�o inst�ncias diferentes abrindo o mesmo arquivo simult�neamente
				copyFile(new FileInputStream(file), out);  
				addPath(digest, file);
				renFile(digest);
			}
		}
		//TODO: manipular exce��o para garantir que o DigestInputStream e OutputStream sejam fechados
		din.close();
		out.close();
	}


	private void copyFile(InputStream in, OutputStream out) throws IOException {
		while(in.available()>0){
			in.read(buffer);
			out.write(buffer);
		}
	}


	private void addPath(byte[] digest, File file) {
		// TODO: adiciona nome de arquivo associado ao digest 
		
	}


	private boolean firstDigest(byte[] digest, String path) {
		// TODO: verificar se h� ocorrencias do digest na lista
		System.out.println(Util.bytesToHex(digest)+path);
		String paths = digests.get(digest);
		if(paths == null) paths = new String();
		//StringBuffer sb = new StringBuffer(paths).append(path);
		return false;
	}


	private OutputStream tempFile() throws FileNotFoundException {
		// TODO: criar um arquivo com nome tempor�rio para renomear posterior mente
		return new FileOutputStream(new File(tempFileName));
	}


	private byte[] digestFile(DigestInputStream din) throws IOException {
		return digestFile(din, null);
	}


	private void renFile(byte[] digest) {
		// TODO: renomear arquivo com nome baseado no hash do conte�do
		
	}

	/**
	 * L� conte�do do arquivo para verificar o digest. Se out n�o for nulo simult�neamente envia os bytes lidos para a sa�da
	 * @param din stream de entrada com capacidade de gerar digest do conte�do
	 * @param out stream de sa�da para grava��o simult�nea � leitura
	 * @return digest do conte�do do arquivo lido
	 * @throws IOException
	 */
	private byte[] digestFile(DigestInputStream din, OutputStream out) throws IOException {
		while(din.available()>0){
			din.read(buffer);
			if(out != null)out.write(buffer);
		}
		return din.getMessageDigest().digest();
	}


	private boolean firstLen(long fileLen) {
		//FEITO: verificar se � o primeiro arquivo com este tamanho
		long faixa = fileLen & ~MASK;
		long bit = BIT << (fileLen & MASK);
		Long bits = filesLens.get(faixa);
		if(bits == null) bits = ZERO;
		if((bits & bit)!=0)return false;
		filesLens.put(faixa, bits | bit);
		return true;
	}
}

class DummyInput extends InputStream{

	private StringBuffer sb = new StringBuffer();
	private int index;
	
	public DummyInput(String s) {
		sb.append(s);
		//System.out.println(s);
	}

	@Override
	public int read() throws IOException {
		int ch = sb .charAt(index);
		index++;
		return ch ;
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		System.arraycopy(sb.toString().getBytes(), index, b, 0, b.length);
		return Math.min(b.length, sb.length()-index);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		System.arraycopy(sb.toString().getBytes(), index, b, off, len);
		return Math.min(len, sb.length()-index);
	}
}