import React, { useEffect, useState } from 'react'
import useAxiosAuth from '../contexts/Axios';
import { useNavigate } from 'react-router-dom';
import { useParams } from "react-router-dom";


export default function AddingBranch() {

    const navigate = useNavigate();
	const { id } = useParams();
    const api = useAxiosAuth();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [form, setForm] = useState({
        name: "",
        address: "",
        ifsc: ""
    });

    useState(() => {
        if(!id) return;
        api.get("/account/branch")
            .then((response) => {
                response.data.map((branch) => {
                    if(branch.id == id) {
                        setForm(branch);
                    }
            });
    }, []);
});


    function handleChange(e) {
        setForm({ ...form, [e.target.name]: e.target.value });
    }

    function addBranch() {
        setLoading(true);
        if (!form.name || !form.address || !form.ifsc) {
            setError("Please fill all the fields");
            setLoading(false);
            return;
        }
        if (document.getElementById("termsandconditions").checked === false) {
            setError("Please agree to the terms and conditions");
            setLoading(false);
            return;
        }
        if(id) {
            api.put("/account/branch/" + id, form)
            .then((res) => {
                setLoading(false);
                navigate("/AdminBranches");
            })
            .catch((err) => {
                if (err.response) setError(err.response.data.message);
                else setError("Something went wrong");
                setLoading(false);
            });
            return;
        }
        else {
            api.post("/account/branch", form)
                .then((res) => {
                    setLoading(false);
                    navigate("/AdminBranches");
                })
                .catch((err) => {
                    if (err.response) setError(err.response.data.message);
                    else setError("Something went wrong");
                    setLoading(false);
                });
        }
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
                    <h1 className="h3 mb-3 fw-normal">Add a new Branch</h1>

                    <div className="my-2 text-start">
						<label htmlFor="typeId" className="form-label">
							Enter the Name of Branch.
						</label>
                        <input type="text" className="form-control" name="name" id="name" placeholder="Enter the Name of Branch" onChange={handleChange} value={form.name}/>
                    </div>

                    <div className="my-2 text-start">
						<label htmlFor="typeId" className="form-label">
							Enter Address of Branch.
						</label>
                        <input type="text" className="form-control" name="address" id="address" placeholder="Enter Address of Branch" onChange={handleChange} value={form.address} />
                    </div>


                    <div className="my-2 text-start">
						<label htmlFor="typeId" className="form-label">
							Enter IFSC code of Branch.
						</label>
                        <input type="text" className="form-control" name="ifsc" id="ifsc" placeholder="Enter IFSC code of Branch" onChange={handleChange} value={form.ifsc} />
                    </div>


                    <p className="invalid-feedback d-block">{error}</p>
                    <div className="form-check text-start">
                        <input className="form-check-input" name="agreement" type="checkbox" id="termsandconditions" />
                        <label className="form-check-label" htmlFor="termsandconditions">
                            I hereby state that I have read and understood the terms and conditions and the information provided by me is true and accurate.
                        </label>
                    </div>
                    <br />
                    <div className="d-flex flex-column flex-md-row justify-content-center gap-4 my-2">
                        <div>
                            <button className="w-100 btn btn-primary" type="button" onClick={addBranch} disabled={loading} >
                                {loading ? (
                                    <div className="spinner-border mx-2" role="status">
                                        <span className="visually-hidden">Loading...</span>
                                    </div>
                                ) : !id ? "Add Branch" : "Update Branch"}
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    )
}
