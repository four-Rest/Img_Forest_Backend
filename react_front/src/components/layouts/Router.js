import { BrowserRouter, Route, Routes } from "react-router-dom";
import Header from "./Header";
import Footer from "./Footer";
import Article from "./Article";
import ArticleTag from "./ArticleTag";
import CheckSocialLogin from "../../api/CheckSocialLogin";
import HomePaging from "./HomePaging";
import Detail from "./Detail";
import {SearchTagProvider} from '../../api/SearchTagContext';
import { IdDetailProvider } from '../../api/IdDetailContext';

function Router() {
  return (
    <BrowserRouter>
      <Header />
      <Routes>
        <Route path="/" element={<HomePaging />} />
        <Route path="/article" element={<Article />} />
        <Route path="/article/:tagString" element={<ArticleTag />} />
        <Route path="/detail/:id" element={<Detail />} />
        <Route path="/check-social-login" element={<CheckSocialLogin />} />
      </Routes>
      <Footer />
    </BrowserRouter>
  );
}

export default Router;
