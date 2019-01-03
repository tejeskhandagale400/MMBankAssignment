package com.moneymoney.account.dao;

import java.sql.SQLException;
import java.util.List;

import com.moneymoney.account.CurrentAccount;
import com.moneymoney.exception.AccountNotFoundException;

public interface CurrentAccountDAO {
	
	int createNewAccount(CurrentAccount account) throws ClassNotFoundException, SQLException;
	CurrentAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	boolean deleteAccount(int accountNumber) throws SQLException, ClassNotFoundException, AccountNotFoundException;
	List<CurrentAccount> getAllCurrentAccount() throws ClassNotFoundException, SQLException;
	void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException;
	double getAccountBalance(int accountNumber) throws SQLException, AccountNotFoundException, ClassNotFoundException;
	CurrentAccount getAccountByName(String accountHolderName) throws AccountNotFoundException, SQLException, ClassNotFoundException;
	CurrentAccount updateAccountInfo(CurrentAccount CurrentAccount) throws ClassNotFoundException, SQLException, AccountNotFoundException;
	List<CurrentAccount> getAccountByBalRange
	(double minimumBalance,	double maxBalance) throws ClassNotFoundException, SQLException;
	
	
 
}
