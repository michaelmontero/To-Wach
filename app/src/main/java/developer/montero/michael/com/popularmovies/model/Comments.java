package developer.montero.michael.com.popularmovies.model;

/**
 * Created by Michael A. Montero on 23/09/2017.
 */

class Comments {
    private int id;
    private String author, comment;

    public Comments(int id, String author, String comment) {
        this.id = id;
        this.author = author;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }
}
