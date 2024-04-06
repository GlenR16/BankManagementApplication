import "./App.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { lazy,Suspense } from "react";

import Root from "./pages/Root";
import Error from "./pages/Error";
import Loading from "./pages/Loading";
import Home from "./pages/Home";
import Withdraw from "./pages/Withdraw";
import Transactions from "./pages/Transactions";
import Transfer from "./pages/Transfer";
import Account from "./pages/Account";
import Deposit from "./pages/Deposit";
import Receipt from "./pages/Receipt";


const Signup = lazy(() => import("./pages/Signup"));
const Login = lazy(() => import("./pages/Login"));
const Dashboard = lazy(() => import("./pages/Dashboard"));
const Administration = lazy(() => import("./pages/Administration"));
const AdminAccounts = lazy(() => import("./pages/AdminAccounts"));
const AdminCustomers = lazy(() => import("./pages/AdminCustomers"));
const AdminTransactions = lazy(() => import("./pages/AdminTransactions"));

const router = createBrowserRouter([{
	path: "/",
	element: <Root />,
	errorElement: <Error />,
	children: [
		{
			path: "/",
			element: <Home />
		},
		{
			path: "/signup",
			element: <Suspense fallback={<Loading />}><Signup /></Suspense>
		},
		{
			path: "/login",
			element: <Suspense fallback={<Loading />}><Login /></Suspense>
		},
        {
            path: "/dashboard",
            element: <Suspense fallback={<Loading />}><Dashboard /></Suspense>
        },
        {
            path: "/administration",
            element: <Suspense fallback={<Loading />}><Administration /></Suspense>
        },
		{
			path: "/withdraw",
			element: <Suspense fallback={<Loading />}><Withdraw /></Suspense>
		},
		{
			path: "/account",
			element: <Suspense fallback={<Loading />}><Account /></Suspense>
		},
		{
			path: "/deposit",
			element: <Suspense fallback={<Loading />}><Deposit /></Suspense>
		},
		{
			path: "/transactions",
			element: <Suspense fallback={<Loading />}><Transactions /></Suspense>
		},
		{
			path: "/transfer",
			element: <Suspense fallback={<Loading />}><Transfer /></Suspense>
		},
		{
			path:"/AdminAccounts",
			element: <Suspense fallback={<Loading />}><AdminAccounts /></Suspense>
		},
		{
			path:"/AdminCustomers",
			element: <Suspense fallback={<Loading />}><AdminCustomers /></Suspense>
		},
		{
			path:"/AdminTransactions",
			element: <Suspense fallback={<Loading />}><AdminTransactions /></Suspense>
		},
		{
			path: "/transaction/:id",
			element: <Suspense fallback={<Loading />}><Receipt /></Suspense>
		}
	]
}]);


function App() {
	return <RouterProvider router={router} />
}

export default App;
