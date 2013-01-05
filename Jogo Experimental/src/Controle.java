import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

final public class Controle {
	private static final String NOME_RS = "Record"; // Nome do RecordStore
	private static long fim; // Variável
	private static int meta; //
	private static int metaInc;
	private static boolean menu;
	private static int pontosMax = restaura();

	public static int restaura() {
		try {
			RecordStore rs = RecordStore.openRecordStore(NOME_RS, true);
			// System.out.println("Controle.restaura "
			// +rs.getNumRecords()+" items");

			if (rs.getNumRecords() > 0) {
				// System.out.println("Controle.restaura"+new
				// String(rs.getRecord(1)));
				return Integer.parseInt(new String(rs.getRecord(1)));
			}
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
		return 0;

	}

	// Inicia a contagem de tempo

	// Verifica se o tempo do jogo se esgotou
	public static boolean tempoEsgotado() {
		return fim - System.currentTimeMillis() < 1;
	}

	// Retorna o valor da meta
	public static int getMeta() {
		return meta;
	}

	public static void incMeta() {
		Controle.meta = meta + metaInc;
	}

	public static void setMetaInc(int value) {
		metaInc = value;
	}

	public static boolean menu() {
		return menu;
	}

	public static void salva(int pontos) {

		if (pontosMax < pontos) {
			pontosMax = pontos;
			try {
				RecordStore rs = RecordStore.openRecordStore(NOME_RS, true);
				String valor = "" + pontosMax;
				if (rs.getNumRecords() == 0)
					rs.addRecord(valor.getBytes(), 0, valor.length());
				else
					rs.setRecord(1, valor.getBytes(), 0, valor.length());

			} catch (RecordStoreFullException e) {
				e.printStackTrace();
			} catch (RecordStoreNotFoundException e) {
				e.printStackTrace();
			} catch (RecordStoreException e) {
				e.printStackTrace();
			}
		}
	}

	public static int getPontosMax() {
		// if(pontosMax == 0) pontosMax = restaura();
		return pontosMax;
	}

}
