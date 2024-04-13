import { NavLink } from "react-router-dom";
import { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { useNavigate } from "react-router-dom";
import { useUser } from "../contexts/UserContext";

export default function Login() {
	const [error, setError] = useState("");
	const [loading, setLoading] = useState(false);
    const { user , changeUser } = useUser();
	const [form, setForm] = useState({
		customerId: "",
		password: "",
	});
	const api = useAxiosAuth();
	const navigate = useNavigate();

	function handleChange(e) {
		setForm({...form,[e.target.name]: e.target.value,});
	}

	function login() {
        setLoading(true);
        if (!form.customerId || !form.password) {
            setError("Please fill all the fields");
            setLoading(false);
            return;
        }
		api.post("/user/login", form)
        .then((res) => {
            sessionStorage.setItem("token", res.data);
            setLoading(false);
        })
        .catch((err) => {
            if (err.response) setError(err.response.data.message);
            else setError("Something went wrong");
            setLoading(false);
        });
	}

    useEffect(() => {
        if (user == null) return;
        else if (user.role == "ADMIN" || user.role == "EMPLOYEE" ) navigate("/administration");
        else navigate("/dashboard");
    },[user]);

    useEffect(() => {
        if (sessionStorage.getItem("token") != null) {
            api.get("/user/details")
            .then((res) => {
                changeUser(res.data);
            })
            .catch((err) => {});
        }
    },[sessionStorage.getItem("token")]);
    
	return (
		<div className="container">
			<div className="row flex-lg-row-reverse align-items-center justify-content-center g-5 m-2 text-center">
				<form action="" method="POST" className="col-12 col-lg-6 card p-4 border-0 shadow">
					<div className="text-center my-2">
						<svg version="1.0" width="70" height="70" id="Layer_1" xmlns="http://www.w3.org/2000/svg" xmlnsXlink="http://www.w3.org/1999/xlink" viewBox="0 0 64 64" enableBackground="new 0 0 64 64" xmlSpace="preserve" fill="#000000">
							<g id="SVGRepo_bgCarrier" strokeWidth="0"></g>
							<g id="SVGRepo_tracerCarrier" strokeLinecap="round" strokeLinejoin="round"></g>
							<g id="SVGRepo_iconCarrier">
								{" "}
								<g>
									{" "}
									<circle fill="#000000" cx="32" cy="14" r="3"></circle> <path fill="#000000" d="M4,25h56c1.794,0,3.368-1.194,3.852-2.922c0.484-1.728-0.242-3.566-1.775-4.497l-28-17 C33.438,0.193,32.719,0,32,0s-1.438,0.193-2.076,0.581l-28,17c-1.533,0.931-2.26,2.77-1.775,4.497C0.632,23.806,2.206,25,4,25z M32,9c2.762,0,5,2.238,5,5s-2.238,5-5,5s-5-2.238-5-5S29.238,9,32,9z"></path> <rect x="34" y="27" fill="#000000" width="8" height="25"></rect> <rect x="46" y="27" fill="#000000" width="8" height="25"></rect> <rect x="22" y="27" fill="#000000" width="8" height="25"></rect> <rect x="10" y="27" fill="#000000" width="8" height="25"></rect> <path fill="#000000" d="M4,58h56c0-2.209-1.791-4-4-4H8C5.791,54,4,55.791,4,58z"></path> <path fill="#000000" d="M63.445,60H0.555C0.211,60.591,0,61.268,0,62v2h64v-2C64,61.268,63.789,60.591,63.445,60z"></path>{" "}
								</g>{" "}
							</g>
						</svg>
					</div>
					<h1 className="h3 mb-3 fw-normal">Login to your account</h1>
					<div className="my-2 text-start">
						<label htmlFor="customerId" className="form-label">
							Customer ID
						</label>
						<input type="text" className="form-control" name="customerId" id="customerId" value={form.customerId} onChange={handleChange} aria-describedby="customerId" placeholder="A1B2C3D4E5F6G7H8I9" />
					</div>
					<div className="my-2 text-start">
						<label htmlFor="password" className="form-label">
							Password
						</label>
						<input type="password" className="form-control" name="password" id="password" value={form.password} onChange={handleChange} placeholder="********" />
					</div>
					{
                        error && <div className="alert alert-danger p-2" role="alert">{error}</div>
                    }
					<hr />
					<div className="row row-cols-1 row-cols-md-2 g-2">
						<div className="col text-start">
							<div>
								Don't have an account ? <NavLink to="/signup">Register</NavLink>
								<br />
							</div>
							<div>
								Forgot your password ? <NavLink to="/">Reset Password</NavLink>
							</div>
						</div>
						<div className="col d-grid" >
							<button className="btn btn-primary" type="button" onClick={login} disabled={loading}>
								{loading ? (
                                    <>
                                        <span className="spinner-border spinner-border-sm" aria-hidden="true"></span>
                                        {" "}
                                        <span role="status">Loading...</span>
                                    </>
								) : (
									"Login"
								)}
							</button>
						</div>
					</div>
				</form>

				<div className="col-12 col-sm-8 col-lg-6 d-none d-md-flex align-items-center justify-content-center">
					<img src="/login.png" alt="Index Image" className="w-75" />
				</div>
			</div>
		</div>
	);
}
