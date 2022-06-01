package in.co.rwork.launcherforseniors;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SpeedDialAdapter extends RecyclerView.Adapter<SpeedDialAdapter.ViewHolder> {
    //private final Context context;
    private static List<SDNumber> speedDials;

    private static int fs;

    public SpeedDialAdapter(Context c ) {
        setUpApps(c);
    }
    public static void setUpApps(@NonNull Context c){

        speedDials = new ArrayList<>();
        ArrayList<String> list = new ArrayList<String>();

        String SpeedDialNumbers = UtilityClass.getPref("SpeedDialNumbers", c.getApplicationContext());
        Collections.addAll(list, SpeedDialNumbers.split("##"));

        list.remove("");
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return a.compareTo(b.toString());
            }
        });

        for (String s : list) {
            SDNumber sd = new SDNumber();
            sd.contact_name = s.split("#")[0];
            sd.contact_number = s.split("#")[1];
            speedDials.add(sd);
        }

        /*Collections.sort(speedDials, new Comparator<SDNumber>() {
            @Override
            public int compare(SDNumber sd1, SDNumber sd2) {
                return sd1.contact_name.compareTo(sd2.contact_name);
            }
        });*/

        if (speedDials.size() == 0) {
            Toast.makeText(c, "No Speed Dials found. \nConfigure Speed Dials in settings. \nPress back to go to Home screen.", Toast.LENGTH_LONG).show();
        }

        fs = Integer.parseInt(UtilityClass.getPref("FontSize", c).replaceAll("sp",""));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.speed_dial_row_view_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String contactName = speedDials.get(position).contact_name;
        String contactNumber = speedDials.get(position).contact_number;

        TextView textView = holder.textView;
        textView.setText("\uD83D\uDC64 " + contactName + "\n\u260E " + contactNumber);

        textView.setTextSize(fs);
    }

    @Override
    public int getItemCount() {
        return speedDials.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            //Finds the views from our row.xml
            textView =  itemView.findViewById(R.id.contact_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Context context = v.getContext();

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + speedDials.get(pos).contact_number));
            context.startActivity(intent);
        }
    }

}
