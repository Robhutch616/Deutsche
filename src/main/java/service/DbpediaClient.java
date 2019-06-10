package service;

import com.google.gson.*;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.FormatUtils;
import utils.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.swing.text.html.HTMLDocument;
import java.io.IOException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Http client based off retrofit
 */
public class DbpediaClient {
    // Base url to use for queries to dbpedia
    public static final String baseUrl = "http://lookup.dbpedia.org/api/";

    private static DbpediaClient instance = null;

    public static DbpediaClient Instance() {
        if (instance == null) {
            instance = new DbpediaClient();
        }
        return instance;
    }

    private Gson gson;
    private OkHttpClient client;
    private Retrofit retrofit;
    private DbpediaApi api;

    private DbpediaClient() {
        gson = new GsonBuilder().create();
        client = new OkHttpClient.Builder().build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .build();
        api = retrofit.create(DbpediaApi.class);
    }

    private HashMap<String, String> createHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        return headers;
    }

    /**
     * Searches dbpedia for the answer to a question with a keyword search term of `searchTerm` and
     * @param searchTerm
     * @param dataKey
     */
    /**
     * NOTE : In production i would use .enqueue(callback) but due to time constraints I will use execute()
     */
    public void searchQuery(final String searchTerm, final String dataKey, @NotNull final JsonCallback callback) {
        try {
            JSONObject keywordSearchResult = searchForKeyWords(searchTerm);
            if (keywordSearchResult == null) {
                callback.onFailure(new Formatter().format("Searching for keyword %s failed", searchTerm).toString());
                return;
            }
            String dataUri = extractFirstUrl(keywordSearchResult);
            if (dataUri == null) {
                callback.onFailure("Failed to find uri of data resource for search term " + searchTerm);
                return;
            }
            JSONObject resourceData = pullResourceData(dataUri);
            if (resourceData == null) {
                callback.onFailure("Failed to pull resource data for uri : " + dataUri);
                return;
            }
            String trimmedDataKey = FormatUtils.removeWhitespace(dataKey).toLowerCase();
            String answerValue = extractDataForAnswer(resourceData, trimmedDataKey);
            if (answerValue == null) {
                callback.onFailure("Failed to extract value for dataKey : " + dataKey);
                return;
            }
            callback.onFoundValue(answerValue);

        } catch (Exception e) {
            Log.error("ERROR");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Search Dbpedia for keyword `searchTerm`
     *
     * @param searchTerm
     * @return
     */
    private JSONObject searchForKeyWords(final String searchTerm) {
        try {
            Response<ResponseBody> response = api.search(createHeaders(), searchTerm).execute();
            // NOTE : in production I would add more checks for response body like checking response code and nullable objects
            return new JSONObject(response.body().string());
        } catch (Exception e) {
            // NOTE : due to time - I have put a generic exception catch
            e.printStackTrace();
        }
        return null;
    }

    /**
     * extracts the url
     *
     * @param object
     * @return
     */
    private String extractFirstUrl(JSONObject object) {
        if (object == null) {
            return null;
        }
        JSONArray array = object.optJSONArray("results");
        if (array == null || array.length() == 0) {
            return null;
        }
        try {
            JSONObject obj = array.getJSONObject(0);
            return obj.optString("uri");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param uri
     * @return
     */
    private JSONObject pullResourceData(String uri) {
        DbLinkParser parser = new DbLinkParser(uri);
        String jsonUrl = parser.parseJsonLink();
        if (jsonUrl == null) {
            Log.error("failed to parse JSON url");
            return null;
        }
        Request req = new Request.Builder()
                .url(jsonUrl)
                .get()
                .build();
        // NOTE : this is also running on main thread - which in production it shouldn't be
        try {
            okhttp3.Response res = client.newCall(req).execute();
            String responseString = res.body().string();
            Log.l("got data resource response : " + responseString);
            return new JSONObject(responseString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String extractDataForAnswer(JSONObject object, String keySuffix) {
        JSONArray array = searchJsonForKeySuffix(object, keySuffix);
        if (array == null) {
            Log.error("Failed to find matching key for suffix %s in json object", keySuffix);
            return null;
        }
        if (array.length() == 0) {
            Log.error("Failed to extract answers : array length 0");
            return null;
        }
        // NOTE : due to time - only considering first value, in production would handle this better
        Log.l("Taking first value");
        try {
            JSONObject firstElement = array.getJSONObject(0);
            return firstElement.optString("value");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Performs an iterative search of all keys in a json object and returns an object in which the key has a suffix of @param keySuffix
     */
    private JSONArray searchJsonForKeySuffix(@Nullable JSONObject object, @NotNull String keySuffix) {
        if (object == null) {
            return null;
        }
        for (Iterator iterator = object.keys(); iterator.hasNext(); ) {
            String key = (String) iterator.next();
            Log.l("Found key : " + key);
            if (key.toLowerCase().matches(".*" + keySuffix)) {
                Log.l("found correct key : " + keySuffix);
                JSONArray obj = object.optJSONArray(key);
                if (obj != null) {
                    return obj;
                }
            }
            JSONObject obj = object.optJSONObject(key);
            if (obj != null) {
                JSONArray objNested = searchJsonForKeySuffix(obj, keySuffix);
                if (objNested != null) {
                    return objNested;
                }
            }
        }
        return null;
    }
}
