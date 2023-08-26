import React, { useState, useContext, useEffect } from 'react';
import { useRouter } from 'next/router';

import pluralize from "pluralize";

import Modal from '../components/Modal.js';

import { ShoppingCartContext } from '../components/ShoppingCartContext.js';
import Image from 'next/image.js';

import { LazyLoadImage } from 'react-lazy-load-image-component';
import 'react-lazy-load-image-component/src/effects/blur.css';

export default function Home({ children }) {
  const router = useRouter();
  const [items, setItems] = useState([]);

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
    setSelectedItem(item);
    setIsModalOpen(true);
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
      } else if (sortType === "name-asc") {
        return a.name.localeCompare(b.name);
      } else if (sortType === "name-desc") {
        return b.name.localeCompare(a.name);
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
  <div className="flex flex-col flex-1 text-white">
    <main className="flex flex-col items-center justify-start flex-1 text-center pt-4">
      {user['username'] && (
        <p className="mt-3 md:text-xl">Welcome back {user["firstname"] || user["username"]}!</p>
      )}
      <p className="mt-3 md:text-xl">You have { pluralize('item', cart.reduce((sum, item) => sum + item.quantity, 0), true) } in your cart</p>

      <hr className="w-2/3 h-1 mx-auto my-4 bg-gray-200 border-0 rounded md:my-10 dark:bg-gray-700" />

      <p className="mb-12 text-2xl font-bold">Catalog</p>

      {/* Add sort and filter controls here */}
      <div className='flex flex-row space-x-8'>
        {/* Sort controls */}
        <div>
          <label htmlFor="sort">Sort by: </label>
          <select id="sort" onChange={e => setSortType(e.target.value)} className='text-black'>
            <option value="price-asc">Price (Low to High)</option>
            <option value="price-desc">Price (High to Low)</option>
            <option value="name-asc">Name (A-Z)</option>
            <option value="name-desc">Name (Z-A)</option>
          </select>
        </div>

        {/* Filter controls */}
        <div>
          <label htmlFor="category">Filter by Category: </label>
          <select id="category" onChange={e => setSelectedCategory(e.target.value)} className='text-black'>
            <option value="All">All</option>
            {categories.map(category => (
              <option key={category} value={category}>{category}</option>
            ))}
          </select>
        </div>

        <div>
          <label htmlFor="brand">Filter by Brand: </label>
          <select id="brand" onChange={e => setSelectedBrand(e.target.value)} className='text-black'>
            <option value="All">All</option>
            {brands.map(brand => (
              <option key={brand} value={brand}>{brand}</option>
            ))}
          </select>
        </div>
      </div>
      
      {/* Catalogue Items */}
      <div className="w-full flex flex-wrap justify-center gap-4 p-4">
        {filteredItems.map((item) => (
          <div key={item.productid} className="w-64 bg-gray-500 flex flex-col items-center justify-center p-4">
            {/* Item Information */}
            <p className="my-4 font-bold">{item.name}</p>

            <hr className="w-2/3 h-0.5 mx-auto mt-1 mb-2 bg-gray-200 border-0 rounded dark:bg-gray-700" />

            <p className="mt-2">Price: ${item.price}</p>
            <p className="mt-2">Category: {item.category}</p>
            <p className="mt-2">Brand: {item.brand}</p>

            <div className='mt-6 w-full flex justify-between'>
              {/* More Details Button */}
              <button onClick={() => { handleOpenModal(item) }} className="mt-2 bg-blue-500 text-white px-4 py-2 rounded">
                More Details
              </button>
              {/* Add To Cart Button */}
              <button onClick={() => { addToCart(item) }} className="mt-2 bg-green-500 text-white px-4 py-2 rounded">
                +
              </button>
            </div>

            {/* Modal Overlay */}
            {isModalOpen && selectedItem.productid === item.productid && (
              <Modal>
                  <div className="flex space-x-8">
                  <LazyLoadImage 
                    src={"http://localhost:8080/product/get/image?item="+item.productid} 
                    alt={item.name} 
                    effect="blur"
                    height={512}
                    width={512}
                    className="object-cover rounded-lg"
                  />
                  <div className='flex flex-col justify-start w-48'>
                    <h2 className="text-2xl font-bold mb-4 text-black text-left">{item.name}</h2>
                    <p className="mb-4 text-left text-black">{item.description}</p>
                    <div className="flex justify-between items-center text-black">
                      <p className="font-bold text-lg text-black">${item.price}</p>
                      <button onClick={() => { addToCart(item) }} className="mt-2 bg-green-500 text-white px-4 py-2 rounded">
                        +
                      </button>
                    </div>
                  </div>
                </div>
              </Modal>
            )}
          </div>
        ))}
      </div>
    </main>
  </div>
  );
}
