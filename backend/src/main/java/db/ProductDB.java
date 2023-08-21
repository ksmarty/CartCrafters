package db;

import dao.ProductDAO;
import model.Product;
import org.javalite.activejdbc.LazyList;

import java.util.List;
import java.util.stream.Collectors;

import static org.javalite.activejdbc.Base.withDb;

public class ProductDB implements ProductDAO {
    @Override
    public LazyList<Product> getAll() {
        return withDb(() -> Product.findAll().load());
    }

    @Override
    public LazyList<Product> getFromCategory(String category) {
        return withDb(() -> Product.where("category = ?", category));
    }

    @Override
    public LazyList<Product> getFromBrand(String brand) {
        return withDb(() -> Product.where("brand = ?", brand));
    }

    @Override
    public List<String> getCategories() {
        return getColumn("category");
    }

    @Override
    public List<String> getBrands() {
        return getColumn("brand");
    }

    @Override
    public Product getProductById(Integer id) {
        return withDb(() -> Product.findFirst("productId = ?", id));
    }

    @Override
    public boolean updateStock(Integer productId, int quantity) {
        return withDb(() -> getProductById(productId).setInteger("stock", quantity).saveIt());
    }

    private List<String> getColumn(String column) {
        return getAll().stream().map(e -> e.getString(column)).distinct().collect(Collectors.toList());
    }
}
