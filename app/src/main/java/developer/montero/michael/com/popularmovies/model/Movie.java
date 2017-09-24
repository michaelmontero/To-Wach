package developer.montero.michael.com.popularmovies.model;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Michael Montero on 25-Aug-17.
 */

public class Movie implements Serializable{
    private String title,image,synopsis,releaseDate;
    private int id,popularity;
    private Double rating;
    private ArrayList<String> videosUrls;
    private ArrayList<Comments> comments;

    public Movie(int id,String title, String image, String synopsis, Double rating, String releaseDate, int popularity,ArrayList<String> videosUrls,ArrayList<Comments> comments) {
        this.title = title;
        this.image = image;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.id = id;
        this.popularity = popularity;
        this.videosUrls = videosUrls;
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getId() {
        return id;
    }

    public Double getRating() {
        return rating;
    }

    public int getPopularity() {
        return popularity;
    }

    public ArrayList<String> getUrls(){
        return videosUrls;
    }
    public ArrayList<Comments> getComments(){return comments;}
    public void setComments(ArrayList<Comments> mComments){
        comments = mComments;
    }
    public void setVideosUrls(ArrayList<String> mVideoUrls){
        videosUrls = mVideoUrls;
    }
}
