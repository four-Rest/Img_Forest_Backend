import { createContext, useMemo, useState } from "react";


const SearchTagContext = createContext();

function SearchTagProvider({children}) {
    const [searchTag, setSearchTag] = useState({

        tag:'',

    });

    const updateSearchTag = newPayload => {
        setSearchTag(newPayload);
    };


    const SearchTagValue = useMemo(

        () => ({
            searchTag,
            updateSearchTag,
        }),
        [searchTag,updateSearchTag],
    );

    return (
        <SearchTagContext.Provider value = {SearchTagValue}>
            {children}
        </SearchTagContext.Provider>
    );
}
export {SearchTagContext,SearchTagProvider}