import React, { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { NavLink, useNavigate } from "react-router-dom";
import { useUser } from "../contexts/UserContext";

export default function Account() {
	const api = useAxiosAuth();
	const navigate = useNavigate();
	const { user } = useUser();

	const [accountNumber, setAccountNumber] = useState("");
	const [accounts, setAccounts] = useState([]);
	const [accountTypes, setAccountTypes] = useState([]);
	const [branches, setBranches] = useState([]);
	const [beneficaries, setBeneficaries] = useState([]);
	const [cards, setCards] = useState([]);

	useEffect(() => {
		if (user == null) navigate("/login");
	}, [user]);

	useEffect(() => {
		api.get("/account/list").then((response) => {
			console.log("Accounts", response.data);
			setAccounts(response.data);
		});
		api.get("/account/type").then((response) => {
			setAccountTypes(response.data);
		});
		api.get("account/branch").then((response) => {
			setBranches(response.data.content);
		});
	}, []);

	useEffect(() => {
		api.get("/account/beneficiary/list/" + accountNumber).then((response) => {
			setBeneficaries(response.data);
		});
		api.get("/card/account/" + accountNumber).then((response) => {
			setCards(response.data);
		});
	}, [accountNumber]);

	function handleChange(e) {
		setAccountNumber(e.target.value);
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
		<div className="container-fluid col-sm-12 col-md-8 my-4">
			<ul class="nav nav-tabs">
				<li class="nav-item" role="presentation">
					<button className="nav-link active text-dark" id="pills-accounts-tab" data-bs-toggle="pill" data-bs-target="#pills-accounts" type="button" role="tab" aria-controls="pills-accounts" aria-selected="true">
						Accounts
					</button>
				</li>

				<li class="nav-item" role="presentation">
					<button className="nav-link text-dark" id="pills-beneficiaries-tab" data-bs-toggle="pill" data-bs-target="#pills-beneficiaries" type="button" role="tab" aria-controls="pills-beneficiaries" aria-selected="false">
						Beneficiaries
					</button>
				</li>

				<li class="nav-item" role="presentation">
					<button className="nav-link text-dark" id="pills-cards-tab" data-bs-toggle="pill" data-bs-target="#pills-cards" type="button" role="tab" aria-controls="pills-cards" aria-selected="false">
						Cards
					</button>
				</li>
			</ul>
			<div className="tab-content" id="pills-tabContent">
				<div className="tab-pane fade show active" id="pills-accounts" role="tabpanel" aria-labelledby="pills-accounts-tab" tabIndex="0">
					<div id="account">
						<div className="card rounded-top-0 mt-0 border-0 shadow">
							<div className="card-body table-responsive">
								<table className="table caption-top text-center">
									<caption className="text-center border-bottom border-2 border-dark">
										<h3 className="d-flex flex-row align-items-center gap-2 text-black">
											Your accounts
											<NavLink className="btn" to="/AccountRegister">
												<svg viewBox="0 0 24 24" width="32" height="32" fill="none" xmlns="http://www.w3.org/2000/svg">
													<g id="SVGRepo_bgCarrier" strokeWidth="0"></g>
													<g id="SVGRepo_tracerCarrier" strokeLinecap="round" strokeLinejoin="round"></g>
													<g id="SVGRepo_iconCarrier">
														{" "}
														<path d="M15 12L12 12M12 12L9 12M12 12L12 9M12 12L12 15" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path> <path d="M22 12C22 16.714 22 19.0711 20.5355 20.5355C19.0711 22 16.714 22 12 22C7.28595 22 4.92893 22 3.46447 20.5355C2 19.0711 2 16.714 2 12C2 7.28595 2 4.92893 3.46447 3.46447C4.92893 2 7.28595 2 12 2C16.714 2 19.0711 2 20.5355 3.46447C21.5093 4.43821 21.8356 5.80655 21.9449 8" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path>{" "}
													</g>
												</svg>
											</NavLink>
										</h3>
									</caption>
									<thead>
										<tr>
											<th scope="col">Account Number</th>
											<th scope="col">IFSC</th>
											<th scope="col">Type</th>
											<th scope="col">Balance</th>
											<th scope="col">Verified</th>
											<th scope="col">Locked</th>
											<th scope="col">Action</th>
										</tr>
									</thead>
									<tbody>
										{accounts.length > 0 ? (
											accounts.map((account, index) => (
												<tr key={index}>
													<td>{account.accountNumber}</td>
													<td>
														{branches.map((branch) => {
															if (branch.id === account.branchId) {
																return branch.ifsc;
															}
														})}
													</td>
													<td>{accountTypes[account.typeId - 1]?.name}</td>
													<td>₹ {account.balance}</td>
													<td>{account.verified ?
														<span className="badge rounded-pill text-bg-success">Verified</span>
														:
														<span className="badge rounded-pill text-bg-warning">Unverified</span>
													}</td>
													<td>{account.locked ?
														<span className="badge rounded-pill text-bg-danger">Locked</span>
														:
														<span className="badge rounded-pill text-bg-primary">Unlocked</span>
													}</td>
													<td>
														<NavLink to={"/accountDetails/" + account.accountNumber} className="text-decoration-none">
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

				<div className="tab-pane fade" id="pills-beneficiaries" role="tabpanel" aria-labelledby="pills-beneficiaries-tab" tabIndex="0">
					<div id="beneficiaries">
						<div className="card rounded-top-0 mt-0 border-0 shadow">
							<div className="card-body table-responsive">
								<table className="table caption-top text-center">
									<caption className="text-center border-bottom border-2 border-dark">
										<h3 className="d-flex flex-row align-items-center justify-content-between gap-2 text-black mx-2">
											<div className="d-flex flex-row align-items-center gap-2 text-black">
												Your Beneficiaries
												<NavLink className="btn" to="/addBeneficiary">
													<svg viewBox="0 0 24 24" width="32" height="32" fill="none" xmlns="http://www.w3.org/2000/svg">
														<g id="SVGRepo_bgCarrier" strokeWidth="0"></g>
														<g id="SVGRepo_tracerCarrier" strokeLinecap="round" strokeLinejoin="round"></g>
														<g id="SVGRepo_iconCarrier">
															{" "}
															<path d="M15 12L12 12M12 12L9 12M12 12L12 9M12 12L12 15" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path> <path d="M22 12C22 16.714 22 19.0711 20.5355 20.5355C19.0711 22 16.714 22 12 22C7.28595 22 4.92893 22 3.46447 20.5355C2 19.0711 2 16.714 2 12C2 7.28595 2 4.92893 3.46447 3.46447C4.92893 2 7.28595 2 12 2C16.714 2 19.0711 2 20.5355 3.46447C21.5093 4.43821 21.8356 5.80655 21.9449 8" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path>{" "}
														</g>
													</svg>
												</NavLink>
											</div>
											<select className="form-select w-50" name="accountNumber" id="accountNumber" onChange={handleChange} aria-label="accountNumber" aria-describedby="accountNumber">
												<option value="" defaultValue>
													Select an Account
												</option>
												{accounts.length > 0 ? (
													accounts.map((account) => (
														<option key={account.id} value={account.accountNumber}>
															{account.accountNumber}
														</option>
													))
												) : (
													<option value="" disabled>
														No Accounts Found
													</option>
												)}
											</select>
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
										{!accountNumber ?
											(
												<tr>
													<td colSpan="10">Select an Account</td>
												</tr>
											) : beneficaries.length > 0 ? (
												beneficaries.map((beneficiary, index) => (
													<tr key={index}>
														<td>{index + 1}</td>
														<td>{beneficiary.recieverNumber}</td>
														<td> {beneficiary.ifsc} </td>
														<td>{beneficiary.name}</td>
													</tr>
												))
											) : (
												<tr>
													<td colSpan="4">No Beneficiares found</td>
												</tr>
											)}
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div className="tab-pane fade" id="pills-cards" role="tabpanel" aria-labelledby="pills-cards-tab" tabIndex="0">
					<div id="cards">
						<div className="card rounded-top-0 mt-0 border-0 shadow">
							<div className="card-body table-responsive">
								<table className="table caption-top text-center">
									<caption className="text-center border-bottom border-2 border-dark">
										<h3 className="d-flex flex-row align-items-center justify-content-between gap-2 text-black mx-2">
											<div className="d-flex flex-row align-items-center gap-2 text-black">
												Your Cards
												<NavLink className="btn" to="/addCard">
													<svg viewBox="0 0 24 24" width="32" height="32" fill="none" xmlns="http://www.w3.org/2000/svg">
														<g id="SVGRepo_bgCarrier" strokeWidth="0"></g>
														<g id="SVGRepo_tracerCarrier" strokeLinecap="round" strokeLinejoin="round"></g>
														<g id="SVGRepo_iconCarrier">
															{" "}
															<path d="M15 12L12 12M12 12L9 12M12 12L12 9M12 12L12 15" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path> <path d="M22 12C22 16.714 22 19.0711 20.5355 20.5355C19.0711 22 16.714 22 12 22C7.28595 22 4.92893 22 3.46447 20.5355C2 19.0711 2 16.714 2 12C2 7.28595 2 4.92893 3.46447 3.46447C4.92893 2 7.28595 2 12 2C16.714 2 19.0711 2 20.5355 3.46447C21.5093 4.43821 21.8356 5.80655 21.9449 8" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path>{" "}
														</g>
													</svg>
												</NavLink>
											</div>
											<select className="form-select w-50" name="accountNumber" id="accountNumber" onChange={handleChange} aria-label="accountNumber" aria-describedby="accountNumber">
												<option value="" defaultValue>
													Select an Account
												</option>
												{accounts.length > 0 ? (
													accounts.map((account) => (
														<option key={account.id} value={account.accountNumber}>
															{account.accountNumber}
														</option>
													))
												) : (
													<option value="" disabled>
														No Accounts Found
													</option>
												)}
											</select>
										</h3>
									</caption>
									<thead>
										<tr>
											<th scope="col">Sr. No</th>
											<th scope="col">Card Number</th>
											<th scope="col">Type</th>
											<th scope="col">Expiry Date</th>
											<th scope="col">Status</th>
											<th scope="col">Action</th>
										</tr>
									</thead>
									<tbody>
										{!accountNumber ?
											(
												<tr>
													<td colSpan="10">Select an Account</td>
												</tr>
											) : cards.length > 0 ? (
												cards.map((card, index) => (
													<tr key={index}>
														<td>{index + 1}</td>
														<td>{card.number}</td>
														<td>{checkTypeCard(card.typeId)}</td>
														<td>{new Date(Date.parse(card.expiryDate))?.toLocaleDateString()}</td>
														<td>
															{
																card.active ?
																	<span className="badge rounded-pill text-bg-success">Active</span>
																	:
																	<span className="badge rounded-pill text-bg-secondary">Inactive</span>
															}
														</td>
														<td>
															<NavLink to={"/cardDetails/" + card.number} className="text-decoration-none">
																View
															</NavLink>
														</td>
													</tr>
												))
											) : (
												<tr>
													<td colSpan="6">No Cards found</td>
												</tr>
											)}
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	);
}
