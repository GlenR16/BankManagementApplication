import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import useAxiosAuth from "../contexts/Axios";
import { Modal } from "bootstrap";

export default function () {
	const navigate = useNavigate();
	const api = useAxiosAuth();

	const [accounts, setAccounts] = useState([]);
	const [error, setError] = useState("");
	const [beneficaries, setBeneficaries] = useState([]);
	const [loading, setLoading] = useState(false);
	const [transaction, setTransaction] = useState({ accountNumber: 0, beneficiaryId: 0, debit: 0, typeId: 1 });

	useEffect(() => {
		api.get("/account/list").then((response) => {
			setAccounts(response.data);
		});
	}, []);

	useEffect(() => {
		api.get("/account/beneficiary/list/" + transaction.accountNumber).then((response) => {
			setBeneficaries(response.data);
		});
	}, [transaction.accountNumber]);

	function handleChange(e) {
		setTransaction({ ...transaction, [e.target.name]: e.target.value });
	}

	function handlePay(e) {
		e.preventDefault();
		setLoading(true);
		if (!transaction.accountNumber || !transaction.debit || !transaction.beneficiaryId) {
			setError("Please fill all the fields");
			setLoading(false);
			return;
		}
		setError("");
		setLoading(false);
		const transferModal = new Modal("#tranferModal");
		transferModal.show();
	}

	function handleClick(e) {
		setLoading(true);
		e.preventDefault();
		console.log(transaction);
		api.post("/transaction/transfer", transaction)
			.then((res) => {
				console.log(res.data);
				navigate("/transaction/" + res.data.id);
				setLoading(false);
				setError("");
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
					<h5 className="card-header p-3 text-center fw-bold">Funds Tranfers</h5>
					<div className="card-body">
						<div className="form-group">
							<form>
								<div className="mb-3">
									<div className="input-group">
										<label className="input-group-text" htmlFor="accountSelect">
											Choose an Account
										</label>
										<select className="form-select" name="accountNumber" id="accountSelect" onChange={handleChange}>
											<option value="" defaultValue>
												Select an Account
											</option>
											{accounts.length > 0 ? (
												accounts.map((account) => (
													<option key={account.id} value={account.accountNumber} disabled={!account.verified}>
														{account.accountNumber} {!account.verified ? "(Not Verified)" : ""}
													</option>
												))
											) : (
												<option value="" disabled>
													No Accounts Found
												</option>
											)}
										</select>
									</div>
									<div className="form-text text-start mx-3">Account Balance : ₹ {accounts.length > 0 ? (accounts.find((account) => account.accountNumber == transaction.accountNumber)?.balance ? accounts.find((account) => account.accountNumber == transaction.accountNumber).balance : 0) : 0}</div>
								</div>
								<div className="mb-3">
									<div className="input-group">
										<label htmlFor="PayeeAccountNumber" className="input-group-text">
											Payee Account Number{" "}
										</label>
										< select className="form-select" id="beneficiaryId" name="beneficiaryId" onChange={handleChange} disabled={!transaction.accountNumber}>
											{!transaction.accountNumber ?  <option value="" defaultValue>
												Select an Account
											</option> : <option value="" defaultValue>
												Select Beneficiary
											</option>}
											
											{beneficaries.length > 0 ? (
													beneficaries.map((account) => (
														<option key={account.id} value={account.id}>
															{account.recieverNumber}
														</option>
													))
												) : (
													<option value="" disabled>
														No Beneficiares found
													</option>
												)}
										</select>
									</div>
								</div>

								<div className="mb-3">
									<div className="input-group">
										<label htmlFor="PayeeAccountName" className="input-group-text">
											Payee Account Name{" "}
										</label>
										<input type="text" id="PayeeAccountName" className="form-control" disabled value={beneficaries.find((x) => x.id == transaction.beneficiaryId)?.name} placeholder="Beneficiary Name" />
									</div>
								</div>

								<div className="mb-3">
									<div className="input-group">
										<label htmlFor="AmountTransfer" className="input-group-text">
											Amount to Transfer{" "}
										</label>
										<span className="input-group-text">
											₹{" "}
										</span>
										<input type="number" className="form-control" id="AmountTransfer" placeholder="Amount" aria-label="To Change" aria-describedby="basic-addon1" name="debit" onChange={handleChange} />
									</div>
								</div>

								{error && <div className="alert alert-danger">{error}</div>}

								<div className="d-grid gap-2 d-md-block text-center">
									<button className="btn btn-primary " type="button" onClick={handlePay}>
										Transfer
									</button>
								</div>
							</form>

							<div className="modal fade" id="tranferModal" data-bs-backdrop="static" data-bs-keyboard="false" tabIndex="-1" aria-labelledby="tranferModalLabel" aria-hidden="true">
								<div className="modal-dialog modal-dialog-centered">
									<div className="modal-content">
										<div className="modal-header">
											<h5 className="modal-title" id="tranferModalLabel">
												Confirm transaction details
											</h5>
										</div>
										<div className="modal-body text-start">
											<div className="row mx-2">
												<div className="col"> Account Number: <b> {transaction.accountNumber} </b></div>
											</div>
											<div className="row mx-2">
												<div className="col">Payee Name: <b> {beneficaries.find((x) => x.id == transaction.beneficiaryId)?.name} </b></div>
											</div>
											<div className="row mx-2">
												<div className="col">Amount: ₹ <b> {transaction.debit} </b></div>
											</div>
										</div>
										<div className="modal-footer justify-content-between">
											<button type="button" className="btn btn-secondary" data-bs-dismiss="modal">
												Cancel Transaction
											</button>
											<button type="button" className="btn btn-primary" data-bs-dismiss="modal" onClick={handleClick} disabled={loading}>
												{loading ? (
													<>
                                                        <span className="spinner-border spinner-border-sm" aria-hidden="true"></span>
                                                        {" "}
                                                        <span role="status">Loading...</span>
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
			</div>
		</div>
	);
}
