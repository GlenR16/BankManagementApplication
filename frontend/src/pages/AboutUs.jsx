export default function AboutUs() {
	return (
		<div className="flex-grow-1 d-flex flex-column align-items-center m-5">
			<h3 className="fw-bold">About Us</h3>
			<div className="p-5">We are a team of 3 engineers at Wissen Technology. This is our internship project. We have built a banking application using Java Springboot and ReactJS. The application follows the microservices architecture. The application has 4 microservices: Account, User, Transaction, and Card. The Account microservice is responsible for managing the accounts of the users. The User microservice is responsible for managing the identity of the users. The Transaction microservice is responsible for managing the transactions of the users. The Card microservice is responsible for managing the cards of the users. The frontend is built using ReactJS. The frontend is responsible for interacting with the backend microservices.</div>
			<h3 className="fw-bold">Our team</h3>
            <div className="container mt-5">
				<div className="row gap-4">
					<div className="card col">
						<div className="card-body">
							<h5 className="card-title">Glen Rodrigues</h5>
							
						</div>
					</div>
					<div className="card col">
						<div className="card-body">
							<h5 className="card-title">Jayesh Bhatia</h5>
						</div>
					</div>
					<div className="card col">
						<div className="card-body">
							<h5 className="card-title">Vir Rao</h5>
							
						</div>
					</div>
				</div>
			</div>
		</div>
	);
}
