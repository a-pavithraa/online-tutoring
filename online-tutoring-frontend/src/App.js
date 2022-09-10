import logo from './logo.svg';
import './App.scss';
import { Layout } from './components/ui/Layout';
import { Route, Routes, Navigate, Link } from 'react-router-dom';
import { Home } from './pages/Home';
import { Classes } from './pages/Classes';
import { Documents } from './pages/Documents';

import Administration from './pages/Administration';

function App() {
  return (
    <div className="App">
    <header className="App-header">
      <Layout>
      <Routes>
        
      <Route path="/" element={<Home />} />
      <Route path="/Classes" element={<Classes />} />
      <Route path="/Documents" element={<Documents />} />
      <Route path="/Administration" element={<Administration />} />
   
   
     </Routes>
      </Layout>
    </header>
  
 </div>
  );
}

export default App;
