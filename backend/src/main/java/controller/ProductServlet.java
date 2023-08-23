package controller;

import com.google.gson.Gson;
import dao.ProductDAO;
import db.ProductDB;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

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
        req.getParameter("field").ifPresentOrElse(
                field -> req.getParameter("q").ifPresentOrElse(
                        query -> {
                            ProductDAO pdb = new ProductDB();

                            switch (field) {
                                case "category" -> res.println(pdb.getFromCategory(query).toJson(true));
                                case "brand" -> res.println(pdb.getFromBrand(query).toJson(true));
                                default -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid field name!");
                            }
                        },
                        () -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Field is not present!")),
                () -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Field is not present!"));
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
                        res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid item number!");
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
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        () -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Image is not present!")),
                () -> res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item number is not present!"));
    }
}
