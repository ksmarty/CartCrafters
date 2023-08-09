# CartCrafters API

## `/user`

### `/user/create`

#### Params

- `username`: `String`
- `password`: `String`

### `/user/login`

#### Params

- `username`: `String`
- `password`: `String`

### `/user/logout` 🔒️

### `/user/details` 🔒️

#### Returns

- Database entry for user. e.g:
    - Model: model.User, table: 'users', attributes: {password=HASHED_PASSWORD, username=coolTestUser123}
