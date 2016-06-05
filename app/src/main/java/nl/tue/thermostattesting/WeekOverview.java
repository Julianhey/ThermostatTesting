package nl.tue.thermostattesting;

        import android.app.Activity;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.Window;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.Spinner;
        import android.widget.SpinnerAdapter;

        import org.thermostatapp.util.HeatingSystem;

/**
 * Created by Julian on 23-5-2016.
 * Edited by Koen on 31-5 at 17.56
 * Edit2
 */
public class WeekOverview extends AppCompatActivity {

    Button Mondaybutton, Tuesdaybutton, Wednesdaybutton, Thursdaybutton, Fridaybutton, Saturdaybutton, Sundaybutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.week_overview);

        Spinner presetSpinner = (Spinner) findViewById(R.id.presetSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Week_Preset_Names, android.R.layout.simple_spinner_dropdown_item);
        presetSpinner.setAdapter(adapter);



    }
}
