package com.coursemis.view.activity;

import java.util.ArrayList;

import com.coursemis.R;
import com.coursemis.util.P;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class TMentionNameActivity_2 extends Activity {
    Intent intent = null;
    Intent intent_temp = null;
    LinearLayout layout = null;
    private String courseweek = null;
    private String coursenumber = null;
    ListView tmn = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmentionnameactivity_2);
        LinearLayout ll = (LinearLayout) findViewById(R.id.layout_2);
        intent = getIntent();
        courseweek = intent.getStringExtra("weekchoose");
        coursenumber = intent.getStringExtra("weeknumber");
        tmn = (ListView) findViewById(R.id.tmentionname_2);
        final ArrayList<String> courseWeek = new ArrayList<String>();
        final ArrayList<String> courseWeek_temp = new ArrayList<String>();
        switch (Integer.parseInt(courseweek)) {
            case 1:
                for (int i = 1; i <= Integer.parseInt(coursenumber); i = i + 2) {
                    courseWeek.add(i + "");
                    courseWeek_temp.add("第				" + i + "				周");
                }

                ArrayAdapter<String> aaRadioButtonAdapter = new ArrayAdapter<String>(
                        this, android.R.layout.simple_list_item_checked, courseWeek_temp);
                tmn.setAdapter(aaRadioButtonAdapter);
                tmn.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                tmn.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                            long arg3) {
                        // TODO Auto-generated method stub
                        Uri data = Uri.parse(courseWeek.get(arg2));
                        Intent result = new Intent(null, data);
                        setResult(RESULT_OK, result);
                        finish();
                    }
                });
                break;
            case 2:
                for (int i = 2; i <= Integer.parseInt(coursenumber); i = i + 2) {

                    courseWeek.add(i + "");
                    courseWeek_temp.add("第				" + i + "				周");
                }

                ArrayAdapter<String> aaRadioButtonAdapter2 = new ArrayAdapter<String>(
                        this, android.R.layout.simple_list_item_checked, courseWeek_temp);
                tmn.setAdapter(aaRadioButtonAdapter2);
                tmn.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                tmn.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                            long arg3) {
                        // TODO Auto-generated method stub
                        Uri data = Uri.parse(courseWeek.get(arg2));
                        Intent result = new Intent(null, data);
                        setResult(RESULT_OK, result);
                        finish();
                    }
                });
                break;


            case 3:
                for (int i = 1; i <= Integer.parseInt(coursenumber); i = i + 1) {
                    courseWeek.add(i + "");
                    courseWeek_temp.add("第				" + i + "				周");


                }


                ArrayAdapter<String> aaRadioButtonAdapter3 = new ArrayAdapter<String>(
                        this, android.R.layout.simple_list_item_checked, courseWeek_temp);
                tmn.setAdapter(aaRadioButtonAdapter3);
                tmn.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                tmn.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                            long arg3) {
                        // TODO Auto-generated method stub
                        Uri data = Uri.parse(courseWeek.get(arg2));
                        Intent result = new Intent(null, data);
                        setResult(RESULT_OK, result);
                        finish();
                    }
                });
                break;


        }


    }

}
