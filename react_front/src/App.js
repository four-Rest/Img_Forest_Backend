/*esLint-disable */
import './App.css'; // 스타일 시트
import React from 'react';
import Router from "./components/layouts/Router";
import { AuthProvider } from './api/AuthContext';
import {SearchTagProvider} from './api/SearchTagContext';
import {IdDetailProvider } from './api/IdDetailContext';

function App() {

  return (
    <AuthProvider>
      <SearchTagProvider>
        <IdDetailProvider>
          <Router/>
        </IdDetailProvider>  
      </SearchTagProvider>
    </AuthProvider>
  );
}

export default App;