CREATE TABLE Users (
  userId INT PRIMARY KEY,
  firstName VARCHAR(50),
  lastName VARCHAR(50),
  email VARCHAR(100),
  password VARCHAR(20),
  address VARCHAR(200)
);

CREATE TABLE Products (
  productId INT PRIMARY KEY,
  name VARCHAR(100),
  description VARCHAR(500),
  category VARCHAR(50),
  brand VARCHAR(50),
  price DECIMAL(10, 2),
  quantity INT
);

CREATE TABLE Orders (
  orderId INT PRIMARY KEY,
  userId INT,
  orderDate DATE,
  totalAmount DECIMAL(10, 2),
  FOREIGN KEY (userId) REFERENCES Users(userId)
);

CREATE TABLE OrderItems (
  orderItemId INT PRIMARY KEY,
  orderId INT,
  productId INT,
  quantity INT,
  amount DECIMAL(10, 2),
  FOREIGN KEY (orderId) REFERENCES Orders(orderId),
  FOREIGN KEY (productId) REFERENCES Products(productId)
);

------------- Dummy Data -------------

-- Sample users
INSERT INTO Users (userId, firstName, lastName, email, password, address)
VALUES
  (1, 'John', 'Doe', 'john@example.com', 'password123', '123 Main St, City, State 12345'),
  (2, 'Jane', 'Doe', 'jane@example.com', 'password456', '456 Park Ave, City, State 12345'),
  (3, 'Bob', 'Smith', 'bob@example.com', 'password789', '789 Elm St, City, State 12345');

-- Sample products
INSERT INTO Products (productId, name, description, category, brand, price, quantity)
VALUES
  (1, 'Product 1', 'Description 1', 'Category 1', 'Brand 1', 19.99, 5),
  (2, 'Product 2', 'Description 2', 'Category 2', 'Brand 2', 29.99, 10),
  (3, 'Product 3', 'Description 3', 'Category 3', 'Brand 3', 39.99, 2);

-- Sample orders
INSERT INTO Orders (orderId, userId, orderDate, totalAmount)
VALUES
  (1, 1, '2023-02-01', 149.97),
  (2, 2, '2023-02-05', 399.93),
  (3, 3, '2023-02-08', 29.99);

-- Sample order items
INSERT INTO OrderItems (orderItemId, orderId, productId, quantity, amount)
VALUES
  (1, 1, 1, 2, 39.98),
  (2, 1, 2, 1, 29.99),
  (3, 2, 1, 3, 59.97),
  (4, 2, 3, 2, 79.98),
  (5, 3, 2, 1, 29.99);
