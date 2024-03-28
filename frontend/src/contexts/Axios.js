import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const useAxiosAuth = () => {
	const navigate = useNavigate();
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
			if (!config.headers.Authorization) {
				config.headers.Authorization = `Bearer ${apiToken}`;
			}
			return config;
		});

		const responseInterceptor = api.interceptors.response.use(
			(response) => response,
			(error) => {
				const config = error.config;
                sessionStorage.removeItem("token");
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