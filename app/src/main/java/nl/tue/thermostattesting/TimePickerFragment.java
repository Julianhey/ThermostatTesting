package nl.tue.thermostattesting;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Julian on 17-6-2016.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    String timegot;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
    /**
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    static TimePickerFragment newInstance(Time time, TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        TimePickerFragment pickerFragment = new TimePickerFragment();
        pickerFragment.setOnTimeSetListener(onTimeSetListener);

        //Pass the date in a bundle.
        Bundle bundle = new Bundle();
        bundle.putSerializable(MOVE_IN_TIME_KEY, time);
        pickerFragment.setArguments(bundle);
        return pickerFragment;
    }

    private void setOnTimeSetListener(TimePickerDialog.OnTimeSetListener listener) {
        this.onTimeSetListener = listener;
    }

    */

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        if (hourOfDay > 9) {
            timegot = hourOfDay + ":" + minute;
        } else{
            timegot = "0" + hourOfDay + ":" + minute;
        }
    }

}