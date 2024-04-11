import React, { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { useUser } from "../contexts/UserContext";
import { NavLink, useNavigate } from "react-router-dom";

export default function AdminBranches() {
	const api = useAxiosAuth();
	const { user } = useUser();
	const navigate = useNavigate();

	const [branches, setBranches] = useState([]);

	useEffect(() => {
		if (user == null) navigate("/login");
		else if (user.role == "USER") navigate("/dashboard");
	}, [user]);

	useEffect(() => {
		api.get("/account/branch")
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
								All Branches
								<NavLink className="btn" to="/addBranch">
									<svg viewBox="0 0 24 24" width="32" height="32" fill="none" xmlns="http://www.w3.org/2000/svg">
										<g id="SVGRepo_bgCarrier" strokeWidth="0"></g>
										<g id="SVGRepo_tracerCarrier" strokeLinecap="round" strokeLinejoin="round"></g>
										<g id="SVGRepo_iconCarrier">
											{" "}
											<path d="M15 12L12 12M12 12L9 12M12 12L12 9M12 12L12 15" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path> <path d="M22 12C22 16.714 22 19.0711 20.5355 20.5355C19.0711 22 16.714 22 12 22C7.28595 22 4.92893 22 3.46447 20.5355C2 19.0711 2 16.714 2 12C2 7.28595 2 4.92893 3.46447 3.46447C4.92893 2 7.28595 2 12 2C16.714 2 19.0711 2 20.5355 3.46447C21.5093 4.43821 21.8356 5.80655 21.9449 8" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path>{" "}
										</g>
									</svg>
								</NavLink>
							</h3>
						</caption>
						<thead>
							<tr>
								<th scope="col">ID</th>
								<th scope="col">Name</th>
								<th scope="col">Address</th>
								<th scope="col">IFSC</th>
								<th scope="col">EDIT</th>
							</tr>
						</thead>
						<tbody>
							{branches.length > 0 ? (
								branches.map((branch, index) => (
									<tr key={index}>
										<td>{branch.id}</td>
										<td>{branch.name}</td>
										<td>{branch.address}</td>
										<td>{branch.ifsc}</td>
										<td>
											<NavLink to={`/addBranch/${branch.id}`}>
												<svg viewBox="0 0 24 24" width="32" height="32" fill="none" xmlns="http://www.w3.org/2000/svg">
													<g id="SVGRepo_bgCarrier" strokeWidth="0"></g>
													<g id="SVGRepo_tracerCarrier" strokeLinecap="round" strokeLinejoin="round"></g>
													<g id="SVGRepo_iconCarrier">
														{" "}
														<path d="M15 12L12 12M12 12L9 12M12 12L12 9M12 12L12 15" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path>{" "}
														<path d="M22 12C22 16.714 22 19.0711 20.5355 20.5355C19.0711 22 16.714 22 12 22C7.28595 22 4.92893 22 3.46447 20.5355C2 19.0711 2 16.714 2 12C2 7.28595 2 4.92893 3.46447 3.46447C4.92893 2 7.28595 2 12 2C16.714 2 19.0711 2 20.5355 3.46447C21.5093 4.43821 21.8356 5.80655 21.9449 8" stroke="#000000" strokeWidth="1.5" strokeLinecap="round"></path>{" "}
													</g>
												</svg>
											</NavLink>
										</td>
									</tr>
								))
							) : (
								<tr>
									<td colSpan="4">No branches found</td>
								</tr>
							)}
						</tbody>
					</table>
				</div>
			</div>
		</div>
	);
}
