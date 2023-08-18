# CartCrafters API

> - 🔑 Requires user be an admin
> - 🔓 Requires user be logged out
> - 🔒️ Requires user be logged in
> - ⠀⠀ Works for guests and users

## `/user`

### `/user/create` 🔓

#### Params

- `username`: `String`
- `password`: `String`

### `/user/login` 🔓

#### Params

- `username`: `String`
- `password`: `String`

### `/user/logout` 🔒️

### `/user/details` 🔒️

#### Returns

- Database entry for user. e.g:
    - Model: model.User, table: 'users', attributes: {password=HASHED_PASSWORD, username=coolTestUser123}

## `/cart`

### `/cart/add`

#### Params

- `item`: `Number`
- `qty`: `Number`

#### Returns

Updated contents of the cart

```js
[
    {
        "cartid": Number,
        "cartitemid": Number,
        "productid": Number,
        "quantity": Number,
        "parents": {
            "products": [
                {
                    "name": String,
                    "price": Number,
                    "productid": Number,
                    "quantity": Number
                }
            ]
        }
    }
]
```

### `/cart/update`

#### Params

- `item`: `Number`
- `qty`: `Number`

#### Returns

Updated contents of the cart. See `/cart/add`

### `/cart/remove`

#### Params

- `item`: `Number`

#### Returns

Updated contents of the cart. See `/cart/add`

### `/cart/get`

#### Returns

Updated contents of the cart. See `/cart/add`

### `/cart/checkout`

#### Returns

Throws 409 if user tries to purchase more of an item than is available.

```js
{
    "orderid": Number,
    "totalamount": Number,
    "userid": Number
}
```

## `/order`

### `/order/get` 🔒

#### Returns

All the user's orders

```js
[
    {
        "orderid": Number,
        "totalamount": Number,
        "userid": Number
    }
]
```

### `/order/getItems` 🔒

#### Params

- `order`: `Number`

#### Returns

All the items in a user's order

```js
[
    {
        "amount": Number,
        "orderid": Number,
        "orderitemid": Number,
        "productid": Number,
        "quantity": Number,
        "parents": {
            "products": [
                {
                    "name": String,
                    "price": Number,
                    "productid": Number,
                    "quantity": Number
                }
            ]
        }
    }
]
```

### `/order/getAll` 🔑

#### Returns

Every order in the database. See `/order/get`

### `/order/getItemsAdmin` 🔑

#### Params

- `order`: `Number`

#### Returns

All the items in any order