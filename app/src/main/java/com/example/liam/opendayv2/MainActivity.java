package com.example.liam.opendayv2;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private  static final int ERROR_DIALOG_REQUEST  = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkservices())
        {
            init();
            Time();
        }

    }
    private void init()
    {
        Button btnmap = (Button) findViewById(R.id.btnmap);
        btnmap.setOnClickListener(new View.OnClickListener()
        {
            @Override
                    public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }

        });
    }

    private void Time()
    {
        Button btntime = (Button) findViewById(R.id.btntime);
        btntime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent timeAc = new Intent(MainActivity.this, TimeTableActivity.class);
                        startActivity(timeAc);
            }
        });



    }

    public boolean checkservices(){

        Log.d(TAG, "checkservices: Checking Google Service Version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        
        if(available == ConnectionResult.SUCCESS)
        {
            //If version correct
            Log.d(TAG, "checkservices: Service is correct");
            
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            Log.d(TAG, "checkservices: An error occured");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();

        }
        else
        {
            Toast.makeText(this,"You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;

    }
}
