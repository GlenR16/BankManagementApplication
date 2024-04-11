import React, { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { useUser } from "../contexts/UserContext";
import { NavLink, useNavigate } from "react-router-dom";

export default function AdminCards() {
	const api = useAxiosAuth();
    const { user } = useUser();
    const navigate = useNavigate();

    const [cards , setCards] = useState([]);

    useEffect(() => {
        if (user == null) navigate("/login");
        else if (user.role == "USER") navigate("/dashboard");
    },[user]);

    useEffect(() => {
        api.get("/card")
        .then((response) => {
            setCards(response.data);
        });
    }, []);

    return (
        <div className="container-fluid col-sm-12 col-md-8 my-4">
			<div className="card mt-4 border-0 shadow">
				<div className="card-body table-responsive">
					<table className="table caption-top">
						<caption className="text-center border-bottom border-2 border-dark">
							<h3 className="d-flex flex-row align-items-center gap-2 text-black fw-bold">All Cards</h3>
						</caption>
						<thead>
							<tr>
								<th scope="col">ID</th>
								<th scope="col">Number</th>
								<th scope="col">Account</th>
								<th scope="col">Type</th>
                                <th scope="col">Action</th>
							</tr>
						</thead>
						<tbody>
							{cards.length > 0 ? (
								cards.map((card, index) => (
									<tr key={index}>
										<td>{card.id}</td>
										<td>{card.number}</td>
										<td>{card.accountNumber}</td>
                                        <td>
                                            {
                                                card.typeId == 1 ?
                                                "Debit"
                                                :
                                                "Credit"
                                            }
                                        </td>
										<td>
                                            <NavLink to={"/cardDetails/"+card.number} >
                                                View
                                            </NavLink>
                                        </td>
									</tr>
								))
							) : (
								<tr>
									<td colSpan="5">No branches found</td>
								</tr>
							)}
						</tbody>
					</table>
				</div>
			</div>
		</div>
    );
}
