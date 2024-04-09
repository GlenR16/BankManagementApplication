import React, { useEffect, useState } from "react";
import { useNavigate } from 'react-router-dom';
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

  function handlePay(e)  {
	// Code to handle payment
	e.preventDefault();
	setLoading(true);
	if (!form.accountNumber || !form.credit) {
		setError("Please fill all the fields");
		setLoading(false);
		return;
	}
	else{
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
		console.log(form)
		api.post("/transaction/deposit", form)
        .then((res) => {
            setLoading(false);
            navigate("/transaction/"+res.data.transaction.id);
        })
        .catch((err) => {
            if (err.response) setError(err.response.data.error);
            else setError("Something went wrong");
            setLoading(false);
        });

	}

    useEffect(() => {
        api.get("/account/list")
        .then((response) => {
            setAccounts(response.data);
        });
    }, []);

	return (
		<div className="mx-auto m-5 p-5">
			<div className="card">
				<h5 className="card-header">Deposit Funds</h5>
				<div className="card-body">
					<div className="form-group">
						<form>
							<div className="mb-3">
								<div className="input-group">
									<label className="input-group-text" htmlFor="accountNumber">
										Choose an Account
									</label>
									<select className="form-select" id="accountNumber" name="accountNumber" onChange={handleChange}>
                                        <option value="">Select an Account</option>
										{
                                            accounts.length > 0 ? accounts.map((account) => {

                                                return <option key={account.accountNumber} value={account.accountNumber}>{account.accountNumber}</option>
                                            }) : <option>No Accounts</option>
                                        }
									</select>
								</div>
								<div className="form-text mx-3">Account Balance : ₹ {accounts.length > 0 ? accounts.find((account) => account.accountNumber == form.accountNumber)?.balance : 0}</div>
							</div>
							<div className="row align-items-center mb-3">
								<label htmlFor="credit" className="col-auto col-form-label">
									Enter Amount to Deposit :{" "}
								</label>
								<div className="input-group col">
									<span className="input-group-text" id="basic-addon1">
										{"\u20B9"}
									</span>
									<input type="text" className="form-control  rounded-end" id="credit" placeholder="Amount" name="credit" value={form.credit} onChange={handleChange} />
								</div>
							</div>
							<p className="invalid-feedback d-block">{error}</p>
							<div className="d-grid  d-md-block text-center">
								<button className="btn btn-primary " type="button" data-bs-toggle =	{(!form.accountNumber || !form.credit) ? ("") : ('modal')} data-bs-target="#staticBackdrop" onClick={handlePay}>Deposit</button>
							</div>
						</form>

						<div className="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabIndex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                            <div className="modal-dialog">
                                <div className="modal-content">
                                    <div className="modal-header">
                                        <h5 className="modal-title" id="staticBackdropLabel">Confirm 
										Deposit details</h5>
                                        <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div className="modal-body">
                                        <b>
                                            <table>
                                                <tbody>
                                                    <tr>
                                                        <td>Account Number : </td>
                                                        <td>{form.accountNumber}</td>
                                                    </tr>
                                                    <tr>
                                                        <td>Amount : </td>
                                                        <td>{form.credit}</td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </b>
                                    </div>
                                    <div className="modal-footer">
                                        <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                        <button type="button" className="btn btn-primary" data-bs-dismiss="modal" onClick={deposit} disabled={loading}>{loading ? (
                                            <div className="spinner-border mx-2" role="status">
                                                <span className="visually-hidden">Loading...</span>
                                            </div>
                                        ) : (
                                            "Confirm"
                                        )}</button>
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

//onClick={() => console.log(form)}