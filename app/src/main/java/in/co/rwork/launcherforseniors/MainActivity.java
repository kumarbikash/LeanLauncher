package in.co.rwork.launcherforseniors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        loadFragment(new HomeScreenFragment());

        setDefaultConfigs();
    }

    @Override
    public void onResume(){
        super.onResume();
        loadFragment(new HomeScreenFragment());
    }

    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        loadFragment(new HomeScreenFragment());
    }

    public void showSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    private void setDefaultConfigs() {
        if(UtilityClass.getPref("FontSize", getApplicationContext()) == null) {
            UtilityClass.putPref("FontSize", "30sp", getApplicationContext());
        }
        if(UtilityClass.getPref("IconSize", getApplicationContext()) == null) {
            UtilityClass.putPref("IconSize", "80dp", getApplicationContext());
        }
    }

}