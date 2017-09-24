package developer.montero.michael.com.popularmovies.model;

/**
 * Created by Michael A. Montero on 23/09/2017.
 */

public class Comments {
    private String id,author, comment;

    public Comments(String id, String author, String comment) {
        this.id = id;
        this.author = author;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }
}
