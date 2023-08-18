import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.CookieManager;

import static org.junit.Assert.assertEquals;

public class CartTests {
    final String BASE_URL = "http://localhost:8080";
    private OkHttpClient client;

    @BeforeClass
    public static void setUp() {
        // Run once before all tests
    }

    @Before
    public void createClient() {
        client = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(new CookieManager()))
                .build();
    }

    private String getPath(String path) {
        return String.format("%s/cart/%s", BASE_URL, path);
    }

    @Test
    public void testGuestCart() throws IOException {
        /* ***********************
         * Add item
         * ***********************/

        Request requestAdd = new Request.Builder()
                .url(getPath("add"))
                .post(new FormBody.Builder()
                        .add("item", "3")
                        .add("qty", "4")
                        .build())
                .build();

        try (Response response = client.newCall(requestAdd).execute()) {
            assert response.body() != null;
            JsonArray responseJSON = new Gson().fromJson(response.body().string(), JsonArray.class);
            JsonObject selected = (JsonObject) responseJSON.asList().stream().filter(e -> ((JsonObject) e).get("productid").getAsInt() == 3).findFirst().orElse(null);
            assert selected != null;
            assertEquals(4, selected.get("quantity").getAsInt());
        }

        /* ***********************
         * Get contents
         * ***********************/

        Request requestGet = new Request.Builder()
                .url(getPath("get"))
                .build();

        try (Response response = client.newCall(requestGet).execute()) {
            assert response.body() != null;
            JsonArray responseJSON = new Gson().fromJson(response.body().string(), JsonArray.class);
            JsonObject selected = (JsonObject) responseJSON.asList().stream().filter(e -> ((JsonObject) e).get("productid").getAsInt() == 3).findFirst().orElse(null);
            assert selected != null;
            assertEquals(4, selected.get("quantity").getAsInt());
        }

        /* ***********************
         * Update quantity
         * ***********************/

        Request requestUpdate = new Request.Builder()
                .url(getPath("update"))
                .post(new FormBody.Builder()
                        .add("item", "3")
                        .add("qty", "2")
                        .build())
                .build();

        try (Response response = client.newCall(requestUpdate).execute()) {
            assert response.body() != null;
            JsonArray responseJSON = new Gson().fromJson(response.body().string(), JsonArray.class);
            JsonObject selected = (JsonObject) responseJSON.asList().stream().filter(e -> ((JsonObject) e).get("productid").getAsInt() == 3).findFirst().orElse(null);
            assert selected != null;
            assertEquals(2, selected.get("quantity").getAsInt());
        }

        /* ***********************
         * Remove item
         * ***********************/

        Request requestRemove = new Request.Builder()
                .url(getPath("remove"))
                .post(new FormBody.Builder()
                        .add("item", "3")
                        .build())
                .build();

        try (Response response = client.newCall(requestRemove).execute()) {
            assert response.body() != null;
            JsonArray responseJSON = new Gson().fromJson(response.body().string(), JsonArray.class);
            assertEquals(0, responseJSON.size());
        }
    }
}
