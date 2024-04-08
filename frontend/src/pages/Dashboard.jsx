import { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { NavLink, useNavigate } from "react-router-dom";

export default function Dashboard() {
	const [accounts, setAccounts] = useState([]);
    const [accountTypes, setAccountTypes] = useState([]);
    const [branches, setBranches] = useState([]);
    const [user, setUser] = useState({});

	const api = useAxiosAuth();
	const navigate = useNavigate();
    
    
	useEffect(() => {
        api.get("/user/details")
        .then((response) => {
            setUser(response.data);
        })
        .catch((error) => {
            return navigate("/login");
        });
		
	}, []);


	function withdraw() {
		console.log("GOING TO WITHDRAW PAGE");
		navigate("/withdraw");
	}

	function deposit() {
		console.log("GOING TO DEPOST PAGE");
		navigate("/deposit");
	}

	function transfer() {
		console.log("GOING TO TRANSFER PAGE");
		navigate("/transfer");
	}

	function account() {
		console.log("GOING TO ACCOUNT PAGE");
		navigate("/account");
	}

	function card() {
		console.log("GOING TO CARD PAGE");
		navigate("/card");
	}

	function transactions() {
		console.log("GOING TO TRANSACTIONS PAGE");
		navigate("/transactions");
	}

	return (
		<div className="container col-sm-12 col-md-8 p-5">
			<div className="row flex-lg-row-reverse align-items-center justify-content-center g-5 py-0 text-center">
				
				<div className="row row-cols-1 row-cols-md-3 g-4">
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Withdraw</p>
								<img src="/withdraw.png" alt="Withdraw Image" width={100}/>
								<p className="card-text">Withdraw money: Transfer funds from your account to access cash.</p>
								<button className="btn btn-success" type="button" onClick={withdraw}>
									Withdraw
								</button>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Deposit</p>
								<img src="/deposit.png" alt="Deposit Image" width={100}/>
								<p className="card-text">Deposit money: Transfer funds to your account to make payments.</p>
								<button className="btn btn-success" type="button" onClick={deposit}>
									Deposit
								</button>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Transfer</p>
								<img src="/transactions.png" alt="Transfer Image" width={100}/>
								<p className="card-text">Transfer money: Transfer funds to another account directly.</p>
								<button className="btn btn-success" type="button" onClick={transfer}>
									Transfer
								</button>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Accounts</p>
								<img src="/accounts.png" alt="Accounts Image" width={100}/>
								<p className="card-text">Account Details: View your account details and statements.</p>
								<button className="btn btn-success" type="button" onClick={account}>
									Accounts
								</button>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Card Transfer</p>
								<img src="/card.png" alt="Cards Image" width={100} height={100}/>
								<p className="card-text">Card Transfer: Transfer funds to a diffrent account using your card.</p>
								<button className="btn btn-success" type="button" onClick={card}> 
									Transfer
								</button>
							</div>
						</div>
					</div>
					<div className="col">
						<div className="card">
							<div className="card-body">
								<p className="card-title">Transaction History</p>
								<img src="/customers.png" alt="Transaction Image" width={100}/>
								<p className="card-text">View your transaction history of previous transactions.</p>
								<button className="btn btn-success" type="button" onClick={transactions}>
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
