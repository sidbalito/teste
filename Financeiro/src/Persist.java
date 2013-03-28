import java.util.Vector;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;


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
	
	public void load(String section, Vector items, Object serializable) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		rs = RecordStore.openRecordStore(section, CREATE);
		for(int i = 1; i <= rs.getNumRecords(); i++){
			byte[] serialized = rs.getRecord(i);
			items.addElement(Factory.newInstance(serializable.getClass(), new String(serialized)));
		}
		rs.closeRecordStore();
	}
	
	public String readRecord(int index, String section) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException{
		rs = RecordStore.openRecordStore(section, CREATE);
		if(index > rs.getNumRecords()) return "";
		return new String(rs.getRecord(index));
	}
	
	public int numRecords(String section) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		rs = RecordStore.openRecordStore(section, CREATE);
		return rs.getNumRecords();
	}
	
	public void writeRecord(String section, String value) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		rs = RecordStore.openRecordStore(section, CREATE);
		rs.addRecord(value.getBytes(), 0, value.length());
	}
}

