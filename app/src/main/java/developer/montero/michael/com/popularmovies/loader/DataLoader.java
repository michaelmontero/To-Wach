package developer.montero.michael.com.popularmovies.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.IOException;
import java.net.URL;

import developer.montero.michael.com.popularmovies.util.NetworkUtil;

/**
 * Created by Michael A. Montero on 23/09/2017.
 */

public abstract class DataLoader extends AsyncTaskLoader<String> {
    private URL param;
    public DataLoader(Context context, URL param) {
        super(context);
        this.param = param;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        String mMovie = null;
        try {
            mMovie = NetworkUtil.getData(param);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mMovie;
    }
}
