package com.example.elavi.bluetoothapp;
//list the devies in the list view and also mentions their information
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    ListView listview;
    BluetoothAdapter bluetoothAdapter;
    List<String> s = new ArrayList<String>();
    String name="";
    String rssi="";
    String address="";
    String toaddtolist="";

    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          String action=intent.getAction();
            System.out.println("Actions is "+action);
            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                button.setEnabled(true);
                textView.setText("Finished");
                s.clear();
                listview.setAdapter(null);
            }
            else if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
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
                for(BluetoothDevice bt : pairedDevices)
                    if(!s.contains(bt.getName()))
                    s.add("Device Name->"+bt.getName() +"---" +" Signal Strength-> "+rssi);
                Log.i("Signal Info is :","name " +name+ " Address "+address+" rssi " +rssi);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, s );
            listview.setAdapter(arrayAdapter);
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
