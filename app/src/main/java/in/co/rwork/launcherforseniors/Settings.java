package in.co.rwork.launcherforseniors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (UtilityClass.getPref("SpeedDialNumbers", getApplicationContext()) == null) {
            UtilityClass.putPref("SpeedDialNumbers", "", getApplicationContext());
        }

        loadExistingValues();

        initializeListeners();

        initializeList();
    }

    private void loadExistingValues() {
        TextView textViewFontSize = findViewById(R.id.tv_font_size_value);
        TextView textViewIconSize = findViewById(R.id.tv_icon_size_value);

        textViewFontSize.setText(UtilityClass.getPref("FontSize", getApplicationContext()));
        textViewIconSize.setText(UtilityClass.getPref("IconSize", getApplicationContext()));

        SeekBar skFS=(SeekBar) findViewById(R.id.seekBarFontSize);
        SeekBar skIS=(SeekBar) findViewById(R.id.seekBarIconSize);

        skFS.setProgress(Integer.parseInt(UtilityClass.getPref("FontSize", getApplicationContext()).replaceAll("sp", "")));

        skIS.setProgress(Integer.parseInt(UtilityClass.getPref("IconSize", getApplicationContext()).replaceAll("dp", "")));
    }

    private void initializeListeners() {
        TextView textViewFontSize = findViewById(R.id.tv_font_size_value);
        TextView textViewIconSize = findViewById(R.id.tv_icon_size_value);

        SeekBar skFS=(SeekBar) findViewById(R.id.seekBarFontSize);
        skFS.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

                if (progress > 20) {
                    textViewFontSize.setText(progress + "sp");
                    UtilityClass.putPref("FontSize", progress + "sp", getApplicationContext());
                }
                else {
                    Toast.makeText(getApplicationContext(), "Select a value above 20", Toast.LENGTH_SHORT).show();
                    loadExistingValues();
                }
            }
        });

        SeekBar skIS=(SeekBar) findViewById(R.id.seekBarIconSize);
        skIS.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

                if (progress > 30) {
                    textViewIconSize.setText(progress + "dp");
                    UtilityClass.putPref("IconSize", progress + "dp", getApplicationContext());
                }
                else {
                    Toast.makeText(getApplicationContext(), "Select a value above 30", Toast.LENGTH_SHORT).show();
                    loadExistingValues();
                }
            }
        });

        EditText etSDName = findViewById(R.id.editTextSDName);

        EditText etSDNumber = findViewById(R.id.editTextSDNumber);

        Button btnAdd = findViewById(R.id.buttonSave);
        btnAdd.setOnClickListener(view -> {
            String name = etSDName.getText().toString().trim();
            String number = etSDNumber.getText().toString().trim();
            if (name.contains("#")) {
                Toast.makeText(this, "Name can't contain #", Toast.LENGTH_SHORT).show();
                return;
            }
            if (name.equals("")) {
                Toast.makeText(this, "Name can't be blank", Toast.LENGTH_SHORT).show();
                return;
            }
            if (number.contains("#")) {
                Toast.makeText(this, "Number can't contain #", Toast.LENGTH_SHORT).show();
                return;
            }
            if (number.equals("")) {
                Toast.makeText(this, "Number can't be blank", Toast.LENGTH_SHORT).show();
                return;
            }

            String newSpeedDialString = name + "#" + number;
            if (UtilityClass.getPref("SpeedDialNumbers", getApplicationContext()) != null) {
                UtilityClass.putPref("SpeedDialNumbers", UtilityClass.getPref("SpeedDialNumbers", getApplicationContext()) + "##" + newSpeedDialString, getApplicationContext());
                initializeList();
            }
            else {
                Toast.makeText(this, "Something went wrong. Try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initializeList() {
        ArrayList<String> list = new ArrayList<String>();
        
        String SpeedDialNumbers = UtilityClass.getPref("SpeedDialNumbers", getApplicationContext());
        Collections.addAll(list, SpeedDialNumbers.split("##"));

        list.remove("");
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return a.compareTo(b.toString());
            }
        });

        //instantiate custom adapter
        SpeedDialSettingsAdapter adapter = new SpeedDialSettingsAdapter(list, this);

        //handle listview and assign adapter
        ListView lsview = (ListView)findViewById(R.id.lview_list);
        lsview.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lsview);
    }

    private void setListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight=0;
        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            view = listAdapter.getView(i, view, listView);

            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
                        ViewGroup.LayoutParams.MATCH_PARENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + ((listView.getDividerHeight()) * (listAdapter.getCount())) + 50;

        listView.setLayoutParams(params);
        listView.requestLayout();

    }


}