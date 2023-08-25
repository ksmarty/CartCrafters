import React, { useState, createContext, useEffect } from 'react';

export const ShoppingCartContext = createContext();

export const ShoppingCartProvider = ({ children }) => {
  const [cart, setCart] = useState([]);
  const [user, setUser] = useState(`guest`);

// Fetch the cart from the server
// Fetch the cart from the server
const fetchCart = () => {
  const url = 'http://localhost:8080/cart/get';

  fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include'
  })
    .then((response) => {
      if (response.status === 200) {
        return response.json();
      } else {
        throw new Error('Unexpected status code');
      }
    })
    .then((data) => {
      console.log(data)
      setCart(data)
      
      //console.log(data);
    })
    .catch((error) => {
      console.error(error);
    });
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