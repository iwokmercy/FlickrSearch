package com.example.mjexco.flickrsearch.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Photos implements Serializable
{

    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("pages")
    @Expose
    private String pages;
    @SerializedName("perpage")
    @Expose
    private int perpage;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("photo")
    @Expose
    private List<Photo> photo = null;
    private final static long serialVersionUID = 3160502957579123L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Photos() {
    }

    /**
     *
     * @param total
     * @param page
     * @param pages
     * @param photo
     * @param perpage
     */
    public Photos(int page, String pages, int perpage, String total, List<Photo> photo) {
        super();
        this.page = page;
        this.pages = pages;
        this.perpage = perpage;
        this.total = total;
        this.photo = photo;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Photo> getPhoto() {
        return photo;
    }

    public void setPhoto(List<Photo> photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("page", page).append("pages", pages).append("perpage", perpage).append("total", total).append("photo", photo).toString();
    }

}
