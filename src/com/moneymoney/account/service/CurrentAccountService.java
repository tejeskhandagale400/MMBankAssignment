package com.moneymoney.account.service;

import java.sql.SQLException;
import java.util.List;

import com.moneymoney.account.CurrentAccount;
import com.moneymoney.exception.AccountNotFoundException;

public interface CurrentAccountService {

	CurrentAccount createNewAccount(String accountHolderName, double accountBalance, double odLimit) throws ClassNotFoundException, SQLException;

	CurrentAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	boolean deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	
	List<CurrentAccount> getAllCurrentAccount() throws ClassNotFoundException, SQLException;

	void fundTransfer(CurrentAccount sender, CurrentAccount receiver, double amount) throws ClassNotFoundException, SQLException;
	void deposit(CurrentAccount account, double amount) throws ClassNotFoundException, SQLException;
	void withdraw(CurrentAccount account, double amount) throws ClassNotFoundException, SQLException;
	
	double getAccountBalance(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	CurrentAccount getAccountByName(String accountHolderName) throws ClassNotFoundException, AccountNotFoundException, SQLException;
	
	List<CurrentAccount> sortAllAccount(int option, int sortBy) throws ClassNotFoundException, SQLException ;

	CurrentAccount updateAccountInfo(CurrentAccount CurrentAccount) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	List<CurrentAccount> getAccountByBalRange(double minimumBalance,
			double maxBalance) throws ClassNotFoundException, SQLException;
}