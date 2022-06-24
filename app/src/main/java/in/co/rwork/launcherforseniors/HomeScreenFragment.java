package in.co.rwork.launcherforseniors;

import static android.content.Context.BATTERY_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.Telephony;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class HomeScreenFragment extends Fragment {

    CardView cardViewDrawer;
    CardView cardViewSMS;
    CardView cardViewCall;
    CardView cardViewSpeedDial;

    public HomeScreenFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BatteryManager bm = (BatteryManager) requireContext().getSystemService(BATTERY_SERVICE);
        int batteryPercent = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        TextView tv_battery_level = view.findViewById(R.id.tv_battery_level);
        tv_battery_level.setText(batteryPercent + " %");

        refreshBatteryIcon(batteryPercent, view);

        TextView tv_date = view.findViewById(R.id.tv_date);
        Date d = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("EEE\ndd/MM/yyyy");
        tv_date.setText(df.format(d));

        cardViewDrawer = view.findViewById(R.id.cv_drawer);
        cardViewDrawer.setOnClickListener(v -> loadFragment(new AppsDrawerFragment()));

        cardViewSMS = view.findViewById(R.id.cv_sms);
        cardViewCall = view.findViewById(R.id.cv_call);
        cardViewSpeedDial = view.findViewById(R.id.cv_speeddial);

        cardViewSMS.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(Telephony.Sms.getDefaultSmsPackage(context));
            if(intent != null) {
                context.startActivity(intent);
            }
            else{
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        cardViewCall.setOnClickListener(v -> {
            Context context = v.getContext();
            TelecomManager manager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
            Intent intent = null;
            intent = context.getPackageManager().getLaunchIntentForPackage(manager.getDefaultDialerPackage());
            if(intent != null) {
                context.startActivity(intent);
            }
            else{
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        cardViewSpeedDial.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, SpeedDial.class);
            context.startActivity(intent);
        });

        loadObjectSizes(view);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void loadObjectSizes(View v) {
        TextView tv_battery_level = v.findViewById(R.id.tv_battery_level);
        TextView tv_date = v.findViewById(R.id.tv_date);
        TextClock tc_clock = v.findViewById(R.id.tc_1);
        TextView tv_drawer = v.findViewById(R.id.tv_drawer);
        TextView tv_sms = v.findViewById(R.id.tv_sms);
        TextView tv_call = v.findViewById(R.id.tv_call);
        TextView tv_speeddial = v.findViewById(R.id.tv_speeddial);

        ImageView iv_drawer = v.findViewById(R.id.icon_drawer);
        ImageView iv_sms = v.findViewById(R.id.icon_sms);
        ImageView iv_call = v.findViewById(R.id.icon_call);
        ImageView iv_setting = v.findViewById(R.id.icon_setting);
        ImageView iv_battery = v.findViewById(R.id.icon_battery);
        ImageView iv_speeddial = v.findViewById(R.id.icon_speeddial);

        int fs = Integer.parseInt(UtilityClass.getPref("FontSize", requireActivity().getApplicationContext()).replaceAll("sp",""));
        int is = Integer.parseInt(UtilityClass.getPref("IconSize", requireActivity().getApplicationContext()).replaceAll("dp",""));

        tv_battery_level.setTextSize(fs);
        tv_date.setTextSize(fs);
        tc_clock.setTextSize(fs);
        tv_drawer.setTextSize(fs);
        tv_sms.setTextSize(fs);
        tv_call.setTextSize(fs);
        tv_speeddial.setTextSize(fs);

        iv_drawer.getLayoutParams().height = is;
        iv_drawer.getLayoutParams().width = is;
        iv_drawer.requestLayout();

        iv_sms.getLayoutParams().height = is;
        iv_sms.getLayoutParams().width = is;
        iv_sms.requestLayout();

        iv_call.getLayoutParams().height = is;
        iv_call.getLayoutParams().width = is;
        iv_call.requestLayout();

        iv_setting.getLayoutParams().height = is;
        iv_setting.getLayoutParams().width = is;
        iv_setting.requestLayout();

        iv_battery.getLayoutParams().height = is;
        iv_battery.getLayoutParams().width = is;
        iv_battery.requestLayout();

        iv_speeddial.getLayoutParams().height = is;
        iv_speeddial.getLayoutParams().width = is;
        iv_speeddial.requestLayout();
    }

    private void refreshBatteryIcon(int batteryLevel, View v) {
        ImageView iv_battery = v.findViewById(R.id.icon_battery);
        if (isPowerConnected(requireContext())) {
            iv_battery.setImageResource(R.drawable.ic_battery_charging);
        }
        else {
            if (batteryLevel > 0 && batteryLevel <= 10) {
                iv_battery.setImageResource(R.drawable.ic_battery_10);
            }
            else if (batteryLevel > 10 && batteryLevel <= 20) {
                iv_battery.setImageResource(R.drawable.ic_battery_20);
            }
            else if (batteryLevel > 20 && batteryLevel <= 30) {
                iv_battery.setImageResource(R.drawable.ic_battery_30);
            }
            else if (batteryLevel > 30 && batteryLevel <= 40) {
                iv_battery.setImageResource(R.drawable.ic_battery_40);
            }
            else if (batteryLevel > 40 && batteryLevel <= 50) {
                iv_battery.setImageResource(R.drawable.ic_battery_50);
            }
            else if (batteryLevel > 50 && batteryLevel <= 60) {
                iv_battery.setImageResource(R.drawable.ic_battery_60);
            }
            else if (batteryLevel > 60 && batteryLevel <= 70) {
                iv_battery.setImageResource(R.drawable.ic_battery_70);
            }
            else if (batteryLevel > 70 && batteryLevel <= 80) {
                iv_battery.setImageResource(R.drawable.ic_battery_80);
            }
            else if (batteryLevel > 80 && batteryLevel <= 90) {
                iv_battery.setImageResource(R.drawable.ic_battery_90);
            }
            else if (batteryLevel > 90 && batteryLevel <= 100) {
                iv_battery.setImageResource(R.drawable.ic_battery_100);
            }
            else {
                iv_battery.setImageResource(R.drawable.ic_battery_empty);
            }
        }
    }

    private boolean isPowerConnected(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
    }
}