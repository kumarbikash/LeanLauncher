package in.co.rwork.launcherforseniors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;


public class SpeedDialSettingsAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private Context context;

    public SpeedDialSettingsAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.speed_dial_setting_item_row, null);
        }

        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(i).replace("#", " ::: "));

        ImageView ivDelete = (ImageView)view.findViewById(R.id.iv_delete);

        ivDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ArrayList<String> list1 = new ArrayList<String>();
                String SpeedDialNumbers = UtilityClass.getPref("SpeedDialNumbers", v.getContext());
                Collections.addAll(list1, SpeedDialNumbers.split("##"));
                list1.remove(list.get(i).replace(" ::: ", "#"));
                String str = String.join("##", list1);

                UtilityClass.putPref("SpeedDialNumbers", str, v.getContext());

                list.remove(i);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
