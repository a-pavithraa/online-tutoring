import logo from "./logo.svg";
import "./App.scss";
import { Layout } from "./components/ui/Layout";
import {
  Route,
  Routes,
 
} from "react-router-dom";
import { Home } from "./pages/Home";
import { Classes } from "./pages/Classes";
import { Documents } from "./pages/Documents";

import Administration from "./pages/Administration";
import Students from "./pages/Students";
import RequireAuth from "./util/RequireAuth";
import Login from "./pages/Login";
import NavBar from "./components/ui/Navbar";
import Assessments from "./pages/Assessments";
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { lightTheme } from "./components/ui/Theme";
import Submissions from "./pages/Submissions";
/**const router = createBrowserRouter([
  {
    path: "/",
    element: <RequireAuth />,
    children: [
      {
        path: "/",
        element: <Layout />,
        children: [
          {
            index: true,
            element: <Classes />,
          },
          {
            path: "/Documents",
            element: <Documents />,
          },
          {
            path: "/Administration",
            element: <Administration />,
          },
          {
            path: "/Assessments",
            element: <Assessments />,
          },
          {
            path: "/Students",
            element: <Students />,
          },
        ],
      },
    ],
  },
  {
    path: "/login",
    element: <Login />,
  },
]);
*/
function App() {
  return (
    <div className="App" >

      <header className="App-header">
        <NavBar />
        <ThemeProvider theme={lightTheme}>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route element={<RequireAuth />}>
            <Route path="/" element={<Layout />}>
              <Route path="/Classes" element={<Classes />} />
              <Route path="/Documents" element={<Documents />} />
              <Route path="/Administration" element={<Administration />} />
              <Route path="/Assessments" element={<Assessments />} />
              <Route path="/Students" element={<Students />} />
              <Route path="/Submissions" element={<Submissions />} />
            </Route>
          </Route>
        </Routes>
        </ThemeProvider>
      </header>
    </div>
  );
}

export default App;
