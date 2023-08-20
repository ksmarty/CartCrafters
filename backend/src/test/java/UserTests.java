import okhttp3.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.CookieManager;

import static org.junit.Assert.assertEquals;

public class UserTests {

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

    private String getPath(String path) {
        return String.format("%s/user/%s", BASE_URL, path);
    }

    @Test
    public void testLogin() throws IOException {
        Response loginResponse = login();
        assertEquals(HttpServletResponse.SC_OK, loginResponse.code());
    }

    @Test
    public void testLogout() throws IOException {
        login();

        Request request = new Request.Builder()
                .url(getPath("logout"))
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
            assertEquals(HttpServletResponse.SC_OK, response.code());
        }
    }

    @Test
    public void testCreateUser() throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add("username", "coolTestUser123")
                .add("password", "securePassw0rd")
                .build();

        Request request = new Request.Builder()
                .url(getPath("create"))
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertEquals(HttpServletResponse.SC_OK, response.code());
        }
    }

    @Test
    public void testSessions() throws IOException {
        Response loginResponse = login();
        assertEquals(HttpServletResponse.SC_OK, loginResponse.code());

        // New session
        createClient();

        // Login will fail if session is preserved between clients
        loginResponse = login();
        assertEquals(HttpServletResponse.SC_OK, loginResponse.code());
    }
}
