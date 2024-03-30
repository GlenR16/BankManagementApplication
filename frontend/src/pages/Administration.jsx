export default function Administration() {
    // check user logged in and role

	return (
		<div className="m-5 p-5">
			<div className="m-5">
				<h1 className="text-center">Admin Dashboard</h1>
			</div>

			<div className="row row-cols-1 row-cols-md-3 g-5">
				<div className="col">
					<div className="card">
						<div className="card-body">
							<p className="card-title">Customers</p>
							<p className="card-text">Lorem ipsum dolor, sit amet consectetur adipisicing elit. Architecto consectetur accusamus a at iste impedit dolorum modi quisquam cupiditate magni corrupti esse dolores, placeat atque illum. Accusamus, consectetur. Minus, sapiente.</p>
							<a href="#" className="btn btn-primary">
								Customers
							</a>
						</div>
					</div>
				</div>
				<div className="col">
					<div className="card">
						<div className="card-body">
							<p className="card-title">Accounts</p>
							<p className="card-text">Lorem ipsum dolor sit amet consectetur adipisicing elit. Dolores commodi et a consectetur tempora veritatis placeat, blanditiis repudiandae magnam unde delectus itaque perspiciatis est alias molestias voluptates deserunt inventore enim!</p>
							<a href="#" className="btn btn-primary">
								Accounts
							</a>
						</div>
					</div>
				</div>
				<div className="col">
					<div className="card">
						<div className="card-body">
							<p className="card-title">Transactions</p>
							<p className="card-text">Lorem ipsum dolor, sit amet consectetur adipisicing elit. Itaque, deserunt blanditiis reiciendis animi quo ut totam inventore voluptatibus nulla asperiores sunt quisquam tempora at fugiat! Fugit explicabo vitae obcaecati numquam!</p>
							<a href="#" className="btn btn-primary">
								Transactions
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	);
}
