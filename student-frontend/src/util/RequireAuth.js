import { Navigate ,useLocation,Outlet} from 'react-router-dom';
import {useContext} from 'react';
import LoginContext from '../store/login-context';
function RequireAuth({ children, redirectTo }) {
  const ctx = useContext(LoginContext);
  let isAuthenticated = ctx.isLoggedIn;
  let location = useLocation();
  

  if (!isAuthenticated) {
    // Redirect them to the /login page, but save the current location they were
    // trying to go to when they were redirected. This allows us to send them
    // along to that page after they login, which is a nicer user experience
    // than dropping them off on the home page.
    return <Navigate to="/login" state={{ path: location.pathname }} replace/>;
  }

  return <Outlet />;
}

  export default RequireAuth;
  