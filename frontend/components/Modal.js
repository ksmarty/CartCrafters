import React from 'react';

const Modal = ({ children }) => {
  return (
    <div id="modal" className="fixed top-0 left-0 w-full h-full flex items-center justify-center bg-black bg-opacity-50">
      <div className="bg-white rounded-lg overflow-auto w-11/12 md:w-1/2 lg:w-1/3 p-6">
        {children}
      </div>
    </div>
  );
};

export default Modal;