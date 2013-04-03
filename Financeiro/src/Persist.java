import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;


public class Persist {
	private static final boolean CREATE = true;
	Hashtable ht = new Hashtable();
	private RecordStore rs;
	
	private void clear(String section) throws RecordStoreNotOpenException, RecordStoreException{
		RecordStore rs = (RecordStore) ht.get(section);
		if(rs != null)	rs.closeRecordStore();
		try{
			RecordStore.deleteRecordStore(section);			
		}catch(Exception e){
			//System.out.println(e.getMessage());
		}
	}
	
	private RecordStore open(String section) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		RecordStore rs = (RecordStore) ht.get(section);
		if(rs == null){
			rs = RecordStore.openRecordStore(section, CREATE);
			ht.put(section, rs);
		}
		return rs;
	}
	
	public void store(String section, Vector items) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		try{
			
			RecordStore.deleteRecordStore(section);
		} catch(Exception e) {
			//System.out.println(e.getMessage());
		} 
		storeVector(section, items);
	}
	
	public void load(String section, Vector items, Object serializable) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		rs = open(section);
		for(int i = 1; i <= rs.getNumRecords(); i++){
			byte[] serialized = rs.getRecord(i);
			items.addElement(Factory.newInstance(serializable.getClass(), new String(serialized)));
		}
	}
	
	public String readRecord(int index, String section) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException{
		rs = open(section);
		if(index > rs.getNumRecords()) return "";
		return new String(rs.getRecord(index));
	}
	
	public int numRecords(String section) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		return open(section).getNumRecords();
	}
	
	public void writeRecord(String section, String value) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		open(section).addRecord(value.getBytes(), 0, value.length());
	}

	public void store(String section, String string) throws RecordStoreNotOpenException, RecordStoreFullException, RecordStoreException {
		clear(section);
		open(section).addRecord(string.getBytes(), 0, string.length());
	}

	
	public Vector loadVector(String section) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		Vector items =  new Vector();
		rs = open(section);
		for(int i = 1; i <= rs.getNumRecords(); i++){
			byte[] serialized = rs.getRecord(i);
			items.addElement( new String(serialized));
		}
		return items;
	}

	public void storeVector(String section, Vector items) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		clear(section);
		rs = open(section);
		for(int i = 0; i < items.size(); i++){
			byte[] serialized = items.elementAt(i).toString().getBytes();
			rs.addRecord(serialized, 0, serialized.length);
		}
	}
	
	public void store(String section, int recordId, String data) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		rs = open(section);
		padding(rs, recordId);
		byte[] bytes = data.getBytes();
		rs.setRecord(recordId, bytes, 0, bytes.length);
	}

	private void padding(RecordStore rs, int recordId) throws RecordStoreNotOpenException, RecordStoreException {
		int next = rs.getNextRecordID();
		if(recordId < next)return;
		for(int i = next; i <= recordId; i++)rs.addRecord(null, 0, 0);
	}
	
	public String load(String section, int recordId) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		rs = open(section);
		int next = rs.getNextRecordID();
		//System.out.println("Next: "+next);
		if(recordId >= next)return "";
		//System.out.println("Lendo registro...");
		return new String(rs.getRecord(recordId));		
	}

	public void remove(String section) throws RecordStoreNotOpenException, RecordStoreException {
		clear(section);
	}
}

