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
import com.wissen.bank.transactionservice.models.CardType;
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

    @GetMapping("/account/{accountNumber}")
    public List<Transaction> getTransactionsByAccountNumber(@PathVariable long accountNumber, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        Account account = accountClientService.getAccountByAccountNumber(accountNumber, customerId).blockOptional().orElse(null);
        if (account != null && (role == Role.ADMIN || role == Role.EMPLOYEE || account.getCustomerId().equals(customerId))) {
            LOGGER.info("Admin {} Getting transactions for account number : {}",customerId,accountNumber);
            return transactionservice.getTransactionsByAccountNumber(accountNumber);
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable long id, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        Transaction transaction = transactionservice.getTransactionByTransactionId(id);
        Account account = accountClientService.getAccountByAccountNumber(transaction.getAccountNumber(), customerId).blockOptional().orElse(null);
        if (account != null && (role == Role.ADMIN || role == Role.EMPLOYEE || account.getCustomerId().equals(customerId))) {
            LOGGER.info("Admin {} Getting transaction id : {}",customerId,id);
            return transaction;
        }
        throw new UnauthorizedException("Unauthorized");
    }

    @PostMapping("/transfer")
    public ResponseEntity<Response> createTransfer(@RequestBody Transaction transaction, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        Account account = accountClientService.getAccountByAccountNumber(transaction.getAccountNumber(), customerId).blockOptional().orElse(null);
        if (account == null || !account.isActive() || account.isDeleted() || account.isLocked() || !account.isVerified() || account.getBalance() < transaction.getDebit()) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        Account _account = accountClientService.updateAccountBalance(transaction.getAccountNumber(), account.getBalance() - transaction.getDebit(), customerId).blockOptional().orElse(null);
        if (_account == null) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        transaction.setBalance(_account.getBalance());
        Transaction transaction1 = transactionservice.createTransaction(transaction, Status.COMPLETED);
        Beneficiary beneficiary = accountClientService.getBeneficiaryByBeneficiaryId(transaction.getBeneficiaryId(), customerId).blockOptional().orElse(null);
        if (beneficiary == null) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        Account beneficiaryAccount = accountClientService.getAccountByAccountNumber(beneficiary.recieverNumber(), customerId).blockOptional().orElse(null);
       
        if (beneficiaryAccount != null) {
            Account _beneficiaryAccount = accountClientService.updateAccountBalance(beneficiary.recieverNumber(), beneficiaryAccount.getBalance() + transaction.getDebit(), customerId).blockOptional().orElse(null);
            if (_beneficiaryAccount == null) {
                throw new TransactionFailedException("Transaction Failed",transaction);
            }
            Transaction transaction2 = Transaction.builder()
                .accountNumber(beneficiary.recieverNumber())
                .credit(transaction.getDebit())
                .debit(0.0)
                .balance(_beneficiaryAccount.getBalance())
                .typeId(1)
                .build();
            transactionservice.createTransaction(transaction2, Status.COMPLETED);
        }
        LOGGER.info("User {} creating transfer transaction with id: {}",customerId,transaction1.getId());
        return ResponseEntity.ok().body(new Response(new Date(), 200, "Transfer Success", transaction1)); 
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Response> createWithdraw(@RequestBody Transaction transaction,@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        Account account = accountClientService.getAccountByAccountNumber(transaction.getAccountNumber(), customerId).blockOptional().orElse(null);
        if (account == null || !account.isActive() || account.isDeleted() || account.isLocked() || !account.isVerified() || account.getBalance() < transaction.getDebit()) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        Account _account = accountClientService.updateAccountBalance(transaction.getAccountNumber(), account.getBalance() - transaction.getDebit(), customerId).blockOptional().orElse(null);
        if (_account == null) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        transaction.setCredit(0.0);
        transaction.setBalance(account.getBalance() - transaction.getDebit());
        Transaction _transaction = transactionservice.createTransaction(transaction,Status.COMPLETED);
        LOGGER.info("User {} creating withdraw transaction with id: {}",customerId,_transaction.getId());
        return ResponseEntity.ok().body(new Response(new Date(), 200, "Withdraw Success", _transaction));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Response> createDeposit(@RequestBody Transaction transaction, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        Account account = accountClientService.getAccountByAccountNumber(transaction.getAccountNumber(), customerId).blockOptional().orElse(null);
        if (!account.isActive() || account.isDeleted() || account.isLocked() || !account.isVerified()) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        Account _account = accountClientService.updateAccountBalance(transaction.getAccountNumber(), account.getBalance() + transaction.getCredit(), customerId).blockOptional().orElse(null);
        if (_account == null) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        transaction.setDebit(0);
        transaction.setBalance(_account.getBalance());
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
            .debit(cardTransactionDao.getAmount())
            .credit(0.0)
            .typeId(cardTransactionDao.getTypeId())
            .build();
        Card cardData = Card.builder()
            .number(cardTransactionDao.getNumber())
            .cvv(cardTransactionDao.getCvv())
            .pin(cardTransactionDao.getPin())
            .build();
        Account account = accountClientService.getAccountByAccountNumber(transaction.getAccountNumber(), customerId).blockOptional().orElse(null);
        if (!account.isActive() || account.isDeleted() || account.isLocked() || !account.isVerified()) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        Card card = cardClientService.getCardByNumber(transaction.getCardNumber(), customerId).blockOptional().orElse(null);
        if (card == null || !card.isActive() || card.isDeleted() || card.isLocked() || !card.isVerified() || card.getCvv() != cardData.getCvv() || card.getPin() != cardData.getPin()){
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        Transaction _transaction;
        if (card.getTypeId() == 1){
            Account _account = accountClientService.updateAccountBalance(transaction.getAccountNumber(), account.getBalance() - transaction.getDebit(), customerId).blockOptional().orElse(null);
            if (_account == null) {
                throw new TransactionFailedException("Transaction Failed",transaction);
            }
            transaction.setBalance(_account.getBalance());
            _transaction = transactionservice.createTransaction(transaction, Status.COMPLETED);
            Beneficiary beneficiary = accountClientService.getBeneficiaryByBeneficiaryId(transaction.getBeneficiaryId(), customerId).blockOptional().orElse(null);
            if (beneficiary == null) {
                throw new TransactionFailedException("Transaction Failed",transaction);
            }
            Account beneficiaryAccount = accountClientService.getAccountByAccountNumber(beneficiary.recieverNumber(), customerId).blockOptional().orElse(null);
            if (beneficiaryAccount != null) {
                Account _beneficiaryAccount = accountClientService.updateAccountBalance(beneficiary.recieverNumber(), beneficiaryAccount.getBalance() + transaction.getDebit(), customerId).blockOptional().orElse(null);
                if (_beneficiaryAccount == null) {
                    throw new TransactionFailedException("Transaction Failed",transaction);
                }
                Transaction transaction2 = Transaction.builder()
                    .accountNumber(beneficiary.recieverNumber())
                    .credit(transaction.getDebit())
                    .debit(0.0)
                    .typeId(4)
                    .balance(beneficiaryAccount.getBalance() + transaction.getDebit())
                    .build();
                transactionservice.createTransaction(transaction2, Status.COMPLETED);
            }
            LOGGER.info("User {} creating transfer transaction with id: {}",customerId,_transaction.getId());
        }
        else{
            CreditCardDetail ccd = cardClientService.getCreditCardDetailByCardId(card.getId(), customerId).blockOptional().orElse(null);
            if (ccd == null) {
                throw new TransactionFailedException("Transaction Failed",transaction);
            }
            if (ccd.creditLimit() < ccd.creditUsed() + transaction.getDebit()) {
                throw new TransactionFailedException("Transaction Failed",transaction);
            }
            CreditCardDetail _ccd = cardClientService.updateCreditCardDetail(new CreditCardDetail(ccd.id(),ccd.cardId(),ccd.creditLimit(),ccd.creditUsed() + transaction.getDebit(),ccd.creditTransactions()+1), customerId).blockOptional().orElse(null);
            if (_ccd == null) {
                throw new TransactionFailedException("Transaction Failed",transaction);
            }
            transaction.setBalance(_ccd.creditLimit() - _ccd.creditUsed());
            System.out.println("Transaction errors: "+transaction);
            _transaction = transactionservice.createTransaction(transaction, Status.COMPLETED);
            Beneficiary beneficiary = accountClientService.getBeneficiaryByBeneficiaryId(transaction.getBeneficiaryId(), customerId).blockOptional().orElse(null);
            if (beneficiary == null) {
                throw new TransactionFailedException("Transaction Failed",transaction);
            }
            Account beneficiaryAccount = accountClientService.getAccountByAccountNumber(beneficiary.recieverNumber(), customerId).blockOptional().orElse(null);
            if (beneficiaryAccount != null) {
                Account _beneficiaryAccount = accountClientService.updateAccountBalance(beneficiary.recieverNumber(), beneficiaryAccount.getBalance() + transaction.getDebit(), customerId).blockOptional().orElse(null);
                if (_beneficiaryAccount == null) {
                    throw new TransactionFailedException("Transaction Failed",transaction);
                }
                Transaction transaction2 = Transaction.builder()
                    .accountNumber(beneficiary.recieverNumber())
                    .credit(transaction.getDebit())
                    .debit(0.0)
                    .balance(_beneficiaryAccount.getBalance())
                    .typeId(1)
                    .build();
                transactionservice.createTransaction(transaction2, Status.COMPLETED);
            }
            LOGGER.info("User {} creating transfer transaction with id: {}",customerId,_transaction.getId());
        }
        return ResponseEntity.ok().body(new Response(new Date(), 200, "CardTransfer Success", _transaction));
    }

    @PostMapping("/pay/{number}")
    public Transaction payCardFees(@PathVariable long number, @RequestBody Transaction transaction, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        Card card = cardClientService.getCardByNumber(number, customerId).blockOptional().orElse(null);
        if (card == null || !card.isActive() || card.isDeleted() || card.isLocked() || !card.isVerified()){
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        CreditCardDetail ccd = cardClientService.getCreditCardDetailByCardId(card.getId(), customerId).blockOptional().orElse(null);
        if (ccd == null) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        CardType cardType = cardClientService.getCardTypeById(card.getTypeId(), customerId).blockOptional().orElse(null);
        transaction.setDebit(ccd.creditUsed() * (1 + cardType.interest()));
        if ( ccd.creditUsed() < 0 ) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        Account account = accountClientService.getAccountByAccountNumber(transaction.getAccountNumber(), customerId).blockOptional().orElse(null);
        if (account == null || !account.isActive() || account.isDeleted() || account.isLocked() || !account.isVerified() || account.getBalance() < transaction.getDebit()) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        Account _account = accountClientService.updateAccountBalance(transaction.getAccountNumber(), account.getBalance() - transaction.getDebit(), customerId).blockOptional().orElse(null);
        transaction.setBalance(_account.getBalance());
        transaction.setBeneficiaryId(1);
        transaction.setTypeId(1);

        Transaction _transaction = transactionservice.createTransaction(transaction, Status.COMPLETED);
        
        CreditCardDetail _ccd = cardClientService.updateCreditCardDetail(new CreditCardDetail(ccd.id(),ccd.cardId(),ccd.creditLimit(),0.0,0), customerId).blockOptional().orElse(null);
        if (_ccd == null) {
            throw new TransactionFailedException("Transaction Failed",transaction);
        }
        LOGGER.info("User {} paying card fees with id: {}",customerId,_transaction.getId());
        return _transaction;
    }


    @PostConstruct
    public void init(){
        Transaction txn = Transaction.builder()
            .accountNumber(9999999999L)
            .cardNumber(9999999999L)
            .beneficiaryId(1)
            .credit(1000.0)
            .debit(0.0)
            .typeId(2)
            .build();
        transactionservice.createTransaction(txn,Status.COMPLETED);
    }


    @ExceptionHandler({ DataIntegrityViolationException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<Response> handleSQLException(Exception e) {
        LOGGER.error("Error: {}", e.getMessage());
        System.out.println("---------------------------------------------");
        System.out.println(e.getMessage());
        e.printStackTrace();
        System.out.println("---------------------------------------------");
        throw new DatabaseIntegrityException("Database Integrity Violation");
    }

    @ExceptionHandler({ TransactionFailedException.class})
    public ResponseEntity<Response> handleTransactionFailedException(TransactionFailedException e) {
        Transaction transaction = e.getTransaction();
        System.out.println("======================================================");
        System.out.println(e.getMessage());
        e.printStackTrace();
        System.out.println("======================================================");
        transactionservice.createTransaction(transaction, Status.FAILED);
        return ResponseEntity.badRequest().body(new Response(new Date(), 400, "Transaction Failed", transaction));
    }
}
 