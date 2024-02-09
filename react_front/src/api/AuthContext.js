import React, { createContext, useContext, useState } from "react";
import cookie from 'react-cookies';
const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [isLogin, setIsLogin] = useState(false);

  const login = () => setIsLogin(true);

  function deleteCookie(name) {
    cookie.remove(name, { path: '/' },1000);
}

  const logout = () => {
    localStorage.removeItem("username");
    localStorage.removeItem("nickname");
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    deleteCookie("accessToken");
    deleteCookie("refreshToken");
    deleteCookie("JSESSIONID"); 
    setIsLogin(false)
    window.location.href = `/`;
    ;
  };
  return (
    <AuthContext.Provider value={{ isLogin, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
