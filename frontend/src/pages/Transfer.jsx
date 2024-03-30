import React from 'react'

export default function () {
  return (
    <div className="mx-auto mt-5 p-5">
        <div className="card ">
            <h5 className="card-header">Funds Tranfers</h5>
            <div className="card-body">
                <div className="form-group">
                    <form>
                        <div className="input-group mb-3">
                            <label className="input-group-text" htmlFor="accountSelect">Choose an Account</label>
                            <select className="form-select" id="accountSelect">
                                <option value="1">00000111000</option>
                                <option value="2">00010002122</option>
                                <option value="3">00012303215</option>
                            </select>
                            <div className="form-text mx-3">Account Balance : &#8377 150000</div>
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
                                <input type="number" id="PayeeAccountNumber" className="form-control  rounded " placeholder="Beneficiary Account Number" />
                            </div>
                        </div>

                        <div className="row align-items-center mb-3">
                            <div className="col-3">
                                <label htmlFor="PayeeAccountIFSC" className="col-form-label">IFSC code : </label>
                            </div>
                            <div className="col">
                                <input type="text" id="PayeeAccountIFSC" className="form-control  rounded" placeholder="Beneficiary Bank IFSC Code" />
                            </div>
                        </div>

                        <div className="row align-items-center mb-3">
                            <div className="col-3">
                                <label htmlFor="AmountTrafer" className="col-form-label">Enter Amount to Transfer : </label>
                            </div>
                            <div className="input-group col">
                                <span className="input-group-text" id="basic-addon1">&#8377</span>
                                <input type="text" className="form-control  rounded" id="AmountTrafer" placeholder="Amount"
                                    aria-label="To Change" aria-describedby="basic-addon1" />
                            </div>
                        </div>

                        <div className="row align-items-center mb-3">
                            <div className="col-3">
                                <label  className="col-form-label">Payment Option : </label>
                            </div>
                            <div className="col-auto">
                                <div className="form-check form-check-inline">
                                    <input className="form-check-input  rounded" type="radio" name="inlineRadioOptions"
                                        id="inlineRadio1" value="option1" />
                                    <label className="form-check-label" htmlFor="inlineRadio1">IMPS</label>
                                </div>
                                <div className="form-check form-check-inline">
                                    <input className="form-check-input  rounded" type="radio" name="inlineRadioOptions"
                                        id="inlineRadio2" value="option2" />
                                    <label className="form-check-label fs-6" htmlFor="inlineRadio2">NEFT</label>
                                </div>
                                <div className="form-check form-check-inline">
                                    <input className="form-check-input  rounded" type="radio" name="inlineRadioOptions"
                                        id="inlineRadio3" value="option3" />
                                    <label className="form-check-label" htmlFor="inlineRadio3">RTGS </label>
                                </div>
                            </div>
                        </div>

                        <div className="d-grid gap-2 d-md-block text-center">
                            <button className="btn btn-primary" type="submit">Pay</button>
                            <button className="btn btn-primary" type="reset">Clear</button>
                          </div>

                    </form>
                </div>
            </div>
        </div>
    </div>

  )
}
