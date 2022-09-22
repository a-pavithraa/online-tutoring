import logo from './logo.svg';
import './App.scss';
import { Layout } from './components/ui/Layout';
import { Route, Routes, Navigate, Link } from 'react-router-dom';
import { Home } from './pages/Home';
import { Classes } from './pages/Classes';
import { Documents } from './pages/Documents';

import Administration from './pages/Administration';
import Students from './pages/Students';
import RequireAuth from './components/util/RequireAuth';
import Login from './pages/Login';

function App() {
  return (
    <div className="App">
    <header className="App-header">
    <Layout>
      <Routes>
        <Route path="/login" element={<Login/>}/>
      <Route element={<RequireAuth />}>
     
      <Route path="/" element={<Home />} />
      <Route path="/Classes" element={<Classes />} />
      <Route path="/Documents" element={<Documents />} />
      <Route path="/Administration" element={<Administration />} />
      <Route path="/Students" element={<Students />} />
    
      </Route> 
   
     </Routes>
     </Layout>
    </header>
  
 </div>
  );
}

export default App;
