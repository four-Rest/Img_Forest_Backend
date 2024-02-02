import logo from './logo.svg';
import './App.css';
import React from 'react';
import { toastNotice } from './components/ToastrConfig';
import { AuthProvider } from './api/AuthContext';
import Header from './components/Header';
import Footer from './components/Footer';
function App() {

  const handleAlert = () => {
    toastNotice('경고 창 띄우기.');
  };

  return (
    <AuthProvider>
    <Header/>
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
      <button onClick={handleAlert}>경고 표시</button>
    </div>
    <Footer/>
    </AuthProvider>
  );
}

export default App;