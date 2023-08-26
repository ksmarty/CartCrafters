import Link from 'next/link'

import React, { useState, useContext, useEffect } from 'react';
import { useRouter } from 'next/router'

import { LazyLoadImage } from 'react-lazy-load-image-component';
import 'react-lazy-load-image-component/src/effects/blur.css';

import Modal from '../components/Modal.js';


import { ShoppingCartContext } from '../components/ShoppingCartContext.js';

// Mock catalogue data
let catalogueItems = [
  //   { id: 1, name: 'Triangle', price: 50, category: 'Shape', brand: 'BrandA' },
  //   { id: 2, name: 'Circle', price: 100, category: 'Shape', brand: 'BrandB' },
  //   { id: 3, name: 'Square', price: 75, category: 'Shape', brand: 'BrandA' },
  // Add more items as needed
];

export default function Home({ children }) {
  const router = useRouter();
  const [items, setItems] = useState(catalogueItems);

  const [selectedCategory, setSelectedCategory] = useState('All');
  const [selectedBrand, setSelectedBrand] = useState('All');

  const [brands, setBrands] = useState([]);
  const [categories, setCategories] = useState([]);

  const [sortType, setSortType] = useState('price-asc');

  const { cart, setCart, user } = useContext(ShoppingCartContext);


  // Add a loading state variable
  const [loading, setLoading] = useState(true);

//Modal Stuff

  // State for the currently selected item and whether the modal is open
  const [selectedItem, setSelectedItem] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  // Function to open the modal and set the selected item
  const handleOpenModal = (item) => {
    setIsModalOpen(true);
    setSelectedItem(item);
  };

  // Function to close the modal
  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedItem(null);
  };

  // Add event listeners to close the modal when clicking outside of it
  useEffect(() => {
    const handleClose = (e) => {
      if (e.target.id === 'modal') {
        handleCloseModal();
      }
    };
    window.addEventListener('click', handleClose);

    // Clean up event listeners
    return () => {
      window.removeEventListener('click', handleClose);
    };
  }, []);

//End Modal Stuff


  const fetchProducts = () => {
    const url = 'http://localhost:8080/product/get/all';

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
        } 
        return response.json();
            })
      .then((data) => {
        //console.log(data)
        setItems(data)
        setBrandsAndCategories();

        // Set loading to false after all data is loaded
        setLoading(false);

        //console.log(data);
      })
      .catch((error) => {
        console.error(error);
      });
  };



  const setBrandsAndCategories = () => {
    // Extract unique brands and categories from items
    let uniqueBrands = [...new Set(items.map(item => item.brand))];
    let uniqueCategories = [...new Set(items.map(item => item.category))];
    setBrands(uniqueBrands);
    setCategories(uniqueCategories);
  }

  useEffect(setBrandsAndCategories, [items]); // Add items to dependency array

  useEffect(fetchProducts, [])

  let filteredItems = items
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

  const addToCart = (item) => {
    const url = 'http://localhost:8080/cart/add';

    const formBody = `item=${encodeURIComponent(item.productid)}&qty=${encodeURIComponent(1)}`;

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
          return response.json();
        } else {
          return;
          throw new Error('Unexpected status code');
          
        }
      })
      .then((data) => {
        // Assuming the server responds with the new cart data
        // Update the cart state
        setCart(data);
      })
      .catch((error) => {
        console.error(error);
      });

    // Existing code
    console.log(cart);
  }

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

  return ( loading ? <div>Loading...</div> :
    <div className="text-white">
      {/* Add sort and filter controls here */}
      <div>
        {/* Sort controls */}
        <label htmlFor="sort">Sort by: </label>
        <select id="sort" onChange={e => setSortType(e.target.value)} className='text-black'>    <option value="price-asc">Price (Low to High)</option>
          <option value="price-desc">Price (High to Low)</option>
          <option value="name">Name</option>
        </select>

        {/* Filter controls */}
        <label htmlFor="category">Filter by Category:</label>
        <select id="category" onChange={e => setSelectedCategory(e.target.value)} className='text-black'>
          <option value="All">All</option>
          {categories.map(category => (
            <option key={category} value={category}>{category}</option>
          ))}
        </select>

        <label htmlFor="brand">Filter by Brand:</label>
        <select id="brand" onChange={e => setSelectedBrand(e.target.value)} className='text-black'>
          <option value="All">All</option>
          {brands.map(brand => (
            <option key={brand} value={brand}>{brand}</option>
          ))}
        </select>
        <br />
      </div>

      <main className="flex flex-col items-center justify-start flex-1 text-center">
        <p className="mt-3 w-full md:text-2xl">Catalog</p>
        <p className="mt-3 w-full md:text-xl">Welcome {user["username"]} </p>
        <p className="mt-3 w-full md:text-xl">You have {cart.reduce((sum, item) => sum + item.quantity, 0)} items in your cart</p>

        {/* Catalogue Items */}
        <div className="flex flex-wrap justify-center gap-4 p-4">
          {filteredItems.map((item) => (
            <div key={item.productid} className="w-64 h-64 bg-gray-200 flex flex-col items-center justify-center p-4 text-black">

              {/* Item Information */}
              <p className="mt-2 font-bold">{item.name}</p>
              <p className="mt-2">Price: ${item.price}</p>
              <p className="mt-2">Category: {item.category}</p>
              <p className="mt-2">Brand: {item.brand}</p>

              {/* Add To Cart Button */}
              <button onClick={() => { addToCart(item) }} className="mt-2 bg-green-500 text-white px-4 py-2 rounded">
                Add to Cart
              </button>
            {/* More Details Button */}
            <button onClick={() => { handleOpenModal(item) }} className="mt-2 bg-blue-500 text-white px-4 py-2 rounded">
              More Details
            </button>
            </div>
          ))}
        </div>
      </main>
      {/* Modal Overlay */}
{isModalOpen && (
  <Modal>
    <h2 className="text-2xl font-bold mb-4 text-black">{selectedItem.name}</h2>
    <LazyLoadImage 
  src={"http://localhost:8080/product/get/image?item="+selectedItem.productid} 
  alt={selectedItem.name} 
  effect="blur"
  className="w-full h-64 object-cover mb-4"
/>    <p className="mb-4 text-black">{selectedItem.description}</p>
    <div className="flex justify-between items-center text-black">
      <p className="font-bold text-lg">${selectedItem.price}</p>
      <button onClick={handleCloseModal} className="py-2 px-4 bg-red-500 text-white rounded hover:bg-red-600">Close</button>
    </div>
  </Modal>
)}
    </div>
  );
}
