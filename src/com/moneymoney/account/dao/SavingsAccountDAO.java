package com.moneymoney.account.dao;

import java.sql.SQLException;
import java.util.List;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.exception.AccountNotFoundException;

public interface SavingsAccountDAO {
	
	SavingsAccount createNewAccount(SavingsAccount account) throws ClassNotFoundException, SQLException;
	SavingsAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	SavingsAccount deleteAccount(int accountNumber);
	List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException;
	void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException;
	double getAccountBalance(int accountNumber) throws SQLException, AccountNotFoundException, ClassNotFoundException;
	SavingsAccount getAccountByName(String accountHolderName) throws AccountNotFoundException, SQLException, ClassNotFoundException;
	SavingsAccount updateAccount(int accountNumber, int input, String newName) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	
	
}
