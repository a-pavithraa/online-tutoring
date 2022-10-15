import React from 'react';
import { Outlet } from 'react-router-dom';
import { Layout } from '../components/ui/Layout';

export const Home =(props)=>{
   <Layout>
    <Outlet/>
   </Layout>
}