import java.util.Vector;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;


public class Persist {
	private static final boolean CREATE = true;
	RecordStore rs;
	public void store(String section, Vector items) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		rs= RecordStore.openRecordStore(section, CREATE);
		for(int i = 0; i < items.size(); i++){
			//byte[] serialized = ((Serializable)items.elementAt(0)).toString();
			//rs.addRecord(serialized, 0, serialized.length);
		}
	}
	
	public void load(String section, Vector items, Serializable serializable) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		rs = RecordStore.openRecordStore(section, CREATE);
		for(int i = 0; i < rs.getNumRecords(); i++){
			byte[] serialized = rs.getRecord(i);
			//items.addElement(serializable.deserialize(serialized));
		}
	}
}
