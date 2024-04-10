import React, { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { NavLink } from "react-router-dom";

export default function AdminAccounts() {
    const [accounts, setAccounts] = useState([]);
    const [accountTypes, setAccountTypes] = useState([]);
    const [branches, setBranches] = useState([]);
    const api = useAxiosAuth();

    useEffect(() => {
        api.get("/account")
        .then((response) => {
            setAccounts(response.data);
        });
        api.get("/account/type")
        .then((response) => {
            setAccountTypes(response.data);
        });
        api.get("account/branch")
        .then((response) => {
            setBranches(response.data);
        });
    }, []);

	return (
		<div className="container-fluid col-sm-12 col-md-8 my-4">
			<div className="card mt-4 border-0 shadow">
				<div className="card-body table-responsive">
					<table className="table caption-top">
						<caption className="text-center border-bottom border-2 border-dark">
							<h3 className="d-flex flex-row align-items-center gap-2 text-black fw-bold">
								All Accounts
							</h3>
						</caption>
						<thead>
							<tr>
								<th scope="col">Sr. No</th>
                                <th scope="col">CustomerId</th>
								<th scope="col">Account Number</th>
								<th scope="col">IFSC</th>
								<th scope="col">Type</th>
								<th scope="col">Balance</th>
								<th scope="col">Status</th>
								<th scope="col">Action</th>
							</tr>
						</thead>
						<tbody>
							{accounts.length > 0 ? (
								accounts.map((account, index) => (
									<tr key={index}>
										<td>{index+1}</td>
                                        <td>{account.customerId}</td>
										<td>{account.accountNumber}</td>
										<td>{branches[account.branchId - 1]?.ifsc}</td>
										<td>{accountTypes[account.typeId - 1]?.name}</td>
                                        <td>₹ {account.balance}</td>
										<td>
                                            {
                                                (account.locked && account.deleted) || (account.deleted) ?
                                                <span className="badge rounded-pill text-bg-danger">Deleted</span>
                                                : 
                                                ""
                                            }
                                            {
                                                account.locked && !account.deleted ?
                                                <span className="badge rounded-pill bg-secondary">Locked</span>
                                                : 
                                                ""
                                            }
                                            {
                                                !account.locked && !account.deleted ?
                                                <span className="badge rounded-pill bg-success">Active</span>
                                                : 
                                                ""
                                            }
                                        </td>
										<td>
											{/* TO CHANGE THE LINK */}
											<NavLink to={"/accountDetails/"+account.accountNumber} className="text-decoration-none">
												View
											</NavLink>
										</td>
									</tr>
								))
							) : (
								<tr>
									<td colSpan="5">No accounts found</td>
								</tr>
							)}
						</tbody>
					</table>
				</div>
			</div>
		</div>
	);
}
