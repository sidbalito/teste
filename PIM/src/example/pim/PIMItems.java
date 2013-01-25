package example.pim;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMItem;
import javax.microedition.pim.PIMList;

public class PIMItems extends MIDlet{

	private Vector listas;
	private PIM pim;

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {}

	protected void pauseApp() {}

	protected void startApp() throws MIDletStateChangeException {
			//*
		try {
			pim = PIM.getInstance();
			for(int i = PIM.CONTACT_LIST;i <= PIM.TODO_LIST;i++)
				lista(i, pim.listPIMLists(i));
			//System.out.println( toString(new int[]{0xFF, 0xF0, 0x0F}));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//*/
	}

	private void lista(int listType, String[] lists) throws PIMException {
		for(int i = 0; i < lists.length; i++){
			System.out.println("\n\n"+lists[i]+'\n');
			PIMList pimlist = pim.openPIMList(listType, PIM.READ_ONLY, lists[i]);
			suportedFields(pimlist);
			listItems(pimlist.items());
		}
	}

	private void suportedFields(PIMList pimlist) {
		int[] suportedFields = pimlist.getSupportedFields();
		for(int i = 0; i < suportedFields.length; i++)
			System.out.println(pimlist.getFieldLabel(suportedFields[i])+": "+ getType(suportedFields[i], pimlist));
	}

	private void listItems(Enumeration items) {
		PIMItem item = null;
		for(;items.hasMoreElements();item = (PIMItem) items.nextElement()){
			printFields(item);
		}
	}

	private void printFields(PIMItem item) {
		if(item == null)return;
		int[] fields = item.getFields();
		for(int i = 0; i < fields.length; i++)
			printField(fields[i], item);
	}

	private void printField(int field, PIMItem item) {
		for(int i = 0; i < item.countValues(field); i++){
			Object value = getValue(field, i, item);
			System.out.println(item.getPIMList().getFieldLabel(field)+": "+ value+" ("+value.getClass()+")");
		}
		
	}

	private Object getValue(int field, int i, PIMItem item) {
		if(item == null) return null;
		int fieldType = item.getPIMList().getFieldDataType(field);
		switch(fieldType){
		case PIMItem.STRING: return item.getString(field, i);
		case PIMItem.BINARY: return  item.getBinary(field, i);
		case PIMItem.INT: return new Integer(item.getInt(field, i));
		case PIMItem.DATE: return new Long(item.getDate(field, i));
		case PIMItem.BOOLEAN: return new Boolean(item.getBoolean(field, i));
		case PIMItem.STRING_ARRAY: return item.getStringArray(field, i);		
		}
		return null;
	}
	
	private String toString(Object field) {
		if(field == null) return "";
			StringBuffer sb = new StringBuffer();
		if(field.getClass() == Long.class) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date(((Long)field).longValue()));
			sb.append(cal.get(Calendar.DAY_OF_MONTH));
			sb.append('/');
			sb.append(cal.get(Calendar.MONTH)+1);
			sb.append('/');
			sb.append(cal.get(Calendar.YEAR));
			return sb.toString();
		}
		if(field.getClass() == Boolean.class){
			return ((Boolean)field).booleanValue()?"Sim":"Não";
		}
		if(field.getClass() == String[].class){
			String[] strings = (String[]) field;
			for(int i = 0; i < strings.length; i++){
				sb.append(' ');
				sb.append(strings[i]);
			}
			return sb.toString();
		}
		if(field.getClass() == int[].class){
			sb = new StringBuffer();
			int[] values  = (int[]) field;
			for(int i = 0; i < values.length; i++)sb.append(Integer.toHexString(values[i])+' ');
			return sb.toString().toUpperCase();
		}
		return field.toString();
	}
	
	private Object getType(int field, PIMList list) {
		if(list == null) return null;
		int fieldType = list.getFieldDataType(field);
		switch(fieldType){
		case PIMItem.STRING: return String.class;
		case PIMItem.BINARY: return   Integer.class+ " array";
		case PIMItem.INT: return Integer.class;;
		case PIMItem.DATE: return Long.class;;
		case PIMItem.BOOLEAN: return Boolean.class;;
		case PIMItem.STRING_ARRAY: return String.class + " array";		
		}
		return null;
	}
	
	
}
