package com.wikisearch.app.interfaces;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.wikisearch.app.model.ContentSource;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCall {
    String BASE_URL = "https://en.wikipedia.org//w/";


    @GET("api.php?action=query&format=json&generator=prefixsearch&redirects=1&formatversion=2&gpslimit=200&prop=" +
            "info%7Cpageimages%7Cpageterms&pilimit=max&exintro&explaintext&exsentences=1&exlimit=max&piprop=original%" +
            "7Cthumbnail&inprop=url&wbptterms=description&\n")
    Call<JsonObject> getGenericJSON(@Query(value = "gpssearch") String param);


}


