import React, {useState, useEffect} from 'react'
import useAxiosAuth from "../contexts/Axios";
export default function Profile() {

    const [user, setUser] = useState({})
    const [loading, setLoading] = useState(false)
	const api = useAxiosAuth();

    useEffect(() => {
        setLoading(true)
        api.get("/user/details")
			.then((response) => {
                setLoading(false);
				setUser(response.data);
		});
    } ,[])

  return (
    <div className="card m-5 p-0 w-75 align-self-center" >
            <h5 className="card-header">User Details</h5>

            <div className="row m-2">
                <p className="col-2 fw-bold"> User Name</p>
                <p className=" col"> {user.name} </p>
            </div>

            <div className="row m-2">
                <p className="col-2 fw-bold">Customer ID</p>
                <p className=" col"> {user.customerId} </p>
            </div>

            <div className="row m-2">
                <p className="col-2 fw-bold"> Gender</p>
                <p className=" col"> {user.gender}</p>
            </div>

            <div className="row m-2">
                <p className="col-2 fw-bold">Email Address</p>
                <p className=" col"> {user.email}</p>
            </div>

            <div className="row m-2">
                <p className="col-2 fw-bold">Phone</p>
                <p className=" col"> {user.phone}</p>
            </div>

            <div className="row m-2">
                <p className="col-2 fw-bold"> Address</p>
                <p className=" col"> {user.address} , {user.city}, {user.state}, {user.pincode}. </p>
            </div>

            <div className="row m-2">
                <p className="col-2 fw-bold"> City</p>
                <p className=" col"> {user.city}  </p>
            </div>

            <div className="row m-2">
                <p className="col-2 fw-bold"> State</p>
                <p className=" col"> {user.state} </p>
            </div>

            <div className="row m-2">
                <p className="col-2 fw-bold"> Pincode</p>
                <p className=" col">{user.pincode} </p>
            </div>

            <div className="row m-2">
                <p className="col-2 fw-bold"> Pan Number</p>
                <p className=" col"> {user.pan}</p>
            </div>
            
            <div className="row m-2">
                <p className="col-2 fw-bold"> Aadhar Number</p>
                <p className=" col"> {user.aadhaar}</p>
            </div>
            
            <div className="row m-2">
                <p className="col-2 fw-bold"> Date of Birth</p>
                <p className=" col"> {new Date(Date.parse(user.dateOfBirth))?.toLocaleDateString()} </p>
            </div>

            <div className="row m-2">
                <p className="col-2 fw-bold">Account Locked </p>
                <p className=" col"> {user.locked? "TRUE" : "FALSE"} </p>
            </div>

        </div>
  )
}
