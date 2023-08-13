// ShoppingCartContext.js
import React from 'react';

export const ShoppingCartContext = React.createContext();

export const ShoppingCartProvider = ({ children }) => {
  const [cart, setCart] = React.useState([]);

  return (
    <ShoppingCartContext.Provider value={[cart, setCart]}>
      {children}
    </ShoppingCartContext.Provider>
  );
};