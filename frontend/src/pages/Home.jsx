import { NavLink } from "react-router-dom";

export default function Home() {
	return (
        <div className="flex-grow-1 d-flex align-items-center justify-content-center">
            <div className="container">
                <div className="row flex-lg-row-reverse align-items-center justify-content-center g-5 p-3">
                    <div className="col-12 col-sm-8 col-lg-6 d-flex align-items-center justify-content-center">
                        <img src="/index.png" alt="Index Image" className="w-75" />
                    </div>
                    <div className="col-12 col-sm-8 col-lg-6">
                        <h1 className="display-5 fw-bold lh-1 mb-3">Invest into a secure future.</h1>
                        <p className="lead">Experience the convenience of managing your finances on the go with our intuitive and secure mobile banking app. With robust security features and real-time transaction monitoring, you can trust Star Bank to keep your money safe and secure.</p>
                        <div className="d-grid gap-2 d-md-flex justify-content-md-start">
                            <NavLink className="btn btn-primary btn-lg px-4" to="/signup">
                                Get Started
                            </NavLink>
                            <NavLink className="btn btn-outline-primary btn-lg px-4" to="/aboutus">
                                About Us
                            </NavLink>
                        </div>
                    </div>
                </div>
            </div>
        </div>
	);
}
