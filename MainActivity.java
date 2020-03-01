package com.example.elavi.bluetoothapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    ListView listview;
    BluetoothAdapter bluetoothAdapter;

    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          String action=intent.getAction();
            System.out.println("Actions is "+action);
            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                button.setEnabled(true);
                textView.setText("Finished");
            }
            else if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                String name="";
                String rssi="";
                String address="";
                BluetoothDevice bluetoothDevice=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                try {
                     name = bluetoothDevice.getName();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    address = bluetoothDevice.getAddress();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                try {
                    rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                Log.i("Signal Info is :","name " +name+ " Address "+address+" rssi " +rssi);
            }
        }
    };
    public void search(View view)
    {
     textView.setText("Searching....");
     button.setEnabled(false);
        bluetoothAdapter.startDiscovery();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button);
        textView=findViewById(R.id.textview);
        listview=findViewById(R.id.listview);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver,intentFilter);
    }
}
