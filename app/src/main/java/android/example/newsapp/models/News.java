package android.example.newsapp.models;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class News {
    private final String title;
    private final ArrayList<String> author;
    private final String date;
    private final Drawable imageUrl;
    private final String url;

    // Constructor
    public News (String title, ArrayList<String> author, String date, Drawable imageUrl, String url){
        this.title = title;
        this.author = author;
        this.date = date;
        this.imageUrl = imageUrl;
        this.url = url;
    }

    /**
     * Getter methods for each fields
     */
    public String getTitle(){
        return title;
    }

    public ArrayList<String> getAuthor(){
        return author;
    }

    public String getDate(){
        return date;
    }

    public Drawable getImageUrl(){ return imageUrl; }

    public String getUrl(){
        return url;
    }
}
