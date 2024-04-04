import { useEffect, useState } from "react";
import useAxiosAuth from "../contexts/Axios";
import { NavLink, useNavigate } from "react-router-dom";
import AdminCustomers from "./AdminCustomers";
import AdminAccount from "./AdminAccounts";



export default function Administration() {
  const [accounts, setAccounts] = useState([]);
  const [accountTypes, setAccountTypes] = useState([]);
  const [branches, setBranches] = useState([]);
  const [user, setUser] = useState({});

  const api = useAxiosAuth();
  const navigate = useNavigate();

  useEffect(() => {
    api
      .get("/user/details")
      .then((response) => {
        setUser(response.data);
      })
      .catch((error) => {
        return navigate("/login");
      });
  }, []);

  function customers() {
    console.log("GOING TO ADMIN CUSTOMERS PAGE");
    navigate("/AdminCustomers");
  }

  function account() {
    console.log("GOING TO ADMIN ACCOUNTS PAGE");
    navigate("/AdminAccounts");
  }

  function transactions() {
    console.log("GOING TO ADMIN TRANSACTIONS PAGE");
    navigate("/AdminTransactions");
  }

  return (
    <div className="container col-sm-12 col-md-8 p-5">
      <div className="row flex-lg-row-reverse align-items-center justify-content-center g-5 py-5 text-center">
        <div className="row row-cols-1 row-cols-md-3 g-4">
          <div className="col">
            <div className="card">
              <div className="card-body">
                <p className="card-title">Customers</p>
				<img src="/customers.png" alt="Index Image" width={100}/>
                <p className="card-text">
                  Customers: View the list of all Customers.
                </p>
                <button className="btn btn-success" type="button" onClick={customers}>Customers</button>
              </div>
            </div>
          </div>
          <div className="col">
            <div className="card">
              <div className="card-body">
                <p className="card-title">Accounts</p>
				<img src="/accounts.png" alt="Index Image" width={100}/>
                <p className="card-text">
                  Accounts: View the list of all Accounts.
                </p>
                <button className="btn btn-success" type="button" onClick={account}>Accounts</button>
              </div>
            </div>
          </div>
          <div className="col">
            <div className="card">
              <div className="card-body">
                <p className="card-title">Transactions</p>
				<img src="/transactions.png" alt="Index Image" width={100}/>
                <p className="card-text">
					Transactions: View the list of all Transactions.
                </p>
                <button className="btn btn-success" type="button" onClick={transactions}> Transactions</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
