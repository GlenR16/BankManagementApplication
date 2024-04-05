import React, { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";

export default function Withdraw() {
    const [accounts, setAccounts] = useState([]);
	const [error, setError] = useState("");
	const [loading, setLoading] = useState(false);
    const api = useAxiosAuth();

    const [form, setForm] = useState({
        accountNumber: "",
        amount: "",
		senderAccount:"",
		receiverAccount:"",
    });

    function handleChange(e) {
    if (e.target.name === "accountNumber") {
      setForm({
        ...form,
        [e.target.name]: e.target.value,
        senderAccount: e.target.value,
		receiverAccount: e.target.value,
      });
    } else {
      setForm({ ...form, [e.target.name]: e.target.value });
    }
  }

	function deposit() {
        setLoading(true);
        if (!form.accountNumber || !form.amount) {
            setError("Please fill all the fields");
            setLoading(false);
            return;
        }
		console.log(form)
		api.post("/transaction/deposit", form)
        .then((res) => {
            sessionStorage.setItem("token", res.data.message);
            setLoading(false);
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
								<label htmlFor="amount" className="col-auto col-form-label">
									Enter Amount to Deposit :{" "}
								</label>
								<div className="input-group col">
									<span className="input-group-text" id="basic-addon1">
										{"\u20B9"}
									</span>
									<input type="text" className="form-control  rounded-end" id="amount" placeholder="Amount" name="amount" value={form.amount} onChange={handleChange} />
								</div>
							</div>
							<p className="invalid-feedback d-block">{error}</p>
							<div className="d-grid  d-md-block text-center">
								<button type="button" onClick={deposit} className="btn btn-primary">
									Deposit
								</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	);
}

//onClick={() => console.log(form)}