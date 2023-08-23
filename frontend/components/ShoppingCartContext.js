import React, { useState, createContext, useEffect } from 'react';

export const ShoppingCartContext = createContext();

export const ShoppingCartProvider = ({ children }) => {
  const [cart, setCart] = useState([]);
  const [user, setUser] = useState({ username: `guest${Math.floor(Math.random() * 10000)}` });

  useEffect(() => {
    // Retrieve the user from local storage once the component has mounted
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
      setUser(JSON.parse(savedUser));
    }
  }, []);

  // Save the user to local storage whenever it changes
  useEffect(() => {
    localStorage.setItem('user', JSON.stringify(user));
  }, [user]);

  return (
    <ShoppingCartContext.Provider value={{ cart, setCart, user, setUser }}>
      {children}
    </ShoppingCartContext.Provider>
  );
};