package com.example.aplicativo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Tela extends Activity implements OnItemClickListener, OnItemLongClickListener{
	
    public static final String DRAWABLE = "android.resource://com.example.aplicativo/";
	public static final String[] ICONES = new String[]{
    	DRAWABLE+R.drawable.g0,
    	DRAWABLE+R.drawable.g1,
    	DRAWABLE+R.drawable.g2,
    	DRAWABLE+R.drawable.g3,
    	DRAWABLE+R.drawable.g4
    };
	private static final int PICK_IMAGE = 1;
	private static final String OPERADORAS = "operadoras";
	private static final String INTERNO = "interno";
	private Cursor people;
	private int indexName;
	private int indexNumber;
	private ArrayList<HashMap<String, Object>> fillMaps;
	private ListView lv;
	private SimpleAdapter adapter;
	private HashMap<String, Integer> operadoras = new HashMap<String, Integer>();

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
         startup();
        setContentView(R.layout.activity_tela);
        lv= (ListView)findViewById(R.id.contatos);

        // create the grid item mapping
        String[] from = new String[] {"item1", "item2", "icone"};
        int[] to = new int[] { R.id.item1, R.id.item2, R.id.imageView1};

        // prepare the list of all records
        fillMaps = new ArrayList<HashMap<String, Object>>();
        /*
        Cursor cur= getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,null,null,null);
		for (boolean hasData = cur.moveToFirst(); hasData; hasData = cur.moveToNext())
        {
            int nameidx=cur.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
            int Ididx=cur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String strName=cur.getString(nameidx);
            String strId=cur.getString(Ididx);
        	HashMap<String, Object> map = new HashMap<String, Object>();
        	map.put("item1", strName);
        	map.put("item2", strId);
        	map.put("icone", ICONES[0]);
        	fillMaps.add(map);
        }//*/		
        
		Uri uri                = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String[] projection    = new String[] { ContactsContract.Contacts.DISPLAY_NAME,
		                                        ContactsContract.CommonDataKinds.Phone.NUMBER};
		String selection       = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1'";
		String[] selectionArgs = null;
		String sortOrder       = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

		people          = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

		indexName   = people.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
		people.moveToFirst();
		do {
		    String strName   = people.getString(indexName);
		    String number = people.getString(indexNumber);
		    Log.e("", PhoneNumberUtils.stripSeparators(number));
        	HashMap<String, Object> map = new HashMap<String, Object>();
        	map.put("item1", strName);
        	map.put("item2", number);
        	map.put("icone", ICONES[getOperadora(number)]);
        	fillMaps.add(map);
		    
		} while (people.moveToNext());//*/
		adapter = new SimpleAdapter(this, fillMaps, R.layout.grid_item, from, to);
        lv.setAdapter(adapter);//*/
        lv.setOnItemLongClickListener(this);
   }
    
    private int getOperadora(String number) {
		if(operadoras.containsKey(number))return operadoras.get(number);
		return 0;
	}

	private void setIcon(int index, int icon, List<HashMap<String, Object>> maps){
         	HashMap<String, Object> map = (HashMap<String, Object>) adapter.getItem(index);
        	map.put("icone", ICONES[icon]);
        	setOperadora((String) map.get("item2"), icon);
//        	maps.set(index, map);
        	adapter.notifyDataSetChanged();
    }

	private void setOperadora(String numero, int icon) {
		operadoras.put(numero, icon);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
		Log.e("", v.findViewById(R.id.imageView1).toString());
        startActivity( new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT, Uri.parse("tel: 75360308") ));
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapter, View v, int position,
			long id) {
			if(!people.moveToPosition(position)) return true;
			Intent intent = new Intent(this, ImagePicker.class);
			intent.putExtra(ImagePicker.NOME, people.getString(indexName));
			String number = people.getString(indexNumber);
			intent.putExtra(ImagePicker.NUMERO, number);
			intent.putExtra(ImagePicker.POSITION, position);
			intent.putExtra(ImagePicker.ICONE, getOperadora(number));
			startActivityForResult(intent, PICK_IMAGE);
//			Log.e("", "LongClick View: "+v+"\nPosition: "+position+"\nId: "+id);
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("", "Result: "+resultCode+" request: "+requestCode);
		if(resultCode == RESULT_OK){
			int position = data.getIntExtra(ImagePicker.POSITION, 0);
			int icone = data.getIntExtra(ImagePicker.ICONE, 0);
			Log.e("", "Position: "+position+" icone: "+icone);
			setIcon(position, icone, fillMaps);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void finish() {
		Editor editor = getPreferences(MODE_PRIVATE).edit();
		Set<String> keySet = operadoras.keySet();
		for (String numero : keySet) {
			editor.putInt(numero, getOperadora(numero));
		}
		editor.commit();
		super.finish();
	}
	
	private void startup(){
		SharedPreferences pref = getPreferences(MODE_PRIVATE);
		Set<String> keySet = pref.getAll().keySet();
		for (String numero : keySet) {
			setOperadora(numero, pref.getInt(numero, 0));
		}
	}
	
	private void exportData(){
		
	}
	
	private void importData(){
		
	}
	
	private void saveData() throws IOException{
		FileOutputStream fo = openFileOutput(INTERNO,	MODE_PRIVATE);
		fo.write(getOperadoras().getBytes());
		fo.close();
	}
		
	private String getOperadoras() {
		Set<String> keys = operadoras.keySet();
		StringBuffer sb =  new StringBuffer();
		for (String number : keys) {
			sb.append(getOperadora(number));
			sb.append(';');
			sb.append(number);
			sb.append('\n');
		}
		return sb.toString();
	}

	private void loadData() throws IOException{
		FileInputStream fi = openFileInput(INTERNO);
		int len = fi.available();
		byte[] buffer =  new byte[len];
		fi.read(buffer );
		setOperadoras(buffer);
		fi.close();
	}

	private void setOperadoras(byte[] buffer) {
		for(int i = 0; i<buffer.length;i++){
			
		}
			
	}
	
    public void imageClick(View v){
    	Log.e("", "clicou na imagem: "+((View)v.getParent()).getTag());
    }
}
