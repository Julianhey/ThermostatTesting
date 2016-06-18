package nl.tue.thermostattesting;

/**
 * Created by Julian on 17-6-2016.
 */
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
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

public class ExpandableListAdapter2 extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private View _calledView;

    Drawable day_expanded_resized;
    Drawable day_collapsed_resized;
    PopupWindow popUp;
    Boolean click;
    TextView saveButton, cancelButton, startTime, endTime, setBtnstart, setBtnend;
    String firstTime, lastTime;




    public ExpandableListAdapter2(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData, View calledView) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._calledView = calledView;

        day_expanded_resized =  context.getDrawable(R.drawable.day_expanded_resized);
        day_collapsed_resized =  context.getDrawable(R.drawable.day_collapsed_resized);

        LayoutInflater inflater2 = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater2.inflate(R.layout.settings_popup, null);
        click = true;



        popUp = new PopupWindow(popUpView, 656, 893, true);
        saveButton = (TextView) popUpView.findViewById(R.id.saveButton);
        cancelButton = (TextView) popUpView.findViewById(R.id.cancelButton);
        startTime = (TextView) popUpView.findViewById(R.id.startTime);
        endTime = (TextView) popUpView.findViewById(R.id.endTime);
        setBtnstart = (TextView) popUpView.findViewById(R.id.setBtnstart);
        setBtnend = (TextView) popUpView.findViewById(R.id.setBtnend);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("PLEASE");
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Have mercy");
                popUp.dismiss();
            }
        });

        setBtnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                FragmentManager fm = ((Activity)_context).getFragmentManager();
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(fm, "timepicker");

                */

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(_context, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute){
                        startTime.setText("Start       " + selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);

                mTimePicker.show();
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
                        endTime.setText("End         " + selectedHour + ":" + selectedMinute);
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
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);

            ImageView settingsButton = (ImageView)convertView.findViewById(R.id.settings_switch);

            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (click){
                        popUp.showAtLocation(_calledView, Gravity.CENTER, 0, 0);

                        firstTime = childText.substring(0,6);
                        lastTime = childText.substring(8);

                        startTime.setText("Start     " + firstTime);
                        endTime.setText("End       "+ lastTime);

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

        txtListChild.setText(childText);
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
    public View getGroupView(int groupPosition, boolean isExpanded,
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


                }
            });
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);

        lblListHeader.setText(headerTitle);



        if (isExpanded){
            convertView.setBackground(day_expanded_resized);
        } else{
            convertView.setBackground(day_collapsed_resized);
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
}
