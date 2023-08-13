import React from 'react'

import Link from 'next/link'

const Back = () => {
    return (
        <Link href="/" className="">
        <div

            className="max-w-xs m-auto text-center cursor-pointer p-2 mt-6 border rounded-xl transition duration-500 ease-in-out transform hover:-translate-y-1 hover:scale-105"
        >
            <h3 className="font-bold">Back</h3>
        </div>
    </Link>
    )
}

export default Back
