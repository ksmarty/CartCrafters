import React, { useState, useContext, useEffect } from 'react';

import { useRouter } from "next/router"
import Transition from "./Transition"

import Link from 'next/link'

import Head from 'next/head'

import Back from "./Back"


import { ShoppingCartContext, ShoppingCartProvider } from '../components/ShoppingCartContext.js';

const Navbar = () => {

  const { cart, setCart, user, setUser } = useContext(ShoppingCartContext);
  const totalItems = cart.reduce((total, item) => total + item.quantity, 0);

  const router = useRouter();

  const [admin, setAdmin] = useState(false);


  const logout = () => {
    const url = 'http://localhost:8080/user/logout'
    fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      credentials: 'include'
    })
      .then(response => {
        if (response.status === 200) {
          setUser('guest');
          alert(`Successfully logged out`);
          router.push('/');
        } else if (response.status === 400 || response.status === 401) {
          alert("Not logged in!");
          router.reload();
        } else {
          throw new Error('Unexpected status code');
        }
      })
      .catch((error) => {
        console.log('Error:', error);
      });

  }

  const checkAdmin = () => {
    const url = 'http://localhost:8080/user/details'
    fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      credentials: 'include'
    })
      .then(response => {
        if (response.status === 200) {

          return response.json()

        } else if (response.status === 400 || response.status === 401) {

        } else {
          throw new Error('Unexpected status code');
          return;
        }
      })
      .then((data) => {
        if (data) {
          setAdmin(data["isadmin"])
          console.log(data)
          console.log(data["isadmin"])
        }

      })
      .catch((error) => {
        console.log('Error:', error);
      });

  }

  useEffect(checkAdmin,[])  

  return (
    <nav className="w-full p-4 flex justify-between items-center bg-blue-500 text-white">
      <Link href="/" className="hover:text-gray-200"><h1 className="text-lg font-bold">CartCrafters </h1></Link>
      <div className="flex gap-4">
        <Link href="/" className="hover:text-gray-200">Catalog</Link>
        <Link href="/cart" className="hover:text-gray-200">Shopping Cart ({totalItems})</Link>
        {user === 'guest' ? <Link href="/register" className="hover:text-gray-200">Register</Link> : ''}
        {
          admin
            ? <Link href="/admin" className="hover:text-gray-200">Admin </Link>
            : null
        }
        {user === 'guest'
          ? <Link href="/login" className="hover:text-gray-200">Login </Link>
          : <button onClick={logout} className="hover:text-gray-200">Logout {user["username"]}</button>}


      </div>
    </nav>
  );

}


const Layout = ({ children }) => {

  // Calculate the total number of items in the cart

  const router = useRouter()


  return (
    <ShoppingCartProvider>
      <div className="text-white p-4 flex flex-col items-center justify-start min-h-screen py-2  bg-gradient-to-tl from-blue-700 via-blue-800 to-gray-900 ">

        <Head>
          <title>CartCrafters</title>
          <link rel="icon" href="/favicon.ico" />
        </Head>

        <Navbar />
        <Transition location={router.pathname}>
          <main>{children}</main>

          {router.pathname != "/" ? <Back /> : ""}


        </Transition>

        {/* Footer */}
        <div className="mt-auto w-full p-4 flex justify-center items-center border-t">
          <a
            className="flex items-center"
            href="https://github.com/ksmarty/CartCrafters"
            target="_blank"
            rel="noopener noreferrer"
          >
            CartCrafters was created by Kyle Schwartz & Lev Kropp for EECS 4413
          </a>
        </div>
      </div>
    </ShoppingCartProvider>
  )
}
export default Layout