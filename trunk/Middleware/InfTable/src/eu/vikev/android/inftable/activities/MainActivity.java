package eu.vikev.android.inftable.activities;


import eu.vikev.android.inftable.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.util.Log;

public class MainActivity extends Activity {

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.i(MainActivity.class.getName(), "MainActivity is opening!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(MainActivity.class.getName(), "MainActivity opened!");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
