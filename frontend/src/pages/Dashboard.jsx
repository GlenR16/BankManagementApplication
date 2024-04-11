import { useEffect } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { useUser } from "../contexts/UserContext";

export default function Dashboard() {
	const navigate = useNavigate();
    const { user } = useUser();

	useEffect(() => {
        if (user == null) navigate("/login");
    },[user]);


	return (
		<div className="container-fluid col-sm-12 col-md-8 my-4">
			<div className="row flex-lg-row-reverse align-items-center justify-content-center text-center">
				<div className="row row-cols-1 row-cols-lg-2 row-cols-xl-3 g-4 mt-0">
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title fs-3">Withdraw</p>
								<img className="card-text py-2" src="/withdraw.png" alt="Withdraw Image" width={100} height={100} />
								<p className="card-text">
                                    Transfer funds from your account to access cash.
                                </p>
                                <NavLink className="btn btn-primary" to="/withdraw" >
									Withdraw
                                </NavLink>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title fs-3">Deposit</p>
								<img className="card-text py-2" src="/deposit.png" alt="Deposit Image" width={100} height={100} />
								<p className="card-text">
                                    Transfer funds to your account to make payments.
                                </p>
								<NavLink className="btn btn-primary" to="/deposit" >
									Deposit
                                </NavLink>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title fs-3">Transfer</p>
								<img className="card-text py-2" src="/transactions.png" alt="Transfer Image" width={100} height={100} />
								<p className="card-text">
                                    Transfer funds to another account directly.
                                </p>
								<NavLink className="btn btn-primary" to="/transfer" >
									Transfer
                                </NavLink>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title fs-3">Accounts</p>
								<img className="card-text py-2" src="/accounts.png" alt="Accounts Image" width={100} height={100}/>
								<p className="card-text">
                                    View your account details and statements.
                                </p>
								<NavLink className="btn btn-primary" to="/account" >
									Accounts
                                </NavLink>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title fs-3">Card Transfer</p>
								<img className="card-text py-2" src="/card.png" alt="Cards Image" width={100} height={100} />
								<p className="card-text">
                                    Transfer funds to a diffrent account using your card.
                                </p>
								<NavLink className="btn btn-primary" to="/card" >
									Card Transfer
                                </NavLink>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title fs-3">Transaction History</p>
								<img className="card-text py-2" src="/history.png" alt="Transaction Image" width={100} height={100} />
								<p className="card-text">View your transaction history of previous transactions.</p>
								<NavLink className="btn btn-primary" to="/transactions" >
									Transactions
                                </NavLink>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	);
}
