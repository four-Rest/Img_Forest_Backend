import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Home from './Home';
import Header from './Header';
import Footer from './Footer';

function Router() {
    return (
      <BrowserRouter>
        <Header/>
          <Routes>            
            <Route path="/home" element={<Home />} />            
          </Routes>
        <Footer/>
      </BrowserRouter>
    );
  };

export default Router;