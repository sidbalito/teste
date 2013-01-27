package com.example.aplicativo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Tela extends Activity {
	
    private static final String DRAWABLE = "android.resource://com.example.aplicativo/";
	private static final String[] ICONE = new String[]{
    	DRAWABLE+R.drawable.g0,
    	DRAWABLE+R.drawable.g1,
    	DRAWABLE+R.drawable.g2,
    	DRAWABLE+R.drawable.g3,
    	DRAWABLE+R.drawable.g4
    };

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_tela);
        ListView lv= (ListView)findViewById(R.id.contatos);

        // create the grid item mapping
        String[] from = new String[] {"item1", "item2", "icone"};
        int[] to = new int[] { R.id.item1, R.id.item2, R.id.imageView1};

        // prepare the list of all records
        List<HashMap<String, Object>> fillMaps = new ArrayList<HashMap<String, Object>>();
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
        	map.put("icone", ICONE[0]);
        	fillMaps.add(map);
        }//*/		
        
		Uri uri                = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String[] projection    = new String[] { ContactsContract.Contacts.DISPLAY_NAME,
		                                        ContactsContract.CommonDataKinds.Phone.NUMBER};
		String selection       = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1'";
		String[] selectionArgs = null;
		String sortOrder       = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

		Cursor people          = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

		int indexName   = people.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

		people.moveToFirst();
		do {
		    String strName   = people.getString(indexName);
		    String number = people.getString(indexNumber);
        	HashMap<String, Object> map = new HashMap<String, Object>();
        	map.put("item1", strName);
        	map.put("item2", number);
        	map.put("icone", ICONE[0]);
        	fillMaps.add(map);
		    
		} while (people.moveToNext());//*/
		
		SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.grid_item, from, to);
        lv.setAdapter(adapter);//*/
    }
}
