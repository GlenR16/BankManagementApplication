import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import useAxiosAuth from '../contexts/Axios';

export default function () {
    const navigate = useNavigate();
    const [accounts, setAccounts] = useState([])
    const [error, setError] = useState("");
    const [beneficaries, setBeneficaries] = useState([]);
    const [loading, setLoading] = useState(false);

    const [transaction, setTransaction] = useState({ accountNumber: 0, beneficiaryId: 0, amount: 0, typeId: 1})
    const api = useAxiosAuth();

    useEffect(() => {
        api.get("/account/list")
            .then((response) => {
                setAccounts(response.data);
            });
    }, []);

    useEffect(() => {
        api.get("/account/beneficiary/list/"+transaction.accountNumber)
        .then((response) => {
            setBeneficaries(response.data);
        });
    }, [transaction.accountNumber]);

    
    function handleChange(e) {
        setTransaction({ ...transaction, [e.target.name]: e.target.value });
    }

    function handlePay(e)  {
        // Code to handle payment
        e.preventDefault();
        setLoading(true);
        if (!transaction.accountNumber || !transaction.amount || !transaction.beneficiaryId) {
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
        e.preventDefault();
        console.log(transaction);
        api.post("/transaction/transfer", transaction)
            .then((res) => {
                console.log(res.data);
                navigate("/transaction/"+res.data.transaction.id);
                setLoading(false);
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
                                    <select className="form-select" name="accountNumber" id="accountSelect" onChange={handleChange}>
                                        <option value="" defaultValue>Select an Account</option>
                                        {
                                            accounts.length > 0 ? accounts.map((account) => (
                                                <option key={account.id} value={account.accountNumber}>{account.accountNumber}</option>
                                            )) : <option value="" disabled>No Accounts Found</option>
                                        }
                                    </select>
                                </div>
                                <div className="form-text mx-3">Account Balance : ₹ {accounts.length > 0 ? accounts.find((account) => account.accountNumber == transaction.accountNumber)?.balance : 0} </div>
                            </div>


                            <div className="row align-items-center mb-3">
                                <div className="col-3">
                                    <label htmlFor="PayeeAccountName" className="col-form-label">Name of Payee : </label>
                                </div>
                                <div className="col">
                                    <input type="text" id="PayeeAccountName" className="form-control  rounded" disabled value={beneficaries.find(x => x.id == transaction.beneficiaryId)?.name} placeholder="Beneficiary Name" />
                                </div>
                            </div>

                            <div className="row align-items-center mb-3">
                                <div className="col-3">
                                    <label htmlFor="PayeeAccountNumber" className="col-form-label">Payee Account Number : </label>
                                </div>
                                <div className="col">
                                <select className="form-select" id="beneficiaryId" name='beneficiaryId' onChange={handleChange}>
                                        <option value="" defaultValue>Select Beneficiary</option>
                                        {
                                            beneficaries.length > 0 ? beneficaries.map((account) => (
                                                <option key={account.id} value={account.id}>{account.recieverNumber}</option>
                                            )) : <option value="" disabled>No Beneficiares found</option>
                                        }
                                    </select>
                                </div>
                            </div>

                            <div className="row align-items-center mb-3">
                                <div className="col-3">
                                    <label htmlFor="AmountTrafer" className="col-form-label">Enter Amount to Transfer : </label>
                                </div>
                                <div className="input-group col">
                                    <span className="input-group-text" id="basic-addon1">₹ </span>
                                    <input type="number" className="form-control  rounded" id="AmountTrafer" placeholder="Amount"
                                        aria-label="To Change" aria-describedby="basic-addon1" name="amount" onChange={handleChange} />
                                </div>
                            </div>


                            <p className="invalid-feedback d-block">{error}</p>


                            <div className="d-grid gap-2 d-md-block text-center">
                                <button className="btn btn-primary " type="button" data-bs-toggle ={(!transaction.accountNumber || !transaction.amount || !transaction.beneficiaryId) ? ("") : ('modal')} data-bs-target="#staticBackdrop" onClick={handlePay}>Transfer</button>
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
                                                        <td>{transaction.accountNumber}</td>
                                                    </tr>
                                                    <tr>
                                                        <td>Payee Name : </td>
                                                        <td>{beneficaries.find(x => x.id == transaction.beneficiaryId)?.name}</td>
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
