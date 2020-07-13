package marianoesteban.guessthelanguage.wikipedia;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WikipediaService {

    @GET("page/random/summary")
    Call<PageSummary> getRandomPageSummary();
}
