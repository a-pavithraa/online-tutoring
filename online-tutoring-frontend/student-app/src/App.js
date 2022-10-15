import logo from './logo.svg';
import './App.scss';
import {
  Route,
  Routes,
 
} from "react-router-dom";

import { ThemeProvider } from '@mui/material';
import { lightTheme } from "./components/ui/Theme";
import UploadAssessment from './pages/UploadAssessment';
import RequireAuth from "./util/RequireAuth";
import Login from "./pages/Login";
import NavBar from "./components/ui/Navbar";
function App() {
  return (
    <div className="App" >

      <header className="App-header">
        <NavBar />
        <ThemeProvider theme={lightTheme}>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route element={<RequireAuth />}>
            <Route>             
              <Route path="/"  element={<UploadAssessment />} />
            </Route>
          </Route>
        </Routes>
        </ThemeProvider>
      </header>
    </div>
  );
}

export default App;
