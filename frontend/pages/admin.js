import React, { useState, useEffect } from 'react';

const AdminPage = () => {
  
  const [orders, setOrders] = useState([]);
  const [orderItems, setOrderItems] = useState([]);
  const [selectedOrder, setSelectedOrder] = useState(null);

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

  const handleShowOrder = (order) => {
    setSelectedOrder(order);

    fetch('http://localhost:8080/order/getItemsAdmin?order=' + order.orderid, {
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
        setOrderItems(data);
      })
      .catch(error => {
        console.error('Error:', error);
      });
  }

  return (
    <div className="container mx-auto px-4">
      <h1 className="text-2xl font-bold mt-8 mb-4 text-center">Admin Panel</h1>

      <hr className="w-2/3 h-1 mx-auto my-4 bg-gray-200 border-0 rounded md:my-10 dark:bg-gray-700" />

      <table className="table-auto w-full mb-4 bg-blue-400 text-black">
        <thead>
          <tr>
            <th className="px-4 py-2">Order ID</th>
            <th className="px-4 py-2">Total Amount</th>
            <th className="px-4 py-2">User ID</th>
            <th className="px-4 py-2">View Order</th>
          </tr>
        </thead>
        <tbody>
          {orders.map((order, index) => (
            <tr key={index} className={index % 2 === 0 ? 'bg-gray-200' : ''}>
              <td className="border px-4 py-2">{order.orderid}</td>
              <td className="border px-4 py-2">${order.totalamount}</td>
              <td className="border px-4 py-2">{order.userid}</td>
              <td className="border px-4 py-2">
                <button className='underline underline-offset-4' onClick={() => { handleShowOrder(order) }}>
                  View Order
                </button>  
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {!!orderItems.length && (
        <div>
          <hr className="w-2/3 h-1 mx-auto my-4 bg-gray-200 border-0 rounded md:my-10 dark:bg-gray-700" />

          <h2 className="text-xl font-bold my-8 text-center">Order #{selectedOrder.orderid}</h2>
          
          <table className="table-fixed w-full mb-4 bg-blue-400 text-black">
            <thead>
              <tr>
                <th className="px-4 py-2">Product Name</th>
                <th className="px-4 py-2">Brand</th>
                <th className="px-4 py-2">Quantity</th>
                <th className="px-4 py-2">Amount</th>
              </tr>
            </thead>
            <tbody>
              {orderItems.map((item, index) => (
                <tr key={index} className={index % 2 === 0 ? 'bg-gray-200' : ''}>
                  <td className="border px-4 py-2">{item.parents.products[0].name}</td>
                  <td className="border px-4 py-2">{item.parents.products[0].brand}</td>
                  <td className="border px-4 py-2">{item.quantity}</td>
                  <td className="border px-4 py-2">${item.amount}</td>
                </tr>
              ))}
            </tbody>
          </table>

          <h2 className="text-xl font-bold my-8 text-center">Total: ${selectedOrder.totalamount}</h2>
        </div>
      )}
    </div>
  );
};

export default AdminPage;