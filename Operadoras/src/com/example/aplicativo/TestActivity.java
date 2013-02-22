package com.example.aplicativo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class TestActivity extends Activity {
        
//        private ViewFlipper flipper = null;
        private GestureDetector gesturedetector = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  //      flipper = new ViewFlipper(this);
        gesturedetector = new GestureDetector(this, new GD());
        gesturedetector.setOnDoubleTapListener(new GD());
/*        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.p0);
		//*/setContentView(R.layout.test_layout);
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