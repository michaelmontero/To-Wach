package developer.montero.michael.com.popularmovies.util;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import developer.montero.michael.com.popularmovies.model.Comments;
import developer.montero.michael.com.popularmovies.model.Movie;

/**
 * Created by Michael Montero on 25-Aug-17.
 */

public class NetworkUtil {

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";

    private final static String LANGUAJE = "language";

    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w185";

    public static URL createUrl(int movieId,String sortBy, String languaje, int page){
        URL url = null;
        Uri.Builder builder = Uri.parse(BASE_URL)
                .buildUpon();

                if(movieId != 0){
                    builder.appendPath(String.valueOf(movieId));
                }
                builder.appendPath(sortBy)
                .appendQueryParameter("api_key",Data.API_KEY)
                .appendQueryParameter("page",String.valueOf(page))
                .appendQueryParameter(LANGUAJE, languaje);
        Uri movieUrl = builder.build();
        try{
            url = new URL(movieUrl.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }

    public static URL createImageUrl(String imageName){
        URL url = null;
        Uri.Builder builder = Uri.parse(BASE_IMAGE_URL)
                .buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendEncodedPath(imageName);
        Uri movieUrl = builder.build();
        try{
            url = new URL(movieUrl.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getData(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * This method returns a Movie ArrayList from the providing json.
     *
     * @param jsonObject The JsonMovie representation to convert in a MovieArray
     * @return movieArrayList
     * @throws JSONException Related json reading and parse
     */
    public static ArrayList<Movie> convertJsonToMovieList(String jsonObject) throws JSONException{
        ArrayList<Movie> movies = new ArrayList<>();
        JSONObject movieJson = new JSONObject(jsonObject);
        JSONArray jsonArray = movieJson.getJSONArray("results");

        for(int x =0 ; x  < jsonArray.length(); x++){
            JSONObject movie = jsonArray.getJSONObject(x);
            int idMovie= movie.getInt("id");
            String movieTitle = movie.getString("title");
            String synopsis  = movie.getString("overview");
            String movieImg = movie.getString("poster_path");
            String releaseDate = movie.getString("release_date");
            Double userRating = movie.getDouble("vote_average");
            int popularity = movie.getInt("popularity");

            movies.add(new Movie(idMovie,movieTitle,movieImg,synopsis,userRating, releaseDate,popularity,null,null));
        }
        return movies;
    }

    public static ArrayList<Comments> convertJsonToCommentList(String commentJson) throws JSONException {
        JSONObject jsonObject = new JSONObject(commentJson);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        ArrayList<Comments> commentsArrayList = new ArrayList<>();
        for(int x=0;x<jsonArray.length();x++){
            JSONObject comments = jsonArray.getJSONObject(x);

            String movieId = comments.getString("id");
            String author = comments.getString("author");
            String content = comments.getString("content");

            commentsArrayList.add(new Comments(movieId,author,content));
        }
        return commentsArrayList;
    }
}
