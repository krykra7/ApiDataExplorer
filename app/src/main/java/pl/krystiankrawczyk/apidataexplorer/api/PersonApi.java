package pl.krystiankrawczyk.apidataexplorer.api;

import pl.krystiankrawczyk.apidataexplorer.api.model.Results;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Krystian Krawczyk on 25.08.2017.
 */

public interface PersonApi {

    @GET("/api/")
    Observable<Results> getPersonList(@Query("results") Integer listSize);
}
