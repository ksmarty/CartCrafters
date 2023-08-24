package controller;

import com.google.gson.Gson;
import db.ProductDB;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.BoundExtractedResult;
import model.Product;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

import static controller.Route.ProtectedRoute.ADMIN;
import static controller.Route.ProtectedRoute.NONE;

@WebServlet(urlPatterns = "/product/*", name = "productServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class ProductServlet extends BaseServlet {
    public ProductServlet() {
        super();
        addPaths("product",
                List.of(
                        new Route("get/all", this::getAll, NONE),
                        new Route("get/categories", this::getCategories, NONE),
                        new Route("get/brands", this::getBrands, NONE),
                        new Route("get/product", this::getProduct, NONE),
                        new Route("search", this::search, NONE),
                        new Route("get/image", this::getImage, NONE),
                        new Route("set/image", this::setImage, ADMIN)
                ));
    }

    public void getAll() {
        res.println(new ProductDB().getAll().toJson(true));
    }

    public void getCategories() {
        res.println(new Gson().toJson(new ProductDB().getCategories()));
    }

    public void getBrands() {
        res.println(new Gson().toJson(new ProductDB().getBrands()));
    }

    public void getProduct() {
        req.getParameterInt("id").ifPresent(
                (id) -> res.println(new ProductDB().getProductById(id).toJson(true)));
    }

    public void search() {
        req.getParameter("q").ifPresentOrElse(
                query -> {
                    Optional<String> category = req.getParameter("category");
                    Optional<String> brand = req.getParameter("brand");

                    List<Product> results = FuzzySearch
                            .extractAll(query, new ProductDB().getAll(), e -> e.getString("name"), 80)
                            .parallelStream()
                            .map(BoundExtractedResult::getReferent)
                            .filter(category.isPresent() ? product -> product.getString("category").equals(category.get()) : product -> true)
                            .filter(brand.isPresent() ? product -> product.getString("brand").equals(brand.get()) : product -> true)
                            .toList();

                    res.println(toJSON(results));
                },
                () -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Query not present!"));
    }

    public void getImage() {
        req.getParameterInt("item").ifPresentOrElse(
                item -> {
                    res.setContentType("image/jpeg");

                    String imagePath = "/storage/images/" + item + ".jpg";

                    // Read the image file from the server
                    try (InputStream inputStream = new FileInputStream(getServletContext().getRealPath(imagePath));
                         OutputStream outputStream = res.getOutputStream()
                    ) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    } catch (IOException e) {
                        res.sendError(HttpServletResponse.SC_NOT_FOUND, "Requested image not found!");
                    }
                },
                () -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item number is not present!"));
    }

    public void setImage() {
        req.getParameterInt("item").ifPresentOrElse(
                item -> req.getPart("image").ifPresentOrElse(
                        image -> {
                            try {
                                image.write(getServletContext().getRealPath("/storage/images/" + item + ".jpg"));
                                res.sendResponse("Set image for product #%d successfully!", item);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        () -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Image is not present!")),
                () -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item number is not present!"));
    }
}
