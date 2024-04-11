import React, { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { useUser } from "../contexts/UserContext";
import { useNavigate } from "react-router-dom";

export default function AdminBranches() {
	const api = useAxiosAuth();
	const { user } = useUser();
    const navigate = useNavigate();

    const [branches, setBranches] = useState([]);

	useEffect(() => {
        console.log(user);
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
							<h3 className="d-flex flex-row align-items-center gap-2 text-black fw-bold">All Branches</h3>
						</caption>
						<thead>
							<tr>
								<th scope="col">ID</th>
								<th scope="col">Name</th>
								<th scope="col">Address</th>
                                <th scope="col">IFSC</th>
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
