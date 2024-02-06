import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Home from './Home';
import Article from "./Article";

function Router() {
    return (
      <BrowserRouter>
          <Routes>
            <Route path="/home" element={<Home />} />
            <Route path="/article" element={<Article />} />

          </Routes>
      </BrowserRouter>
    );
  };

export default Router;