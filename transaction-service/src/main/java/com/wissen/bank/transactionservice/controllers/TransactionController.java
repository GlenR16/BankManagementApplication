package com.wissen.bank.transactionservice.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.wissen.bank.transactionservice.models.Transaction;
import com.wissen.bank.transactionservice.dao.CardTransactionDao;
import com.wissen.bank.transactionservice.exceptions.DatabaseIntegrityException;
import com.wissen.bank.transactionservice.exceptions.TransactionFailedException;
import com.wissen.bank.transactionservice.exceptions.UnauthorizedException;
import com.wissen.bank.transactionservice.models.Account;
import com.wissen.bank.transactionservice.models.Beneficiary;
import com.wissen.bank.transactionservice.models.Card;
import com.wissen.bank.transactionservice.models.CreditCardDetail;
import com.wissen.bank.transactionservice.models.Role;
import com.wissen.bank.transactionservice.models.Status;
import com.wissen.bank.transactionservice.responses.Response;
import com.wissen.bank.transactionservice.services.AccountClientService;
import com.wissen.bank.transactionservice.services.CardClientService;
import com.wissen.bank.transactionservice.services.TransactionService;

import jakarta.annotation.PostConstruct;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/transaction")

public class TransactionController {

    @Autowired
    private TransactionService transactionservice;

    @Autowired
    private AccountClientService accountClientService;

    @Autowired
    private CardClientService cardClientService;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public List<Transaction> getAllTransactions(@RequestHeader("Customer") String customerId,@RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            LOGGER.info("Admin {} getting all transactions",customerId);
            return transactionservice.getAllTransactions();
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable long id, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        Transaction transaction = transactionservice.getTransactionByTransactionId(id);
        Account account = accountClientService.getAccountByAccountNumber(transaction.getAccountNumber(), customerId, role.toString()).block();
        if (role == Role.ADMIN || role == Role.EMPLOYEE || account.getCustomerId().equals(customerId) ) {
            LOGGER.info("Admin {} Getting transaction id : {}",customerId,id);
            return transaction;
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PostMapping("/transfer")
    public ResponseEntity<Response> createTransfer(@RequestBody Transaction transaction, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        Account account = accountClientService.getAccountByAccountNumber(transaction.getAccountNumber(), customerId, role.toString()).block();
        if (!account.isActive() || account.isDeleted() || account.isLocked() || !account.isVerified() || account.getBalance() < transaction.getAmount()) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        Account _account = accountClientService.updateAccountBalance(transaction.getAccountNumber(), account.getBalance() - transaction.getAmount(), customerId, role.toString()).block();
        if (_account == null) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        Transaction _transaction = transactionservice.createTransaction(transaction, Status.COMPLETED);
        Beneficiary beneficiary = accountClientService.getBeneficiaryByBeneficiaryId(transaction.getBeneficiaryId(), customerId, role.toString()).block();
        if (beneficiary == null) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        Account beneficiaryAccount = accountClientService.getAccountByAccountNumber(beneficiary.recieverNumber(), customerId, role.toString()).block();
        if (beneficiaryAccount != null) {
            Account _beneficiaryAccount = accountClientService.updateAccountBalance(beneficiary.recieverNumber(), beneficiaryAccount.getBalance() + transaction.getAmount(), customerId, role.toString()).block();
            if (_beneficiaryAccount == null) {
                throw new TransactionFailedException("Transaction Failed",transaction);
            }
            Transaction transaction2 = Transaction.builder()
                .accountNumber(beneficiary.recieverNumber())
                .amount(transaction.getAmount())
                .typeId(1)
                .build();
            transactionservice.createTransaction(transaction2, Status.COMPLETED);
        }
        LOGGER.info("User {} creating transfer transaction with id: {}",customerId,_transaction.getId());
        return ResponseEntity.ok().body(new Response(new Date(), 200, "Transfer Success", _transaction)); 
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Response> createWithdraw(@RequestBody Transaction transaction,@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        Account account = accountClientService.getAccountByAccountNumber(transaction.getAccountNumber(), customerId, role.toString()).block();
        if (!account.isActive() || account.isDeleted() || account.isLocked() || !account.isVerified() || account.getBalance() < transaction.getAmount()) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        Account _account = accountClientService.updateAccountBalance(transaction.getAccountNumber(), account.getBalance() - transaction.getAmount(), customerId, role.toString()).block();
        if (_account == null) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        Transaction _transaction = transactionservice.createTransaction(transaction,Status.COMPLETED);
        LOGGER.info("User {} creating withdraw transaction with id: {}",customerId,_transaction.getId());
        return ResponseEntity.ok().body(new Response(new Date(), 200, "Withdraw Success", _transaction));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Response> createDeposit(@RequestBody Transaction transaction, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        Account account = accountClientService.getAccountByAccountNumber(transaction.getAccountNumber(), customerId, role.toString()).block();
        System.out.println(account.toString());
        if (!account.isActive() || account.isDeleted() || account.isLocked() || !account.isVerified()) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        Account _account = accountClientService.updateAccountBalance(transaction.getAccountNumber(), account.getBalance() + transaction.getAmount(), customerId, role.toString()).block();
        if (_account == null) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        Transaction _transaction = transactionservice.createTransaction(transaction,Status.COMPLETED);
        LOGGER.info("User {} creating deposit transaction with id: {}",customerId,_transaction.getId());
        return ResponseEntity.ok().body(new Response(new Date(), 200, "Deposit Success", _transaction));
    }

    @PostMapping("/cardTransfer")
    public ResponseEntity<Response> createCardTransfer(@RequestBody CardTransactionDao cardTransactionDao, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        Transaction transaction = Transaction.builder()
            .accountNumber(cardTransactionDao.getAccountNumber())
            .cardNumber(cardTransactionDao.getNumber())
            .beneficiaryId(cardTransactionDao.getBeneficiaryId())
            .amount(cardTransactionDao.getAmount())
            .typeId(cardTransactionDao.getTypeId())
            .build();
        Card cardData = Card.builder()
            .number(cardTransactionDao.getNumber())
            .cvv(cardTransactionDao.getCvv())
            .pin(cardTransactionDao.getPin())
            .build();
        Account account = accountClientService.getAccountByAccountNumber(transaction.getAccountNumber(), customerId, role.toString()).block();
        if (!account.isActive() || account.isDeleted() || account.isLocked() || !account.isVerified()) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        Card card = cardClientService.getCardByNumber(transaction.getCardNumber(), customerId, role.toString()).block();
        if (card == null || !card.isActive() || card.isDeleted() || card.isLocked() || !card.isVerified() || card.getCvv() != cardData.getCvv() || card.getPin() != cardData.getPin()){
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        Transaction _transaction;
        if (card.getTypeId() == 1){
            Account _account = accountClientService.updateAccountBalance(transaction.getAccountNumber(), account.getBalance() - transaction.getAmount(), customerId, role.toString()).block();
            if (_account == null) {
                throw new TransactionFailedException("Transaction Failed",transaction);
            }
            _transaction = transactionservice.createTransaction(transaction, Status.COMPLETED);
            Beneficiary beneficiary = accountClientService.getBeneficiaryByBeneficiaryId(transaction.getBeneficiaryId(), customerId, role.toString()).block();
            if (beneficiary == null) {
                throw new TransactionFailedException("Transaction Failed",transaction);
            }
            Account beneficiaryAccount = accountClientService.getAccountByAccountNumber(beneficiary.recieverNumber(), customerId, role.toString()).block();
            if (beneficiaryAccount != null) {
                Account _beneficiaryAccount = accountClientService.updateAccountBalance(beneficiary.recieverNumber(), beneficiaryAccount.getBalance() + transaction.getAmount(), customerId, role.toString()).block();
                if (_beneficiaryAccount == null) {
                    throw new TransactionFailedException("Transaction Failed",transaction);
                }
                Transaction transaction2 = Transaction.builder()
                    .accountNumber(beneficiary.recieverNumber())
                    .amount(transaction.getAmount())
                    .typeId(1)
                    .build();
                transactionservice.createTransaction(transaction2, Status.COMPLETED);
            }
            LOGGER.info("User {} creating transfer transaction with id: {}",customerId,_transaction.getId());
        }
        else{
            CreditCardDetail ccd = cardClientService.getCreditCardDetailByCardId(card.getId(), customerId, role.toString()).block();
            if (ccd == null) {
                throw new TransactionFailedException("Transaction Failed",transaction);
            }
            if (ccd.creditLimit() < ccd.creditUsed() + transaction.getAmount()) {
                throw new TransactionFailedException("Transaction Failed",transaction);
            }
            CreditCardDetail _ccd = cardClientService.updateCreditCardDetail(new CreditCardDetail(ccd.id(),ccd.cardId(),ccd.creditLimit(),ccd.creditUsed() + transaction.getAmount(),ccd.creditTransactions()+1), customerId, role.toString()).block();
            if (_ccd == null) {
                throw new TransactionFailedException("Transaction Failed",transaction);
            }
            _transaction = transactionservice.createTransaction(transaction, Status.COMPLETED);
            Beneficiary beneficiary = accountClientService.getBeneficiaryByBeneficiaryId(transaction.getBeneficiaryId(), customerId, role.toString()).block();
            if (beneficiary == null) {
                throw new TransactionFailedException("Transaction Failed",transaction);
            }
            Account beneficiaryAccount = accountClientService.getAccountByAccountNumber(beneficiary.recieverNumber(), customerId, role.toString()).block();
            if (beneficiaryAccount != null) {
                Account _beneficiaryAccount = accountClientService.updateAccountBalance(beneficiary.recieverNumber(), beneficiaryAccount.getBalance() + transaction.getAmount(), customerId, role.toString()).block();
                if (_beneficiaryAccount == null) {
                    throw new TransactionFailedException("Transaction Failed",transaction);
                }
                Transaction transaction2 = Transaction.builder()
                    .accountNumber(beneficiary.recieverNumber())
                    .amount(transaction.getAmount())
                    .typeId(1)
                    .build();
                transactionservice.createTransaction(transaction2, Status.COMPLETED);
            }
            LOGGER.info("User {} creating transfer transaction with id: {}",customerId,_transaction.getId());
        }
        return ResponseEntity.ok().body(new Response(new Date(), 200, "CardTransfer Success", _transaction));
    }


    @PostConstruct
    public void init(){
        Transaction txn = Transaction.builder()
            .accountNumber(9999999999L)
            .cardNumber(9999999999L)
            .beneficiaryId(1)
            .amount(300000)
            .typeId(2)
            .build();
        transactionservice.createTransaction(txn,Status.COMPLETED);
    }


    @ExceptionHandler({ DataIntegrityViolationException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<Response> handleSQLException(Exception e) {
        LOGGER.error("Error: {}", e.getMessage());
        throw new DatabaseIntegrityException("Database Integrity Violation");
    }

    @ExceptionHandler({ TransactionFailedException.class})
    public ResponseEntity<Response> handleTransactionFailedException(TransactionFailedException e) {
        Transaction transaction = e.getTransaction();
        transactionservice.createTransaction(transaction, Status.FAILED);
        return ResponseEntity.badRequest().body(new Response(new Date(), 400, "Transaction Failed", transaction));
    }
}
 