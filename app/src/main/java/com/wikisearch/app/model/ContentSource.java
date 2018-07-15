package com.wikisearch.app.model;

import com.google.gson.annotations.SerializedName;

public class ContentSource {

    private String title;
    private String fullurl;
    private ContentImage thumbnail;
    private ContentImage original;
    private ContentTerms terms;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullurl() {
        return fullurl;
    }

    public void setFullurl(String fullurl) {
        this.fullurl = fullurl;
    }

    public ContentImage getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ContentImage thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ContentImage getOriginal() {
        return original;
    }

    public void setOriginal(ContentImage original) {
        this.original = original;
    }

    public ContentTerms getTerms() {
        return terms;
    }

    public void setTerms(ContentTerms terms) {
        this.terms = terms;
    }

    @Override
    public String toString() {
        return "ContentSource{" +
                "title='" + title + '\'' +
                ", fullurl='" + fullurl + '\'' +
                ", thumbnail=" + thumbnail +
                ", original=" + original +
                ", terms=" + terms +
                '}';
    }

    //        @BindingAdapter("android:url")
//        public static void setImage(ImageView view, String url) {
//            Glide.with(view.getContext())
//                    .load(url)
//                    .apply(new RequestOptions().placeholder(R.drawable.wiki_search_logo).error(R.drawable.wiki_search_logo))
//                    .into(view);
//        }

}
