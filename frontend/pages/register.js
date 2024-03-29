import React from "react";
import { useState, useContext } from "react";

import { useRouter } from "next/router";

import { ShoppingCartContext } from '../components/ShoppingCartContext';

const Register = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const router = useRouter();

  const {user, setUser } = useContext(ShoppingCartContext);

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

    const url = `http://localhost:8080/user/create`;

    // Format the body as form data
    const formData = new URLSearchParams();
    formData.append("username", username);
    formData.append("password", password);

    fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: formData.toString(),
      credentials: 'include'
    })
      .then((response) => {
        if (response.status === 200) {
          fetchUser();
          return response.json();
        } else if (response.status === 400) {
          alert("Bad credentials");
          router.reload();
        } else if (response.status === 409) {
          alert("User already exists");
          router.reload();
        } else {
          throw new Error("Unexpected status code");
        }
      })
      .then(()=>{
        alert(`User ${username} created successfully!`);
        router.push("/");
      })
      .catch((error) => {
        console.log("Error:", error);
      });

    console.log("Form submitted", { username, password });
  };

  return (
    <div className="container mx-auto max-w-md py-4">
      <h1 className="text-3xl font-bold my-4">Register</h1>

      <hr className="h-1 md:-mx-16 my-4 bg-gray-200 border-0 rounded my-10 dark:bg-gray-700" />

      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label
            className="block text-white text-sm font-bold mb-2"
            htmlFor="username"
          >
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
          <label
            className="block text-white text-sm font-bold mb-2"
            htmlFor="password"
          >
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
            Register
          </button>
        </div>
      </form>
    </div>
  );
};

export default Register;
