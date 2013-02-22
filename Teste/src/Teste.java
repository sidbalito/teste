import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;

import mujmail.html.Browser;
//Midlet's User interface API
//import javax.microedition.midlet.*;

public class Teste extends MIDlet
			implements CommandListener {

	private static final int HTTP_INPUT_BUFFER_SIZE = 1024;
	private Form helloForm;
	private Command exitCommand;

/**
* Midlet constructor
 * @throws IOException 
 * @throws ConnectionNotFoundException 
* */

  public Teste() throws IOException {
	 
	//platformRequest("https://docs.google.com");

/*  	exitCommand = new Command("Exit", Command.EXIT, 1);

     	helloForm = new Form("Device " +
     			"Specs");

  	// Use System.getProperty() to query device configurations
  	helloForm.append("Platform : "
		+ System.getProperty("microedition.platform") + "\n");
  	helloForm.append("Encoding : "
		+ System.getProperty("microedition.encoding") + "\n");
  	helloForm.append("Configuration : "
		+ System.getProperty("microedition.configuration") + "\n");
  	helloForm.append("Profiles : "
		+ System.getProperty("microedition.profiles") + "\n");
  	helloForm.append("Locale : "
		+ System.getProperty("microedition.locale") + "\n");

  	// Use java.lang's Runtime class to query device memory
  	long total = Runtime.getRuntime().totalMemory();
  	long livre = Runtime.getRuntime().freeMemory();
  	long percentual = (livre*100)/total;
  	helloForm.append("Total Memory: "
		+ total  + "\n");
  	helloForm.append("Free Memory: "
		+ livre  + " " +
				"("+percentual+"%)\n");

  	// Forcibly call garbage collector
  	Runtime.getRuntime().gc();

  	helloForm.addCommand(exitCommand);
  	helloForm.setCommandListener(this);//*/
	  //connect();
	  Display.getDisplay(this).setCurrent(new ColorPicker(false));// Browser("<html>Texto <u>sublinhado<u> resublinhado</u> sublinhado</u> e normal</html>"));//(new HTTP().doPost("http://trtcons.trtsp.jus.br/dwp/consultasphp/public/index.php/pautaTurma/data", "turma=1"))));

  }

  /**
   * The paused state changed to active state.
   */

  protected void startApp() {
	  try {/*
		for(int i = 1; i < 19; i++){
			pt = new PautaTurma(sendData(new StringBuffer("turma="+i), "http://trtcons.trtsp.jus.br/dwp/consultasphp/public/index.php/pautaTurma/data"));
			String[] values = pt.getValues("dataForm");
			//System.out.println(values.length);
			
			for(int j = 0; j < values.length; j++) System.out.println(i+"ª Turma-"+values[j]);
		}//*/
 	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     Display.getDisplay(this).setCurrent(helloForm);
  }

  /**
   * The MIDlet frees as much resources as it can.
   */

  protected void pauseApp() {}


  protected void destroyApp(boolean bool) {}
  /**
   * Midlet event handling
   */
  public void commandAction(Command cmd, Displayable disp) {
      if (cmd == exitCommand) {
          destroyApp(false);
          notifyDestroyed();
      }
  }
  
  public void connect(){
	  HttpConnection c = null;
	  InputStream is = null;
	  StringBuffer sb = new StringBuffer();
	  try {
	    c = (HttpConnection)Connector.open(
	       "http://lx-sed-dwp.trtsp.jus.br/internet/noticia.php?cod_noticia=7000", 
	       Connector.READ_WRITE, true);
	    c.setRequestMethod(HttpConnection.GET); //default
	    //c.setRequestProperty("Accept-Charset","UTF-8;q=0.7,*;q=0.7");
	    is = c.openInputStream(); // transition to connected!
	    
	    int ch = 0;
		while(ch != -1){
	    	sb.append((char)ch);
	    	ch = is.read();
	    }
	    //System.out.println(i);
	    if(is.available()>0){
		    byte[] buffer = new byte[is.available()];
			is.read(buffer);
			
		    ch = 0;
		    for(int ccnt=0; ccnt < buffer.length; ccnt++) { // get the title.
		      ch = buffer[ccnt];
		      if (ch == -1){
		    	//System.out.println("breaking");
		        break;
		      }
		      sb.append((char)ch);
	//	      System.out.println((char)ch);
		    }
	    }//*/
	  }
	  catch (IOException x){
	  	x.printStackTrace();
	  }
	  finally{
	       try     {
	         is.close();
	            c.close();
	       } catch (IOException x){
	            x.printStackTrace();
	       }
	  }
	  //sb.append("<html>Teste<br><!>comentário<--");
	  //System.out.println(sb.length());
	  System.out.println(sb.toString());
	  sb.setLength(0);
	  sb.append("<html><a href=\"www.google.com\">Google</a></html>");
	
	  Display.getDisplay(this).setCurrent(new Browser((sb.toString())));
  }
  
  String sendData(StringBuffer data,String serverUrl) throws IOException  
  {            
      HttpConnection connection = (HttpConnection) Connector.open(serverUrl,Connector.READ_WRITE);
      
      connection.setRequestMethod(HttpConnection.POST);        
      connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
      //connection.setRequestProperty("Content-Length",String.valueOf(data.toString().getBytes("UTF-8").length));        
      connection.setRequestProperty("User-Agent","Profile/MIDP-2.0 Configuration/CLDC-1.1");
      connection.setRequestProperty("Accept-Charset","UTF-8;q=0.7,*;q=0.7");
      connection.setRequestProperty("Accept-Encoding","gzip, deflate");
      connection.setRequestProperty("Accept","text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");        
      
      InputStream inputStream = null;
      OutputStreamWriter out = null;
      byte[] readData = new byte[HTTP_INPUT_BUFFER_SIZE];        
               
      String response;       
      
      try
      {
          // --- write ---
          out = new OutputStreamWriter(connection.openOutputStream(),"UTF-8");            
          out.write(data.toString());            
          out.close();
          // --- read ---
          
          int responseCode = connection.getResponseCode();            
          
          if (responseCode != HttpConnection.HTTP_OK)
          {
              throw new IOException("HTTP response code: " + responseCode);
          }
          
          inputStream = connection.openInputStream();
                                 
          int actual = inputStream.read(readData);
          //System.out.println(actual);
          if (actual==0)
          {
              //throw new IOException("No response from server");                
          }
          
          
          response = new String(readData,0,actual,"UTF-8");/*
          //system.out.println(response);
          if (!response.equalsIgnoreCase(SUCCESS_MESSAGE))
          {
              throw new IOException(response);
          }//*/            
      }
      finally
      {            
          if (out!=null)
          {
              try
              {
                  out.close();
              } catch (IOException ex) {}
          }
          if (inputStream!=null)
          {
              try
              {
                  inputStream.close();
              } catch (IOException ex) {}
          }                       
      }
	return response;        
  }  
  
}
class PautaTurma{
	private static final int VALUE = 1;
	private static final int VAR = 0;
	private static final Hashtable values = new Hashtable();

	public PautaTurma(String resposta) {
		String var = "";
		int mode = VAR;
		boolean string = false;
		byte[] resp = resposta.getBytes();
		StringBuffer sb = new StringBuffer();
		int count = 0;
		boolean array = false;
		for(int i = 0; i<resp.length;i++){
			switch(resp[i]){
			case '{': if(array)count++; break;
			case '}':break;
			case '\"': string = !string; break;
			case ':': mode = VALUE; break;
			case ',': mode = VAR;break;
			case ']': array=false; break;
			case '[': array = true;
			//System.out.println("case array: "+array);
				mode = VAR;
				count = 0;
			break;
			default:
				while (resp[i] != '\"') {
					if(!string & (Character.toLowerCase((char) resp[i]) < 'a') | 
							(Character.toLowerCase((char) resp[i]) > 'z'))break; 
					sb = sb.append((char)resp[i]);
					i++;
					//System.out.println(sb);
				}
				//system.out.println(mode+": "+sb);
				string = false;
				//System.out.println("array: "+array);
				if(mode == VAR) {
					if(array)sb.append(count);
					var = sb.toString();
				}
				else if((mode & VALUE) == VALUE) values.put(var, sb.toString());
				sb.setLength(0);
			}
			//System.out.println((char)resp[i]+": "+array);
			
		}//system.out.println(values);
	}
	public String getValue(String key){
		if(values.containsKey(key)) return "";
		return (String) values.get(key);
	}
	
	public String[] getValues(String key){
		Vector v = new Vector();
		int i = 1;
		while(values.containsKey(key+i) ){
		//system.out.println(key+i);
			v.addElement(values.get(key+i));
			i++;
		}
		String[] s = new String[v.size()];
		for(i = 0; i < s.length; i++) {
			s[i]=(String) v.elementAt(i);
			//system.out.println(s[i]);
		}
		return s;
	}
	
}
