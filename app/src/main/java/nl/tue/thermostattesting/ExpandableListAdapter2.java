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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
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
    WeekProgram wpgNew;

    List<String> listDataHeaderNew;
    HashMap<String, List<String>> listDataChildNew;

    Drawable day_expanded_resized, day_collapsed_resized, moon_light_resized, sun_light_resized;

    PopupWindow popUp;
    Boolean click, cancel;
    TextView saveButton, cancelButton, startTime, endTime, setBtnstart, setBtnend, title, dayText, switchText, typeText;
    String firstTime, lastTime, day_or_night, type;
    int startTimeI, endTimeI;
    ToggleButton day_or_nightSwitch;
    ArrayList<String> timeBlockS = new ArrayList<>();
    ArrayList<Switch> DaySwitches;




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

                saveInput();


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

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);

            TextView textView = (TextView) convertView.findViewById(R.id.lblListItem);

            if (!childText.contains("N")){
                textView.setCompoundDrawablesWithIntrinsicBounds(sun_light_resized,null,null,null);
            }else {

                textView.setCompoundDrawablesWithIntrinsicBounds(moon_light_resized,null,null,null);
            }

            ImageView settingsButton = (ImageView)convertView.findViewById(R.id.settings_switch);

            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (click){
                        popUp.showAtLocation(_calledView, Gravity.CENTER, 0, 0);
                        popUp.setOutsideTouchable(true);
                        popUp.setFocusable(true);
                        cancel = false;

                        popUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                //saveInput();
                            }
                        });

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


                        title.setText("Configure Switch");
                        startTime.setText("Start    " + firstTime);
                        endTime.setText("End      "+ lastTime);
                        dayText.setText(groupPosition + "");
                        switchText.setText(childPosition + "");
                        typeText.setText(type);

                        click = false;
                    } else{
                        popUp.dismiss();
                        click = true;

                    }
                }
            });
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText.substring(1));
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

        outOf10.setText(_wpg.get_nr_switches_active(groupPosition) + "/9");
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


    public void saveInput(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(_wpg.toXML());
                    if (switchText.getText().toString() != "") {
                        _wpg.RemoveSwitch(Integer.parseInt(switchText.getText().toString()), _listDataHeader.get(Integer.parseInt(dayText.getText().toString())));
                    }
                    _wpg.AddSwitch(startTimeI, endTimeI, day_or_night, _listDataHeader.get(Integer.parseInt(dayText.getText().toString())));
                    _wpg.check_duplicates(_wpg.getSwitchArrayL(Integer.parseInt(dayText.getText().toString())));
                    try {
                        Thread.sleep(500);                 //1000 milliseconds is one second.
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    wpgNew = _wpg;
                    System.out.println(wpgNew.toXML());
                } catch (Exception e) {
                    System.err.println("Error from getdata " + e);
                }
            }
        }).start();


        try {
            Thread.sleep(1500);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        new GetInfo2ndRefresh().execute();
        popUp.dismiss();

    }

    public class GetInfo2ndRefresh extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute(){

        }

        @Override
        protected Void doInBackground(Void...params){

            try{
                String[] valid_days = {"Monday", "Tuesday", "Wednesday",
                        "Thursday", "Friday", "Saturday", "Sunday"};




                listDataHeaderNew = new ArrayList<String>();
                listDataChildNew = new HashMap<String, List<String>>();

                // Adding child data
                for (int j = 0; j < 7; j++) {
                    listDataHeaderNew.add(valid_days[j]);


                    // Adding child data
                    List<String> switches = Switchlist(j);



                    listDataChildNew.put(listDataHeaderNew.get(j), switches); // Header, Child data
                }


            }catch (Exception e){
                System.err.println("Error from getdata " + e);
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result){

            System.out.println(listDataChildNew.get("Monday") + "<--");
            setNewItems(listDataHeaderNew, listDataChildNew);

        }

    }
    public ArrayList<String> Switchlist(final int daynumber) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    DaySwitches = wpgNew.getSwitchArrayL(daynumber);
                    timeBlockS.clear();

                    System.out.println("Im really refreshing stuff, honest!");
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

        System.out.println(timeBlockS);
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

    public void setNewItems(List<String> listDataHeader,HashMap<String, List<String>> listChildData) {
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.notifyDataSetChanged();

    }
}