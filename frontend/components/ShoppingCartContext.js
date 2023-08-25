import React, { useState, createContext, useEffect } from 'react';

export const ShoppingCartContext = createContext();

export const ShoppingCartProvider = ({ children }) => {
  const [cart, setCart] = useState([]);
  const [user, setUser] = useState(`guest`);

// Fetch the cart from the server
// Fetch the cart from the server
const fetchCart = async () => {
  try {
    const response = await fetch('http://localhost:8080/cart/get');

    if (response.ok) {
      const text = await response.text();
      console.log('Raw response:', text);
      const data = JSON.parse(text);
      setCart(data);
    } else {
      console.error(`HTTP error! status: ${response.status}`);
    }

  } catch (error) {
    console.error('Error:', error);
  }
};



  useEffect(() => {
    // Retrieve the user from local storage once the component has mounted
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
      setUser(JSON.parse(savedUser));
    }
    fetchCart();


  }, []);

  // Save the user to local storage whenever it changes
  useEffect(() => {
    localStorage.setItem('user', JSON.stringify(user));
  }, [user]);

  useEffect(() => {
    localStorage.setItem('cart', JSON.stringify(cart));
  }, [cart])



  return (
    <ShoppingCartContext.Provider value={{ cart, setCart, user, setUser }}>
      {children}
    </ShoppingCartContext.Provider>
  );
};