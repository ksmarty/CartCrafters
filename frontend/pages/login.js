import React from 'react'

import { useState, useContext } from 'react';

import { ShoppingCartContext } from '../components/ShoppingCartContext';

const Login = () => {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const { setUser } = useContext(ShoppingCartContext);

    const handleSubmit = (event) => {
      event.preventDefault();
  
      // Construct the URL with the username and password
      const url = `http://localhost:8080/user/login?username=${username}&password=${password}`;


  
      fetch(url, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
      })
      .then(response => {
        if (response.status === 200) {
            setUser(username);
            alert(`User successfully logged in`);
            window.location.href = "/";
        } else if (response.status === 400) {
            alert("Incorrect username and/or password");
            window.location.reload();
        } else {
            throw new Error('Unexpected status code');
        }
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
