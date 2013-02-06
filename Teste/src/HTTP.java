import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;


public class HTTP {

	private static final int END_CONN = -1;
	private static final String PROPERTY_CONTENT = "Content-Type";
	private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";

	private String read(InputStream is) throws IOException{
		StringBuffer sb = new StringBuffer();
		int ch = 0;
		do{
	    	ch = is.read();
	    	if(ch != END_CONN)sb.append((char)ch);
	    }while(ch != END_CONN);
        is.close();
	return sb.toString();		
	}
	
	public String doGet(String url) throws IOException{
		  HttpConnection conn = null;
		  InputStream is = null;
		    conn = (HttpConnection)Connector.open(url, 
		       Connector.READ_WRITE, true);
		    conn.setRequestMethod(HttpConnection.GET); //default
		    is = conn.openInputStream(); // transition to connected!
		    String result = read(is);
		    conn.close();
		    return result;
	}

	public String doPost(String url, String params) throws IOException{
		//Cria a conexão HTTP
		HttpConnection connection = (HttpConnection) Connector.open(url,Connector.READ_WRITE);
		connection.setRequestMethod(HttpConnection.POST);        
		connection.setRequestProperty(PROPERTY_CONTENT, CONTENT_TYPE);
		//Envia parâmetros
		OutputStreamWriter out = new OutputStreamWriter(connection.openOutputStream(),"UTF-8");            
		out.write(params);            
		out.close();
		//Código de resposta
		int responseCode = connection.getResponseCode();            
		if (responseCode != HttpConnection.HTTP_OK) throw new IOException("HTTP response code: " + responseCode);
		//Lê o conteúdo da resposta
		InputStream inputStream = connection.openInputStream();
		String response = read(inputStream);
		return response;        
	}  
}
