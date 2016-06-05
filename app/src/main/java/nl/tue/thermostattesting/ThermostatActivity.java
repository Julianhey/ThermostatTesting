package nl.tue.thermostattesting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.thermostatapp.util.HeatingSystem;

import java.math.BigDecimal;

public class ThermostatActivity extends AppCompatActivity {

    //double vtemp;
    TextView temp;
    BigDecimal vtemp;
    BigDecimal pointone = new BigDecimal("0.1");


    public void getcurrentTemp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
        try {
            //vtemp = Double.parseDouble(HeatingSystem.get("currentTemperature"));
            vtemp = new BigDecimal(HeatingSystem.get("currentTemperature"));             //.valueOf(vtemp);
            vtemp.setScale(10, BigDecimal.ROUND_CEILING);
            System.out.println(vtemp);

        } catch (Exception e) {
            System.err.println("Error from getdata "+e);
        }
    }
        }).start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_thermostat);
        Button bPlus = (Button) findViewById(R.id.bPlus);
        Button bMinus = (Button) findViewById(R.id.bMinus);
        temp = (TextView) findViewById(R.id.temp);
        Button weekOverview = (Button) findViewById(R.id.week_overview);
        temp.setText("- \u2103");
        getcurrentTemp();
        System.out.println(vtemp);
        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        temp.setText(vtemp+" \u2103");

        weekOverview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(view.getContext(), WeekOverview.class);
                startActivity(intent);
            }
        });


        bPlus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                vtemp = vtemp.add(pointone);
                temp.setText(vtemp+" \u2103");

            }
        });
        assert bMinus != null;
        bMinus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                vtemp = vtemp.subtract(pointone);
                temp.setText(vtemp+" \u2103");

            }
        });


    }
}
