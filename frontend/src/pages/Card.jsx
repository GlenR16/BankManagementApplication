import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import useAxiosAuth from "../contexts/Axios";
import { Modal } from "bootstrap";

export default function () {
	const navigate = useNavigate();
	const api = useAxiosAuth();

	const [accounts, setAccounts] = useState([]);
	const [cards, setCards] = useState([]);
	const [error, setError] = useState(null);
	const [beneficaries, setBeneficaries] = useState([]);
	const [loading, setLoading] = useState(false);
    const [creditDetail, setCreditDetail] = useState({});
	const [transaction, setTransaction] = useState({ number: 0, pin: 0, cvv: 0, accountNumber: 0, beneficiaryId: 0, amount: 0, typeId: 4 });

	useEffect(() => {
		api.get("/card/list").then((response) => {
			setCards(response.data);
		});
        api.get("/account/list").then((response) => {
            setAccounts(response.data);
        });
	}, []);

    useEffect(() => {
        const card = cards.find((card) => card.number == transaction.number);
        api.get("/account/beneficiary/list/" + card?.accountNumber).then((response) => {
            setBeneficaries(response.data);
        });
        setTransaction({ ...transaction, accountNumber: card?.accountNumber });
        if (card?.typeId == 2) {
            api.get("/card/creditCardDetails/" + card?.id).then((response) => {
                setCreditDetail(response.data);
            });
        }
        console.log(transaction);
        console.log(creditDetail);
    }, [transaction.number]);

	useEffect(() => {
        if (transaction.accountNumber) {
            
        }
	}, [transaction.accountNumber]);

	function handleChange(e) {
        setTransaction({ ...transaction, [e.target.name]: e.target.value });
	}

	function handlePay(e) {
		e.preventDefault();
		setLoading(true);
		if ( !transaction.accountNumber || !transaction.amount || !transaction.beneficiaryId || !transaction.number || !transaction.pin || !transaction.cvv ) {
			setError("Please fill all the fields");
			setLoading(false);
			return;
		}
        const modal = new Modal("#cardTransferModal");
        modal.show();
        setError("");
		setLoading(false);

	}

	function handleClick(e) {
		setLoading(true);
		e.preventDefault();
		api.post("/transaction/cardTransfer", transaction)
			.then((res) => {
				navigate("/transaction/" + res.data.transaction.id);
				setLoading(false);
			})
			.catch((err) => {
				if (err.response) setError(err.response.data.error);
				else setError("Something went wrong");
				setLoading(false);
			});
	}

	return (
		<div className="container">
			<div className="row flex-lg-row-reverse align-items-center justify-content-center g-5 m-2 text-center">
				<div className="col-12 col-lg-6 px-0 card border-0 shadow">
					<h5 className="card-header">Card Transfer</h5>
					<div className="card-body">
						<div className="form-group">
							<form>
								<div className="mb-3">
									<div className="input-group">
										<label className="input-group-text" htmlFor="cardSelect">
											Select Card
										</label>
										<select className="form-select" name="number" id="cardSelect" onChange={handleChange}>
											<option value="" defaultValue>
												Select Card
											</option>
											{cards.length > 0 ? (
												cards.map((card) => (
													<option key={card.id} value={card.number} >
														{card.number}
													</option>
												))
											) : (
												<option value="" disabled>
													No Cards Found
												</option>
											)}
										</select>
									</div>
									
								</div>

								<div className="mb-3">
									<div className="input-group">
                                        <label htmlFor="Pin" className="input-group-text">
                                            Card PIN {" "}
                                        </label>
										<input type="number" className="form-control" id="Pin" placeholder="Pin" name="pin" onChange={handleChange} />
									</div>
								</div>

								<div className="mb-3">
									<div className="input-group">
                                        <label htmlFor="Cvv" className="input-group-text">
                                            Card CVV {" "}
                                        </label>
										<input type="number" className="form-control" id="Cvv" placeholder="Cvv" aria-label="To Change" aria-describedby="basic-addon1" name="cvv" onChange={handleChange} />
									</div>
								</div>

								<div className="mb-3">
									<div className="input-group">
                                        <label htmlFor="accountNumber" className="input-group-text">
                                            Account Number{" "}
                                        </label>
										<input type="text" id="accountNumber" className="form-control" disabled value={transaction.accountNumber} placeholder="Account Numbr" />
									</div>
                                    {
                                        cards.find((card) => card.number == transaction.number)?.typeId == 1 &&
                                        <div className="form-text mx-3 text-start">Account Balance : ₹ {accounts.length > 0 ? (accounts.find((account) => account.accountNumber == transaction.accountNumber)?.balance ? accounts.find((account) => account.accountNumber == transaction.accountNumber).balance : 0) : 0} </div>
                                    }
                                    {
                                        cards.find((card) => card.number == transaction.number)?.typeId == 2 &&
                                        <div className="form-text mx-3 text-start">Credit Available : ₹ {creditDetail.creditLimit - creditDetail.creditUsed} / {creditDetail.creditLimit} </div>
                                    }
								</div>

								<div className="mb-3">
									<div className="input-group">
                                        <label htmlFor="PayeeAccountNumber" className="input-group-text">
                                            Payee Account Number {" "}
                                        </label>
										<select className="form-select" id="beneficiaryId" name="beneficiaryId" onChange={handleChange}>
											<option value="" defaultValue>
												Select Beneficiary
											</option>
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
										<input type="text" id="PayeeAccountName" className="form-control" disabled value={beneficaries.find((x) => x.id == transaction.beneficiaryId)?.name} placeholder="Beneficiary Name" />
									</div>
								</div>

								<div className="mb-3">
									<div className="input-group">
                                        <label htmlFor="AmountTransfer" className="input-group-text">
                                            Enter Amount to Transfer{" "}
                                        </label>
										<span className="input-group-text">
											₹{" "}
										</span>
										<input type="number" className="form-control" id="AmountTransfer" placeholder="Amount" aria-label="To Change" aria-describedby="basic-addon1" name="amount" onChange={handleChange} />
									</div>
								</div>
								{error && <div className="alert alert-danger">{error}</div>}
								<div className="d-grid gap-2 d-md-block text-center">
									<button className="btn btn-primary " type="button" onClick={handlePay}>
										Confirm Payment
									</button>
								</div>
							</form>

							<div className="modal fade" id="cardTransferModal" data-bs-backdrop="static" data-bs-keyboard="false" tabIndex="-1" aria-labelledby="cardTransferModalLabel" aria-hidden="true">
								<div className="modal-dialog modal-dialog-centered">
									<div className="modal-content">
										<div className="modal-header">
											<h5 className="modal-title" id="cardTransferModalLabel">
                                                Confirm transaction details
											</h5>
										</div>
										<div className="modal-body text-start">
                                            <div className="row mx-2">
												<div className="col"> Card Number: <b> {transaction.number} </b></div>
											</div>
                                            <div className="row mx-2">
												<div className="col">Account Number: <b> {transaction.accountNumber} </b></div>
											</div>
                                            <div className="row mx-2">
												<div className="col">Payee Name: <b> {beneficaries.find((x) => x.id == transaction.beneficiaryId)?.name} </b></div>
											</div>
											<div className="row mx-2">
												<div className="col">Amount: ₹ <b> {transaction.amount} </b></div>
											</div>
										</div>
										<div className="modal-footer justify-content-between">
											<button type="button" className="btn btn-secondary" data-bs-dismiss="modal">
												Cancel Transaction
											</button>
											<button type="button" className="btn btn-primary" data-bs-dismiss="modal" onClick={handleClick} disabled={loading}>
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
			</div>
		</div>
	);
}
