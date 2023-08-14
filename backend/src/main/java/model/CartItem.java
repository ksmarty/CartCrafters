package model;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

@IdName("cartItemId")
@Table("Cart_Items")
@BelongsToParents({
        @BelongsTo(parent = Cart.class, foreignKeyName = "cartId"),
        @BelongsTo(parent = Product.class, foreignKeyName = "productId"),
})
public class CartItem extends Model {}
