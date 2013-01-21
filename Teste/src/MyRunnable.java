
public class MyRunnable implements Runnable {
	private String name;
	private Interface valor;
	public MyRunnable(String name, Interface valor) {
		this.name = name;
		this.valor = valor;
	}
	public void run() {
		while(valor.getValor() < 10000){
			sincronizado();
			try {
				Thread.sleep(System.currentTimeMillis()&7);
			} catch (Exception e) {
			}
		}
	}
	private void sincronizado() {
		valor.inc();
		System.out.println(this.getName()+": "+valor);
	}
	private String getName() {
		return name;		
	}

}

interface Interface{
	public int getValor();
	public int setValor(int valor);
	public int inc();
}
