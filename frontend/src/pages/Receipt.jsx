import Alert from "../components/Alert";
import React, { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { useParams } from "react-router-dom";

export default function Reciept() {
	const api = useAxiosAuth();
	const { id } = useParams();
	const [transaction, setTransaction] = useState({});
	const [beneficiary, setBeneficiary] = useState({});
	const [alert, setAlert] = useState(null);

	useEffect(() => {
		api.get("/transaction/" + id)
			.then((response) => {
				console.log(response.data);
				setTransaction(response.data);
				setAlert({ message: "Transaction Successful", type: "success" });
                setTimeout(() => {
                    setAlert(null);
                }, 6000);
				api.get("/account/beneficiary/" + response.data.beneficiaryId).then((response) => {
					setBeneficiary(response.data);
				});
			})
			.catch((err) => {
				console.log(err.response);
			});
	}, []);

	return (
		<>
			<Alert alert={alert} />
			<div className="container-fluid col-sm-12 col-md-8 my-4">
				<div className="card gap-2">
					<h5 className="card-header p-3 text-center fw-bold">Transaction Reciept</h5>
					<div className="row mx-2 mt-3">
						<div className="col-sm-6 col-md-2 fw-bold"> Transaction ID</div>
						<div className=" col"> {transaction.id}</div>
					</div>
					<div className="row mx-2">
						<div className="col-sm-6 col-md-2 fw-bold"> Account</div>
						<div className=" col"> {transaction.accountNumber}</div>
					</div>
					{transaction.typeId == 1 || transaction.typeId == 4 ? (
						<div className="row mx-2">
							<div className="col-sm-6 col-md-2 fw-bold"> Reciever Account</div>
							<div className=" col"> {beneficiary.recieverNumber}</div>
						</div>
					) : (
						""
					)}
					<div className="row mx-2">
						<div className="col-sm-6 col-md-2 fw-bold"> Balance</div>
						<div className=" col"> {transaction.balance}</div>
					</div>
					<div className="row mx-2">
						<div className="col-sm-6 col-md-2 fw-bold">{transaction.debit != 0 ? "Debit" : "Credit"}</div>
						<div className=" col">{transaction.debit != 0 ? transaction.debit : transaction.credit}</div>
					</div>
					<div className="row mx-2">
						<div className="col-sm-6 col-md-2 fw-bold">Date</div>
						<div className=" col"> {transaction.createdAt?.substring(0, 10)} </div>
					</div>
					<div className="row mx-2">
						<div className="col-sm-6 col-md-2 fw-bold"> Time </div>
						<div className=" col"> {new Date(Date.parse(transaction.createdAt))?.toLocaleTimeString()} </div>
					</div>
					<div className="row mx-2 mb-3">
						<div className="col-sm-6 col-md-2 fw-bold">Status </div>
						<div className=" col">{transaction.status == "COMPLETED" ? <span className="badge rounded-pill text-bg-success">Success</span> : <span className="badge rounded-pill text-bg-danger">Failed</span>}</div>
					</div>
				</div>
			</div>
		</>
	);
}
