package com.example.aplicativo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class FlipperActivity extends Activity {
        
        private ViewFlipper flipper = null;
        private GestureDetector gesturedetector = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        flipper = new ViewFlipper(this);
        //addViews();
        gesturedetector = new GestureDetector(this, new GD());
        gesturedetector.setOnDoubleTapListener(new GD());
        setContentView(flipper);
    }

        
        private void addViews(){
                for(int index=0; index<3; ++index)
                {
                        flipper.addView(new TextView(this),index);
                }//*/
        }
        

        @Override
        public boolean onTouchEvent(MotionEvent event) {
                return gesturedetector.onTouchEvent(event);
        }
}

class GD extends SimpleOnGestureListener{
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
       	Log.e("", "GD scroll x:"+(int)distanceX +"y: "+ (int)distanceY);
        return false;
}
	
}