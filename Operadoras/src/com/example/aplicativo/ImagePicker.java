package com.example.aplicativo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class ImagePicker extends Activity {

	public static final String NOME = "nome";
	public static final String NUMERO = "numero";
	public static final String ICONE = "icone";
	static final String POSITION = "position";
	
	public static final String DRAWABLE =  Tela.DRAWABLE;
	public static final String[] ICONES = new String[]{
    	DRAWABLE+R.drawable.p0,
    	DRAWABLE+R.drawable.p1,
    	DRAWABLE+R.drawable.p2,
    	DRAWABLE+R.drawable.p3,
    	DRAWABLE+R.drawable.p4
    };

	
	private GestureDetector gd;
	private ViewFlipper vf;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_piker);
		Intent intent = getIntent();
		((TextView)findViewById(R.id.tvNome)).setText(intent.getStringExtra(NOME));
		((TextView)findViewById(R.id.tvTelefone)).setText(intent.getStringExtra(NUMERO));//*/
		position = intent.getIntExtra(POSITION, 0);
		vf = (ViewFlipper) findViewById(R.id.view_flipper);// new ViewFlipper(this);
		ImageView iv;
		for(int i = 0; i<ICONES.length;i++){
			iv = new ImageView(this);
			iv.setImageURI(Uri.parse(ICONES[i]));
			vf.addView(iv);
		}
		vf.setDisplayedChild(intent.getIntExtra(ICONE, 0));
		GestureListener gl = new GestureListener(this, vf);
		gd = new GestureDetector(this, gl);
		gd.setOnDoubleTapListener(gl);
	}	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gd.onTouchEvent(event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_image_piker, menu);
		return true;
	}



	public void setResult() {
		Intent intent = getIntent();
		intent.putExtra(POSITION, position);
		intent.putExtra(ICONE, vf.getDisplayedChild());
		setResult(RESULT_OK, intent );
		finish();
	}

}
class GestureListener extends SimpleOnGestureListener{
	private ViewFlipper vf;
	private ImagePicker activity;
	public GestureListener(Activity activity, ViewFlipper vf) {
		this.vf = vf;
		this.activity = (ImagePicker) activity;
	}
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
    	if(distanceX>20)  vf.showNext();//Log.e("", "Para a esquerda");
    	if(distanceX<-20) vf.showPrevious();//Log.e("", "Para a direita");
        return false;
    }
    
    @Override
    public void onLongPress(MotionEvent e) {
    	super.onLongPress(e);
    	
    }
    
    @Override
    public boolean onDoubleTap(MotionEvent e) {
     	activity.setResult();
     	
   	return true;
    }
    
    
    
}
