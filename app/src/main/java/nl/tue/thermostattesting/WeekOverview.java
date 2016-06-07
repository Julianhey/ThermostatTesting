package nl.tue.thermostattesting;

        import android.app.Activity;
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

        import org.thermostatapp.util.HeatingSystem;
        import org.thermostatapp.util.WeekProgram;

/**
 * Created by Julian on 23-5-2016.
 * Edited by Koen on 31-5 at 17.56
 * Edit2
 */
public class WeekOverview extends AppCompatActivity {

    Button Mondaybutton, Tuesdaybutton, Wednesdaybutton, Thursdaybutton, Fridaybutton, Saturdaybutton, Sundaybutton;
    WeekProgram wpg;
    String dayViewS, timeViewS, currTempViewS,dayTempViewS, nightTempViewS, vacViewS;
    TextView dayView, timeView, currTempView, dayTempView, nightTempView, vacView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.week_overview);

        Spinner presetSpinner = (Spinner) findViewById(R.id.presetSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Week_Preset_Names, android.R.layout.simple_spinner_dropdown_item);
        presetSpinner.setAdapter(adapter);

        dayView = (TextView) findViewById(R.id.dayView);
        timeView = (TextView) findViewById(R.id.timeView);
        currTempView = (TextView) findViewById(R.id.currTempView);
        dayTempView = (TextView) findViewById(R.id.dayTempView);
        nightTempView = (TextView) findViewById(R.id.nightTempView);
        vacView = (TextView) findViewById(R.id.vacView);

        //getanddisplaydata button
        Mondaybutton = (Button)findViewById(R.id.Mondaybutton);

        Mondaybutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //getAndSetInfo();
            }
        });

    }

    public void getAndSetInfo() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    dayViewS = HeatingSystem.get("day");
                    timeViewS = HeatingSystem.get("time");
                    currTempViewS = HeatingSystem.get("currentTemperature");
                    dayTempViewS = HeatingSystem.get("dayTemperature");
                    nightTempViewS = HeatingSystem.get("nightTemperature");
                    vacViewS = HeatingSystem.get("weekProgramState");


                    //wpg = HeatingSystem.getWeekProgram();

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

        dayView.setText("Day: " + dayTempViewS);
        timeView.setText("Time: " + timeViewS);
        currTempView.setText("Current Temperature: " + currTempViewS + "\u2103;");
        dayTempView.setText("Day Temperature: " + dayTempViewS + "\u2103;");
        nightTempView.setText("Night Temperature: " + nightTempViewS + "\u2103;");
        vacView.setText("Vacation mode: " + vacViewS);
    }
}
