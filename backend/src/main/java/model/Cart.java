package model;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.IdName;

@IdName("cartId")
@BelongsTo(parent = User.class, foreignKeyName = "userId")
public class Cart extends Model {}
