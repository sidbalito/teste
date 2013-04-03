import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;



public class EditaNome extends Form{
	private TextField nome = new TextField("Nome", "", 100, 0);
	public EditaNome() {
		super("");
		append(nome);
	}
	
	public String getNome(){
		return nome.getString();
	}

}
