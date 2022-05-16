package in.co.rwork.launcherforseniors;

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

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = requireContext().registerReceiver(null, intentFilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPercent = level * 100 / (float) scale;

        TextView tv_battery_level = view.findViewById(R.id.tv_battery_level);
        tv_battery_level.setText(batteryPercent + " %");

        TextView tv_date = view.findViewById(R.id.tv_date);
        Date d = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("EEE\ndd/MM/yyyy");
        tv_date.setText(df.format(d));

        cardViewDrawer = view.findViewById(R.id.cv_drawer);
        cardViewDrawer.setOnClickListener(v -> loadFragment(new AppsDrawerFragment()));

        cardViewSMS = view.findViewById(R.id.cv_sms);
        cardViewCall = view.findViewById(R.id.cv_call);

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
}