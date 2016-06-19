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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.thermostatapp.util.HeatingSystem;
import org.thermostatapp.util.Switch;
import org.thermostatapp.util.WeekProgram;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;



public class ThermostatActivity extends AppCompatActivity {

    //double vtemp;
    WeekProgram wpg;
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
    String dayViewS, timeViewS, currTempViewS, dayTempViewS, nightTempViewS, vacViewS, nextSwitchTimeS, targetTempS;
    String[] valid_days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    TextView temp, dayView, timeView, currTempView, dayTempView, nightTempView, vacView, tempWarningView, currentTempView, nextSwitchVal;
    static int counter = 0;
    int timeInInt;
    Timer timer = new Timer();
    TimerTask timerTask;
    ArrayList<Switch> switches;
    android.widget.Switch vacSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_thermostat);

        dayView = (TextView) findViewById(R.id.dayView);
        timeView = (TextView) findViewById(R.id.timeView);
        tempWarningView = (TextView) findViewById(R.id.tempWarningView);
        currentTempView = (TextView) findViewById(R.id.currentTempView);
        nextSwitchVal = (TextView) findViewById(R.id.nextSwitchVal);

        bPlus = (ImageView) findViewById(R.id.bPlus);
        bPlus0_1 = (ImageView) findViewById(R.id.bPlus0_1);
        bMinus = (ImageView) findViewById(R.id.bMinus);
        bMinus0_1 = (ImageView) findViewById(R.id.bMinus0_1);
        Button scheduleReturnButton = (Button) findViewById(R.id.ScheduleReturnButton);
        vacSwitch = (android.widget.Switch) findViewById(R.id.Vacswitch);

        temp = (TextView) findViewById(R.id.temp);
        Button weekOverview = (Button) findViewById(R.id.week_overview);

        assert weekOverview != null;
        weekOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), WeekOverview.class);
                intent.putExtra("nightTemp", nightTempViewS);
                intent.putExtra("dayTemp", dayTempViewS);

                startActivity(intent);
            }
        });

        assert bPlus != null;
        bPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vtemp.compareTo(twenine) <= 0) {
                    vtemp = vtemp.add(one);
                    temp.setText(vtemp + " \u2103");
                    fixArrows();
                    new SetTemp().execute();
                }
            }
        });

        assert bPlus0_1 != null;
        bPlus0_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vtemp.compareTo(thirty) < 0) {
                    vtemp = vtemp.add(pointone);
                    temp.setText(vtemp + " \u2103");
                    fixArrows();
                    new SetTemp().execute();
                }
            }
        });

        assert bMinus != null;
        bMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vtemp.compareTo(six) >= 0) {
                    vtemp = vtemp.subtract(one);
                    temp.setText(vtemp + " \u2103");
                    fixArrows();
                    new SetTemp().execute();
                }
            }
        });


        assert bMinus0_1 != null;
        bMinus0_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vtemp.compareTo(five) > 0) {
                    vtemp = vtemp.subtract(pointone);
                    temp.setText(vtemp + " \u2103");
                    fixArrows();
                    new SetTemp().execute();
                }
            }
        });


        assert scheduleReturnButton != null;
        scheduleReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        vacSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new SetVacationOn().execute();
                } else {
                    new SetVacationOff().execute();
                }
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();

        new GetTargetTemp().execute();
        new GetNextSwitch().execute();
        timer.scheduleAtFixedRate(new myTimerTask(), 0, 1000);
    }

    protected void onPause() {
        super.onPause();
        timer.cancel();
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

    private class GetTargetTemp extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                targetTempS = HeatingSystem.get("targetTemperature");
                vtemp = new BigDecimal(targetTempS);
                vtemp.setScale(10, BigDecimal.ROUND_CEILING);
            } catch (Exception e) {
                System.err.println("Error from getdata " + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            temp.setText(vtemp + " \u2103");
        }
    }

    private class SetVacationOn extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HeatingSystem.put("weekProgramState", "off");
            } catch (Exception e) {
                System.err.println("Error from getdata " + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    private class SetVacationOff extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HeatingSystem.put("weekProgramState", "on");
            } catch (Exception e) {
                System.err.println("Error from getdata " + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    private class GetNextSwitch extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                wpg = HeatingSystem.getWeekProgram();
                timeViewS = HeatingSystem.get("time");
                timeInInt = timeToInt(timeViewS);
                dayViewS = HeatingSystem.get("day");
                dayTempViewS = HeatingSystem.get("dayTemperature");
                nightTempViewS = HeatingSystem.get("nightTemperature");
            } catch (Exception e) {
                System.err.println("Error from getdata " + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            switches = wpg.getSwitchArrayL(dayToInt(dayViewS));

            nextSwitchTimeS = dayOfNextSwitch() + " at " + timeOfNextSwitch() + " to " + tempOfNextSwitch() + " \u2103";
            nextSwitchVal.setText(nextSwitchTimeS);
        }
    }

    /*
    vacViewS = HeatingSystem.get("weekProgramState");

    if (vacViewS.equals("on")) {
        tempWarningView.setText(" ");
    } else {
        tempWarningView.setText("Weekprogram is now overwritten");
    }
    */

    private class SetTemp extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                HeatingSystem.put("targetTemperature", vtemp.toString());
            } catch (Exception e) {
                System.err.println("Error from getdata " + e);
            }

            return null;
        }
    }

    // this method changes a string of a day into an int
    // Monday -> 0, Tuesday -> 1, etc...
    public int dayToInt(String day) {
        for (int i = 0; i < valid_days.length; i++) {
            if (valid_days[i].equals(day)) {
                return i;
            }
        }
        return 0;
    }

    // changes a time into hours * 100
    // copied from Switch class
    public int timeToInt(String time) {
        String front = time.substring(0, 2);
        // Get the first 2 digits if they are there.
        String back = time.substring(3, 5);
        // Get the last 2 digits if they are there.
        int front_int = Integer.parseInt(front);
        int back_int = Integer.parseInt(back);
        int time_int = front_int * 100
                + (int) ((float) back_int / 60.0 * 100.0);
        System.out.println(time_int);
        return time_int;
    }

    // returns if the next switch is today
    // the public arraylist switches, should already be set to the switches of today
    // the time should also be up-to-date
    public boolean isNextSwitchToday() {
        boolean nextSwitchFound = false;

        for (Switch s : switches) {
            if (s.getState() && s.getTime_Int() > timeInInt) {
                nextSwitchFound = true;
            }
        }

        return nextSwitchFound;
    }

    // returns the time of the next switch
    public String timeOfNextSwitch() {
        int lowestSwitchTime = 0;
        boolean firstSwitchFound = false;

        if (isNextSwitchToday()) {
            for (Switch s : switches) {
                if (s.getState() && s.getTime_Int() > timeInInt) {
                    if (!firstSwitchFound || s.getTime_Int() < lowestSwitchTime) {
                        firstSwitchFound = true;
                        lowestSwitchTime = s.getTime_Int();
                        nextSwitchTimeS = s.getTime();
                    }
                }
            }
            return nextSwitchTimeS;
        } else {
            return "00:00";
        }

    }

    // returns the day of the next switch
    public String dayOfNextSwitch() {

        if (isNextSwitchToday()) {
            return dayViewS;
        } else {
            int nextDayInt = dayToInt(dayViewS) + 1;

            if (nextDayInt == 7) {
                nextDayInt = 0;
            }
            return valid_days[nextDayInt];
        }
    }

    // returns the temperature of the next switch
    public String tempOfNextSwitch() {
        if (isNextSwitchToday()) {
            String timeNextSwitch = timeOfNextSwitch();

            for (Switch s : switches) {
                if (s.getTime() == timeNextSwitch) {
                    if (s.getType().equals("night")) {
                        return nightTempViewS;
                    } else {
                        return dayTempViewS;
                    }
                }
            }
        } else {
            return nightTempViewS;
        }
        return "?";
    }


    private class myTimerTask extends TimerTask {

        @Override
        public void run() {
            try {
                timeViewS = HeatingSystem.get("time");
                dayViewS = HeatingSystem.get("day");
                currTempViewS = HeatingSystem.get("currentTemperature");
            } catch (Exception e) {
                System.err.println("Error from getdata " + e);
            }
            updateView.sendEmptyMessage(0);
        }
    }

    private Handler updateView = new Handler() {
        public void handleMessage(Message msg) {
            timeView.setText(timeViewS);
            dayView.setText(dayViewS);
            currentTempView.setText("Currently: " + currTempViewS + " ℃");
        }
    };
}