import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHashtag } from '@fortawesome/free-solid-svg-icons';
import { faGithub } from '@fortawesome/free-brands-svg-icons';

const Footer = () => {
  return (
    <footer className="footer items-center p-4 sticky top-[100vh] bg-gray-200 text-black">
      <div className="flex flex-row justify-between w-full">
        <div className="flex flex-row items-center justify-center">
          <FontAwesomeIcon icon={faHashtag} className="mr-3" />
          <p>Copyright © 2023 - All right reserved</p>
        </div>
        <div className="items-center">
          <a href="https://github.com/four-Rest/Img_Forest" target="_blank" rel="noopener noreferrer">
            <FontAwesomeIcon icon={faGithub} className="fa-xl" />
          </a>
        </div>
      </div>
    </footer>
  );
};

export default Footer;