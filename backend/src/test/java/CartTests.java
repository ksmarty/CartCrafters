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

    private Response login() throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add("username", "john123")
                .add("password", "password123")
                .build();

        Request request = new Request.Builder()
                .url(getPath("login"))
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response;
        }
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
            JsonObject selected = responseJSON.asList().stream()
                    .map(e -> (JsonObject) e)
                    .filter(e -> e.get("productid").getAsInt() == 3)
                    .findFirst().orElse(null);
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
            JsonObject selected = responseJSON.asList().stream()
                    .map(e -> (JsonObject) e)
                    .filter(e -> e.get("productid").getAsInt() == 3)
                    .findFirst().orElse(null);
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
            JsonObject selected = responseJSON.asList().stream()
                    .map(e -> (JsonObject) e)
                    .filter(e -> e.get("productid").getAsInt() == 3)
                    .findFirst().orElse(null);
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

    @Test
    public void testCheckout() throws IOException {
        login();

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
            JsonObject selected = responseJSON.asList().stream()
                    .map(e -> (JsonObject) e)
                    .filter(e -> e.get("productid").getAsInt() == 3)
                    .findFirst().orElse(null);
            assert selected != null;
            assertEquals(4, selected.get("quantity").getAsInt());
        }

        /* ***********************
         * Add item
         * ***********************/

        requestAdd = new Request.Builder()
                .url(getPath("add"))
                .post(new FormBody.Builder()
                        .add("item", "4")
                        .add("qty", "1")
                        .build())
                .build();

        try (Response response = client.newCall(requestAdd).execute()) {
            assert response.body() != null;
            JsonArray responseJSON = new Gson().fromJson(response.body().string(), JsonArray.class);
            JsonObject selected = responseJSON.asList().stream()
                    .map(e -> (JsonObject) e)
                    .filter(e -> e.get("productid").getAsInt() == 4)
                    .findFirst().orElse(null);
            assert selected != null;
            assertEquals(1, selected.get("quantity").getAsInt());
        }

        /* ***********************
         * Checkout
         * ***********************/

        requestAdd = new Request.Builder()
                .url(getPath("checkout"))
                .post(new FormBody.Builder()
                        .build())
                .build();

        try (Response response = client.newCall(requestAdd).execute()) {
            assert response.body() != null;
            System.out.println(response.body().string());
            JsonObject responseJSON = new Gson().fromJson(response.body().string(), JsonObject.class);
            assertEquals(659.95, responseJSON.get("totalamount").getAsDouble(), 0.01);
        }
    }
}
