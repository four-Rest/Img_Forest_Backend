import React, { createContext, useContext, useState } from "react";

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [isLogin, setIsLogin] = useState(false);

  const login = () => setIsLogin(true);

  function deleteCookie(name) {
    document.cookie =
      name + "=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;";
  }

  const logout = () => {
    localStorage.removeItem("username");
    localStorage.removeItem("nickname");
    deleteCookie("accessToken");
    deleteCookie("refreshToken");

    setIsLogin(false);
  };
  return (
    <AuthContext.Provider value={{ isLogin, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
