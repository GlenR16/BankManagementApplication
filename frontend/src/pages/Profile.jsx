import { useState, useEffect } from "react";
import useAxiosAuth from "../contexts/Axios";
import { useParams } from "react-router-dom";
import { useUser } from "../contexts/UserContext";

export default function Profile() {
	const [profile, setProfile] = useState({});
	const api = useAxiosAuth();
    const { user } = useUser();
	const { customerId } = useParams();

    function refreshData(){
        api.get("/user/" + customerId).then((response) => {
            setProfile(response.data);
        });
    }

	useEffect(() => {
        refreshData();
	}, []);

    function lockUnlockUser(){
        api.post("/user/lock/"+customerId).then((response) => {
            refreshData();
        });
    }

    function deleteUser(){
        api.delete("/user/"+customerId).then((response) => {
            refreshData();
        });
    }

	return (
		<div className="container-fluid col-sm-12 col-md-8 my-4">
			<div className="card gap-2">
				<h5 className="card-header p-3 text-center fw-bold">Customer Details</h5>
				<div className="row mx-2 mt-3">
					<div className="col-sm-6 col-md-2 fw-bold"> User Name </div>
					<div className=" col"> {profile.name}  </div>
				</div>
				<div className="row mx-2">
					<div className="col-sm-6 col-md-2 fw-bold">Customer ID </div>
					<div className=" col"> {profile.customerId}  </div>
				</div>
				<div className="row mx-2">
					<div className="col-sm-6 col-md-2 fw-bold"> Gender </div>
					<div className=" col"> {profile.gender} </div>
				</div>
				<div className="row mx-2">
					<div className="col-sm-6 col-md-2 fw-bold">Email Address </div>
					<div className=" col"> {profile.email} </div>
				</div>
				<div className="row mx-2">
					<div className="col-sm-6 col-md-2 fw-bold">Phone </div>
					<div className=" col"> {profile.phone} </div>
				</div>
				<div className="row mx-2">
					<div className="col-sm-6 col-md-2 fw-bold"> Address </div>
					<div className=" col">
						{profile.address} , {profile.city}, {profile.state}, {profile.pincode}{" "}
					 </div>
				</div>
				<div className="row mx-2">
					<div className="col-sm-6 col-md-2 fw-bold"> City </div>
					<div className=" col"> {profile.city}  </div>
				</div>
				<div className="row mx-2">
					<div className="col-sm-6 col-md-2 fw-bold"> State </div>
					<div className=" col"> {profile.state}  </div>
				</div>
				<div className="row mx-2">
					<div className="col-sm-6 col-md-2 fw-bold"> Pincode </div>
					<div className=" col">{profile.pincode}  </div>
				</div>
				<div className="row mx-2">
					<div className="col-sm-6 col-md-2 fw-bold"> Pan Number </div>
					<div className=" col"> {profile.pan} </div>
				</div>
				<div className="row mx-2">
					<div className="col-sm-6 col-md-2 fw-bold"> Aadhar Number </div>
					<div className=" col"> {profile.aadhaar} </div>
				</div>
				<div className="row mx-2">
					<div className="col-sm-6 col-md-2 fw-bold"> Date of Birth </div>
					<div className=" col"> {new Date(Date.parse(profile.dateOfBirth))?.toLocaleDateString()}  </div>
				</div>
				<div className="row mx-2">
					<div className="col-sm-6 col-md-2 fw-bold">Account Status  </div>
					<div className=" col">
						{(profile.locked && profile.deleted) || profile.deleted ? <span className="badge rounded-pill text-bg-danger">Deleted</span> : ""}
						{profile.locked && !profile.deleted ? <span className="badge rounded-pill bg-secondary">Locked</span> : ""}
						{!profile.locked && !profile.deleted ? <span className="badge rounded-pill bg-success">Active</span> : ""}
					 </div>
				</div>
                {
                    (user.role == "ADMIN" || user.role == "EMPLOYEE") && !profile.deleted ?
                    <div className="row p-2 g-4 m-2">
                        <div className="col d-grid">
                            <button type="button" className="btn btn-warning" onClick={lockUnlockUser}>
                                {
                                    profile.locked ? "Unlock Account" : "Lock Account"
                                }
                            </button>
                        </div>
                        <div className="col d-grid">
                            <button type="button" className="btn btn-danger" onClick={deleteUser}>
                                Delete Account
                            </button>
                        </div>
                    </div>
                    :
                    ""
                }
			</div>
		</div>
	);
}
