import Alert from '../components/Alert'
import React, { useEffect, useState } from 'react'
import useAxiosAuth from '../contexts/Axios';
import { useParams } from 'react-router-dom';
function Receipt() {
    const api = useAxiosAuth();
    const {id} = useParams();
    const [transaction, setTransaction] = useState({});
	const [loading, setLoading] = useState(false);
    const [beneficaries, setBeneficaries] = useState([]);
    const [beneficiary, setBeneficiary] = useState({});
    const [alert, setAlert] = useState(null);

  const showAlert = (message, type) => {
    setAlert({
      msg: message,
      type: type
    })
    setTimeout(() => {
      setAlert(null);
    }, 5000)
  }

    useEffect(() => {
        setLoading(true);
        showAlert("Transaction successful", "success")
        api.get("/transaction/"+id)
            .then((response) => {
                console.log("Respise : ",response.data);
                setTransaction(response.data);
                api.get("/account/beneficiary/"+response.data.beneficiaryId)
                    .then((response) => {
                        setBeneficiary(response.data);
                    });
            })
            .catch((err) => {
                console.log("Error : ",transaction);
                console.log(err.response);
            });
    } ,[])


    return (
        <>
            <Alert alert={alert}/>

            <div className="mx-auto mt-0 p-3" style={{ width: '60%' }}>
                <div className="card ">
                    <h5 className="card-header">Transaction Recipt</h5>

                    <div className="row m-2">
                        <p className="col-3 fw-bold"> Transaction ID : </p>
                        <p className=" col"> {transaction.id}</p>
                    </div>

                    <div className="row m-2">
                        <p className="col-3 fw-bold"> Account Number : </p>
                        <p className=" col"> {transaction.accountNumber}</p>
                    </div>
                    {
                        ( transaction.typeId == 1 || transaction.typeId == 4 ) ?
                        <div className="row m-2" >
                            <p className="col-3 fw-bold"> Reciever Account : </p>
                            <p className=" col"> {beneficiary.accountNumber}</p>
                        </div>
                        :
                        ""
                    }

                    <div className="row m-2">
                        <p className="col-3 fw-bold">Amount : </p>
                        <p className=" col"> {transaction.amount}</p>
                    </div>


                    <div className="row m-2">
                        <p className="col-3 fw-bold">  DATE : </p>
                        <p className=" col"> {transaction.createdAt?.substring(0,10)} </p>
                    </div>

                    <div className="row m-2">
                        <p className="col-3 fw-bold">  Time : </p>
                        <p className=" col"> {new Date(Date.parse(transaction.createdAt))?.toLocaleTimeString()} </p>
                    </div>

                    <div className="row m-2">
                        <p className="col-3 fw-bold">Status : </p>
                        <p className=" col"> {transaction.status}</p>
                    </div>
                </div>
            </div >
        </>
    )
}

export default Receipt