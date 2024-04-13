package com.wissen.bank.transactionservice.controllers;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.wissen.bank.transactionservice.dao.CardTransactionDao;
import com.wissen.bank.transactionservice.models.Account;
import com.wissen.bank.transactionservice.models.Beneficiary;
import com.wissen.bank.transactionservice.models.Card;
import com.wissen.bank.transactionservice.models.CardType;
import com.wissen.bank.transactionservice.models.CreditCardDetail;
import com.wissen.bank.transactionservice.models.Role;
import com.wissen.bank.transactionservice.models.Status;
import com.wissen.bank.transactionservice.models.Transaction;
import com.wissen.bank.transactionservice.responses.BillResponse;
import com.wissen.bank.transactionservice.services.AccountClientService;
import com.wissen.bank.transactionservice.services.CardClientService;
import com.wissen.bank.transactionservice.services.TransactionService;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/transaction")

public class TransactionController {

    @Autowired
    private TransactionService transactionservice;

    @Autowired
    private AccountClientService accountClientService;

    @Autowired
    private CardClientService cardClientService;

    @GetMapping("")
    public List<Transaction> getAllTransactions(@RequestHeader("Customer") String customerId,@RequestHeader("Role") Role role) {
        if (role == Role.ADMIN || role == Role.EMPLOYEE) {
            return transactionservice.getAllTransactions();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot access these details.");
    }

    @GetMapping("/account/{accountNumber}")
    public List<Transaction> getTransactionsByAccountNumber(@PathVariable long accountNumber, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        Account account = accountClientService.getAccountByAccountNumber(accountNumber, customerId).blockOptional().orElse(null);
        if (account != null && (role == Role.ADMIN || role == Role.EMPLOYEE || account.getCustomerId().equals(customerId))) {
            return transactionservice.getTransactionsByAccountNumber(accountNumber);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot access these details.");
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable long id, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        Transaction transaction = transactionservice.getTransactionByTransactionId(id);
        Account account = accountClientService.getAccountByAccountNumber(transaction.getAccountNumber(), customerId).blockOptional().orElse(null);
        if (account != null && (role == Role.ADMIN || role == Role.EMPLOYEE || account.getCustomerId().equals(customerId))) {
            return transaction;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User cannot access these details.");
    }

    @PostMapping("/transfer")
    public Transaction createTransfer(@RequestBody Transaction transaction, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        Account account = accountClientService.getAccountByAccountNumber(transaction.getAccountNumber(), customerId).blockOptional().orElse(null);
        Beneficiary beneficiary = accountClientService.getBeneficiaryByBeneficiaryId(transaction.getBeneficiaryId(), customerId).blockOptional().orElse(null);
        if (account == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        if (beneficiary == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Beneficiary not found.");
        if (!account.isActive() || account.isDeleted() || account.isLocked() || !account.isVerified() ) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account not active.");
        if (account.getBalance() < transaction.getDebit()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance.");
        if (account.getAccountNumber() == beneficiary.recieverNumber()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot transfer to same account.");
        account = accountClientService.updateAccountBalance(transaction.getAccountNumber(), account.getBalance() - transaction.getDebit(), customerId).blockOptional().orElse(null);
        if (account == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cloud not update account balance.");
        transaction.setBalance(account.getBalance());
        Transaction transaction1 = transactionservice.createTransaction(transaction, Status.COMPLETED);
        Account beneficiaryAccount = accountClientService.getAccountByAccountNumber(beneficiary.recieverNumber(), customerId).blockOptional().orElse(null);
        if (beneficiaryAccount != null) {
            Account _beneficiaryAccount = accountClientService.updateAccountBalance(beneficiary.recieverNumber(), beneficiaryAccount.getBalance() + transaction.getDebit(), customerId).blockOptional().orElse(null);
            if (_beneficiaryAccount == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cloud not update beneficiary account balance.");
            Transaction transaction2 = Transaction.builder()
                .accountNumber(beneficiary.recieverNumber())
                .credit(transaction.getDebit())
                .debit(0.0)
                .balance(_beneficiaryAccount.getBalance())
                .typeId(1)
                .build();
            transactionservice.createTransaction(transaction2, Status.COMPLETED);
        }
        return transaction1;
    }

    @PostMapping("/withdraw")
    public Transaction createWithdraw(@RequestBody Transaction transaction,@RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        Account account = accountClientService.getAccountByAccountNumber(transaction.getAccountNumber(), customerId).blockOptional().orElse(null);
        if (account == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        if (!account.isActive() || account.isDeleted() || account.isLocked() || !account.isVerified() ) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account not active.");
        if (account.getBalance() < transaction.getDebit()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance.");
        Account _account = accountClientService.updateAccountBalance(transaction.getAccountNumber(), account.getBalance() - transaction.getDebit(), customerId).blockOptional().orElse(null);
        if (_account == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cloud not update account balance.");
        transaction.setCredit(0.0);
        transaction.setBalance(account.getBalance() - transaction.getDebit());
        Transaction _transaction = transactionservice.createTransaction(transaction,Status.COMPLETED);
        return _transaction;
    }

    @PostMapping("/deposit")
    public Transaction createDeposit(@RequestBody Transaction transaction, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
        Account account = accountClientService.getAccountByAccountNumber(transaction.getAccountNumber(), customerId).blockOptional().orElse(null);
        if (account == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        if (!account.isActive() || account.isDeleted() || account.isLocked() || !account.isVerified() ) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account not active.");
        Account _account = accountClientService.updateAccountBalance(transaction.getAccountNumber(), account.getBalance() + transaction.getCredit(), customerId).blockOptional().orElse(null);
        if (_account == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cloud not update account balance.");
        transaction.setDebit(0);
        transaction.setBalance(_account.getBalance());
        Transaction _transaction = transactionservice.createTransaction(transaction,Status.COMPLETED);
        return _transaction;
    }

    @PostMapping("/cardTransfer")
    public Transaction createCardTransfer(@RequestBody CardTransactionDao cardTransactionDao, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role) {
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
        Card card = cardClientService.getCardByNumber(transaction.getCardNumber(), customerId).blockOptional().orElse(null);
        Beneficiary beneficiary = accountClientService.getBeneficiaryByBeneficiaryId(transaction.getBeneficiaryId(), customerId).blockOptional().orElse(null);
        if (account == null ) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        if (!account.isActive() || account.isDeleted() || account.isLocked() || !account.isVerified() ) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account not active.");
        if (beneficiary == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Beneficiary not found.");
        if (card == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found.");
        if (!card.isActive() || card.isDeleted() || card.isLocked() || !card.isVerified()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Card not active.");
        if (card.getPin() != cardData.getPin() || card.getCvv() != cardData.getCvv()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid card details.");
        if (account.getAccountNumber() == beneficiary.recieverNumber()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot transfer to same account.");
        Transaction transaction1;
        if (card.getTypeId() == 1){
            if (account.getBalance() < transaction.getDebit()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance.");
            account = accountClientService.updateAccountBalance(transaction.getAccountNumber(), account.getBalance() - transaction.getDebit(), customerId).blockOptional().orElse(null);
            if (account == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cloud not update account balance.");
            transaction.setBalance(account.getBalance());
            transaction1 = transactionservice.createTransaction(transaction, Status.COMPLETED);
            Account beneficiaryAccount = accountClientService.getAccountByAccountNumber(beneficiary.recieverNumber(), customerId).blockOptional().orElse(null);
            if (beneficiaryAccount != null) {
                Account _beneficiaryAccount = accountClientService.updateAccountBalance(beneficiary.recieverNumber(), beneficiaryAccount.getBalance() + transaction.getDebit(), customerId).blockOptional().orElse(null);
                if (_beneficiaryAccount == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cloud not update beneficiary account balance.");
                Transaction transaction2 = Transaction.builder()
                    .accountNumber(beneficiary.recieverNumber())
                    .credit(transaction.getDebit())
                    .debit(0.0)
                    .typeId(4)
                    .balance(beneficiaryAccount.getBalance() + transaction.getDebit())
                    .build();
                transaction2 = transactionservice.createTransaction(transaction2, Status.COMPLETED);
            }
        }
        else{
            CreditCardDetail ccd = cardClientService.getCreditCardDetailByCardId(card.getId(), customerId).blockOptional().orElse(null);
            if (ccd == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit Card Details not found.");
            if (ccd.creditLimit() < ccd.creditUsed() + transaction.getDebit()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient credit balance.");
            CreditCardDetail _ccd = cardClientService.updateCreditCardDetail(new CreditCardDetail(ccd.id(),ccd.cardId(),ccd.creditLimit(),ccd.creditUsed() + transaction.getDebit(),ccd.creditTransactions()+1), customerId).blockOptional().orElse(null);
            if (_ccd == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cloud not update credit card balance.");
            transaction.setBalance(_ccd.creditLimit() - _ccd.creditUsed());
            transaction1 = transactionservice.createTransaction(transaction, Status.COMPLETED);
            Account beneficiaryAccount = accountClientService.getAccountByAccountNumber(beneficiary.recieverNumber(), customerId).blockOptional().orElse(null);
            if (beneficiaryAccount != null) {
                Account _beneficiaryAccount = accountClientService.updateAccountBalance(beneficiary.recieverNumber(), beneficiaryAccount.getBalance() + transaction.getDebit(), customerId).blockOptional().orElse(null);
                if (_beneficiaryAccount == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cloud not update beneficiary account balance.");
                Transaction transaction2 = Transaction.builder()
                    .accountNumber(beneficiary.recieverNumber())
                    .credit(transaction.getDebit())
                    .debit(0.0)
                    .balance(_beneficiaryAccount.getBalance())
                    .typeId(1)
                    .build();
                transaction2 = transactionservice.createTransaction(transaction2, Status.COMPLETED);
            }
        }
        return transaction1;
    }

    @GetMapping("/pay/{number}")
    public ResponseEntity<BillResponse> getBillDetails(@PathVariable long number, @RequestHeader("Customer") String customer, @RequestHeader("Role") Role role) {
        Card card = cardClientService.getCardByNumber(number, customer).blockOptional().orElse(null);
        if (card == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found.");
        if (!card.isActive() || card.isDeleted() || card.isLocked() || !card.isVerified()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Card not active.");
        CreditCardDetail ccd = cardClientService.getCreditCardDetailByCardId(card.getId(), customer).blockOptional().orElse(null);
        if (ccd == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit card details not found.");
        CardType cardType = cardClientService.getCardTypeById(card.getTypeId(), customer).blockOptional().orElse(null);
        if (cardType == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card type not found.");
        List<Transaction> transactions = transactionservice.getTransactionsByAccountNumber(card.getAccountNumber());
        double total = ccd.creditUsed() * (1 + cardType.interest());
        transactions  = transactions.stream().filter(t -> t.getTypeId() == 2).limit(ccd.creditTransactions()).collect(Collectors.toList());
        return ResponseEntity.ok().body(new BillResponse(new Date(),200,transactions,total,cardType.interest()));
    }

    @PostMapping("/pay/{number}")
    public Transaction payCardFees(@PathVariable long number, @RequestBody Transaction transaction, @RequestHeader("Customer") String customerId, @RequestHeader("Role") Role role){
        Card card = cardClientService.getCardByNumber(number, customerId).blockOptional().orElse(null);
        if (card == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found.");
        if (!card.isActive() || card.isDeleted() || card.isLocked() || !card.isVerified()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Card not active.");
        CreditCardDetail ccd = cardClientService.getCreditCardDetailByCardId(card.getId(), customerId).blockOptional().orElse(null);
        if (ccd == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit card details not found.");
        CardType cardType = cardClientService.getCardTypeById(card.getTypeId(), customerId).blockOptional().orElse(null);
        transaction.setDebit(ccd.creditUsed() * (1 + cardType.interest()));
        if ( ccd.creditUsed() < 0 ) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit bill payed already.");
        Account account = accountClientService.getAccountByAccountNumber(transaction.getAccountNumber(), customerId).blockOptional().orElse(null);
        if (account == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        if (!account.isActive() || account.isDeleted() || account.isLocked() || !account.isVerified() ) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account not active.");
        if (account.getBalance() < transaction.getDebit()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance.");
        Account _account = accountClientService.updateAccountBalance(transaction.getAccountNumber(), account.getBalance() - transaction.getDebit(), customerId).blockOptional().orElse(null);
        transaction.setBalance(_account.getBalance());
        transaction.setBeneficiaryId(1);
        transaction.setTypeId(1);
        Transaction _transaction = transactionservice.createTransaction(transaction, Status.COMPLETED);
        CreditCardDetail _ccd = cardClientService.updateCreditCardDetail(new CreditCardDetail(ccd.id(),ccd.cardId(),ccd.creditLimit(),0.0,0), customerId).blockOptional().orElse(null);
        if (_ccd == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cloud not update credit card balance.");
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


    @ExceptionHandler({ DataIntegrityViolationException.class, EmptyResultDataAccessException.class, SQLIntegrityConstraintViolationException.class})
    public void handleSQLException(Exception e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some fields are already used.");
    }

}
 