package pl.krystiankrawczyk.apidataexplorer.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Krystian Krawczyk on 25.08.2017.
 */

public class ServiceGenerator {

    private static final String API_BASE_URL = "https://randomuser.me";

    private static PersonApi service;

    public ServiceGenerator() {
    }

    public static PersonApi getInstance() {
        if (service == null) {
            service = createRetrofitSerivice(PersonApi.class, API_BASE_URL);
        }
        return service;
    }

    private static <T> T createRetrofitSerivice(final Class<T> serviceClass, final String endPoint) {
        final Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(endPoint)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return restAdapter.create(serviceClass);
    }

}
