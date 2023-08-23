import React, { useState, useContext, useEffect } from 'react';

import { useRouter } from "next/router" 
import Transition from "./Transition" 

import Link from 'next/link'

import Head from 'next/head'

import Back from "./Back"


import { ShoppingCartContext, ShoppingCartProvider } from '../components/ShoppingCartContext.js';

const Navbar = () => {

  const { cart } = useContext(ShoppingCartContext);
  const totalItems = cart.reduce((total, item) => total + item.quantity, 0);



  return (
      <nav className="w-full p-4 flex justify-between items-center bg-blue-500 text-white">
      <Link href="/" className="hover:text-gray-200"><h1 className="text-lg font-bold">CartCrafters</h1></Link>
      <div className="flex gap-4">
        <Link href="/" className="hover:text-gray-200">Catalog</Link>
        <Link href="/cart" className="hover:text-gray-200">Shopping Cart ({cart.length})</Link>
        <Link href="/checkout" className="hover:text-gray-200">Checkout</Link>
        <Link href="/register" className="hover:text-gray-200">Register</Link>
        <Link href="/admin" className="hover:text-gray-200">Admin </Link>
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