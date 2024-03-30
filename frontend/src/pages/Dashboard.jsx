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

	function withdraw()
	{
		console.log("GOING TO WITHDRAW PAGE");
		navigate("/withdraw");
	}

	function deposit()
	{
		console.log("GOING TO DEPOST PAGE");
		navigate("/deposit");
	}

	function transfer()
	{
		console.log("GOING TO TRANSFER PAGE");
		navigate("/transfer");
	}
	
	function account()
	{
		console.log("GOING TO ACCOUNT PAGE");
		navigate("/account");
	}
	
	function transactions()
	{
		console.log("GOING TO TRANSACTIONS PAGE");
		navigate("/transactions");
	}
	return (
		<div className="m-5 p-5">
			<div className="m-5">
				<div className="row row-cols-1 row-cols-md-3 g-4">
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Withdraw</p>
								<p className="card-text">Withdraw money: Transfer funds from your account to access cash.</p>
								<button className="btn btn-primary" type="button" onClick={withdraw}>
									Withdraw
								</button>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Deposit</p>
								<p className="card-text">Deposit money: Transfer funds to your account to make payments.</p>
								<button className="btn btn-primary" type="button" onClick={deposit}>
									Deposit
								</button>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Transfer</p>
								<p className="card-text">Transfer money: Transfer funds to another account directly.</p>
								<button className="btn btn-primary" type="button" onClick={transfer}>
									Transfer
								</button>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Account</p>
								<p className="card-text">Account Details: View your account details and statements.</p>
								<button className="btn btn-primary" type="button" onClick={account}>
									Account
								</button>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Account Balance</p>
								<p className="card-text">Account Balance: View your account balance amount here.</p>
								<button className="btn btn-primary" type="button">
									Balance
								</button>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Transaction History</p>
								<p className="card-text">View your transaction history of previous transactions.</p>
								<button className="btn btn-primary" type="button" onClick={transactions}>
									Transactions
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	);
}
