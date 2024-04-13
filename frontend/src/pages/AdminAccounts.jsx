import React, { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { NavLink } from "react-router-dom";
import { useUser } from "../contexts/UserContext";

export default function AdminAccounts() {
    const api = useAxiosAuth();
    const { user } = useUser();

    const [accounts, setAccounts] = useState([]);
    const [accountTypes, setAccountTypes] = useState([]);
    const [branches, setBranches] = useState([]);
    const [page, setPage] = useState(0);

    useEffect(() => {
        if (user == null) navigate("/login");
        else if (user.role == "USER") navigate("/dashboard");
    },[user]);

    useEffect(() => {
        api.get("/account?page="+page)
        .then((response) => {
            setAccounts(response.data);
        });
        api.get("/account/type")
        .then((response) => {
            setAccountTypes(response.data);
        });
        api.get("/account/branch")
        .then((response) => {
            setBranches(response.data.content);
        });
    }, []);

	return (
		<div className="container-fluid col-sm-12 col-md-8 my-4">
			<div className="card mt-4 border-0 shadow">
				<div className="card-body table-responsive">
					<table className="table caption-top text-center">
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
								<th scope="col">Account Status</th>
								<th scope="col">Verified Status</th>
								<th scope="col">Action</th>
							</tr>
						</thead>
						<tbody>
							{accounts?.content?.length > 0 ? (
								accounts?.content?.map((account, index) => (
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
                                            {
                                                account.verified ?
                                                <span className="badge rounded-pill bg-success">Verified</span>
                                                :
                                                <span className="badge rounded-pill bg-danger">Not Verified</span>
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
									<td colSpan="9">No accounts found</td>
								</tr>
							)}
						</tbody>
					</table>
                    <nav aria-label="...">
						<ul className="pagination justify-content-end">
							<li className={`page-item ${accounts.first ? "disabled" : ""}`}>
								<button className="page-link" disabled={accounts.first} onClick={() => setPage(page - 1)}>
									Previous
								</button>
							</li>
							{[...Array(accounts.totalPages).keys()].map((pageNo) => (
								<li key={pageNo} className={`page-item ${pageNo == page ? "active" : ""}`}>
									<button className="page-link" onClick={() => setPage(pageNo)}>
										{pageNo + 1}
									</button>
								</li>
							))}
							<li className={`page-item ${accounts.last ? "disabled" : ""}`}>
								<button className="page-link" disabled={accounts.last} onClick={() => setPage(page + 1)}>Next</button>
							</li>
						</ul>
					</nav>
				</div>
			</div>
		</div>
	);
}
