import React, { useState, useEffect } from "react";
import Alert from "../components/Alert";
import useAxiosAuth from "../contexts/Axios";
import { useParams } from "react-router-dom";

export default function card() {
  const [card, setCard] = useState({});
  const [creditCard, setCreditCard] = useState({});
  const [bill, setBill] = useState({});
  const [loading, setLoading] = useState(false);
  const api = useAxiosAuth();
  
  const { number } = useParams();

  useEffect(() => {
    setLoading(true);
    api.get("/card/" + number).then((response) => {
      setLoading(false);
      setCard(response.data);
      if(response.data.typeId === 2){
        api.get("/card/creditCardDetails/" + response.data.id)
        .then((response) => {
          setCreditCard(response.data);
          api.get("/transaction/pay/"+ number)
          .then((response) => {
            setBill(response.data)
          })
        } )
      }
    });
  }, []);

  



  function checkTypeCard(typeId) {
		if (typeId === 1) {
			return "Debit Card";
		} else if (typeId === 2) {
			return "Credit Card";
		} else {
			return "Unknown";
		}
	}

  return (
    <>

      <div className="card m-5 p-0 w-75 align-self-center">
        <h5 className="card-header">Account Details</h5>

        <div className="row m-2">
          <p className="col-2 fw-bold"> Card Number</p>
          <p className=" col"> {card.number} </p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold"> Account Number</p>
          <p className=" col"> {card.accountNumber} </p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold"> CVV</p>
          <p className=" col"> {card.cvv} </p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold"> PIN</p>
          <p className=" col"> {card.pin} </p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold"> Type</p>
          <p className=" col"> {checkTypeCard(card.typeId)} </p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold"> Expiry Date</p>
          <p className=" col"> {new Date(Date.parse(card.expiryDate))?.toLocaleDateString()} </p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold"> Active</p>
          <p className=" col"> {card.active ? "TRUE" : "FALSE"} </p>
        </div>
    
        <div className="row m-2">
          <p className="col-2 fw-bold"> Credit Limit</p>
          <p className=" col"> {creditCard.creditLimit} </p>
        </div>
        
        <div className="row m-2">
          <p className="col-2 fw-bold"> Credit Used</p>
          <p className=" col"> {creditCard.creditUsed} </p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold"> Credit Transactions</p>
          <p className=" col"> {creditCard.creditTransactions} </p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold"> Total Bill</p>
          <p className=" col"> {bill.total} </p>
        </div>

        <div className="row m-2">
          <p className="col-2 fw-bold"> Credit Intrest</p>
          <p className=" col"> {bill.interest}% </p>
        </div>

      </div>
    </>
  );
}
