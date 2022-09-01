package com.example.movieapple;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieClient {
    private static MovieClient instance = null;
    private static MovieInterface movieInterface;
    private static String baseUrl = "http://www.kobis.or.kr";

    private MovieClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        movieInterface = retrofit.create(MovieInterface.class);
    }

    public static MovieClient getInstance() {
        if (instance == null) {
            instance = new MovieClient();
        }
        return instance;
    }

    public static MovieInterface getMovieInterface() {
        return movieInterface;
    }
}
