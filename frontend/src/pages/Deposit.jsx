import React from 'react'

export default function Deposit() {
    return (
        <div className="mx-auto m-5 p-5">
            <div className="card">
                <h5 className="card-header">Deposit Funds</h5>
                <div className="card-body">
                    <div className="form-group">
                        <form>
                            <div className="mb-3">
                                <div className="input-group">
                                    <label className="input-group-text" htmlFor="accountSelect">Choose an Account</label>
                                    <select className="form-select" id="accountSelect">
                                        <option value="1">00000111000</option>
                                        <option value="2">00010002122</option>
                                        <option value="3">00012303215</option>
                                    </select>
                                </div>
                                <div className="form-text mx-3">Account Balance : &#8377 150000</div>
                            </div>
                            <div className="row align-items-center mb-3">
                                <label htmlFor="AmountTrafer" className="col-auto col-form-label">Enter Amount to Deposit : </label>
                                <div className="input-group col">
                                    <span className="input-group-text" id="basic-addon1">&#8377</span>
                                    <input type="text" className="form-control  rounded" id="AmountTrafer"
                                        placeholder="Amount" aria-label="To Change" aria-describedby="basic-addon1" />

                                </div>
                            </div>

                            <div className="d-grid  d-md-block text-center">
                                <button type="submit" className="btn btn-primary">Deposit</button>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}
