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

public class Base {
	private static final String ALGORITM = "MD5";
	private static final int BUFFER_SIZE = 1024;
	private byte[] buffer = new byte[BUFFER_SIZE];
	private String tempFileName = "TEMP.TMP";
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
			digest = digestFile(din);
			if(firstDigest(digest)) {
				copyFile(din, out);
				addPath(digest, file);
				renFile(digest);
			}
		}
		//TODO: manipular exce��o para garantir que o DigestInputStream e OutputStream sejam fechados
		din.close();
		out.close();
	}


	private void copyFile(DigestInputStream din, OutputStream out) {
		// TODO: copiar conte�do para o arquivo tempor�rio
		
	}


	private void addPath(byte[] digest, File file) {
		// TODO: adiciona nome de arquivo associado ao digest 
		
	}


	private boolean firstDigest(byte[] digest) {
		// TODO: verificar se h� ocorrencias do digest na lista
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


	private byte[] digestFile(DigestInputStream din, OutputStream out) throws IOException {
		// TODO: carregar conte�do do arquivo para verificar seu hash e copiar conte�do se for o caso
		while(din.available()>0){
			din.read(buffer);
			if(out != null)out.write(buffer);
		}
		return din.getMessageDigest().digest();
	}


	private boolean firstLen(long fileLen) {
		// TODO: verificar se � o primeiro arquivo com este tamanho
		return false;
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