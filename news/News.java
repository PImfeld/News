package com.example.news;

public class News {
    /**Constant value represents no image provided*/
    private static final int NO_IMAGE_PROVIDED = -1;

    /** title of the article */
    private String mTitle;

    /** section of the news */
    private String mSection;

    /** author of the article */
    private int mAuthor;

    /** Website URL of the news */
    private String mUrl;

    /**
     * Constructs a new {@link News} object.
     *
     * @param title is the title of the article
     * @param section is the section of the news
     * @param url is the website URL where the news is located
     */
    public News(String title, String section, String url) {
        mTitle = title;
        mSection = section;
        mUrl = url;
    }

    /**
     * Constructs a new {@link News} object.
     *
     * @param title is the title of the article
     * @param section is the section of the news
     * @param author is the author of the article
     * @param url is the website URL where the news is located
     */
    public News(String title, String section, int author, String url) {
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mUrl = url;
    }

    /**
     * Returns the title of the article.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the section of the news.
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Returns the author of the article.
     */
    public int getAuthor() {
        return mAuthor;
    }

    /**
     * Returns the website URL for the news.
     * @return
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * Determines if there is an author for the article
     */
    public boolean hasAuthor() {
    return mAuthor != NO_IMAGE_PROVIDED;
    }

}
