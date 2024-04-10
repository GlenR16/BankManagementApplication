import { useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useUser } from "./UserContext";

const useAxiosAuth = () => {
    const navigate = useNavigate();
    const { deleteUser } = useUser();
	const apiToken = sessionStorage.getItem("token");
	const api = axios.create({
		baseURL: import.meta.env.VITE_BACKEND_URL,
		headers: {
			"Content-Type": "application/json",
			Authorization: `Bearer ${apiToken}`,
		},
	});

	useEffect(() => {
		const requestInterceptor = api.interceptors.request.use((config) => {
			if (!config.headers.Authorization && apiToken) {
				config.headers.Authorization = `Bearer ${apiToken}`;
			}
			return config;
		});

		const responseInterceptor = api.interceptors.response.use(
			(response) => response,
			(error) => {
                console.log("internal => ",error.request.status);
                if (error.request.status == 401) {
                    sessionStorage.removeItem("token");
                    deleteUser();
                    navigate("/login");
                }
				return Promise.reject(error);
			}
		);

		return () => {
			api.interceptors.request.eject(requestInterceptor);
			api.interceptors.response.eject(responseInterceptor);
		};
	});
	return api;
};

export default useAxiosAuth;