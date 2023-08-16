import okhttp3.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.CookieManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
            assertTrue(response.body().string().contains("\"quantity\":4"));
        }

        /* ***********************
         * Get contents
         * ***********************/

        Request requestGet = new Request.Builder()
                .url(getPath("get"))
                .build();

        try (Response response = client.newCall(requestGet).execute()) {
            assert response.body() != null;
            assertTrue(response.body().string().contains("\"quantity\":4"));
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
            assertTrue(response.body().string().contains("\"quantity\":2"));
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
            assertEquals("[\n\n]", response.body().string().trim());
        }
    }
}
