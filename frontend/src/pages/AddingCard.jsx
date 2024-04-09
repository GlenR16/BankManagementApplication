import React, { useEffect, useState } from 'react'
import useAxiosAuth from '../contexts/Axios';
import { useNavigate } from 'react-router-dom';

export default function addCard() {

    const navigate = useNavigate();
    const api = useAxiosAuth();
    const [accounts, setAccounts] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [form, setForm] = useState({
        accountNumber: "",
        typeId: "",
    });

    useEffect(() => {
        api.get("/account/list")
            .then((response) => {
                setAccounts(response.data);
                console.log(response.data);
            });
    }, []);

    function handleChange(e) {
        setForm({ ...form, [e.target.name]: e.target.value });
    }

    function AddCard() {
        console.log(form);
        setLoading(true);
        if (!form.accountNumber || !form.typeId) {
            setError("Please fill all the fields");
            setLoading(false);
            return;
        }
        if (document.getElementById("termsandconditions").checked === false) {
            setError("Please agree to the terms and conditions");
            setLoading(false);
            return;
        }
        api.post("/card", form)
            .then((res) => {
                setLoading(false);
                navigate("/dashboard");
            })
            .catch((err) => {
                if (err.response) setError(err.response.data.error);
                else setError("Something went wrong");
                setLoading(false);
            });
    }

    return (
        <div className="container">
            <div className="row flex-lg-row-reverse align-items-center justify-content-center g-5 m-3 text-center">
                <form action="" method="post" className="col-12 col-md-8 card p-4 border-0 shadow">
                    <div className="text-center my-2">
                        <svg version="1.0" width="70" height="70" id="Layer_1" xmlns="http://www.w3.org/2000/svg" xmlnsXlink="http://www.w3.org/1999/xlink" viewBox="0 0 64 64" enableBackground="new 0 0 64 64" xmlSpace="preserve" fill="#000000">
                            <g id="SVGRepo_bgCarrier" strokeWidth="0"></g>
                            <g id="SVGRepo_tracerCarrier" strokeLinecap="round" strokeLinejoin="round"></g>
                            <g id="SVGRepo_iconCarrier">
                                {" "}
                                <g>
                                    {" "}
                                    <circle fill="#000000" cx="32" cy="14" r="3"></circle> <path fill="#000000" d="M4,25h56c1.794,0,3.368-1.194,3.852-2.922c0.484-1.728-0.242-3.566-1.775-4.497l-28-17 C33.438,0.193,32.719,0,32,0s-1.438,0.193-2.076,0.581l-28,17c-1.533,0.931-2.26,2.77-1.775,4.497C0.632,23.806,2.206,25,4,25z M32,9c2.762,0,5,2.238,5,5s-2.238,5-5,5s-5-2.238-5-5S29.238,9,32,9z"></path> <rect x="34" y="27" fill="#000000" width="8" height="25"></rect> <rect x="46" y="27" fill="#000000" width="8" height="25"></rect> <rect x="22" y="27" fill="#000000" width="8" height="25"></rect> <rect x="10" y="27" fill="#000000" width="8" height="25"></rect> <path fill="#000000" d="M4,58h56c0-2.209-1.791-4-4-4H8C5.791,54,4,55.791,4,58z"></path> <path fill="#000000" d="M63.445,60H0.555C0.211,60.591,0,61.268,0,62v2h64v-2C64,61.268,63.789,60.591,63.445,60z"></path>{" "}
                                </g>{" "}
                            </g>
                        </svg>
                    </div>
                    <h1 className="h3 mb-3 fw-normal">Add a new Card</h1>

                    <div className="my-2 text-start">
                        <label htmlFor="branchId" className="form-label">
                            Choose an account
                        </label>
                        <select className="form-select" name="accountNumber" id="accountNumber" onChange={handleChange} aria-label="accountNumber" aria-describedby="accountNumber">
                            <option value="" defaultValue>Select an Account</option>
                            {
                                accounts.length > 0 ? accounts.map((account) => (
                                    <option key={account.id} value={account.accountNumber}>{account.accountNumber}</option>
                                )) : <option value="" disabled>No Accounts Found</option>
                            }
                        </select>
                    </div>

                    <div className="my-2 text-start">
                        <label htmlFor="branchId" className="form-label">
                            Choose Card Type
                        </label>
                        <select className="form-select" name="typeId" id="typeId" value={form.typeId} onChange={handleChange} aria-label="typeId" aria-describedby="typeId">
                            <option value="">Select Card Type</option>
                            <option value="1">Debit Card</option>
							<option value="2">Credit Card</option>
						</select>
                    </div>

                   

                    <p className="invalid-feedback d-block">{error}</p>
                    <div className="form-check text-start">
                        <input className="form-check-input" name="agreement" type="checkbox" id="termsandconditions" />
                        <label className="form-check-label" htmlFor="termsandconditions">
                            I hereby state that I have read and understood the terms and conditions and the information provided by me is true and accurate.
                        </label>
                    </div>
                    <br />
                    <div className="d-flex flex-column flex-md-row justify-content-between gap-4 my-2">
                        <div>
                            <button className="w-100 btn btn-primary" type="button" onClick={AddCard} disabled={loading} style={{ marginLeft: '250%' }}>
                                {loading ? (
                                    <div className="spinner-border mx-2" role="status">
                                        <span className="visually-hidden">Loading...</span>
                                    </div>
                                ) : (
                                    "Add Card"
                                )}
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    )
}
