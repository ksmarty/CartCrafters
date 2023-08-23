import Link from 'next/link'

import React, { useState, useContext, useEffect } from 'react';
import { useRouter } from 'next/router'




import { ShoppingCartContext } from '../components/ShoppingCartContext.js';

// Mock catalogue data
const catalogueItems = [
  { id: 1, name: 'Triangle', price: 50, category: 'Shape', brand: 'BrandA' },
  { id: 2, name: 'Circle', price: 100, category: 'Shape', brand: 'BrandB' },
  { id: 3, name: 'Square', price: 75, category: 'Shape', brand: 'BrandA' },
  // Add more items as needed
];

export default function Home({ children }) {
  const router = useRouter();
  const [items, setItems] = useState(catalogueItems);

  const [selectedCategory, setSelectedCategory] = useState('All');
  const [selectedBrand, setSelectedBrand] = useState('All');

  const [sortType, setSortType] = useState('price-asc');

  const [cart, setCart] = useContext(ShoppingCartContext);

  const { user } = useContext(ShoppingCartContext);  // access user from ShoppingCartContext

  useEffect(() => {
    console.log(user.username);  // log the user's username
  }, []);

  const filteredItems = catalogueItems
  .filter(item => {
    return (
      (selectedCategory === "All" || item.category === selectedCategory) &&
      (selectedBrand === "All" || item.brand === selectedBrand)
    );
  })
  .sort((a, b) => {
    if (sortType === "price-asc") {
      return a.price - b.price;
    } else if (sortType === "price-desc") {
      return b.price - a.price;
    } else if (sortType === "name") {
      return a.name.localeCompare(b.name);
    }
    return 0;
  });

  const sortItems = (type) => {
    // Clone the current state
    const newItems = [...filteredItems];

    if (type === 'price-asc') {
      newItems.sort((a, b) => a.price - b.price);
    } else if (type === 'price-desc') {
      newItems.sort((a, b) => b.price - a.price);
    } else if (type === 'name') {
      newItems.sort((a, b) => a.name.localeCompare(b.name));
    }

    setItems(newItems);
  };

  const filterItems = (type, value) => {
    let newItems = [];

    if (type === 'category') {
      newItems = catalogueItems.filter((item) => item.category === value);
    } else if (type === 'brand') {
      newItems = catalogueItems.filter((item) => item.brand === value);
    }

    setItems(newItems);
  };

  return (
    <div className="text-yellow-600">
      {/* Add sort and filter controls here */}
      <div>
  {/* Sort controls */}
  <label htmlFor="sort">Sort by: </label>
  <select id="sort" onChange={e => setSortType(e.target.value)}>    <option value="price-asc">Price (Low to High)</option>
    <option value="price-desc">Price (High to Low)</option>
    <option value="name">Name</option>
  </select>

      {/* Filter controls */}
      <label htmlFor="category">Filter by Category:</label>
      <select id="category" onChange={e => setSelectedCategory(e.target.value)}>
        <option value="All">All</option>
        <option value="Shape">Shape</option>
        {/* Add more categories as needed */}
      </select>
      

      <label htmlFor="brand">Filter by Brand:</label>
      <select id="brand" onChange={e => setSelectedBrand(e.target.value)}>
        <option value="All">All</option>
        <option value="BrandA">BrandA</option>
        <option value="BrandB">BrandB</option>
        {/* Add more brands as needed */}
      </select>
  <br />
</div>

      <main className="flex flex-col items-center justify-start flex-1 text-center">
        <p className="mt-3 w-full md:text-2xl">Catalog</p>

        {/* Catalogue Items */}
        <div className="flex flex-wrap justify-center gap-4 p-4">
        {filteredItems.map((item) => (
  <div key={item.id} className="w-64 h-64 bg-gray-200 flex flex-col items-center justify-center p-4">
    <svg
      width="50"
      height="50"
      viewBox="0 0 50 50"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      {/* Use different SVG shapes based on the item name */}
      {item.name === 'Triangle' && <path d="M25 0L50 50H0L25 0Z" fill="#C4C4C4" />}
      {item.name === 'Circle' && <circle cx="25" cy="25" r="25" fill="#C4C4C4" />}
      {item.name === 'Square' && <rect width="50" height="50" fill="#C4C4C4" />}
    </svg>
    {/* Item Information */}
    <p className="mt-2 font-bold">{item.name}</p>
    <p className="mt-2">Price: ${item.price}</p>
    <p className="mt-2">Category: {item.category}</p>
    <p className="mt-2">Brand: {item.brand}</p>

      {/* Add To Cart Button */}
      <button onClick={() => {
          setCart([...cart, item])
          console.log(cart)
          }} className="mt-2 bg-green-500 text-white px-4 py-2 rounded">
        Add to Cart
      </button>
    
  </div>
))}
        </div>
      </main>
    </div>
  );
}
