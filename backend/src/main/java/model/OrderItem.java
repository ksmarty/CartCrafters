package model;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.IdName;

@IdName("orderItemId")
@BelongsToParents({
        @BelongsTo(parent = Order.class, foreignKeyName = "orderId"),
        @BelongsTo(parent = Product.class, foreignKeyName = "productId"),
})
public class OrderItem extends Model {}
