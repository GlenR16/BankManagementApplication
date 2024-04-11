import React, { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { NavLink } from "react-router-dom";

export default function AdminCustomers() {
	const [transactions, setTransactions] = useState([]);
	const [beneficaries, setBeneficaries] = useState([]);
	const [accounts, setAccounts] = useState([]);
    const [account, setAccount] = useState("");
	const api = useAxiosAuth();

	function handleChange(e) {
		setAccount(e.target.value);
	}

	useEffect(() => {
		api.get("/account/list").then((response) => {
			setAccounts(response.data);
		});
	}, []);

	useEffect(() => {
		api.get("/transaction/account/" + account).then((response) => {
			setTransactions(response.data);
		});
		api.get("/account/beneficiary/list").then((response) => {
			setBeneficaries(response.data);
		});
	}, [account]);

	function checkType(transaction) {
		if (transaction.typeId == 1) {
			return "Account Transfer";
		} else if (transaction.typeId == 2) {
			return "Withdraw";
		} else if (transaction.typeId == 3) {
			return "Deposit";
		} else if (transaction.typeId == 4) {
			return "Card Transfer";
		}
	}

	return (
		<div className="container-fluid col-sm-12 col-md-8 my-4">
			<div className="card mt-1 border-0 shadow">
				<div className="card-body table-responsive">
					<table className="table caption-top">
						<caption className="text-center border-bottom border-2 border-dark">
							<h3 className="d-flex flex-row align-items-center justify-content-between gap-2 text-black mx-2">
								<div className="d-flex flex-row align-items-center gap-2 text-black">Transactions</div>
								<div className="w-25">
									<select className="form-select" id="accountNumber" name="accountNumber" onChange={handleChange}>
										<option value="">Select an Account</option>
										{accounts.length > 0 ? (
											accounts.map((account) => {
												return (
													<option key={account.accountNumber} value={account.accountNumber}>
														{account.accountNumber}
													</option>
												);
											})
										) : (
											<option>No Accounts</option>
										)}
									</select>
									<div className="form-text fs-6">
										Account Balance : ₹ {accounts.find((x) => x.accountNumber == account)?.balance ?? 0}
									</div>
								</div>
							</h3>
						</caption>
						<thead>
							<tr>
								<th scope="col">ID</th>
								<th scope="col">Account</th>
								<th scope="col">Card Number</th>
								<th scope="col">Beneficiary Number</th>
								<th scope="col">Amount</th>
								<th scope="col">Balance</th>
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
										<td>{transaction.id}</td>
										<td>{transaction.accountNumber}</td>
										<td>{transaction.cardNumber}</td>
										<td>{beneficaries.find((x) => x.id == transaction.beneficiaryId)?.recieverNumber ?? "-"}</td>
										<td>{transaction.debit != 0 ? 
                                        <div className="text-danger" >- {transaction.debit}</div>
                                        : 
                                        <div className="text-success" >+ {transaction.credit}</div>
                                        }</td>
                                        <td>
                                            {transaction.balance}
                                        </td>
										<td>{checkType(transaction)}</td>
										<td>{transaction.createdAt.substring(0, 10)}</td>
										<td>{new Date(Date.parse(transaction.createdAt))?.toLocaleTimeString()}</td>
										<td>
                                            {
                                                transaction.status == "COMPLETED" ?
                                                <span className="badge rounded-pill text-bg-success">Success</span>
                                                :
                                                <span className="badge rounded-pill text-bg-danger">Failed</span>
                                            }    
                                        </td>
									</tr>
								))
							) : (
								<tr>
									<td colSpan="10">No Transactions found</td>
								</tr>
							)}
						</tbody>
					</table>
				</div>
			</div>
		</div>
	);
}
