import java.util.Vector;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;


public class Persist {
	private static final boolean CREATE = true;
	RecordStore rs;
	public void store(String section, Vector items) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		try{
			RecordStore.deleteRecordStore(section);
		} catch(Exception e) {} 
		rs= RecordStore.openRecordStore(section, CREATE);
		for(int i = 0; i < items.size(); i++){
			byte[] serialized = items.elementAt(i).toString().getBytes();
			rs.addRecord(serialized, 0, serialized.length);
		}
	}
	
	public void load(String section, Vector items, Serializable serializable) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		rs = RecordStore.openRecordStore(section, CREATE);
		for(int i = 1; i <= rs.getNumRecords(); i++){
			byte[] serialized = rs.getRecord(i);
			items.addElement(serializable.fromString(new String(serialized)));
		}
		rs.closeRecordStore();
	}
}

