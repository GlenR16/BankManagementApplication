export default function Administration() {
    // check user logged in and role

	return (
		<div class="m-5 p-5">
			<div class="m-5">
				<h1 class="text-center">Admin Dashboard</h1>
			</div>

			<div class="row row-cols-1 row-cols-md-3 g-5">
				<div class="col">
					<div class="card">
						<div class="card-body">
							<p class="card-title">Customers</p>
							<p class="card-text">Lorem ipsum dolor, sit amet consectetur adipisicing elit. Architecto consectetur accusamus a at iste impedit dolorum modi quisquam cupiditate magni corrupti esse dolores, placeat atque illum. Accusamus, consectetur. Minus, sapiente.</p>
							<a href="#" class="btn btn-primary">
								Customers
							</a>
						</div>
					</div>
				</div>
				<div class="col">
					<div class="card">
						<div class="card-body">
							<p class="card-title">Accounts</p>
							<p class="card-text">Lorem ipsum dolor sit amet consectetur adipisicing elit. Dolores commodi et a consectetur tempora veritatis placeat, blanditiis repudiandae magnam unde delectus itaque perspiciatis est alias molestias voluptates deserunt inventore enim!</p>
							<a href="#" class="btn btn-primary">
								Accounts
							</a>
						</div>
					</div>
				</div>
				<div class="col">
					<div class="card">
						<div class="card-body">
							<p class="card-title">Transactions</p>
							<p class="card-text">Lorem ipsum dolor, sit amet consectetur adipisicing elit. Itaque, deserunt blanditiis reiciendis animi quo ut totam inventore voluptatibus nulla asperiores sunt quisquam tempora at fugiat! Fugit explicabo vitae obcaecati numquam!</p>
							<a href="#" class="btn btn-primary">
								Transactions
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	);
}
