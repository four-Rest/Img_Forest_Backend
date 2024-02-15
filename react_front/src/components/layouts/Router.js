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
import MyArticle from "./MyArticle";
import Modify from "./Modify";
function Router() {
  return (
    <div className="flex flex-col min-h-screen">
    <BrowserRouter>
      <Header />
      <Routes className="flex-grow">
        <Route path="/" element={<HomePaging />} />
        <Route path="/article" element={<Article />} />
        <Route path="/article/modify/:id" element={<Modify />} />
        <Route path="/article/:tagString" element={<ArticleTag />} />
        <Route path="/my-article/:userNick" element={<MyArticle />} />
        <Route path="/detail/:id" element={<Detail />} />
        <Route path="/check-social-login" element={<CheckSocialLogin />} />
      </Routes>
      <Footer />
    </BrowserRouter>
    </div>
  );
}

export default Router;