/*esLint-disable */
import './App.css'; // 스타일 시트
import React from 'react';
import Router from "./components/layouts/Router";
import { AuthProvider } from './api/AuthContext';

function App() {

  return (
    <AuthProvider>
      <Router>
    
      </Router>
    </AuthProvider>
  );
}

export default App;