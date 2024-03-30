import React from 'react'

export default function Transactions() {
  return (
    <div className="m-5 p-5">
			<div className="row flex-lg-row-reverse align-items-center justify-content-center g-4 py-5 text-center">
                <div className="card">
                    <div className="card-body">Balance: 0.0</div>
                </div>
                <div className="card">
                    <div className="card-body">
                        <table className="table">
                            <thead>
                                <tr>
                                    <th scope="col">Transaction ID</th>
                                    <th scope="col">To</th>
                                    <th scope="col">From</th>
                                    <th scope="col">Amount</th>
                                    <th scope="col">Status</th>
                                    <th scope="col">Type</th>
                                </tr>
                            </thead>
                            <tbody>
                                
                                <tr>
                                    <th scope="row">1</th>
                                    <td>Glen R</td>
                                    <td>Glen R</td>
                                    <td>10000.0</td>
                                    <td>Complete</td>
                                    <td>Deposit</td>
                                </tr>
                                
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
		</div>
  )
}
