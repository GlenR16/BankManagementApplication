import React, { useState, useEffect } from "react";
import Alert from "../components/Alert";
import useAxiosAuth from "../contexts/Axios";
import { useParams } from "react-router-dom";
import { useUser } from "../contexts/UserContext";

export default function Account() {
    const api = useAxiosAuth();
    const { user } = useUser();

	const [account, setAccount] = useState({});
    const [ alert, setAlert ] = useState(null);
	const [accountTypes, setAccountTypes] = useState([]);
	const [branch, setBranch] = useState([]);
	const { AccountNumber } = useParams();

	useEffect(() => {
        refreshData()
		api.get("/account/" + AccountNumber).then((response) => {
            setAccount(response.data);
            api.get("account/branch").then((branchResponse) => {
                branchResponse.data.forEach((x) => {
                    if (x.id === response.data.branchId) {
                        setBranch(x);
                    }
                });
            });
            api.get("/account/type").then((typeResponse) => {
                setAccountTypes(typeResponse.data);
            });
        });
        
	}, []);

    function refreshData(){
        api.get("/account/" + AccountNumber).then((response) => {
			setAccount(response.data);
		});
    }

	function lock() {
		api.post("/account/lock/" + AccountNumber)
        .then((response) => {
			refreshData();
            setAlert({type: "success", message: "Account Locked Successfully"});
            setTimeout(() => {
                setAlert(null);
            }, 6000);
		})
        .catch((error) => {
            setAlert({type: "danger", message: error.response.data.message});
            setTimeout(() => {
                setAlert(null);
            }, 6000);
        });
	}

	function verify() {
		api.post("/account/verify/" + AccountNumber).then((response) => {
            refreshData();
            setAlert({type: "success", message: "Account Verified Successfully"});
            setTimeout(() => {
                setAlert(null);
            }, 6000);
		})
        .catch((error) => {
            setAlert({type: "danger", message: error.response.data.message});
            setTimeout(() => {
                setAlert(null);
            }, 6000);
        });
	}

	function deleteAcc() {
		api.delete("/account/" + AccountNumber).then((response) => {
            refreshData();
            setAlert({type: "success", message: "Account Deleted Successfully"});
            setTimeout(() => {
                setAlert(null);
            }, 6000);
		})
        .catch((error) => {
            setAlert({type: "danger", message: error.response.data.message});
            setTimeout(() => {
                setAlert(null);
            }, 6000);
        });
	}
	return (
		<>
			<Alert alert={alert} />
			<div className="container-fluid col-sm-12 col-md-8 my-4">
                <div className="card">
                    <h5 className="card-header p-3 text-center fw-bold">Account Details</h5>
                    <div className="card-body m-2">
                        <div className="row">
                            <div className="col-sm-12 col-md-6">
                                <div className="row my-2">
                                    <div className="col-6 fw-bold"> Account Number </div>
                                    <div className=" col"> {account.accountNumber}  </div>
                                </div>
                                <div className="row my-2">
                                    <div className="col-6 fw-bold">Customer ID </div>
                                    <div className=" col"> {account.customerId}  </div>
                                </div>
                                <div className="row my-2">
                                    <div className="col-6 fw-bold"> Branch </div>
                                    <div className=" col"> {branch.name} </div>
                                </div>
                                <div className="row my-2">
                                    <div className="col-6 fw-bold"> IFSC  </div>
                                    <div className=" col"> {branch.ifsc} </div>
                                </div>
                                <div className="row my-2">
                                    <div className="col-6 fw-bold">Account Type </div>
                                    <div className=" col"> {accountTypes[account.typeId - 1]?.name} </div>
                                </div>
                            </div>
                            <div className="col-sm-12 col-md-6">
                                <div className="row my-2">
                                    <div className="col-6 fw-bold">Balance </div>
                                    <div className=" col">₹ {account.balance} </div>
                                </div>
                                <div className="row my-2">
                                    <div className="col-6 fw-bold"> Created at </div>
                                    <div className=" col"> {new Date(Date.parse(account.createdAt))?.toLocaleString()} </div>
                                </div>
                                <div className="row my-2">
                                    <div className="col-6 fw-bold"> Verification Status </div>
                                    <div className=" col"> 
                                        {
                                        account.verified ? 
                                        <span className="badge rounded-pill text-bg-success">Verified</span>
                                        : 
                                        <span className="badge rounded-pill text-bg-warning">Unverified</span>
                                        }
                                    </div>
                                </div>
                                <div className="row my-2">
                                    <div className="col-6 fw-bold"> Lock Status  </div>
                                    <div className=" col">
                                        {
                                        account.locked ?
                                        <span className="badge rounded-pill text-bg-danger">Locked</span>
                                        : 
                                        <span className="badge rounded-pill text-bg-success">Unlocked</span>
                                        }
                                    </div>
                                </div>
                                <div className="row my-2">
                                    <div className="col-6 fw-bold"> Account Status  </div>
                                    <div className=" col">
                                        {
                                        account.deleted ? 
                                        <span className="badge rounded-pill text-bg-danger">Deleted</span>
                                        : 
                                        <span className="badge rounded-pill text-bg-primary">Active</span>
                                        }
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row mt-4 mx-2">
                            {!account.verified && (user.role == "ADMIN" || user.role == "EMPLOYEE") && (
                                <div className="col d-grid">
                                    <button className="btn btn-primary" onClick={verify}>
                                        Verify Account
                                    </button>
                                </div>
                            )}
                            
                            <div className="col d-grid">
                                <button className="btn btn-warning" onClick={lock}>
                                    {
                                        account.locked ? "Unlock Account" : "Lock Account"
                                    }
                                </button>
                            </div>

                            {!account.deleted && (user.role == "ADMIN" || user.role == "EMPLOYEE") && (
                                <div className="col d-grid">
                                    <button className="btn btn-danger" onClick={deleteAcc}>
                                        Delete Account
                                    </button>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
			</div>
		</>
	);
}
