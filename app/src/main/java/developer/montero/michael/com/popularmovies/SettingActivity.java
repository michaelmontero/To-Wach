package developer.montero.michael.com.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingFragment())
                .commit();
    }
}
