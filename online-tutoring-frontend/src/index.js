import React from 'react';
import ReactDOM from 'react-dom/client';

import App from './App';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter } from 'react-router-dom';

import { AuthContextProvider } from './store/auth-context';
import { QueryClient, QueryClientProvider } from "react-query";
import { LoginContextProvider } from './store/login-context';
const root = ReactDOM.createRoot(document.getElementById('root'));
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retryDelay: attemptIndex => Math.min(1000 * 2 ** attemptIndex, 30000),
      retry:3,
      refetchOnWindowFocus:false
     // staleTime: 120000
    },
  },
})
root.render(
  <QueryClientProvider client={queryClient}>
  <BrowserRouter> 
  <LoginContextProvider>
  <AuthContextProvider>

    <App />
    </AuthContextProvider>
    </LoginContextProvider>
  </BrowserRouter>
  </QueryClientProvider>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
