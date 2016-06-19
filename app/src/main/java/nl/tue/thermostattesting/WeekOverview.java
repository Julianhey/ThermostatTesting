package nl.tue.thermostattesting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.Toast;


import org.thermostatapp.util.HeatingSystem;
import org.thermostatapp.util.Switch;
import org.thermostatapp.util.WeekProgram;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Julian on 23-5-2016.
 * Edited by Koen on 31-5 at 17.56
 * Edit2
 */
public class WeekOverview extends Activity {

    Button thermostat_activity;
    android.widget.Switch vacSwitch;
    WeekProgram wpg;
    String Tuesday, dayViewS, timeViewS, currTempViewS, dayTempViewS, nightTempViewS, vacViewS;
    ArrayList<Switch> DaySwitches;
    //TextView timeBlock0, timeBlock1, timeBlock2, timeBlock3, timeBlock4, timeBlock5, timeBlock6, timeBlock7, timeBlock8;
    ArrayList<String> timeBlockS = new ArrayList<>();
    TextView[] timeBlockViews = new TextView[9];

    TextView tempDay, tempNight;
    BigDecimal vtempD, vtempN;
    BigDecimal pointone = new BigDecimal("0.1");
    BigDecimal one = new BigDecimal("1");
    BigDecimal five = new BigDecimal("5");
    BigDecimal six = new BigDecimal("6");
    BigDecimal thirty = new BigDecimal("30");
    BigDecimal twenine = new BigDecimal("29");

    LinearLayout linlaHeaderProgress;
    LinearLayout week_overview;
    ViewGroup.LayoutParams params;

    ExpandableListView switchListView;

    String[] valid_days = {"Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"};

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    SparseArray<Group> groups = new SparseArray<Group>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.week_overview);
        week_overview = new LinearLayout(this);

        //Code for a spinner if needed.
        //Spinner presetSpinner = (Spinner) findViewById(R.id.presetSpinner);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Week_Preset_Names, android.R.layout.simple_spinner_dropdown_item);
        //presetSpinner.setAdapter(adapter);


        thermostat_activity = (Button) findViewById(R.id.thermostat_activity);
        //Mondaybutton = (Button) findViewById(R.id.Mondaybutton);
        vacSwitch = (android.widget.Switch) findViewById(R.id.Vacswitch);

        ImageView bPlus = (ImageView) findViewById(R.id.bPlus);
        ImageView bPlus0_1 = (ImageView) findViewById(R.id.bPlus0_1);
        ImageView bMinus = (ImageView) findViewById(R.id.bMinus);
        ImageView bMinus0_1 = (ImageView) findViewById(R.id.bMinus0_1);
        ImageView bPlusNight = (ImageView) findViewById(R.id.bPlusNight);
        ImageView bPlus0_1Night = (ImageView) findViewById(R.id.bPlus0_1Night);
        ImageView bMinusNight = (ImageView) findViewById(R.id.bMinusNight);
        ImageView bMinus0_1Night = (ImageView) findViewById(R.id.bMinus0_1Night);



        tempDay = (TextView) findViewById(R.id.tempDay);
        tempNight = (TextView) findViewById(R.id.tempNight);

        linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);

        switchListView = (ExpandableListView) findViewById(R.id.SwitchlistView);


        //getWeekProgram();
        //getcurrentT();

        new GetTemps().execute();
        //new GetInfo().execute();
        new GetInfo2nd().execute();









        switchListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                        listDataHeader.get(groupPosition);

            }
        });


        thermostat_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        assert bPlus != null;
        bPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vtempD.compareTo(twenine) <= 0) {
                    vtempD = vtempD.add(one);
                    tempDay.setText(vtempD + " \u2103");
                    new SetTemp().execute();
                } else {
                    //Make a popup error thing that says that you cant set the temp to < 5
                }
            }
        });

        assert bPlus0_1 != null;
        bPlus0_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vtempD.compareTo(thirty) < 0) {
                    vtempD = vtempD.add(pointone);
                    tempDay.setText(vtempD + " \u2103");
                } else {
                    //Make a popup error thing that says that you cant set the temp to < 5
                }
            }
        });

        assert bMinus != null;
        bMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vtempD.compareTo(six) >= 0) {
                    vtempD = vtempD.subtract(one);
                    tempDay.setText(vtempD + " \u2103");
                } else {
                    //Make a popup error thing that says that you cant set the temp to < 5
                }

            }
        });


        assert bMinus0_1 != null;
        bMinus0_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vtempD.compareTo(five) > 0) {
                    vtempD = vtempD.subtract(pointone);
                    tempDay.setText(vtempD + " \u2103");
                } else {
                    //Make a popup error thing that says that you cant set the temp to < 5
                }
            }
        });

        assert bPlusNight != null;
        bPlusNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vtempN.compareTo(twenine) <= 0) {
                    vtempN = vtempN.add(one);
                    tempNight.setText(vtempN + " \u2103");
                } else {
                    //Make a popup error thing that says that you cant set the temp to < 5
                }
            }
        });

        assert bPlus0_1Night != null;
        bPlus0_1Night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vtempN.compareTo(thirty) < 0) {
                    vtempN = vtempN.add(pointone);
                    tempNight.setText(vtempN + " \u2103");
                } else {
                    //Make a popup error thing that says that you cant set the temp to < 5
                }
            }
        });

        assert bMinusNight != null;
        bMinusNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vtempN.compareTo(six) >= 0) {
                    vtempN = vtempN.subtract(one);
                    tempNight.setText(vtempN + " \u2103");
                } else {
                    //Make a popup error thing that says that you cant set the temp to < 5
                }

            }
        });


        assert bMinus0_1Night != null;
        bMinus0_1Night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vtempN.compareTo(five) > 0) {
                    vtempN = vtempN.subtract(pointone);
                    tempNight.setText(vtempN + " \u2103");
                } else {
                    //Make a popup error thing that says that you cant set the temp to < 5
                }
            }
        });

        vacSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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


    }

    public ArrayList<String> Switchlist(final int daynumber) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    DaySwitches = wpg.getSwitchArrayL(daynumber);
                    timeBlockS.clear();

                    for (int i = 0; i < 10; i++) {

                        int startTime = DaySwitches.get(i).getTime_Int();
                        int durationTime = DaySwitches.get(i).getDur();
                        String startTimeS = int_time_to_string(startTime);
                        String endTimeS = int_time_to_string(startTime + durationTime);
                        if (DaySwitches.get(i).getState()) {
                            if (DaySwitches.get(i).getType().equals("night")) {
                                timeBlockS.add("N" + startTimeS + " - " + endTimeS);
                            } else if (DaySwitches.get(i).getType().equals("day")) {
                                timeBlockS.add("D" + startTimeS + " - " + endTimeS);
                            }

                        }
                    }


                } catch (Exception e) {
                    System.err.println("Error from getdata " + e);
                }
            }
        }).start();


        try {
            Thread.sleep(500);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

/**

        for (int i = 0; i < 9; i++) {
            if (DaySwitches.get(i).getType().equals("night")) {
                //Display night icon
            } else if (DaySwitches.get(i).getType().equals("day")) {
                //Display day icon
            }
        }
         */

        return timeBlockS;

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




    private class GetInfo2nd extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute(){
            linlaHeaderProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void...params){

            try{
                wpg = HeatingSystem.getWeekProgram();

                listDataHeader = new ArrayList<String>();
                listDataChild = new HashMap<String, List<String>>();

                // Adding child data
                for (int j = 0; j < 7; j++) {
                    listDataHeader.add(valid_days[j]);


                    // Adding child data
                    List<String> switches = Switchlist(j);



                    listDataChild.put(listDataHeader.get(j), switches); // Header, Child data
                }


            }catch (Exception e){
                System.err.println("Error from getdata " + e);
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result){

            ExpandableListAdapter2 adapter = new ExpandableListAdapter2(WeekOverview.this,
                    listDataHeader,listDataChild, week_overview, wpg);
            switchListView.setAdapter(adapter);
            linlaHeaderProgress.setVisibility(View.GONE);

        }
    }






    private class GetTemps extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void...params){

            try{
                vtempD = new BigDecimal(HeatingSystem.get("dayTemperature"));             //.valueOf(vtemp);
                vtempD.setScale(10, BigDecimal.ROUND_CEILING);
                System.out.println(vtempD);
                vtempN = new BigDecimal(HeatingSystem.get("dayTemperature"));             //.valueOf(vtemp);
                vtempN.setScale(10, BigDecimal.ROUND_CEILING);
                System.out.println(vtempD);

            }catch (Exception e){
                System.err.println("Error from getdata " + e);
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result){

            tempDay.setText(vtempD + " \u2103");
            tempNight.setText(vtempN + " \u2103");


        }
    }

    private class SetTemp extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void...params){

            try{
                HeatingSystem.put("nightTemperature", "6.0");


            }catch (Exception e){
                System.err.println("Error from getdata " + e);
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result){

            tempDay.setText(vtempD + " \u2103");
            tempNight.setText(vtempN + " \u2103");


        }
    }

}


