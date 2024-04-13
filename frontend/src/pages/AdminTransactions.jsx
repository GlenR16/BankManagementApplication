import React, { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { useUser } from "../contexts/UserContext";

export default function AdminTransactions() {
	const api = useAxiosAuth();
	const { user } = useUser();

	const [transactions, setTransactions] = useState([]);
	const [beneficaries, setBeneficaries] = useState([]);
	const [page, setPage] = useState(0);

	useEffect(() => {
		if (user == null) navigate("/login");
		else if (user.role == "USER") navigate("/dashboard");
	}, [user]);

	useEffect(() => {
		api.get("/transaction?page=" + page).then((response) => {
			setTransactions(response.data);
		});
	}, [page]);

	useEffect(() => {
		api.get("/account/beneficiary").then((response) => {
			setBeneficaries(response.data);
		});
	}, []);

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
			<div className="card mt-4 border-0 shadow">
				<div className="card-body table-responsive">
					<table className="table caption-top">
						<caption className="text-center border-bottom border-2 border-dark">
							<h3 className="d-flex flex-row align-items-center gap-2 text-black fw-bold">All Transactions</h3>
						</caption>
						<thead>
							<tr>
								<th scope="col">ID</th>
								<th scope="col">Account</th>
								<th scope="col">Card Number</th>
								<th scope="col">Receiver Account</th>
								<th scope="col">Amount</th>
								<th scope="col">Balance</th>
								<th scope="col">Type</th>
								<th scope="col">Date</th>
								<th scope="col">Status</th>
							</tr>
						</thead>
						<tbody>
							{transactions?.content?.length > 0 ? (
								transactions?.content?.map((transaction, index) => (
									<tr key={index}>
										<td>{transaction.id}</td>
										<td>{transaction.accountNumber}</td>
										<td>{transaction.cardNumber ? transaction.cardNumber : "-"}</td>
										<td>{beneficaries.find((x) => x.id === transaction.beneficiaryId)?.recieverNumber ?? "-"}</td>
										<td>{transaction.debit !== 0 ? <div className="text-danger">- {Number.parseFloat(transaction.debit).toFixed(2)}</div> : <div className="text-success">+ {Number.parseFloat(transaction.credit).toFixed(2)}</div>}</td>
										<td> {transaction.balance} </td>
										<td>{checkType(transaction)}</td>
										<td>{new Date(transaction.createdAt).toLocaleString()}</td>
										<td>{transaction.status === "COMPLETED" ? <span className="badge rounded-pill text-bg-success">Success</span> : <span className="badge rounded-pill text-bg-danger">Failed</span>}</td>
									</tr>
								))
							) : (
								<tr>
									<td colSpan="10">No Transactions found</td>
								</tr>
							)}
						</tbody>
					</table>
					<nav aria-label="...">
						<ul className="pagination justify-content-end">
							<li className={`page-item ${transactions.first ? "disabled" : ""}`}>
								<button className="page-link" disabled={transactions.first} onClick={() => setPage(page - 1)}>
									Previous
								</button>
							</li>
							{[...Array(transactions.totalPages).keys()].map((pageNo) => (
								<li key={pageNo} className={`page-item ${pageNo == page ? "active" : ""}`}>
									<button className="page-link" onClick={() => setPage(pageNo)}>
										{pageNo + 1}
									</button>
								</li>
							))}
							<li className={`page-item ${transactions.last ? "disabled" : ""}`}>
								<button className="page-link" disabled={transactions.last} onClick={() => setPage(page + 1)}>Next</button>
							</li>
						</ul>
					</nav>
				</div>
			</div>
		</div>
	);
}
