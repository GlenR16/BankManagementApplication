import { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { NavLink, useNavigate } from "react-router-dom";

export default function Signup() {
	const api = useAxiosAuth();
	const navigate = useNavigate();
	const [error, setError] = useState("");
	const [loading, setLoading] = useState(false);
	const [details, setDetails] = useState({})
	const [form, setForm] = useState({
		name: "",
		email: "",
		password: "",
		confirmPassword: "",
		phone: "",
		aadhaar: "",
		pan: "",
		state: "",
		city: "",
		address: "",
		pincode: "",
		dateOfBirth: "",
		gender: "",
	});

	function handleChange(e) {
		setForm({
			...form,
			[e.target.name]: e.target.value,
		});
		console.log(form);
	}

	function signup() {
		setLoading(true);
		if (!form.name || !form.email || !form.password || !form.confirmPassword || !form.phone || !form.aadhaar || !form.pan || !form.state || !form.city || !form.address || !form.pincode || !form.dateOfBirth || !form.gender) {
			setError("Please fill all the fields");
			setLoading(false);
			return;
		}
		if (form.password !== form.confirmPassword) {
			setError("Passwords do not match");
			setLoading(false);
			return;
		}
		if (document.getElementById("termsandconditions").checked === false) {
			setError("Please agree to the terms and conditions");
			setLoading(false);
			return;
		}

		api.post("/user/signup", form)
			.then((res) => {
				sessionStorage.setItem("token", res.data.message);
				setLoading(false);
			})
			.catch((err) => {
				console.log("ERROR");
				if (err.response) setError(err.response.data.error);
				else setError("Something went wrong");
				setLoading(false);
			});

	}
	useEffect(() => {
			// let signupElement = document.getElementById("signup");
			// signupElement.setAttribute("data-bs-toggle", "modal");
			setLoading(false);
		api.get("/user/details")
			.then((res) => {
				setDetails(res.data);
				console.log("SET DETAILS : ",res.data);
				setLoading(false);
			})
			.catch((err) => {
				console.log(err);
			});
	}, [sessionStorage.getItem("token")]);

	return (
		<>
			<div className="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabIndex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
				<div className="modal-dialog">
					<div className="modal-content">
						<div className="modal-header">
							<h1 className="modal-title fs-5" id="staticBackdropLabel">Modal title</h1>
							<button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
						</div>
						<div className="modal-body">
							<p>Account Created Successfully</p>
							<p>Customer ID : {details.customerId}</p>
							<p>Password : {details.password}</p>
							<p>Please remember your customer-id and password for future login</p>
						</div>
						<div className="modal-footer">
							<button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
							<button type="button" className="btn btn-primary" data-bs-dismiss="modal" onClick={() => navigate("/login")}>Understood</button>
						</div>
					</div>
				</div>
			</div>



			<div className="container">
				<div className="row flex-lg-row-reverse align-items-center justify-content-center g-5 m-3 text-center">
					<form action="" method="post" className="col-12 col-md-8 card p-4 border-0 shadow">
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
						<h1 className="h3 mb-3 fw-normal">Create a new Account</h1>
						<div className="my-2 text-start">
							<label htmlFor="name" className="form-label">
								Full Name
							</label>
							<input type="text" className="form-control" name="name" id="name" value={form.name} onChange={handleChange} aria-describedby="name" />
						</div>
						<div className="my-2 text-start">
							<label htmlFor="email" className="form-label">
								Email
							</label>
							<input type="email" className="form-control" name="email" id="email" value={form.email} onChange={handleChange} aria-describedby="email" />
						</div>
						<div className="my-2 text-start">
							<label htmlFor="password" className="form-label">
								Password
							</label>
							<input type="password" className="form-control" name="password" id="password" value={form.password} onChange={handleChange} aria-describedby="password" />
						</div>
						<div className="my-2 text-start">
							<label htmlFor="confirmPassword" className="form-label">
								Confirm Password
							</label>
							<input type="password" className="form-control" name="confirmPassword" id="confirmPassword" value={form.confirmPassword} onChange={handleChange} aria-describedby="confirmPassword" />
						</div>
						<div className="my-2 text-start">
							<label htmlFor="phone" className="form-label">
								Phone
							</label>
							<input type="tel" className="form-control" name="phone" id="phone" value={form.phone} onChange={handleChange} aria-describedby="phone" pattern="[0-9]{10}" />
						</div>
						<div className="my-2 text-start">
							<label htmlFor="aadhaar" className="form-label">
								Aadhaar Number
							</label>
							<input type="text" className="form-control" name="aadhaar" id="aadhaar" value={form.aadhaar} onChange={handleChange} aria-describedby="aadhaar" />
						</div>
						<div className="my-2 text-start">
							<label htmlFor="pan" className="form-label">
								PAN Number
							</label>
							<input type="text" className="form-control" name="pan" id="pan" value={form.pan} onChange={handleChange} aria-describedby="pan" />
						</div>
						<div className="my-2 text-start">
							<label htmlFor="address" className="form-label">
								Address Line 1
							</label>
							<input type="text" className="form-control" name="address" id="address" value={form.address} onChange={handleChange} aria-describedby="address" />
						</div>
						<div className="my-2 text-start">
							<label htmlFor="city" className="form-label">
								City
							</label>
							<input type="text" className="form-control" name="city" id="city" value={form.city} onChange={handleChange} aria-describedby="city" />
						</div>
						<div className="my-2 text-start">
							<label htmlFor="pincode" className="form-label">
								Zip Code
							</label>
							<input type="text" className="form-control" name="pincode" id="pincode" value={form.pincode} onChange={handleChange} aria-describedby="pincode" />
						</div>
						<div className="my-2 text-start">
							<label htmlFor="state" className="form-label">
								State
							</label>
							<select className="form-select" name="state" id="state" value={form.state} onChange={handleChange} aria-label="state" aria-describedby="state">
								<option value="">Select State</option>
								<option value="Andhra Pradesh">Andhra Pradesh</option>
								<option value="Arunachal Pradesh">Arunachal Pradesh</option>
								<option value="Assam">Assam</option>
								<option value="Bihar">Bihar</option>
								<option value="Chhattisgarh">Chhattisgarh</option>
								<option value="Gujarat">Gujarat</option>
								<option value="Haryana">Haryana</option>
								<option value="Himachal Pradesh">Himachal Pradesh</option>
								<option value="Jammu and Kashmir">Jammu and Kashmir</option>
								<option value="Goa">Goa</option>
								<option value="Jharkhand">Jharkhand</option>
								<option value="Karnataka">Karnataka</option>
								<option value="Kerala">Kerala</option>
								<option value="Madhya Pradesh">Madhya Pradesh</option>
								<option value="Maharashtra">Maharashtra</option>
								<option value="Manipur">Manipur</option>
								<option value="Meghalaya">Meghalaya</option>
								<option value="Mizoram">Mizoram</option>
								<option value="Nagaland">Nagaland</option>
								<option value="Odisha">Odisha</option>
								<option value="Punjab">Punjab</option>
								<option value="Rajasthan">Rajasthan</option>
								<option value="Sikkim">Sikkim</option>
								<option value="Tamil Nadu">Tamil Nadu</option>
								<option value="Telangana">Telangana</option>
								<option value="Tripura">Tripura</option>
								<option value="Uttarakhand">Uttarakhand</option>
								<option value="Uttar Pradesh">Uttar Pradesh</option>
								<option value="West Bengal">West Bengal</option>
								<option value="Andaman and Nicobar Islands">Andaman and Nicobar Islands</option>
								<option value="Chandigarh">Chandigarh</option>
								<option value="Dadra and Nagar Haveli">Dadra and Nagar Haveli</option>
								<option value="Daman and Diu">Daman and Diu</option>
								<option value="Delhi">Delhi</option>
								<option value="Lakshadweep">Lakshadweep</option>
								<option value="Puducherry">Puducherry</option>
							</select>
						</div>

						<div className="my-2 text-start">
							<label htmlFor="dateOfBirth" className="form-label">
								Date of Birth
							</label>
							<input type="date" className="form-control" name="dateOfBirth" id="dateOfBirth" value={form.dateOfBirth} onChange={handleChange} aria-describedby="dateOfBirth" />
						</div>
						<div className="my-2 text-start">
							<label htmlFor="gender" className="form-label">
								Gender
							</label>
							<select className="form-select" name="gender" id="gender" value={form.gender} onChange={handleChange} aria-label="gender" aria-describedby="gender">
								<option value="">Select Gender</option>
								<option value="Male">Male</option>
								<option value="Female">Female</option>
							</select>
						</div>
						{/* <div className="my-2 text-start">
						<label htmlFor="accountType" className="form-label">
							Account Type
						</label>
                        <select className="form-select" name="accountType" id="accountType" value={form.accountType} onChange={handleChange} aria-label="Account Type" aria-describedby="accountType">
                            <option value="">Select Account Type</option>
                            //dynamically bring options
                            <option value="Savings Account">Savings Account</option>
                        </select>
					</div> */}

						<div className="my-2 text-start">
							<label htmlFor="services" className="form-label">
								Services Required
							</label>

							<div className="custom-control custom-checkbox">
								<input type="checkbox" name="services" className="form-check-input" id="atmcard" />
								<label className="custom-control-label ms-2" htmlFor="atmcard">
									ATM Card
								</label>
							</div>

							<div className="custom-control custom-checkbox">
								<input type="checkbox" name="services" className="form-check-input" id="internetbanking" />
								<label className="custom-control-label ms-2" htmlFor="internetbanking">
									Internet Banking
								</label>
							</div>

							<div className="custom-control custom-checkbox">
								<input type="checkbox" name="services" className="form-check-input" id="mobilebanking" />
								<label className="custom-control-label ms-2" htmlFor="mobilebanking">
									Mobile Banking
								</label>
							</div>

							<div className="custom-control custom-checkbox">
								<input type="checkbox" name="services" className="form-check-input" id="emailalerts" />
								<label className="custom-control-label ms-2" htmlFor="emailalerts">
									Email Alerts
								</label>
							</div>

							<div className="custom-control custom-checkbox">
								<input type="checkbox" name="services" className="form-check-input" id="chequebook" />
								<label className="custom-control-label ms-2" htmlFor="chequebook">
									Cheque Book
								</label>
							</div>

							<div className="custom-control custom-checkbox">
								<input type="checkbox" name="services" className="form-check-input" id="statement" />
								<label className="custom-control-label ms-2" htmlFor="statement">
									E-Statement
								</label>
							</div>
						</div>
						<hr />
						<p className="invalid-feedback d-block">{error}</p>
						<div className="form-check text-start">
							<input className="form-check-input" name="agreement" type="checkbox" id="termsandconditions" />
							<label className="form-check-label" htmlFor="termsandconditions">
								I hereby state that I have read and understood the terms and conditions and the information provided by me is true and accurate.
							</label>
						</div>
						<br />
						<div className="d-flex flex-column flex-md-row justify-content-between gap-4 my-2">
							<div className="d-flex flex-column text-start">
								<div>
									Already have an account ? <NavLink to="/login">Login</NavLink>
									<br />
								</div>
							</div>
							<div>

								<button id="signup" className="w-100 btn btn-primary" type="button" onClick={signup} disabled={loading} data-bs-target="#staticBackdrop" data-bs-toggle ={(!details) ? ("") : ('modal')} >
									{loading ? (
										<div className="spinner-border mx-2" role="status">
											<span className="visually-hidden">Loading...</span>
										</div>
									) : (
										"Signup"
									)}
								</button>
							</div>
						</div>
					</form>
				</div>
			</div>

		</>
	);
}
