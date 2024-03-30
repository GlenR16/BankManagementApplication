import useAxiosAuth from "../contexts/Axios";

export default function Dashboard() {
	const api = useAxiosAuth();
    // check user logged in
	return (
		<div class="m-5 p-5">
			<form class="d-flex my-2" role="search">
				<input class="form-control me-2" type="search" placeholder="Search" aria-label="Search" />
				<button class="btn btn-outline-success" type="submit">
					Search
				</button>
			</form>

			<div class="m-5">
				<div class="row row-cols-1 row-cols-md-3 g-4">
					<div class="col">
						<div class="card">
							<div class="card-body">
								<p class="card-title">Withdraw</p>
								<p class="card-text">Withdraw money: Transfer funds from your account to access cash.</p>
								<a href="./withdraw.html" class="btn btn-primary">
									Withdraw
								</a>
							</div>
						</div>
					</div>
					<div class="col">
						<div class="card">
							<div class="card-body">
								<p class="card-title">Deposit</p>
								<p class="card-text">Deposit money: Transfer funds to your account to make payments.</p>
								<a href="./deposit.html" class="btn btn-primary">
									Deposit
								</a>
							</div>
						</div>
					</div>
					<div class="col">
						<div class="card">
							<div class="card-body">
								<p class="card-title">Transfer</p>
								<p class="card-text">Transfer money: Transfer funds to another account directly.</p>
								<a href="./transfer.html" class="btn btn-primary">
									Transfer
								</a>
							</div>
						</div>
					</div>
					<div class="col">
						<div class="card">
							<div class="card-body">
								<p class="card-title">Account</p>
								<p class="card-text">Account Details: View your account details and statements.</p>
								<a href="./account.html" class="btn btn-primary">
									Account
								</a>
							</div>
						</div>
					</div>
					<div class="col">
						<div class="card">
							<div class="card-body">
								<p class="card-title">Account Balance</p>
								<p class="card-text">Account Balance: View your account balance amount here.</p>
								<a href="#" class="btn btn-primary">
									Balance
								</a>
							</div>
						</div>
					</div>
					<div class="col">
						<div class="card">
							<div class="card-body">
								<p class="card-title">Transaction History</p>
								<p class="card-text">View your transaction history of previous transactions.</p>
								<a href="./transactions.html" class="btn btn-primary">
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
