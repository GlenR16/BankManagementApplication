import { useEffect } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { useUser } from "../contexts/UserContext";

export default function Administration() {
	const navigate = useNavigate();
    const { user } = useUser();

    useEffect(() => {
        if (user == null) navigate("/login");
        else if (user.role == "USER") navigate("/dashboard");
    },[user]);


	return (
		<div className="container-fluid col-sm-12 col-md-8 my-4">
			<div className="row flex-lg-row-reverse align-items-center justify-content-center text-center">
				<div className="row row-cols-1 row-cols-lg-2 row-cols-xl-3 g-4 mt-0">
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title fs-3">Customers</p>
								<img className="card-text py-2" src="/customers.png" alt="Index Image" width={100} height={100} />
								<p className="card-text">
                                    View and manage all customers.
                                </p>
                                <NavLink className="btn btn-primary" to="/AdminCustomers" >
                                    Customers
                                </NavLink>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title fs-3">Accounts</p>
								<img className="card-text py-2" src="/accounts.png" alt="Index Image" width={100} height={100} />
								<p className="card-text">
                                    View and manage all user accounts.
                                </p>
                                <NavLink className="btn btn-primary" to="/AdminAccounts" >
                                    Accounts
                                </NavLink>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title fs-3">Transactions</p>
								<img className="card-text py-2" src="/transactions.png" alt="Index Image" width={100} height={100} />
								<p className="card-text">
                                    View and analyze all transactions.
                                </p>
								<NavLink className="btn btn-primary" to="/AdminTransactions" >
                                    Transactions
                                </NavLink>
							</div>
						</div>
					</div>
                    <div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title fs-3">Branches</p>
								<img className="card-text py-2" src="/branch.png" alt="Index Image" width={100} height={100} />
								<p className="card-text">
                                    View and edit branches.
                                </p>
								<NavLink className="btn btn-primary" to="/AdminBranches" >
                                    Branches
                                </NavLink>
							</div>
						</div>
					</div>
                    <div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title fs-3">Cards</p>
								<img className="card-text py-2" src="/card.png" alt="Index Image" width={100} height={100} />
								<p className="card-text">
                                    View and verify all cards.
                                </p>
								<NavLink className="btn btn-primary" to="/AdminCards" >
                                    Cards
                                </NavLink>
							</div>
						</div>
					</div>

					
				</div>
			</div>
		</div>
	);
}
