import { NavLink,useRouteError } from "react-router-dom";
import TopNavbar from '../components/TopNavbar';

export default function Error() {
    const error = useRouteError();
	return (
        <>
            <TopNavbar />
            <div className="flex-grow-1 d-flex align-items-center justify-content-center">
                <div className="container">
                    <div className="row flex-lg-row-reverse align-items-center justify-content-center text-center p-3">
                        <h1 className="display-3 fw-bold">{error.status}</h1>
                        <p className="display-6 fw-semibold">{error.statusText}</p>
                        <svg viewBox="0 0 24 24" width="200" height="200" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <g id="SVGRepo_bgCarrier" strokeWidth="0"></g>
                            <g id="SVGRepo_tracerCarrier" strokeLinecap="round" strokeLinejoin="round"></g>
                            <g id="SVGRepo_iconCarrier">
                                {" "}
                                <path d="M6.30928 9C8.59494 5 9.96832 3 12 3C14.3107 3 15.7699 5.58716 18.6883 10.7615L19.0519 11.4063C21.4771 15.7061 22.6897 17.856 21.5937 19.428C20.4978 21 17.7864 21 12.3637 21H11.6363C6.21356 21 3.50217 21 2.40626 19.428C1.45498 18.0635 2.24306 16.2635 4.05373 13" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path> <path d="M12 8V13" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path> <circle cx="12" cy="16" r="1" fill="#000000"></circle>{" "}
                            </g>
                        </svg>
                        <div>
                            <NavLink to="/" className="btn btn-primary">
                                Home Page
                            </NavLink>
                        </div>
                    </div>
                </div>
            </div>
        </>
	);
}
