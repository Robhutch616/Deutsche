package service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

import java.util.Map;

/**
 * Using retrofit interface methodology
 */
public interface DbpediaApi {

    @GET("search/KeywordSearch")
    Call<ResponseBody> search(@HeaderMap Map<String,String> headers, @Query("QueryString") String query);
}
