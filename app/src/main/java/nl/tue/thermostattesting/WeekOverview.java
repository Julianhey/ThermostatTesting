package nl.tue.thermostattesting;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.view.Window;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.Spinner;
        import android.widget.SpinnerAdapter;
        import android.widget.TextView;
        import android.widget.CompoundButton;



        import org.thermostatapp.util.HeatingSystem;
        import org.thermostatapp.util.Switch;
        import org.thermostatapp.util.WeekProgram;

        import java.util.ArrayList;

/**
 * Created by Julian on 23-5-2016.
 * Edited by Koen on 31-5 at 17.56
 * Edit2
 */
public class WeekOverview extends AppCompatActivity {

    Button Mondaybutton, Tuesdaybutton, Wednesdaybutton, Thursdaybutton, Fridaybutton, Saturdaybutton, Sundaybutton;
    Button thermostat_activity;
    android.widget.Switch vacSwitch;
    WeekProgram wpg;
    String Tuesday, dayViewS, timeViewS, currTempViewS,dayTempViewS, nightTempViewS, vacViewS;
    ArrayList<Switch> TuesdaySwitches;
    //TextView timeBlock0, timeBlock1, timeBlock2, timeBlock3, timeBlock4, timeBlock5, timeBlock6, timeBlock7, timeBlock8;
    String [] timeBlockS = new String[9];
    TextView[] timeBlockViews = new TextView[9];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.week_overview);

        //Code for a spinner if needed.
        //Spinner presetSpinner = (Spinner) findViewById(R.id.presetSpinner);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Week_Preset_Names, android.R.layout.simple_spinner_dropdown_item);
        //presetSpinner.setAdapter(adapter);



        timeBlockViews[0] = (TextView)findViewById(R.id.timeBlock0);
        timeBlockViews[1] = (TextView)findViewById(R.id.timeBlock1);
        timeBlockViews[2] = (TextView)findViewById(R.id.timeBlock2);
        timeBlockViews[3] = (TextView)findViewById(R.id.timeBlock3);
        timeBlockViews[4] = (TextView)findViewById(R.id.timeBlock4);
        timeBlockViews[5] = (TextView)findViewById(R.id.timeBlock5);
        timeBlockViews[6] = (TextView)findViewById(R.id.timeBlock6);
        timeBlockViews[7] = (TextView)findViewById(R.id.timeBlock7);
        timeBlockViews[8] = (TextView)findViewById(R.id.timeBlock8);


        thermostat_activity = (Button) findViewById(R.id.thermostat_activity);
        Mondaybutton = (Button)findViewById(R.id.Mondaybutton);
        vacSwitch = (android.widget.Switch) findViewById(R.id.Vacswitch);


        thermostat_activity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });


        vacSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HeatingSystem.put("weekProgramState", "off");
                            } catch (Exception e) {
                                System.err.println("Error from getdata " + e);
                            }
                        }
                        }).start();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                    try {
                        HeatingSystem.put("weekProgramState", "on");
                    } catch (Exception e) {
                        System.err.println("Error from getdata " + e);
                    }
                }
            }).start();
                }
            }
        });

        Mondaybutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                getAndDisplayWPG();

            }
        });

    }

    public void getAndDisplayWPG() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //dayViewS = HeatingSystem.get("day");
                    //timeViewS = HeatingSystem.get("time");
                    //currTempViewS = HeatingSystem.get("currentTemperature");
                    //dayTempViewS = HeatingSystem.get("dayTemperature");
                    //nightTempViewS = HeatingSystem.get("nightTemperature");
                    //vacViewS = HeatingSystem.get("weekProgramState");


                    wpg = HeatingSystem.getWeekProgram();
                    TuesdaySwitches = wpg.getSwitchArrayL(1);



                    for (int i = 0; i < 9; i++) {

                        int startTime = TuesdaySwitches.get(i).getTime_Int();
                        int durationTime = TuesdaySwitches.get(i).getDur();
                        String startTimeS = int_time_to_string(startTime);
                        String endTimeS = int_time_to_string(startTime + durationTime);
                        if (TuesdaySwitches.get(i).getState()) {
                            timeBlockS[i] = startTimeS + " - " + endTimeS;
                        }else{
                            timeBlockS[i] = "";
                        }
                    }




                } catch (Exception e) {
                    System.err.println("Error from getdata "+e);
                }
            }
        }).start();

        try {
            Thread.sleep(300);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }


        for (int i = 0; i < 9; i++) {
            if (TuesdaySwitches.get(i).getType().equals("night")) {
                timeBlockViews[i].setBackgroundColor(android.graphics.Color.argb(255, 30, 144, 255));
                timeBlockViews[i].setTextColor(android.graphics.Color.argb(255, 255, 255, 255));
            }else if (TuesdaySwitches.get(i).getType().equals("day")){
                timeBlockViews[i].setBackgroundColor(android.graphics.Color.argb(255, 240,230,140));
            }
            timeBlockViews[i].setText(timeBlockS[i]);
        }

       // dayView.setText("Day: " + dayTempViewS);

    }


    String int_time_to_string(int time_var) {
        String hours = Integer.toString(time_var / 100);
        String mins = Integer.toString(time_var - time_var / 100 * 100);
        if (time_var < 1000)
            hours = "0" + hours;
        if (time_var - time_var / 100 * 100 < 10)
            mins = "0" + mins;

        return hours + ":" + mins;
    }
}
