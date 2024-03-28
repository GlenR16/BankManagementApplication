import { Offcanvas } from "bootstrap";
import { NavLink, useNavigate } from "react-router-dom";

export default function TopNavbar() {
	const navigate = useNavigate();
	const token = sessionStorage.getItem("token", null);

	function logout() {
		sessionStorage.removeItem("token");
		sessionStorage.removeItem("refresh");
		navigate("/auth/login");
	}

	return (
		<nav className="navbar bg-light">
			<div className="container">
				<a className="navbar-brand d-inline-flex align-items-center gap-2" href="#">
					<svg viewBox="0 0 24 24" height="36px" width="36px" fill="none" xmlns="http://www.w3.org/2000/svg">
						<g id="SVGRepo_bgCarrier" strokeWidth="0"></g>
						<g id="SVGRepo_tracerCarrier" strokeLinecap="round" strokeLinejoin="round"></g>
						<g id="SVGRepo_iconCarrier">
							<path d="M6.03954 7.77203C3.57986 8.32856 2.35002 8.60682 2.05742 9.54773C1.76482 10.4886 2.60325 11.4691 4.2801 13.4299L4.71392 13.9372C5.19043 14.4944 5.42868 14.773 5.53586 15.1177C5.64305 15.4624 5.60703 15.8341 5.53498 16.5776L5.4694 17.2544C5.21588 19.8706 5.08912 21.1787 5.85515 21.7602C6.62118 22.3417 7.77268 21.8115 10.0757 20.7512L10.6715 20.4768C11.3259 20.1755 11.6531 20.0248 12 20.0248C12.3469 20.0248 12.6741 20.1755 13.3285 20.4768L13.9243 20.7512C16.2273 21.8115 17.3788 22.3417 18.1449 21.7602C18.9109 21.1787 18.7841 19.8706 18.5306 17.2544M19.7199 13.4299C21.3968 11.4691 22.2352 10.4886 21.9426 9.54773C21.65 8.60682 20.4201 8.32856 17.9605 7.77203L17.3241 7.62805C16.6251 7.4699 16.2757 7.39083 15.9951 7.17781C15.7144 6.96479 15.5345 6.64193 15.1745 5.99623L14.8468 5.40837C13.5802 3.13612 12.9469 2 12 2C11.0531 2 10.4198 3.13613 9.15316 5.40838" stroke="#1C274C" strokeWidth="1.5" strokeLinecap="round"></path>
						</g>
					</svg>
					Star Bank
				</a>
				<button className="btn" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasNavbar" aria-controls="offcanvasNavbar">
					<svg viewBox="0 0 24 24" width="36px" height="36px" fill="none" xmlns="http://www.w3.org/2000/svg">
						<g id="SVGRepo_bgCarrier" strokeWidth={0} />
						<g id="SVGRepo_tracerCarrier" strokeLinecap="round" strokeLinejoin="round" />
						<g id="SVGRepo_iconCarrier">
							{" "}
							<path d="M4 7L7 7M20 7L11 7" stroke="#000000" strokeWidth="1.5" strokeLinecap="round" /> <path d="M20 17H17M4 17L13 17" stroke="#000000" strokeWidth="1.5" strokeLinecap="round" /> <path d="M4 12H7L20 12" stroke="#000000" strokeWidth="1.5" strokeLinecap="round" />{" "}
						</g>
					</svg>
				</button>
				<div className="offcanvas offcanvas-start px-3" tabIndex="-1" id="offcanvasNavbar" aria-labelledby="offcanvasNavbarLabel">
					<div className="offcanvas-header">
						<h5 className="offcanvas-title d-inline-flex align-items-center gap-2" id="offcanvasNavbarLabel">
							<svg viewBox="0 0 24 24" height="36px" width="36px" fill="none" xmlns="http://www.w3.org/2000/svg">
								<g id="SVGRepo_bgCarrier" strokeWidth="0"></g>
								<g id="SVGRepo_tracerCarrier" strokeLinecap="round" strokeLinejoin="round"></g>
								<g id="SVGRepo_iconCarrier">
									<path d="M6.03954 7.77203C3.57986 8.32856 2.35002 8.60682 2.05742 9.54773C1.76482 10.4886 2.60325 11.4691 4.2801 13.4299L4.71392 13.9372C5.19043 14.4944 5.42868 14.773 5.53586 15.1177C5.64305 15.4624 5.60703 15.8341 5.53498 16.5776L5.4694 17.2544C5.21588 19.8706 5.08912 21.1787 5.85515 21.7602C6.62118 22.3417 7.77268 21.8115 10.0757 20.7512L10.6715 20.4768C11.3259 20.1755 11.6531 20.0248 12 20.0248C12.3469 20.0248 12.6741 20.1755 13.3285 20.4768L13.9243 20.7512C16.2273 21.8115 17.3788 22.3417 18.1449 21.7602C18.9109 21.1787 18.7841 19.8706 18.5306 17.2544M19.7199 13.4299C21.3968 11.4691 22.2352 10.4886 21.9426 9.54773C21.65 8.60682 20.4201 8.32856 17.9605 7.77203L17.3241 7.62805C16.6251 7.4699 16.2757 7.39083 15.9951 7.17781C15.7144 6.96479 15.5345 6.64193 15.1745 5.99623L14.8468 5.40837C13.5802 3.13612 12.9469 2 12 2C11.0531 2 10.4198 3.13613 9.15316 5.40838" stroke="#1C274C" strokeWidth="1.5" strokeLinecap="round"></path>
								</g>
							</svg>
							Star Bank
						</h5>
						<button type="button" className="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
					</div>
					<div className="offcanvas-body">
						<ul className="navbar-nav justify-content-end flex-grow-1 pe-3">
							<li className="nav-item">
								<NavLink to="/" className="nav-link">
									Home
								</NavLink>
							</li>
							{token ? (
								<>
                                    <li className="nav-item">
										<NavLink to="/dashboard" className="nav-link">
											Dashboard
										</NavLink>
									</li>
									<li className="nav-item">
										<NavLink to="/profile" className="nav-link">
											Profile
										</NavLink>
									</li>
									<li className="nav-item">
										<button onClick={logout} className="nav-link">
											Logout
										</button>
									</li>
								</>
							) : (
								<>
									<li className="nav-item">
										<NavLink to="/signup" className="nav-link">
											Signup
										</NavLink>
									</li>
									<li className="nav-item">
										<NavLink to="/login" className="nav-link">
											Login
										</NavLink>
									</li>
								</>
							)}
						</ul>
					</div>
				</div>
			</div>
		</nav>
	);
}
