import React from 'react'

import { useState, useContext } from 'react';

import { ShoppingCartContext } from '../components/ShoppingCartContext';

import { useRouter } from 'next/router';

const Login = () => {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const {user, setUser } = useContext(ShoppingCartContext);

    const router = useRouter();

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

    const fetchUser = () => {
      const url = 'http://localhost:8080/user/details'
    
      let error = false
    
      fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        credentials: 'include'
      })
      .then((response) => {
        if (response.status === 200) {
          return response.json()
        } else {
          error = true
          console.log('error')
          setUser([])
          return false;
        }
      })
      .then((data) => {
        console.log(data)
        if (!error) {
    
          setUser(data)
        }
        
      })
    }

    const handleSubmit = (event) => {
      event.preventDefault();

      setUsername(username.trim());
      setPassword(password.trim());

  
      if (!username) {
        alert("Please enter a valid username!");
        return
      }

      if (!password) {
        alert("Please enter a valid password!")
        return
      }

      if (user != "guest") {
        alert("You are already logged in!")
        router.push('/');
        return
      }


      // Construct the URL with the username and password
      const url = `http://localhost:8080/user/login`;

      // Format the body as form data
      const formData = new URLSearchParams();
      formData.append('username', username);
      formData.append('password', password);

  
      fetch(url, {
          method: 'POST',
          headers: {
              'Content-Type': 'application/x-www-form-urlencoded',
          },
          body: formData.toString(),
          credentials: 'include'
      })
      .then(response => {
        if (response.status === 200) {
          fetchUser();
          fetchCart();
  return response.json()
        } else if (response.status === 400 || response.status === 401) {
            alert("Incorrect username and/or password");
            router.reload();
        } else {
            throw new Error('Unexpected status code');
        }
    })
    .then((data) => {
      
      alert(`User successfully logged in`);
      router.push('/');
    })
    .catch((error) => {
        console.log('Error:', error);
    });
  
      console.log('Form submitted', { username, password });
  };

      return (
        <div className="container mx-auto max-w-md">
        <h1 className="text-3xl font-bold mb-4">Login</h1>

        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-white text-sm font-bold mb-2" htmlFor="username">
              Username
            </label>
            <input
              className="shadow appearance-none border rounded w-full py-2 px-3 text-black leading-tight focus:outline-none focus:shadow-outline"
              id="username"
              type="text"
              value={username}
              onChange={(event) => setUsername(event.target.value)}
            />
          </div>

          <div className="mb-4">
            <label className="block text-white text-sm font-bold mb-2" htmlFor="password">
              Password
            </label>
            <input
              className="shadow appearance-none border rounded w-full py-2 px-3 text-black leading-tight focus:outline-none focus:shadow-outline"
              id="password"
              type="password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
            />
          </div>

          <div className="flex items-center justify-between">
            <button
              className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
              type="submit"
            >
              Login
            </button>
          </div>
        </form>
      </div>
    )
}

export default Login
