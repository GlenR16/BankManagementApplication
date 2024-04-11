import React, { useState, useEffect } from "react";
import useAxiosAuth from "../contexts/Axios";
import { useParams } from "react-router-dom";
import { useUser } from "../contexts/UserContext";

export default function CardDetails() {
	const api = useAxiosAuth();
	const { user } = useUser();
	const { number } = useParams();

	const [card, setCard] = useState({});
	const [creditCard, setCreditCard] = useState({});
	const [bill, setBill] = useState({});
	const [form, setForm] = useState({});
	const [accounts, setAccounts] = useState([]);

	useEffect(() => {
		api.get("/account/list").then((response) => {
			setAccounts(response.data);
		});
		refreshData();
	}, []);

	function deleteCard() {
		api.delete("/card/" + number).then((response) => {
			refreshData();
		});
	}

	function handleChange(event) {
		setForm({ ...form, [event.target.name]: event.target.value });
	}

	function payBill() {
		api.post("/transaction/pay/" + number, form)
			.then((response) => {
				refreshData();
			})
			.catch((error) => {
				alert(error);
			});
	}

	function changePin() {
		api.put("/card/" + number, form)
			.then((response) => {
				refreshData();
			})
			.catch((error) => {
				alert(error);
			});
	}

	function refreshData() {
		api.get("/card/" + number).then((response) => {
			setCard(response.data);
			if (response.data.typeId === 2) {
				api.get("/card/creditCardDetails/" + response.data.id).then((response) => {
					setCreditCard(response.data);
					api.get("/transaction/pay/" + number).then((response) => {
						setBill(response.data);
					});
				});
			}
		});
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
			<div className="card gap-2 mb-4">
				<h5 className="card-header p-3 text-center fw-bold">Card Details</h5>
				<div className="row">
					<div className="col-sm-12 col-md-6 my-3">
						<div className="row mx-2 mt-2">
							<div className="col-6 fw-bold"> Card Number</div>
							<div className=" col"> {card.number} </div>
						</div>

						<div className="row mx-2 mt-2">
							<div className="col-6 fw-bold"> Account Number</div>
							<div className=" col"> {card.accountNumber} </div>
						</div>

						<div className="row mx-2 mt-2">
							<div className="col-6 fw-bold"> CVV</div>
							<div className=" col"> {card.cvv} </div>
						</div>

						<div className="row mx-2 mt-2">
							<div className="col-6 fw-bold"> PIN</div>
							<div className=" col"> {card.pin} </div>
						</div>

						<div className="row mx-2 mt-2">
							<div className="col-6 fw-bold"> Type</div>
							<div className=" col"> {checkTypeCard(card.typeId)} </div>
						</div>

						<div className="row mx-2 mt-2">
							<div className="col-6 fw-bold"> Expiry Date</div>
							<div className=" col"> {new Date(Date.parse(card.expiryDate))?.toLocaleDateString()} </div>
						</div>

						<div className="row mx-2 mt-2">
							<div className="col-6 fw-bold">Status</div>
							<div className=" col">
								{(!card.verified && card.deleted) || card.deleted ? <span className="badge rounded-pill text-bg-danger">Deleted</span> : ""}
								{card.verified && !card.deleted ? <span className="badge rounded-pill bg-success">Verified</span> : ""}
								{!card.verified && !card.deleted ? <span className="badge rounded-pill bg-success">Unverified</span> : ""}
							</div>
						</div>
					</div>
					{card.typeId == 2 ? (
						<div className="col-sm-12 col-md-6 my-3">
							<div className="row mx-2 mt-2">
								<div className="col-6 fw-bold"> Credit Limit</div>
								<div className=" col"> {creditCard.creditLimit} </div>
							</div>

							<div className="row mx-2 mt-2">
								<div className="col-6 fw-bold"> Credit Used</div>
								<div className=" col"> {creditCard.creditUsed} </div>
							</div>

							<div className="row mx-2 mt-2">
								<div className="col-6 fw-bold"> Credit Transactions</div>
								<div className=" col"> {creditCard.creditTransactions} </div>
							</div>

							<div className="row mx-2 mt-2">
								<div className="col-6 fw-bold"> Total Bill</div>
								<div className=" col"> {bill.total} </div>
							</div>

							<div className="row mx-2 mt-2 mb-3">
								<div className="col-6 fw-bold"> Credit Intrest</div>
								<div className=" col"> {bill.interest*100} % </div>
							</div>
                            {
                                bill.total > 0 &&

                                <div className="row mx-2 mt-2 mb-3">
                                    <div className="input-group w-75">
                                        <select className="form-select w-25" id="accountNumber" name="accountNumber" onChange={handleChange}>
                                            <option value="">Select an Account</option>
                                            {accounts.length > 0 ? (
                                                accounts.map((account) => {
                                                    return (
                                                        <option key={account.accountNumber} value={account.accountNumber} disabled={!account.verified}>
                                                            {account.accountNumber} {!account.verified ? "(Not Verified)" : ""}
                                                        </option>
                                                    );
                                                })
                                            ) : (
                                                <option>No Accounts</option>
                                            )}
                                        </select>
                                        <button type="button" className="btn btn-primary" onClick={payBill}>
                                            Pay Bill
                                        </button>
                                    </div>
                                </div>
                            }
						</div>
					) : (
						""
					)}
				</div>
				{(user.role == "ADMIN" || user.role == "EMPLOYEE") && !card.deleted ? (
					<div className="row p-2 g-4 m-2">
						<div className="col d-grid">
							<button type="button" className="btn btn-danger" onClick={deleteCard}>
								Delete Card
							</button>
						</div>
					</div>
				) : (
					""
				)}
			</div>
			{!card.deleted ? (
				<div className="card gap-2">
                    <h5 className="card-header p-3 text-center fw-bold">PIN Tools</h5>
					<div className="form-group m-4">
						<div className="mb-3">
							<div className="input-group">
								<label htmlFor="oldPin" className="input-group-text">
									Card Old PIN{" "}
								</label>
								<input type="number" className="form-control" id="oldPin" placeholder="Pin" name="oldPin" onChange={handleChange} />
							</div>
						</div>
						<div className="mb-3">
							<div className="input-group">
								<label htmlFor="newPin" className="input-group-text">
									Card New PIN{" "}
								</label>
								<input type="number" className="form-control" id="newPin" placeholder="Pin" name="newPin" onChange={handleChange} />
							</div>
						</div>
						<div className="d-grid">
							<button type="button" className="btn btn-primary" onClick={changePin}>
								Change PIN
							</button>
						</div>
					</div>
				</div>
			) : (
				""
			)}
		</div>
	);
}
