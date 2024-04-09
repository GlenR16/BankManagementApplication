import React, { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { NavLink, useNavigate } from "react-router-dom";

export default function Account() {
	const [accountNumber, setAccountNumber] = useState("");
	const [accounts, setAccounts] = useState([]);
	const [accountTypes, setAccountTypes] = useState([]);
	const [branches, setBranches] = useState([]);
	const [beneficaries, setBeneficaries] = useState([]);
	const [cards, setCards] = useState([]);
	const api = useAxiosAuth();
	const navigate = useNavigate();


	useEffect(() => {
		api.get("/account/list")
			.then((response) => {
				setAccounts(response.data);
			});
		api.get("/account/type")
			.then((response) => {
				setAccountTypes(response.data);
			});
		api.get("account/branch")
			.then((response) => {
				setBranches(response.data);
			});
	}, []);


	useEffect(() => {
		api.get("/account/beneficiary/list/" + accountNumber)
			.then((response) => {
				setBeneficaries(response.data);
			});
		api.get("/card/account/" + accountNumber)
			.then((response) => {
				setCards(response.data);
				console.log("CARDS : ",response.data);
			});
	}, [accountNumber]);

	function createAccount() {
		navigate("/AccountRegister");
	}

	function createBeneficiaries() {
		navigate("/addBeneficiary");
	}

	function handleChange(e) {
		setAccountNumber(e.target.value);
	}

	function addCard()
	{
		navigate("/addCard");
	}

	function checkTypeCard(typeId) {
		if (typeId === 1) {
			return "Debit Card";
		} else if (typeId === 2) {
			return "Credit Card";
		} else {
			return "Unknown";
		}
	}

	return (
		<>

				<ul class="nav nav-tabs mt-5 mx-5" style={{width: '25%'}}>
					<li class="nav-item"  role="presentation">
					<button className="nav-link active text-dark" id="pills-home-tab" data-bs-toggle="pill" data-bs-target="#pills-home" type="button" role="tab" aria-controls="pills-home" aria-selected="true" >Accounts</button>
					</li>

					<li class="nav-item" role="presentation">
					<button className="nav-link text-dark" id="pills-profile-tab" data-bs-toggle="pill" data-bs-target="#pills-profile" type="button" role="tab" aria-controls="pills-profile" aria-selected="false">Beneficiaries</button>
					</li>

					<li class="nav-item" role="presentation">
					<button className="nav-link text-dark" id="pills-contact-tab" data-bs-toggle="pill" data-bs-target="#pills-contact" type="button" role="tab" aria-controls="pills-contact" aria-selected="false">Cards</button>
					</li>
				</ul>

			<div className="tab-content" id="pills-tabContent">
				<div className="tab-pane fade show active" id="pills-home" role="tabpanel" aria-labelledby="pills-home-tab" tabIndex="0">
					<div id="account" className="mt-0 px-4" >
						<div className="card mt-0 border-0 shadow">
							<div className="card-body table-responsive">

								<table className="table caption-top">
									<caption className="text-center border-bottom border-2 border-dark">
										<h3 className="d-flex flex-row align-items-center gap-2 text-black" >
											Your accounts
											<a className="btn" onClick={createAccount}>
												<svg viewBox="0 0 24 24" width="32" height="32" fill="none" xmlns="http://www.w3.org/2000/svg">
													<g id="SVGRepo_bgCarrier" strokeWidth="0"></g>
													<g id="SVGRepo_tracerCarrier" strokeLinecap="round" strokeLinejoin="round"></g>
													<g id="SVGRepo_iconCarrier">
														{" "}
														<path d="M15 12L12 12M12 12L9 12M12 12L12 9M12 12L12 15" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path> <path d="M22 12C22 16.714 22 19.0711 20.5355 20.5355C19.0711 22 16.714 22 12 22C7.28595 22 4.92893 22 3.46447 20.5355C2 19.0711 2 16.714 2 12C2 7.28595 2 4.92893 3.46447 3.46447C4.92893 2 7.28595 2 12 2C16.714 2 19.0711 2 20.5355 3.46447C21.5093 4.43821 21.8356 5.80655 21.9449 8" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path>{" "}
													</g>
												</svg>
											</a>
										</h3>
									</caption>
									<thead>
										<tr>
											<th scope="col">Number</th>
											<th scope="col">IFSC</th>
											<th scope="col">Type</th>
											<th scope="col">Balance</th>
											<th scope="col">Action</th>
										</tr>
									</thead>
									<tbody>
										{accounts.length > 0 ? (
											accounts.map((account, index) => (
												<tr key={index}>
													<td>{account.accountNumber}</td>
													<td>{branches[account.branchId - 1]?.ifsc}</td>
													<td>{accountTypes[account.typeId - 1]?.name}</td>
													<td>₹ {account.balance}</td>
													<td>
														<NavLink to="#" className="text-decoration-none">
															View
														</NavLink>
													</td>
												</tr>
											))
										) : (
											<tr>
												<td colSpan="5">No accounts found</td>
											</tr>
										)}
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>


				<div className="tab-pane fade" id="pills-profile" role="tabpanel" aria-labelledby="pills-profile-tab" tabIndex="0" >
					<div id="beneficiaries" className="mt-0 px-4" >
						
						<div className="card mt- border-0 shadow">
							<div className="card-body table-responsive">
							<div className="my-2 text-start" style={{width: '25%' , marginLeft: '39%'}}>
							<select className="form-select" name="accountNumber" id="accountNumber" onChange={handleChange} aria-label="accountNumber" aria-describedby="accountNumber">
								<option value="" defaultValue>Select an Account</option>
								{
									accounts.length > 0 ? accounts.map((account) => (
										<option key={account.id} value={account.accountNumber}>{account.accountNumber}</option>
									)) : <option value="" disabled>No Accounts Found</option>
								}
							</select>
						</div>
						<hr></hr>
								<table className="table caption-top">
									<caption className="text-center border-bottom border-2 border-dark">
										<h3 className="d-flex flex-row align-items-center gap-2 text-black" >
											Your Beneficiaries
											<a className="btn" onClick={createBeneficiaries}>
												<svg viewBox="0 0 24 24" width="32" height="32" fill="none" xmlns="http://www.w3.org/2000/svg">
													<g id="SVGRepo_bgCarrier" strokeWidth="0"></g>
													<g id="SVGRepo_tracerCarrier" strokeLinecap="round" strokeLinejoin="round"></g>
													<g id="SVGRepo_iconCarrier">
														{" "}
														<path d="M15 12L12 12M12 12L9 12M12 12L12 9M12 12L12 15" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path> <path d="M22 12C22 16.714 22 19.0711 20.5355 20.5355C19.0711 22 16.714 22 12 22C7.28595 22 4.92893 22 3.46447 20.5355C2 19.0711 2 16.714 2 12C2 7.28595 2 4.92893 3.46447 3.46447C4.92893 2 7.28595 2 12 2C16.714 2 19.0711 2 20.5355 3.46447C21.5093 4.43821 21.8356 5.80655 21.9449 8" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path>{" "}
													</g>
												</svg>
											</a>
										</h3>
									</caption>
									<thead>
										<tr>
											<th scope="col">ID</th>
											<th scope="col">Account Number</th>
											<th scope="col">IFSC</th>
											<th scope="col">Name</th>
										</tr>
									</thead>
									<tbody>
										{beneficaries.length > 0 ? (
											beneficaries.map((beneficiary, index) => (
												<tr key={index}>
													<td>{index+1}</td>
													<td>{beneficiary.recieverNumber}</td>
													<td> {beneficiary.ifsc} </td>
													<td>{beneficiary.name}</td>
												</tr>
											))
										) : (
											<tr>
												<td colSpan="5">No Beneficiares found</td>
											</tr>
										)}
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div className="tab-pane fade" id="pills-contact" role="tabpanel" aria-labelledby="pills-contact-tab" tabIndex="0">
					<div id="cards" className="mt-0 px-4" >
						
						<div className="card mt-0 border-0 shadow">
							<div className="card-body table-responsive">
							<div className="my-2 text-start" style={{width: '25%' , marginLeft: '39%'}}>
							<select className="form-select" name="accountNumber" id="accountNumber" onChange={handleChange} aria-label="accountNumber" aria-describedby="accountNumber">
								<option value="" defaultValue>Select an Account</option>
								{
									accounts.length > 0 ? accounts.map((account) => (
										<option key={account.id} value={account.accountNumber}>{account.accountNumber}</option>
									)) : <option value="" disabled>No Accounts Found</option>
								}
							</select>
						</div>
						<hr></hr>
								<table className="table caption-top">
									<caption className="text-center border-bottom border-2 border-dark">
										<h3 className="d-flex flex-row align-items-center gap-2 text-black" >
											Your Cards
											<a className="btn" onClick={addCard}>
												<svg viewBox="0 0 24 24" width="32" height="32" fill="none" xmlns="http://www.w3.org/2000/svg">
													<g id="SVGRepo_bgCarrier" strokeWidth="0"></g>
													<g id="SVGRepo_tracerCarrier" strokeLinecap="round" strokeLinejoin="round"></g>
													<g id="SVGRepo_iconCarrier">
														{" "}
														<path d="M15 12L12 12M12 12L9 12M12 12L12 9M12 12L12 15" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path> <path d="M22 12C22 16.714 22 19.0711 20.5355 20.5355C19.0711 22 16.714 22 12 22C7.28595 22 4.92893 22 3.46447 20.5355C2 19.0711 2 16.714 2 12C2 7.28595 2 4.92893 3.46447 3.46447C4.92893 2 7.28595 2 12 2C16.714 2 19.0711 2 20.5355 3.46447C21.5093 4.43821 21.8356 5.80655 21.9449 8" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path>{" "}
													</g>
												</svg>
											</a>
										</h3>
									</caption>
									<thead>
										<tr>
											<th scope="col">ID</th>    {/*Not Requiered, change to index*/}
											<th scope="col">Card Number</th>
											<th scope="col">Account Number</th>   {/*Not Requiered*/}
											<th scope="col">CVV</th>  {/*To be Hidden*/}
											<th scope="col">PIN</th>  {/*To be Hidden*/}
											<th scope="col">Type</th>
											<th scope="col">Expiry Date</th>
											<th scope="col">Active</th>
										</tr>
									</thead>
									<tbody>
										{cards.length > 0 ? (
											cards.map((card, index) => (
												<tr key={index}>
													<td>{index+1}</td>
													<td>{card.number}</td>
													<td>{card.accountNumber}</td>
													<td> {card.cvv} </td>
													<td>{card.pin}</td>
													<td>{checkTypeCard(card.typeId)}</td>
													<td>{new Date(Date.parse(card.expiryDate))?.toLocaleDateString()}</td>
													<td>{card.active ? "TRUE" : "FALSE"}</td>
												</tr>
											))
										) : (
											<tr>
												<td colSpan="5">No Cards found</td>
											</tr>
										)}
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</>
	);
}
