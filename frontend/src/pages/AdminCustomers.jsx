import React, { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { NavLink } from "react-router-dom";

export default function AdminCustomers() {
    const [users, setUsers] = useState([]);
    const api = useAxiosAuth();

    useEffect(() => {
        api.get("/user")
        .then((response) => {
            setUsers(response.data);
        });
    }, []);

	return (
		<div className="container-fluid col-sm-12 col-md-8 my-4">
			<div className="card mt-4 border-0 shadow">
				<div className="card-body table-responsive">
					<table className="table caption-top">
						<caption className="text-center border-bottom border-2 border-dark">
							<h3 className="d-flex flex-row align-items-center gap-2 text-black fw-bold">
								All Customers
							</h3>
						</caption>
						<thead>
							<tr>
                                <th scope="col">Sr.No</th>
                                <th scope="col">Customer ID</th>
								<th scope="col">Name</th>
								<th scope="col">Email</th>
								<th scope="col">Status</th>
								<th scope="col">Action</th>
							</tr>
						</thead>
						<tbody>
							{users.length > 0 ? (
								users.map((user, index) => (
									<tr key={index}>
                                        <td>{index+1}</td>
                                        <td>{user.customerId}</td>
                                        <td>{user.name}</td>
                                        <td>{user.email}</td>
										<td>
                                            {
                                                (user.locked && user.deleted) || (user.deleted) ?
                                                <span className="badge rounded-pill text-bg-danger">Deleted</span>
                                                : 
                                                ""
                                            }
                                            {
                                                user.locked && !user.deleted ?
                                                <span className="badge rounded-pill bg-secondary">Locked</span>
                                                : 
                                                ""
                                            }
                                            {
                                                !user.locked && !user.deleted ?
                                                <span className="badge rounded-pill bg-success">Active</span>
                                                : 
                                                ""
                                            }
                                        </td>
										<td>
											<NavLink to={"/profile/"+user.customerId}className="text-decoration-none">
												View
											</NavLink>
										</td>
									</tr>
								))
							) : (
								<tr>
									<td colSpan="5">No users found</td>
								</tr>
							)}
						</tbody>
					</table>
				</div>
			</div>
		</div>
	);
}
