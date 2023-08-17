package dao;

import model.Product;
import org.javalite.activejdbc.LazyList;

import java.util.List;

public interface ProductDAO {
    LazyList<Product> getAll();

    LazyList<Product> getFromCategory(String category);

    LazyList<Product> getFromBrand(String brand);

    List<String> getCategories();

    List<String> getBrands();

    Product getProductById(String id);

    boolean updateStock(String productId, int quantity);
}
