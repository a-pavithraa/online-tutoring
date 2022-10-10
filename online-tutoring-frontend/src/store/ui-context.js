import React, { createContext, useState } from 'react';

 const UIContext = createContext({

    mobileOpen: false,
   
    selectedMenu:0,
    setSelectedMenu:()=>{},
    switchDrawToggle: () => { }

});

export const UIContextProvider = (props) => {
    const [mobileOpen, setMobileOpen] = useState(false);
    const [selectedMenu,setSelectedMenu]=useState(0);
    const switchDrawToggle = () => {
        setMobileOpen(!mobileOpen);
    };
    const context = {
        mobileOpen,
        switchDrawToggle,        
        selectedMenu,
        setSelectedMenu

    }

    return <UIContext.Provider value={context}>
        {props.children}
    </UIContext.Provider>

}

export default UIContext;