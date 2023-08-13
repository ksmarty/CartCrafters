import React from "react"
import { useRouter } from "next/router" 
import Transition from "./Transition" 

import Link from 'next/link'

import Head from 'next/head'

import Back from "./Back"


import { ShoppingCartProvider } from './ShoppingCartContext';

const Layout = ({ children }) => {




  const router = useRouter() 

  
  return (
    <ShoppingCartProvider>
    <div className="text-white p-4 flex flex-col items-center justify-start min-h-screen py-2  bg-gradient-to-tl from-blue-700 via-blue-800 to-gray-900 ">
      
      <Head>
        <title>CartCrafters</title>
        <link rel="icon" href="/favicon.ico" />
      </Head>
      
      {/* Navbar component that doesn't animate */}
      <nav className="w-full p-4 flex justify-between items-center bg-blue-500 text-white">
      <h1 className="text-lg font-bold">CartCrafters</h1>
      <div className="flex gap-4">
        <Link href="/" className="hover:text-gray-200">Catalog View</Link>
        <Link href="/cart" className="hover:text-gray-200">Shopping Cart View</Link>
        <Link href="/checkout" className="hover:text-gray-200">Checkout View</Link>
        <Link href="/register" className="hover:text-gray-200">Registration View</Link>
        <Link href="/admin" className="hover:text-gray-200">Admin Page View</Link>
      </div>
    </nav>
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