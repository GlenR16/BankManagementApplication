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
		<div className="mt-5 p-4">
			<div className="card mt-4 border-0 shadow">
				<div className="card-body table-responsive">
					<table className="table caption-top">
						<caption className="text-center border-bottom border-2 border-dark">
							<h3 className="d-flex flex-row align-items-center gap-2 text-black">
								All Customers
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
                                <th scope="col">Sr.No</th>
                                <th scope="col">Customer ID</th>
								<th scope="col">Name</th>
								<th scope="col">Gender</th>
                                <th scope="col">Pin</th>
								<th scope="col">City</th>
								<th scope="col">State</th>
								<th scope="col">Action</th>
							</tr>
						</thead>
						<tbody>
							{users.length > 0 ? (
								users.map((user, index) => (
									<tr key={index}>
                                        <td>{user.id}</td>
                                        <td>{user.customerId}</td>
                                        <td>{user.name}</td>
                                        <td>{user.gender}</td>
                                        <td>{user.pincode}</td>
                                        <td>{user.city}</td>
                                        <td>{user.state}</td>
										<td>
											<NavLink to="#" className="text-decoration-none">
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
