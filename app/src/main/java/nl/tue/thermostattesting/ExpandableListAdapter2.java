package nl.tue.thermostattesting;

/**
 * Created by Julian on 17-6-2016.
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.thermostatapp.util.HeatingSystem;
import org.thermostatapp.util.Switch;
import org.thermostatapp.util.WeekProgram;
import org.w3c.dom.Text;

public class ExpandableListAdapter2 extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private View _calledView;
    private WeekProgram _wpg;


    List<String> listDataHeaderNew;
    HashMap<String, List<String>> listDataChildNew;

    Drawable day_expanded_resized, day_collapsed_resized, moon_light_resized, sun_light_resized;

    PopupWindow popUp;
    Boolean click, cancel;
    TextView saveButton, cancelButton, startTime, endTime, setBtnstart, setBtnend, title, dayText, switchText, typeText;
    String firstTime, lastTime, day_or_night, type, cDay, dayS, nightS;
    int startTimeI, endTimeI;
    ToggleButton day_or_nightSwitch;
    ArrayList<String> timeBlockS;
    ArrayList<Switch> DaySwitches;
    ArrayList<Switch>  weekDay;





    public ExpandableListAdapter2(Context context, List<String> listDataHeader,
                                  HashMap<String, List<String>> listChildData, View calledView, WeekProgram wpg) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._calledView = calledView;
        this._wpg = wpg;

        day_expanded_resized =  context.getDrawable(R.drawable.day_expanded_resized);
        day_collapsed_resized =  context.getDrawable(R.drawable.day_collapsed_resized);
        moon_light_resized = context.getDrawable(R.drawable.moon_light_resized);
        sun_light_resized = context.getDrawable(R.drawable.sun_light_resized);

        LayoutInflater inflater2 = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater2.inflate(R.layout.settings_popup, null);
        click = true;
        cancel = false;
        day_or_night = "day";



        popUp = new PopupWindow(popUpView, 656, 950, true);
        saveButton = (TextView) popUpView.findViewById(R.id.saveButton);
        cancelButton = (TextView) popUpView.findViewById(R.id.cancelButton);
        startTime = (TextView) popUpView.findViewById(R.id.startTime);
        endTime = (TextView) popUpView.findViewById(R.id.endTime);
        title = (TextView) popUpView.findViewById(R.id.title);
        dayText = (TextView)popUpView.findViewById(R.id.infoText);
        switchText = (TextView) popUpView.findViewById(R.id.switchText);
        typeText = (TextView) popUpView.findViewById(R.id.typeText);
        setBtnstart = (TextView) popUpView.findViewById(R.id.setBtnstart);
        setBtnend = (TextView) popUpView.findViewById(R.id.setBtnend);
        day_or_nightSwitch = (ToggleButton) popUpView.findViewById(R.id.day_or_nightSwitch);



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (saveButton.getText().toString().equals("Save")) {
                    WeekOverview.addInput(startTimeI, endTimeI, typeText.getText().toString(), Integer.parseInt(dayText.getText().toString()));
                } else if (saveButton.getText().toString().equals("Delete")){
                    WeekOverview.deleteInput(Integer.parseInt(switchText.getText().toString()), endTimeI, typeText.getText().toString(), Integer.parseInt(dayText.getText().toString()));

                }
                popUp.dismiss();

            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancel = true;
                popUp.dismiss();
            }
        });

        setBtnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(_context, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute){
                        startTime.setText("Start    " + selectedHour + ":" + selectedMinute);

                        startTimeI = (selectedHour * 100) + selectedMinute;
                    }
                }, hour, minute, true);

                mTimePicker.show();
            }
        });


        day_or_nightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    day_or_night = "day";
                } else {
                    day_or_night = "night";
                }
            }
        });
        setBtnend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(_context, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute){
                        endTime.setText("End      " + selectedHour + ":" + selectedMinute);

                        endTimeI = (selectedHour * 100) + selectedMinute;
                    }
                }, hour, minute, true);

                mTimePicker.show();
            }
        });

    }




    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {


        DaySwitches = _wpg.getSwitchArrayL(groupPosition);
        int startTimeTestInt = DaySwitches.get(childPosition).getTime_Int();
        int durationTime = DaySwitches.get(childPosition).getDur();
        String startTimeS = DaySwitches.get(childPosition).getTime();
        String endTimeS = int_time_to_string(startTimeTestInt + durationTime);

        final String childText = " " + startTimeS + " - " + endTimeS;
        /**
        if (DaySwitches.get(childPosition).getType().equals("night")) {
            childText = "N" + startTimeS + " - " + endTimeS;//(String) getChild(groupPosition, childPosition);
        } else if (DaySwitches.get(childPosition).getType().equals("day")){
            childText = "D" + startTimeS + " - " + endTimeS;//(String) getChild(groupPosition, childPosition);
        }
        */

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);

            TextView textView = (TextView) convertView.findViewById(R.id.lblListItem);
            ImageView childIcon = (ImageView) convertView.findViewById(R.id.childIcon);

            if (DaySwitches.get(childPosition).getType().equals("night")){
                childIcon.setImageResource(R.drawable.moon_light_resized);

            }else if (DaySwitches.get(childPosition).getType().equals("day")){
                childIcon.setImageResource(R.drawable.sun_light_resized);
            }
            System.out.println(DaySwitches.get(childPosition).getState() + " " + childPosition);


            //<----------------------------------------------------------------------------------------------------------------------------------- This should be the place where we can set the visibility of those not used.

            /**
            if (childPosition > _wpg.get_nr_switches_active(groupPosition) ){
                System.out.println("GONE");
                convertView.setVisibility(View.INVISIBLE);
            }

             */

            ImageView settingsButton = (ImageView)convertView.findViewById(R.id.settings_switch);

            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (click){
                        popUp.showAtLocation(_calledView, Gravity.CENTER, 0, 0);
                        popUp.setOutsideTouchable(true);
                        popUp.setFocusable(true);
                        cancel = false;



                        firstTime = childText.substring(1,7);
                        lastTime = childText.substring(9);
                        type = childText.substring(0,1);
                        if (type.equals("D")){
                            day_or_nightSwitch.setChecked(false);
                            day_or_night = "day";
                        } else if (type.equals("N")){
                            day_or_nightSwitch.setChecked(true);
                            day_or_night = "night";
                        }


                        title.setText("Deleting Switch");
                        startTime.setText("Start    " + firstTime);
                        endTime.setText("End      "+ lastTime);
                        dayText.setText(groupPosition + "");
                        switchText.setText(childPosition + "");
                        setBtnstart.setVisibility(View.INVISIBLE);
                        setBtnend.setVisibility(View.INVISIBLE);
                        typeText.setText(type);
                        saveButton.setText("Delete");
                        day_or_nightSwitch.setClickable(false);

                        click = false;
                    } else{
                        popUp.dismiss();
                        click = true;

                    }
                }
            });
        }

        if (convertView != null) {
            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText.substring(1));
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);

            ImageView addButton = (ImageView)convertView.findViewById(R.id.new_switch);




            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (click){
                        popUp.showAtLocation(_calledView, Gravity.CENTER, 0, 0);
                        popUp.setOutsideTouchable(true);
                        popUp.setFocusable(true);

                        popUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                //saveInput();
                            }
                        });


                        firstTime = "00:00";
                        //lastTime = childText.substring(9);

                        startTime.setText("Start    " + firstTime);
                        endTime.setText("End      "+ firstTime);

                        dayText.setText(groupPosition + "");
                        switchText.setText("add");
                        saveButton.setText("Save");
                        day_or_nightSwitch.setClickable(true);

                        title.setText("Add New Switch");
                        click = false;
                    } else{
                        popUp.dismiss();
                        click = true;

                    }


                }
            });
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);

        TextView outOf10 = (TextView) convertView.findViewById(R.id.outOf10);

        outOf10.setText(_wpg.get_nr_switches_active(groupPosition) + "/10");
        ImageView addButton = (ImageView)convertView.findViewById(R.id.new_switch);
        lblListHeader.setText(headerTitle);



        if (isExpanded){
            convertView.setBackground(day_expanded_resized);
            addButton.setVisibility(View.VISIBLE);
            addButton.setClickable(true);
        } else{
            convertView.setBackground(day_collapsed_resized);
            addButton.setVisibility(View.INVISIBLE);
            addButton.setClickable(false);
        }



        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
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

    public void setNewItems(List<String> listDataHeader,HashMap<String, List<String>> listChildData, WeekProgram wpg) {
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._wpg = wpg;
        this.notifyDataSetChanged();

    }
}