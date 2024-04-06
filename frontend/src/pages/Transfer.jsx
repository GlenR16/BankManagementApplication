import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import useAxiosAuth from '../contexts/Axios';

export default function () {
    const navigate = useNavigate();
    const [accounts, setAccounts] = useState([])
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const [transaction, setTransaction] = useState({ senderAccount: "", senderCardId: "", receiverAccount: "", amount: 0, typeId: 1, accountNumber: "", })
    const api = useAxiosAuth();

    useEffect(() => {
        // Code to fetch accounts 
        api.get("/account/list")
            .then((response) => {
                setAccounts(response.data);
            });
    }, []);

    function handleChange(e) {
        // Code to handle change in account
        // console.log(e.target.value);
        setTransaction({ ...transaction, senderAccount: e.target.value, accountNumber: e.target.value, senderCardId: e.target.value });
    }

    function handlePay(e)  {
        // Code to handle payment
        e.preventDefault();
        setLoading(true);
        if (!transaction.senderAccount || !transaction.amount || !transaction.receiverAccount || !transaction) {
            setError("Please fill all the fields");
            setLoading(false);
            return;
        }
        else{
            setLoading(false);
        }
    }

    function handleClick(e) {
        setLoading(true);
        
        // Code to handle transfer
        e.preventDefault();
        console.log(transaction);
        api.post("/transaction/transfer", transaction)
            .then((res) => {
                console.log(res.data);
                setLoading(false);
                navigate("/transaction/"+res.data.transaction.id);

            })
            .catch((err) => {
				if (err.response) setError(err.response.data.error);
                else setError("Something went wrong");
                console.log(err.response);
                setLoading(false);
            });

    }

    return (
        <div className="mx-auto mt-5 p-5">
            <div className="card ">
                <h5 className="card-header">Funds Tranfers</h5>
                <div className="card-body">
                    <div className="form-group">
                        <form>
                            <div className="mb-3">
                                <div className="input-group">
                                    <label className="input-group-text" htmlFor="accountSelect">Choose an Account</label>
                                    <select className="form-select" id="accountSelect" onChange={handleChange}>
                                        <option value="" defaultValue>Select an Account</option>
                                        {
                                            accounts.length > 0 ? accounts.map((account) => (
                                                <option key={account.id} value={account.accountNumber}>{account.accountNumber}</option>
                                            )) : <option value="" disabled>No Accounts Found</option>
                                        }
                                    </select>
                                </div>
                                <div className="form-text mx-3">Account Balance : ₹ {accounts.length > 0 ? accounts.find((account) => account.accountNumber == transaction.senderAccount)?.balance : 0} </div>
                            </div>


                            <div className="row align-items-center mb-3">
                                <div className="col-3">
                                    <label htmlFor="PayeeAccountName" className="col-form-label">Name of Payee : </label>
                                </div>
                                <div className="col">
                                    <input type="text" id="PayeeAccountName" className="form-control  rounded" placeholder="Beneficiary Name" />
                                </div>
                            </div>

                            <div className="row align-items-center mb-3">
                                <div className="col-3">
                                    <label htmlFor="PayeeAccountNumber" className="col-form-label">Payee Account Number : </label>
                                </div>
                                <div className="col">
                                    <input type="number" id="PayeeAccountNumber" className="form-control  rounded " placeholder="Beneficiary Account Number" onChange={(e) => { setTransaction({ ...transaction, receiverAccount: e.target.value }) }} />
                                </div>
                            </div>

                            <div className="row align-items-center mb-3">
                                <div className="col-3">
                                    <label htmlFor="AmountTrafer" className="col-form-label">Enter Amount to Transfer : </label>
                                </div>
                                <div className="input-group col">
                                    <span className="input-group-text" id="basic-addon1">₹ </span>
                                    <input type="number" className="form-control  rounded" id="AmountTrafer" placeholder="Amount"
                                        aria-label="To Change" aria-describedby="basic-addon1" onChange={(e) => { setTransaction({ ...transaction, amount: e.target.valueAsNumber }) }} />
                                </div>
                            </div>

                            <div className="row align-items-center mb-3">
                                <div className="col-3">
                                    <label className="col-form-label">Payment Option : </label>
                                </div>
                                <div className="col-auto">
                                    <div className="form-check form-check-inline">
                                        <input className="form-check-input  rounded" type="radio" name="inlineRadioOptions"
                                            id="inlineRadio1" value="1" />
                                        <label className="form-check-label" htmlFor="inlineRadio1" >IMPS</label>
                                    </div>
                                    <div className="form-check form-check-inline">
                                        <input className="form-check-input  rounded" type="radio" name="inlineRadioOptions"
                                            id="inlineRadio2" value="4" />
                                        <label className="form-check-label fs-6" htmlFor="inlineRadio2">NEFT</label>
                                    </div>
                                    <div className="form-check form-check-inline">
                                        <input className="form-check-input  rounded" type="radio" name="inlineRadioOptions"
                                            id="inlineRadio3" value="5" />
                                        <label className="form-check-label" htmlFor="inlineRadio3">RTGS </label>
                                    </div>
                                </div>
                            </div>

                            <p className="invalid-feedback d-block">{error}</p>


                            <div className="d-grid gap-2 d-md-block text-center">
                                <button className="btn btn-primary " type="button" data-bs-toggle ={(!transaction.senderAccount || !transaction.amount || !transaction.receiverAccount || !transaction) ? ("") : ('modal')} data-bs-target="#staticBackdrop" onClick={handlePay}>Pay</button>
                                <button className="btn btn-primary" type="reset">Clear</button>
                            </div>
                            

                        </form>

                        <div className="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabIndex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                            <div className="modal-dialog">
                                <div className="modal-content">
                                    <div className="modal-header">
                                        <h5 className="modal-title" id="staticBackdropLabel">Confirm Transfer details</h5>
                                        <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div className="modal-body">
                                        <b>
                                            <table>
                                                <tbody>
                                                    <tr>
                                                        <td>Account Number : </td>
                                                        <td>{transaction.senderAccount}</td>
                                                    </tr>
                                                    <tr>
                                                        <td>Payee AC Number : </td>
                                                        <td>{transaction.receiverAccount}</td>
                                                    </tr>
                                                    <tr>
                                                        <td>Amount : </td>
                                                        <td>{transaction.amount}</td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </b>
                                    </div>
                                    <div className="modal-footer">
                                        <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                        <button type="button" className="btn btn-primary" data-bs-dismiss="modal" onClick={handleClick} disabled={loading}>{loading ? (
                                            <div className="spinner-border mx-2" role="status">
                                                <span className="visually-hidden">Loading...</span>
                                            </div>
                                        ) : (
                                            "Confirm"
                                        )}</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    )
}
