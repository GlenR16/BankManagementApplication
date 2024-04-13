import React, { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { useNavigate } from "react-router-dom";
import { Modal } from "bootstrap";

export default function Withdraw() {
	const api = useAxiosAuth();
	const navigate = useNavigate();

	const [accounts, setAccounts] = useState([]);
	const [error, setError] = useState("");
	const [loading, setLoading] = useState(false);
	const [form, setForm] = useState({
		accountNumber: "",
		debit: "",
		typeId: 2,
	});

	useEffect(() => {
		api.get("/account/list").then((response) => {
			setAccounts(response.data);
		});
	}, []);

	function handleChange(e) {
		setForm({ ...form, [e.target.name]: e.target.value });
	}

	function handlePay() {
		setLoading(true);
		if (!form.accountNumber || !form.debit) {
			setError("Please fill all the fields");
			setLoading(false);
			return;
		}
        setError("");
        const withdrawModal = new Modal("#withdrawConfirmation");
        withdrawModal.show();
		setLoading(false);
	}

	function handleConfirm() {
		setLoading(true);
		api.post("/transaction/withdraw", form)
			.then((res) => {
				setLoading(false);
				navigate("/transaction/" + res.data.id);
			})
			.catch((err) => {
                if (err.response) setError(err.response.data.message);
				else setError("Something went wrong");
				setLoading(false);
			});
	}

	return (
		<div className="container">
			<div className="row flex-lg-row-reverse align-items-center justify-content-center g-5 m-2 text-center">
				<div className="col-12 col-lg-6 px-0 card border-0 shadow">
					<h5 className="card-header p-3 text-center fw-bold">Withdraw Funds</h5>
					<div className="card-body px-4">
						<div className="form-group">
							<form>
								<div className="mb-3">
									<div className="input-group">
										<label className="input-group-text" htmlFor="accountNumber">
											Account Number
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
								<div className="mb-3">
									<div className="input-group col">
										<span className="input-group-text" id="basic-addon1">
                                            ₹
										</span>
										<input type="text" className="form-control  rounded-end" id="debit" placeholder="Amount" name="debit" value={form.debit} onChange={handleChange} />
									</div>
								</div>
								{error && <div className="alert alert-danger">{error}</div>}
								<div className="d-grid  d-md-block text-center">
									<button type="button" className="btn btn-primary" onClick={handlePay}>
										Withdraw
									</button>
								</div>
							</form>
							<div className="modal fade" id="withdrawConfirmation" data-bs-backdrop="static" data-bs-keyboard="false" tabIndex="-1" aria-labelledby="withdrawConfirmationLabel" aria-hidden="true">
								<div className="modal-dialog modal-dialog-centered">
									<div className="modal-content">
										<div className="modal-header">
											<h5 className="modal-title fw-semibold" id="withdrawConfirmationLabel">
												Confirm transaction details
											</h5>
										</div>
										<div className="modal-body text-start">
											<div className="row mx-2">
												<div className="col"> Account Number: <b> {form.accountNumber} </b></div>
											</div>
											<div className="row mx-2">
												<div className="col">Amount: ₹ <b> {form.debit} </b></div>
											</div>
										</div>
										<div className="modal-footer justify-content-between">
											<button type="button" className="btn btn-secondary" data-bs-dismiss="modal">
												Cancel Transaction
											</button>
											<button type="button" className="btn btn-primary" data-bs-dismiss="modal" onClick={handleConfirm} disabled={loading}>
												{loading ? (
													<>
														<span className="spinner-border spinner-border-sm" aria-hidden="true"></span> <span role="status">Loading...</span>
													</>
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
					<img src="/atm.png" alt="Index Image" className="w-75" />
				</div>
			</div>
		</div>
	);
}

//onClick={() => console.log(form)}
