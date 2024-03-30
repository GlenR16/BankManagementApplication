import useAxiosAuth from "../contexts/Axios";
import { useNavigate } from "react-router-dom";

export default function Dashboard() {
	const api = useAxiosAuth();
    const navigate = useNavigate();

    api.get("/user/details")
    .then((response) => {
        console.log(response.data);
    })
    .catch((error) => {
        navigate("/login");
    });

	return (
		<div className="m-5 p-5">
			<div className="m-5">
				<div className="row row-cols-1 row-cols-md-3 g-4">
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Withdraw</p>
								<p className="card-text">Withdraw money: Transfer funds from your account to access cash.</p>
								<a href="./withdraw.html" className="btn btn-primary">
									Withdraw
								</a>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Deposit</p>
								<p className="card-text">Deposit money: Transfer funds to your account to make payments.</p>
								<a href="./deposit.html" className="btn btn-primary">
									Deposit
								</a>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Transfer</p>
								<p className="card-text">Transfer money: Transfer funds to another account directly.</p>
								<a href="./transfer.html" className="btn btn-primary">
									Transfer
								</a>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Account</p>
								<p className="card-text">Account Details: View your account details and statements.</p>
								<a href="./account.html" className="btn btn-primary">
									Account
								</a>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Account Balance</p>
								<p className="card-text">Account Balance: View your account balance amount here.</p>
								<a href="#" className="btn btn-primary">
									Balance
								</a>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Transaction History</p>
								<p className="card-text">View your transaction history of previous transactions.</p>
								<a href="./transactions.html" className="btn btn-primary">
									Transactions
								</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	);
}
