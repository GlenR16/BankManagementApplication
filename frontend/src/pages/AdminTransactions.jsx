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

	return (
		<div className="mt-5 p-4">
			<div className="card mt-4 border-0 shadow">
				<div className="card-body table-responsive">
					<table className="table caption-top">
						<caption className="text-center border-bottom border-2 border-dark">
							<h3 className="d-flex flex-row align-items-center gap-2 text-black">
								All Transactions
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
                                <th scope="col">ID</th>
                                <th scope="col">Sender Account</th>
                                <th scope="col">Sender Card Id</th>
                                <th scope="col">Receiver Account</th>
                                <th scope="col">Amount</th>
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
                                        <td>{beneficaries[transaction.beneficiaryId]?.accountNumber}</td>
                                        <td>{transaction.amount}</td>
                                        <td>{transaction.createdAt.substring(0,10)}</td>
                                        <td>{transaction.createdAt.substring(11,19).replace("T"," ")}</td>
                                        <td>{transaction.status}</td>
										<td>
											<NavLink to="#" className="text-decoration-none">
												View
											</NavLink>
										</td>
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
