import React, { createContext, useState } from 'react';

 const AuthContext = createContext({

    mobileOpen: false,
    teacherId: 1,
    selectedMenu:0,
    setSelectedMenu:()=>{},
    switchDrawToggle: () => { }

});

export const AuthContextProvider = (props) => {
    const [mobileOpen, setMobileOpen] = useState(false);
    const [selectedMenu,setSelectedMenu]=useState(0);
    const switchDrawToggle = () => {
        setMobileOpen(!mobileOpen);
    };
    const context = {
        mobileOpen,
        switchDrawToggle,
        teacherId:1,
        selectedMenu,
        setSelectedMenu

    }

    return <AuthContext.Provider value={context}>
        {props.children}
    </AuthContext.Provider>

}

export default AuthContext;