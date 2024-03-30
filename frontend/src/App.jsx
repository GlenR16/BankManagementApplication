import "./App.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { lazy,Suspense } from "react";

import Root from "./pages/Root";
import Error from "./pages/Error";
import Loading from "./pages/Loading";
import Home from "./pages/Home";

const Signup = lazy(() => import("./pages/Signup"));
const Login = lazy(() => import("./pages/Login"));
const Dashboard = lazy(() => import("./pages/Dashboard"));
const Administration = lazy(() => import("./pages/Administration"));

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
        }
	]
}]);


function App() {
	return <RouterProvider router={router} />
}

export default App;
