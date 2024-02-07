import { BrowserRouter, Route, Routes } from "react-router-dom";
import Home from "./Home";
import Header from "./Header";
import Footer from "./Footer";
import Article from "./Article";

function Router() {
  return (
    <BrowserRouter>
      <Header />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/article" element={<Article />} />
      </Routes>
      <Footer />
    </BrowserRouter>
  );
}

export default Router;
