import React, { useContext, useState } from 'react';
import { ShoppingCartContext } from '../components/ShoppingCartContext.js';

const Cart = () => {
  const { cart, setCart, user } = useContext(ShoppingCartContext);

  const [quantityInputs, setQuantityInputs] = useState(cart.map(item => item.quantity));

  const handleQuantityChange = (index, newQuantity) => {
    let newQuantityInputs = [...quantityInputs];
    newQuantityInputs[index] = newQuantity;
    setQuantityInputs(newQuantityInputs);
  }


  const updateQuantity = (index) => {
    const itemToUpdate = cart[index];
    const url = 'http://localhost:8080/cart/update';
    const formBody = `item=${encodeURIComponent(itemToUpdate.productid)}&qty=${encodeURIComponent(quantityInputs[index])}`;

    fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: formBody,
      credentials: 'include'
    })
      .then((response) => {
        if (response.status === 200) {
          let newCart = [...cart];
          newCart[index].quantity = quantityInputs[index];
          setCart(newCart);
          alert("Quantity updated!")
        } else {
          throw new Error('Unexpected status code');
        }
      })
      .catch((error) => {
        console.error(error);
      });
  }

  const totalPrice = cart.reduce((total, item) => total + (item.parents.products[0].price * item.quantity), 0);

  const removeItem = (index) => {
    const itemToRemove = cart[index];
    const url = 'http://localhost:8080/cart/remove';
    const formBody = `item=${encodeURIComponent(itemToRemove.productid)}`;
  
    fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: formBody,
      credentials: 'include'
    })
      .then((response) => {
        if (response.status === 200) {
          const newCart = [...cart];
          newCart.splice(index, 1);
          setCart(newCart);
        } else {
          throw new Error('Unexpected status code');
        }
      })
      .catch((error) => {
        console.error(error);
      });
  }

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
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Quantity
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Remove
            </th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {cart.map((item, i) => (
            <tr key={i}>
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="flex items-center">
                  <div className="text-sm font-medium text-gray-900">{item.parents.products[0].name}</div>
                </div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="text-sm text-gray-900">${item.parents.products[0].price.toFixed(2)}</div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="text-sm text-gray-900">{item.parents.products[0].category}</div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
                <div className="text-sm text-gray-900">{item.parents.products[0].brand}</div>
              </td>
              <td className="px-6 py-4 whitespace-nowrap">
  <input type="number" min="1" value={quantityInputs[i]} onChange={(e) => handleQuantityChange(i, e.target.value)} className="text-sm text-gray-900" />
  <button onClick={() => updateQuantity(i)} className='pl-4 text-black'>Update</button>
</td>
              <td className="px-6 py-4 text-red-500 whitespace-nowrap">
                <button onClick={() => removeItem(i)}>
                  Remove Item
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="mt-4">
        <p className="text-lg">Total Price: ${totalPrice.toFixed(2)}</p>
      </div>

      <div className="mt-4">
        <button className="bg-blue-500 text-white px-4 py-2 rounded">Checkout</button>
      </div>
    </div>
  );
}

export default Cart;