package controller;

import com.google.gson.Gson;
import dao.ProductDAO;
import db.ProductDB;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static controller.Route.ProtectedRoute.NONE;

@WebServlet(urlPatterns = "/product/*", name = "productServlet")
public class ProductServlet extends BaseServlet {
    public ProductServlet() {
        super();
        addPaths("product",
                List.of(
                        new Route("get/all", this::getAll, NONE),
                        new Route("get/categories", this::getCategories, NONE),
                        new Route("get/brands", this::getBrands, NONE),
                        new Route("get/product", this::getProduct, NONE),
                        new Route("search", this::search, NONE)
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
}
