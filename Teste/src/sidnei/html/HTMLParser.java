package sidnei.html;

import java.io.IOException;
import java.io.InputStream;

public class HTMLParser {
	private InputStream input;
	public HTMLParser(String string) {
		input = new InputStreamString(string);
	}
	public void parse(){
		
	}
}

class InputStreamString extends InputStream{
	
	private String stream;
	private int position;

	public InputStreamString(String string) {
		stream = string;
	}

	public int read() throws IOException {		
		char ch = stream.charAt(position);
		position++;
		return ch;
	}
	
	public int read(byte[] buffer, int pos, int count) throws IOException {
		System.arraycopy(stream, pos, buffer, 0, count);
		return Math.min(count, stream.length()-pos);
	}
	
	public int available() throws IOException {
		return stream.length()-position;
	}
	
	public void close() throws IOException {
		stream = null;
		super.close();
	}
}