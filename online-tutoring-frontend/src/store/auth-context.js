import React, { createContext, useState } from 'react';

 const AuthContext = createContext({

    mobileOpen: false,
    switchDrawToggle: () => { }

});

export const AuthContextProvider = (props) => {
    const [mobileOpen, setMobileOpen] = useState(false);
    const switchDrawToggle = () => {
        setMobileOpen(!mobileOpen);
    };
    const context = {
        mobileOpen,
        switchDrawToggle

    }

    return <AuthContext.Provider value={context}>
        {props.children}
    </AuthContext.Provider>

}

export default AuthContext;