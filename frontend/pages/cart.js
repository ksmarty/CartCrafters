import React, { useContext } from 'react';
import { ShoppingCartContext } from '../components/ShoppingCartContext.js';

const Cart = () => {
  const { cart, setCart, user } = useContext(ShoppingCartContext);

  const totalPrice = cart.reduce((total, item) => total + item.price, 0);

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-4">Shopping Cart</h1>
      <table className="min-w-full divide-y divide-gray-200">
        <thead className="bg-gray-50">
          <tr>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Item
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Price
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Category
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Brand
            </th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {cart.map((item, i) => (
            <tr key={i}>
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="flex items-center">
                  <div className="text-sm font-medium text-gray-900">{item.name}</div>
                </div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="text-sm text-gray-900">${item.price}</div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="text-sm text-gray-900">{item.category}</div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="text-sm text-gray-900">{item.brand}</div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="mt-4">
        <p className="text-lg">Total Price: ${totalPrice}</p>
      </div>

      <div className="mt-4">
        <button className="bg-blue-500 text-white px-4 py-2 rounded">Checkout</button>
      </div>
    </div>
  );
}

export default Cart;