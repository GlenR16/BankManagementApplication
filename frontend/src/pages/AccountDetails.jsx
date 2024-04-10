import React, { useState, useEffect } from "react";
import Alert from "../components/Alert";
import useAxiosAuth from "../contexts/Axios";
import { useParams } from "react-router-dom";

export default function Profile() {
  const [account, setAccounts] = useState({});
  const [loading, setLoading] = useState(false);
  const api = useAxiosAuth();
  const [accountTypes, setAccountTypes] = useState([]);
  const [branches, setBranches] = useState([]);
  const [alert, setAlert] = useState(null);
  const { AccountNumber } = useParams();
  console.log(AccountNumber);

  useEffect(() => {
    setLoading(true);
    const message = localStorage.getItem("alert");
    const type = localStorage.getItem("alertType");
    if (message && type) {
      showAlert(message, type);
      localStorage.removeItem("alert");
      localStorage.removeItem("alertType");
      console.log("alerted");
    }
    api.get("/account/" + AccountNumber).then((response) => {
      setLoading(false);
      setAccounts(response.data);
    });
    api.get("/account/type").then((response) => {
      setAccountTypes(response.data);
    });
    api.get("account/branch").then((response) => {
      setBranches(response.data);
    });
  }, []);

  const showAlert = (message, type) => {
    console.log("alerted by show alert");
    setAlert({
      msg: message,
      type: type,
    });
    setTimeout(() => {
      setAlert(null);
    }, 5000);
  };
  function lock() {
    api.post("/account/lock/" + AccountNumber).then((response) => {
      localStorage.setItem("alert", "Account Locked");
      localStorage.setItem("alertType", "warning");
      window.location.reload();
    });
  }

  function verify() {
    api.post("/account/verify/" + AccountNumber).then((response) => {
      localStorage.setItem("alert", "Account Verified");
      localStorage.setItem("alertType", "success");
      console.log("verified");
      window.location.reload();
    });
  }

  function deleteAcc() {
    api.delete("/account/" + AccountNumber).then((response) => {
      localStorage.setItem("alert", "Account Deleted");
      localStorage.setItem("alertType", "danger");
      window.location.reload();
    });
  }
  return (
    <>
      <Alert alert={alert} />
      <div className="card m-5 p-0 w-75 align-self-center">
        <h5 className="card-header">Account Details</h5>

        <div className="row m-2">
          <p className="col-2 fw-bold"> Account Number</p>
          <p className=" col"> {account.accountNumber} </p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold">Customer ID</p>
          <p className=" col"> {account.customerId} </p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold"> Branch</p>
          <p className=" col"> {branches[account.branchId - 1]?.name}</p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold"> IFSC </p>
          <p className=" col"> {branches[account.branchId - 1]?.ifsc}</p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold">Account Type</p>
          <p className=" col"> {accountTypes[account.typeId - 1]?.name}</p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold">Balance</p>
          <p className=" col"> {account.balance}</p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold"> Created at</p>
          <p className=" col">
            {" "}
            {new Date(Date.parse(account.createdAt))?.toLocaleDateString()}
          </p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold"> Time of Creation</p>
          <p className=" col">
            {" "}
            {new Date(Date.parse(account.createdAt))?.toLocaleTimeString()}{" "}
          </p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold"> Verified</p>
          <p className=" col"> {account.verified ? "TRUE" : "FALSE"} </p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold"> Account Locked </p>
          <p className=" col"> {account.locked ? "TRUE" : "FALSE"} </p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold"> Account Deleted </p>
          <p className=" col"> {account.deleted ? "TRUE" : "FALSE"} </p>
        </div>

        {
          <div className="d-flex flex-column flex-md-row justify-content-center gap-4 my-4">
            {!account.verified && (
              <button className="btn btn-primary" onClick={verify}>
                Verify Account
              </button>
            )}

            <button className="btn btn-warning" onClick={lock}>
              Lock Account
            </button>

            { !account.deleted && (<button className="btn btn-danger" onClick={deleteAcc}>
              Delete Account
            </button>)}
          </div>
        }
      </div>
    </>
  );
}
