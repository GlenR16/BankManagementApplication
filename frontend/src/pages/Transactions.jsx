import React, { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { NavLink } from "react-router-dom";

export default function AdminCustomers() {
    const [transactions, setTransactions] = useState([]);
	const [beneficaries, setBeneficaries] = useState([]);
	const [accounts, setAccounts] = useState([]);
    const api = useAxiosAuth();

    function handleChange(e) {
		setAccounts(e.target.value);
	}

    const [form, setForm] = useState({
		accountNumber: "",
		amount: "",
		senderAccount: "",
		receiverAccount: "",
		typeId: 2,
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
    
    useEffect(() => {
        api.get("/account/list")
			.then((response) => {
				setAccounts(response.data);
		});
    }, []);

	useEffect(() => {
		api.get("/transaction/account/"+form.accountNumber)
        .then((response) => {
            setTransactions(response.data);
        });
		api.get("/account/beneficiary/list")
        .then((response) => {
            setBeneficaries(response.data);
        });
	} , [form.accountNumber]);

	

    function checkType(transaction) {
        if(transaction.typeId == 1){
            return "Account Transfer";
        }
        else if (transaction.typeId == 2) {
            return "Withdraw";
        }
        else if (transaction.typeId == 3) {
            return "Deposit";
        }
        else if (transaction.typeId == 4) {
            return "Card Transfer";
        }
    }

	return (
		<>
        <div className="mt-4 px-4" >
								<div className="input-group" style={{width: '25%' , marginLeft: '39%'}}  >
									<label className="input-group-text" htmlFor="accountNumber">
										Account Number
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
								<div className="form-text" style={{marginLeft: '45%'}}>Account Balance : ₹ {accounts.length > 0 ? accounts.find((account) => account.accountNumber == form.accountNumber)?.balance : 0}</div>
							</div>
		<div className="mt-0 p-4">
			<div className="card mt-1 border-0 shadow">
				<div className="card-body table-responsive">
					<table className="table caption-top">
						<caption className="text-center border-bottom border-2 border-dark">
							<h3 className="d-flex flex-row align-items-center gap-2 text-black">
								Transactions
								<a className="btn">
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
                                <th scope="col">Index</th>
                                <th scope="col">Account</th>
                                <th scope="col">Card Id</th>
                                <th scope="col">Beneficiary Account</th>
                                <th scope="col">Amount</th>
                                <th scope="col">Type</th>
                                <th scope="col">Date</th>
                                <th scope="col">Time</th>
                                <th scope="col">Status</th>
							</tr>
						</thead>
						<tbody>
							{transactions.length > 0 ? (
								transactions.map((transaction, index) => (
									<tr key={index}>
                                        <td>{index+1}</td>
                                        <td>{transaction.accountNumber}</td>
                                        <td>{transaction.cardNumber}</td>
                                        <td>{beneficaries.find(x => x.id == transaction.beneficiaryId)?.recieverNumber }</td>
                                        <td>{transaction.debit != 0 ? transaction.debit : transaction.credit}</td>
										<td>{checkType(transaction)}</td>
                                        <td>{transaction.createdAt.substring(0,10)}</td>
                                        <td>{new Date(Date.parse(transaction.createdAt))?.toLocaleTimeString()}</td>
                                        <td>{transaction.status}</td>
									</tr>
								))
							) : (
								<tr>
									<td colSpan="5">No Transactions found</td>
								</tr>
							)}
						</tbody>
					</table>
				</div>
			</div>
		</div>
		</>
	);
}
