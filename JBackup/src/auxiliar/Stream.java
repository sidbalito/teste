package auxiliar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
public class Stream {
	private static final String NOTHING = "";
	private static int inIndex;
	//private static StringBuffer buffer = new StringBuffer();
	private static InputStream in = newIn(NOTHING);
	
	private static OutputStream out = new OutputStream() {
		
		private StringBuffer buffer;

		@Override
		public void write(int b) throws IOException {
			buffer.append((char)b);
		}
	};

	public static InputStream getIn() {
		return in;
	}

	public static OutputStream getOut() {
		return out;
	}

	public static void resetIn() {
		inIndex = 0;
	}

	public static InputStream newIn(final String s) {
		InputStream input = new InputStream() {
			StringBuffer buffer;

			@Override
			public int read() throws IOException {
				int ch = this.buffer.charAt(inIndex);
				inIndex++;
				return ch;
			}

			public InputStream append(String s) {
				if(buffer == null) buffer = new StringBuffer(s);
				return this;
			}
			
			
		}.append(s);
			try {
				System.out.println(input.read());
			} catch (IOException e) {
				// TODO: Manipular exceção (não pode ser repasada para campo estático)
				e.printStackTrace();
			}
		return input;
	}
}
