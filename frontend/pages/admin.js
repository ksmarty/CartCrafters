import React, { useState, useEffect } from 'react';

const AdminPage = () => {
  
  const [orderItems, setOrderItems] = useState([]);
  const [orders, setOrders] = useState([]);
  const [selectedFile, setSelectedFile] = useState(null);
  const [selectedProductId, setSelectedProductId] = useState(null);

  useEffect(() => {
    // Fetch orders from the API
    fetch('http://localhost:8080/order/getAll', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
    })
      .then(response => {
        if (response.ok) {
          return response.json();
        } else {
          throw new Error('Error while fetching orders');
        }
      })
      .then(data => {
        // Update the orders state
        console.log(data)
        setOrders(data);
      })
      .catch(error => {
        console.error('Error:', error);
      });

    // Fetch order items...
  }, []);

  const setImage = (productId, imageFile) => {
    const formData = new FormData();
    formData.append('item', productId);
    formData.append('image', imageFile);
  
    fetch('http://localhost:8080/product/set/image', {
      method: 'POST',
      body: formData,
      credentials: 'include',
    })
      .then(response => {
        if (response.ok) {
          alert('Image set successfully');
        } else {
          throw new Error('Error while setting image');
        }
      })
      .catch(error => {
        console.error('Error:', error);
        alert('Failed to set image');
      });
  };

  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
  };

  const handleProductIdChange = (event) => {
    setSelectedProductId(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    if (selectedProductId && selectedFile) {
      setImage(selectedProductId, selectedFile);
    } else {
      alert('Please select a product ID and an image');
    }
  };

  return (
    <div className="container mx-auto px-4  ">
      <h1 className="text-2xl font-bold mb-4">Admin Panel</h1>

      <h2 className="text-xl font-bold mb-2">Orders</h2>
      <table className="table-auto w-full mb-4 bg-blue-400 text-black">
        <thead>
          <tr>
            <th className="px-4 py-2">Order ID</th>
            <th className="px-4 py-2">Total Amount</th>
            <th className="px-4 py-2">User ID</th>
          </tr>
        </thead>
        <tbody>
          {orders.map((order, index) => (
            <tr key={index} className={index % 2 === 0 ? 'bg-gray-200' : ''}>
              <td className="border px-4 py-2">{order.orderid}</td>
              <td className="border px-4 py-2">{order.totalamount}</td>
              <td className="border px-4 py-2">{order.userid}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <h2 className="text-xl font-bold mb-2">Set Product Image</h2>
      <form onSubmit={handleSubmit} className="mb-4">
        <div className="flex mb-2">
          <label htmlFor="product-id" className="mr-2">Product ID:</label>
          <input type="number" id="product-id" onChange={handleProductIdChange} className="border px-2 py-1 text-black"/>
        </div>
        <div className="flex mb-2">
          <label htmlFor="product-image" className="mr-2">Image:</label>
          <input type="file" id="product-image" onChange={handleFileChange} className="border px-2 py-1"/>
        </div>
        <button type="submit" className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
          Upload Image
        </button>
      </form>

      {/* Render the rest of your admin page here */}
    </div>
  );
};

export default AdminPage;