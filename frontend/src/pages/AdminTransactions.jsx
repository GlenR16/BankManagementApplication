import React, { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { NavLink } from "react-router-dom";

export default function AdminCustomers() {
    const [transactions, setTransactions] = useState([]);
	const [beneficaries, setBeneficaries] = useState([]);
    const api = useAxiosAuth();

    useEffect(() => {
        api.get("/transaction")
        .then((response) => {
            setTransactions(response.data);
        });
		api.get("/account/beneficiary")
        .then((response) => {
            setBeneficaries(response.data);
        });
    }, []);

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
		<div className="mt-0 p-4">
			<div className="card mt-4 border-0 shadow">
				<div className="card-body table-responsive">
					<table className="table caption-top">
						<caption className="text-center border-bottom border-2 border-dark">
							<h3 className="d-flex flex-row align-items-center gap-2 text-black">
								All Transactions
							</h3>
						</caption>
						<thead>
							<tr>
                                <th scope="col">ID</th>
                                <th scope="col">Sender Account</th>
                                <th scope="col">Sender Card Id</th>
                                <th scope="col">Receiver Account</th>
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
                                        <td>{transaction.id}</td>
                                        <td>{transaction.accountNumber}</td>
                                        <td>{transaction.cardNumber}</td>
                                        <td>{beneficaries[transaction.beneficiaryId-1]?.recieverNumber}</td>
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
	);
}
