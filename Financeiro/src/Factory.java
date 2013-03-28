
public class Factory {
	public static Object newInstance(Class classe, String param){
		if(classe == Lancamento.class) return new Lancamento(param);
		return null;		
	}
}
