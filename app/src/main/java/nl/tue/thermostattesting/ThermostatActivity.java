package nl.tue.thermostattesting;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.thermostatapp.util.HeatingSystem;
import org.thermostatapp.util.WeekProgram;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;



public class ThermostatActivity extends AppCompatActivity {

    //double vtemp;
    WeekProgram wpg;
    TextView temp;
    ImageView bPlus;
    ImageView bPlus0_1;
    ImageView bMinus;
    ImageView bMinus0_1;
    BigDecimal vtemp;
    BigDecimal pointone = new BigDecimal("0.1");
    BigDecimal one = new BigDecimal("1");
    BigDecimal five = new BigDecimal("5");
    BigDecimal six = new BigDecimal("6");
    BigDecimal thirty = new BigDecimal("30");
    BigDecimal twenine = new BigDecimal("29");
    String dayViewS, timeViewS, currTempViewS, dayTempViewS, nightTempViewS, vacViewS;
    TextView dayView, timeView, currTempView, dayTempView, nightTempView, vacView;
    static int counter = 0;
    Timer timer;
    TimerTask timerTask;
    boolean notinOverview = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_thermostat);

        dayView = (TextView) findViewById(R.id.dayView);
        timeView = (TextView) findViewById(R.id.timeView);

        bPlus = (ImageView) findViewById(R.id.bPlus);
        bPlus0_1 = (ImageView) findViewById(R.id.bPlus0_1);
        bMinus = (ImageView) findViewById(R.id.bMinus);
        bMinus0_1 = (ImageView) findViewById(R.id.bMinus0_1);
        Button scheduleReturnButton = (Button) findViewById(R.id.ScheduleReturnButton);

        temp = (TextView) findViewById(R.id.temp);
        Button weekOverview = (Button) findViewById(R.id.week_overview);

        assert weekOverview != null;
        weekOverview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


                Intent intent = new Intent(view.getContext(), WeekOverview.class);
                intent.putExtra("nightTemp", nightTempViewS);
                intent.putExtra("dayTemp", dayTempViewS);

                startActivity(intent);
            }
        });

        assert bPlus != null;
        bPlus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (vtemp.compareTo(twenine) <= 0){
                    vtemp = vtemp.add(one);
                    temp.setText(vtemp+" \u2103");
                    fixArrows();
                }
            }
        });

        assert bPlus0_1 != null;
        bPlus0_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (vtemp.compareTo(thirty) < 0){
                    vtemp = vtemp.add(pointone);
                    temp.setText(vtemp+" \u2103");
                    fixArrows();
                }
            }
        });

        assert bMinus != null;
        bMinus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (vtemp.compareTo(six) >= 0) {
                    vtemp = vtemp.subtract(one);
                    temp.setText(vtemp + " \u2103");
                    fixArrows();
                }
            }
        });


        assert bMinus0_1 != null;
        bMinus0_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (vtemp.compareTo(five) > 0){
                    vtemp = vtemp.subtract(pointone);
                    temp.setText(vtemp+" \u2103");
                    fixArrows();
                }
            }
        });


        assert scheduleReturnButton != null;
        scheduleReturnButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

            }
        });





    }

    @Override
    protected void onResume() {
        super.onResume();

        notinOverview = true;
        resume();
        new GetTemps().execute();

    }

    protected void onPause(){
        super.onPause();

        notinOverview = false;
        pause();
    }

    void fixArrows() {
        if (vtemp.compareTo(thirty) == 0) {
            bPlus0_1.setImageResource(R.drawable.arrow_up_gray);
        } else {
            bPlus0_1.setImageResource(R.drawable.arrow_up);
        }
        if (vtemp.compareTo(twenine) > 0) {
            bPlus.setImageResource(R.drawable.arrow_up_gray);
        } else {
            bPlus.setImageResource(R.drawable.arrow_up);
        }
        if (vtemp.compareTo(six) < 0) {
            bMinus.setImageResource(R.drawable.arrow_down_gray);
        } else {
            bMinus.setImageResource(R.drawable.arrow_down);
        }
        if (vtemp.compareTo(five) == 0) {
            bMinus0_1.setImageResource(R.drawable.arrow_down_gray);
        } else {
            bMinus0_1.setImageResource(R.drawable.arrow_down);
        }
    }

    private class GetTemps extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void...params){

            try{
                vtemp = new BigDecimal(HeatingSystem.get("dayTemperature"));             //.valueOf(vtemp);
                vtemp.setScale(10, BigDecimal.ROUND_CEILING);
                dayViewS = HeatingSystem.get("day");


            }catch (Exception e){
                System.err.println("Error from getdata " + e);
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result){

            temp.setText(vtemp + " \u2103");
            dayView.setText(dayViewS);
            timeView.setText(timeViewS);


        }
    }



    public void pause(){
        this.timer.cancel();
    }

    public void resume(){
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new myTimerTask(), 0, 1000);
    }


    private class myTimerTask extends TimerTask {

        @Override
        public void run() {
            if (notinOverview) {
                try {
                    timeViewS = HeatingSystem.get("time");
                } catch (Exception e) {
                    System.err.println("Error from getdata " + e);
                }
                updatetimeView.sendEmptyMessage(0);
            }
        }
    }



        private Handler updatetimeView = new Handler() {
            public void handleMessage(Message msg) {
                timeView.setText(timeViewS);
            }
        };
    }
