import "./App.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { lazy,Suspense } from "react";

import { UserProvider } from "./contexts/UserContext";
import Root from "./pages/Root";
import Error from "./pages/Error";
import Loading from "./pages/Loading";
import Home from "./pages/Home";


const Signup = lazy(() => import("./pages/Signup"));
const Login = lazy(() => import("./pages/Login"));
const Dashboard = lazy(() => import("./pages/Dashboard"));
const Administration = lazy(() => import("./pages/Administration"));
const AdminAccounts = lazy(() => import("./pages/AdminAccounts"));
const AdminCustomers = lazy(() => import("./pages/AdminCustomers"));
const AdminTransactions = lazy(() => import("./pages/AdminTransactions"));
const Profile = lazy(() => import("./pages/Profile"));
const AccountDetails = lazy(() => import("./pages/AccountDetails"));
const Transactions = lazy(() => import("./pages/Transactions"));
const Transfer = lazy(() => import("./pages/Transfer"));
const Account = lazy(() => import("./pages/Account"));
const Receipt = lazy(() => import("./pages/Receipt"));
const Card = lazy(() => import("./pages/Card"));
const Withdraw = lazy(() =>import("./pages/Withdraw"));
const Deposit = lazy(() =>import("./pages/Deposit"));
const AddingBenificary = lazy(() => import("./pages/AddingBenificary"));
const AddingCard = lazy(() => import("./pages/AddingCard"));
const AccountRegistration = lazy(() => import("./pages/AccountRegistration"));
const CardDetails = lazy(()=> import("./pages/CardDetails"));
const AboutUs = lazy(()=> import("./pages/AboutUs"));
const AdminBranches = lazy(()=> import("./pages/AdminBranches"));

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
		},
		{
			path: "/Card",
			element: <Suspense fallback={<Loading />}><Card /></Suspense>
		},
		{
			path: "/AccountRegister",
			element: <Suspense fallback={<Loading />}><AccountRegistration /></Suspense>
		},
		{
			path: "/addBeneficiary",
			element: <Suspense fallback={<Loading />}><AddingBenificary /></Suspense>
		},
		{
			path: "/addCard",
			element: <Suspense fallback={<Loading />}><AddingCard /></Suspense>
		},
		{
			path: "/profile/:customerId",
			element: <Suspense fallback={<Loading />}><Profile /></Suspense>
		},
		{
			path: "/accountDetails/:AccountNumber",
			element: <Suspense fallback={<Loading />}><AccountDetails /></Suspense>
		},
		{
			path: "/cardDetails/:number",
			element: <Suspense fallback={<Loading />}><CardDetails /></Suspense>
		},
        {
            path: "/aboutus",
            element: <Suspense fallback={<Loading />}><AboutUs /></Suspense>
        },
        {
            path: "/AdminBranches",
            element: <Suspense fallback={<Loading />}><AdminBranches /></Suspense>
        }
	]
}]);


function App() {
	return (
		<UserProvider>
			<RouterProvider router={router} />
		</UserProvider>
	);
}

export default App;
