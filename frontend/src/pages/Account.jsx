import React from 'react'

export default function Account() {
    return (
        <div className="mx-auto mt-5 p-4">
            <div className="card ">
                <h5 className="card-header">Account Details</h5>

                <div className="row m-2">
                    <p className="col-2 fw-bold"> User Name</p>
                    <p className=" col"> Mukesh Kumar</p>
                </div>

                <div className="row m-2">
                    <p className="col-2 fw-bold"> Account Number</p>
                    <p className=" col"> 00000111000</p>
                </div>

                <div className="row m-2">
                    <p className="col-2 fw-bold"> Gender</p>
                    <p className=" col"> Male</p>
                </div>

                <div className="row m-2">
                    <p className="col-2 fw-bold">Email Address</p>
                    <p className=" col"> mukesh.kumar@example.com</p>
                </div>

                <div className="row m-2">
                    <p className="col- fw-bold"> Address</p>
                    <p className=" col"> City/State/pincode</p>
                </div>

                <div className="row m-2">
                    <p className="col-2 fw-bold"> Pan Number</p>
                    <p className=" col"> XXXXX0000X</p>
                </div>

                <div className="row m-2">
                    <p className="col-2 fw-bold"> Aadhar Number</p>
                    <p className=" col"> 0000 0000 0000</p>
                </div>

                <div className="row m-2">
                    <p className="col-2 fw-bold"> Account Type</p>
                    <p className=" col"> Savings </p>
                </div>

            </div>
        </div >
    )
}
