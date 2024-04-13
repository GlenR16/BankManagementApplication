import React, { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { useUser } from "../contexts/UserContext";
import { NavLink, useNavigate } from "react-router-dom";

export default function AdminCards() {
	const api = useAxiosAuth();
    const { user } = useUser();
    const navigate = useNavigate();
    
    const [cards , setCards] = useState({});
    const [page, setPage] = useState(0);

    useEffect(() => {
        if (user == null) navigate("/login");
        else if (user.role == "USER") navigate("/dashboard");
    },[user]);

    useEffect(() => {
        api.get("/card?page="+page)
        .then((response) => {
            setCards(response.data);
        });
    }, [page]);

    return (
        <div className="container-fluid col-sm-12 col-md-8 my-4">
			<div className="card mt-4 border-0 shadow">
				<div className="card-body table-responsive">
					<table className="table caption-top text-center">
						<caption className="text-center border-bottom border-2 border-dark">
							<h3 className="d-flex flex-row align-items-center gap-2 text-black fw-bold">All Cards</h3>
						</caption>
						<thead>
							<tr>
								<th scope="col">ID</th>
								<th scope="col">Number</th>
								<th scope="col">Account</th>
								<th scope="col">Type</th>
								<th scope="col">Verification Status</th>
								<th scope="col">Status</th>
                                <th scope="col">Action</th>
							</tr>
						</thead>
						<tbody>
							{cards?.content?.length > 0 ? (
								cards?.content?.map((card, index) => (
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
                                            {
                                                card.verified ?
                                                <span className="badge rounded-pill bg-success">Verified</span>
                                                :
                                                <span className="badge rounded-pill bg-warning">Not Verified</span>
                                            }
                                        </td>
                                        <td>
                                            {
                                                (card.locked && card.deleted) || (card.deleted) ?
                                                <span className="badge rounded-pill text-bg-danger">Deleted</span>
                                                : 
                                                ""
                                            }
                                            {
                                                card.locked && !card.deleted ?
                                                <span className="badge rounded-pill bg-secondary">Locked</span>
                                                : 
                                                ""
                                            }
                                            {
                                                !card.locked && !card.deleted ?
                                                <span className="badge rounded-pill bg-success">Active</span>
                                                : 
                                                ""
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
									<td colSpan="5">No cards found</td>
								</tr>
							)}
						</tbody>
					</table>
                    <nav aria-label="...">
						<ul className="pagination justify-content-end">
							<li className={`page-item ${cards.first ? "disabled" : ""}`}>
								<button className="page-link" disabled={cards.first} onClick={() => setPage(page - 1)}>
									Previous
								</button>
							</li>
							{[...Array(cards.totalPages).keys()].map((pageNo) => (
								<li key={pageNo} className={`page-item ${pageNo == page ? "active" : ""}`}>
									<button className="page-link" onClick={() => setPage(pageNo)}>
										{pageNo + 1}
									</button>
								</li>
							))}
							<li className={`page-item ${cards.last ? "disabled" : ""}`}>
								<button className="page-link" disabled={cards.last} onClick={() => setPage(page + 1)}>Next</button>
							</li>
						</ul>
					</nav>
				</div>
			</div>
		</div>
    );
}
