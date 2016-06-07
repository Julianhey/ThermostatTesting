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

public class ThermostatActivity extends AppCompatActivity {

    int vtemp = 21;
    TextView temp;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_thermostat);
        Button bPlus = (Button) findViewById(R.id.bPlus);
        Button bMinus = (Button) findViewById(R.id.bMinus);
        temp = (TextView) findViewById(R.id.temp);
        Button weekOverview = (Button) findViewById(R.id.week_overview);

        weekOverview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(view.getContext(), WeekOverview.class);
                startActivity(intent);
            }
        });

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(vtemp);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                temp.setText(progress+" \u2103");
                vtemp =  progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        bPlus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                vtemp ++;
                temp.setText(vtemp+" \u2103");
                seekBar.setProgress(vtemp);
            }
        });
        assert bMinus != null;
        bMinus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                vtemp--;
                temp.setText(vtemp+" \u2103");
                seekBar.setProgress(vtemp);
            }
        });


    }
}
