/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sani.ango.popularmoviesapp.utilities;

import android.annotation.SuppressLint;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    static String MOVIE_BASE_URL;
    static String PARAM_API;
    static String API_KEY;

    public static URL buildUrl(String sortBy, Context context) {
        MOVIE_BASE_URL = context.getString(R.string.base_url);
        PARAM_API = context.getString(R.string.api_param);
        API_KEY = context.getString(R.string.api_key);
        
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendEncodedPath(sortBy)
                    .appendQueryParameter(PARAM_API, API_KEY)
                    .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    @SuppressLint("NewApi")
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        StringBuilder builder;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            int response = urlConnection.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK)
            {
                builder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream())))
                {
                    String line;

                    while ((line = reader.readLine()) != null){
                        builder.append(line);
                    }
                }
                return builder.toString();
            }
        }catch (Exception e)
        {

        } finally {
            urlConnection.disconnect();
        }
        return null;
    }
}
