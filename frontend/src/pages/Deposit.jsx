import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import useAxiosAuth from "../contexts/Axios";

export default function Withdraw() {
	const navigate = useNavigate();
	const [accounts, setAccounts] = useState([]);
	const [error, setError] = useState("");
	const [loading, setLoading] = useState(false);
	const api = useAxiosAuth();

	const [form, setForm] = useState({
		accountNumber: "",
		credit: "",
		typeId: 3,
	});

	function handleChange(e) {
		setForm({
			...form,
			[e.target.name]: e.target.value,
		});
	}

	function handlePay(e) {
		// Code to handle payment
		e.preventDefault();
		setLoading(true);
		if (!form.accountNumber || !form.credit) {
			setError("Please fill all the fields");
			setLoading(false);
			return;
		} else {
			setLoading(false);
		}
	}

	function deposit() {
		setLoading(true);
		if (!form.accountNumber || !form.credit) {
			setError("Please fill all the fields");
			setLoading(false);
			return;
		}
		console.log(form);
		api.post("/transaction/deposit", form)
			.then((res) => {
				setLoading(false);
				navigate("/transaction/" + res.data.transaction.id);
			})
			.catch((err) => {
				if (err.response) setError(err.response.data.error);
				else setError("Something went wrong");
				setLoading(false);
			});
	}

	useEffect(() => {
		api.get("/account/list").then((response) => {
			setAccounts(response.data);
		});
	}, []);

	return (
		<div className="container">
			<div className="row flex-lg-row-reverse align-items-center justify-content-center g-5 m-2 text-center">
				<div className="col-12 col-lg-6 px-0 card border-0 shadow">
					<h5 className="card-header p-3 text-center fw-bold">Deposit Funds</h5>
					<div className="card-body px-4">
						<div className="form-group">
							<form>
								<div className="mb-3">
									<div className="input-group">
										<label className="input-group-text" htmlFor="accountNumber">
											Choose an Account
										</label>
										<select className="form-select" id="accountNumber" name="accountNumber" onChange={handleChange}>
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
									</div>
									<div className="form-text text-start mx-3">Account Balance : ₹ {accounts.length > 0 ? (accounts.find((account) => account.accountNumber == form.accountNumber)?.balance ? accounts.find((account) => account.accountNumber == form.accountNumber).balance : 0) : 0}</div>
								</div>
								<div className="row align-items-center mb-3">
									<div className="input-group col">
										<span className="input-group-text" id="basic-addon1">
											{"\u20B9"}
										</span>
										<input type="text" className="form-control  rounded-end" id="credit" placeholder="Amount" name="credit" value={form.credit} onChange={handleChange} />
									</div>
								</div>
								{error && <div className="alert alert-danger">{error}</div>}
								<div className="d-grid  d-md-block text-center">
									<button className="btn btn-primary " type="button" data-bs-toggle={!form.accountNumber || !form.credit ? "" : "modal"} data-bs-target="#depositModal" onClick={handlePay}>
										Deposit
									</button>
								</div>
							</form>

							<div className="modal fade" id="depositModal" data-bs-backdrop="static" data-bs-keyboard="false" tabIndex="-1" aria-labelledby="depositModalLabel" aria-hidden="true">
								<div className="modal-dialog modal-dialog-centered">
									<div className="modal-content">
										<div className="modal-header">
											<h5 className="modal-title fw-semibold" id="depositModalLabel">
												Confirm transaction details
											</h5>
										</div>
										<div className="modal-body text-start">
											<div className="row mx-2">
												<div className="col"> Account Number: <b> {form.accountNumber} </b></div>
											</div>
											<div className="row mx-2">
												<div className="col">Amount: ₹ <b> {form.credit} </b></div>
											</div>
										</div>
										<div className="modal-footer">
											<button type="button" className="btn btn-secondary" data-bs-dismiss="modal">
												Close
											</button>
											<button type="button" className="btn btn-primary" data-bs-dismiss="modal" onClick={deposit} disabled={loading}>
												{loading ? (
													<div className="spinner-border mx-2" role="status">
														<span className="visually-hidden">Loading...</span>
													</div>
												) : (
													"Confirm"
												)}
											</button>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
                <div className="col-12 col-sm-8 col-lg-6 d-none d-md-flex align-items-center justify-content-center">
					<img src="/index.png" alt="Index Image" className="w-75" />
				</div>
			</div>
		</div>
	);
}

//onClick={() => console.log(form)}
