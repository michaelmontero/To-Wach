package developer.montero.michael.com.popularmovies;


import android.os.Bundle;
import android.preference.PreferenceFragment;


public class SettingFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
