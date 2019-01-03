package com.moneymoney.account.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.moneymoney.account.CurrentAccount;
import com.moneymoney.account.dao.CurrentAccountDAO;
import com.moneymoney.account.dao.CurrentAccountDAOImpl;
import com.moneymoney.account.factory.AccountFactory;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;
import com.moneymoney.exception.InsufficientFundsException;
import com.moneymoney.exception.InvalidInputException;

public class CurrentAccountServiceImpl implements CurrentAccountService {

	private AccountFactory factory;
	private CurrentAccountDAO CurrentAccountDAO;

	public CurrentAccountServiceImpl() {
		factory = AccountFactory.getInstance();
		CurrentAccountDAO = new CurrentAccountDAOImpl();
	}

	@Override
	public CurrentAccount createNewAccount(String accountHolderName, double accountBalance, double odLimit)
			throws ClassNotFoundException, SQLException {
		CurrentAccount account = factory.createNewCurrentAccount(accountHolderName, accountBalance, odLimit);
		int accountId=CurrentAccountDAO.createNewAccount(account);
		account=new CurrentAccount(accountId,account.getBankAccount().getAccountHolderName(),
				account.getBankAccount().getAccountBalance(),account.getOdLimit());
		return account;
	}

	@Override
	public List<CurrentAccount> getAllCurrentAccount() throws ClassNotFoundException, SQLException {
		return CurrentAccountDAO.getAllCurrentAccount();
	}

	@Override
	public void deposit(CurrentAccount account, double amount) throws ClassNotFoundException, SQLException {
		if (amount > 0) {
			double currentBalance = account.getBankAccount().getAccountBalance();
			currentBalance += amount;
			CurrentAccountDAO.updateBalance(account.getBankAccount().getAccountNumber(), currentBalance);
			//CurrentAccountDAO.commit();
		}else {
			throw new InvalidInputException("Invalid Input Amount!");
		}
	}
	@Override
	public void withdraw(CurrentAccount account, double amount) throws ClassNotFoundException, SQLException {
		double currentBalance = account.getBankAccount().getAccountBalance();
		if (amount > 0 && currentBalance+account.getOdLimit() >= amount) {
			currentBalance -= amount;
			CurrentAccountDAO.updateBalance(account.getBankAccount().getAccountNumber(), currentBalance);
			//CurrentAccountDAO.commit();
		} else {
			throw new InsufficientFundsException("Invalid Input or Insufficient Funds!");
		}
	}

	@Override
	public void fundTransfer(CurrentAccount sender, CurrentAccount receiver, double amount)
			throws ClassNotFoundException, SQLException {
		try {
			withdraw(sender, amount);
			deposit(receiver, amount);
			DBUtil.commit();
		} catch (InvalidInputException | InsufficientFundsException e) {
			e.printStackTrace();
			DBUtil.rollback();
		} catch(Exception e) {
			e.printStackTrace();
			DBUtil.rollback();
		}
	}

	

	@Override
	public CurrentAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		return CurrentAccountDAO.getAccountById(accountNumber);
	}

	@Override
	public boolean deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		 
		return CurrentAccountDAO.deleteAccount(accountNumber);
	}

	@Override
	public double getAccountBalance(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		
 		return CurrentAccountDAO.getAccountBalance(accountNumber);
	}

	@Override
	public CurrentAccount getAccountByName(String accountHolderName) throws ClassNotFoundException, AccountNotFoundException, SQLException {

		return CurrentAccountDAO.getAccountByName(accountHolderName);
 
	}

	

	@Override
	public List<CurrentAccount> sortAllAccount(int option, int sortBy) throws ClassNotFoundException, SQLException {
		List<CurrentAccount> inputAccountList = new ArrayList<CurrentAccount>();
		inputAccountList = getAllCurrentAccount();
		switch(option)
		{
		case 1:sortAccountsByAccountNumber(inputAccountList,sortBy);
	 			break;
		case 2:sortAccountsByAccountHolderName(inputAccountList,sortBy);
			break;
		case 3:sortAccountsByAccountBalance(inputAccountList,sortBy);
		break;
		}
		return inputAccountList;
	}
	
	

	private List<CurrentAccount> sortAccountsByAccountBalance(List<CurrentAccount> inputAccountList, int sortBy) {
		if(sortBy == 1)
		{
			Collections.sort(inputAccountList, new Comparator<CurrentAccount>()
				{
					@Override
					public int compare(CurrentAccount accountOne, CurrentAccount accountTwo) {
					if(accountOne.getBankAccount().getAccountBalance() < accountTwo.getBankAccount().getAccountBalance())
						return -1;
					else if(accountOne.getBankAccount().getAccountBalance() == accountTwo.getBankAccount().getAccountBalance())
						return 0;
					else
					return  1;
					}
			
				}
			);
			return inputAccountList;
		}
		else if(sortBy == 2)
		{
			Collections.sort(inputAccountList, new Comparator<CurrentAccount>()
		 	{

						@Override
						public int compare(CurrentAccount accountOne, CurrentAccount accountTwo) {
							if(accountOne.getBankAccount().getAccountBalance() < accountTwo.getBankAccount().getAccountBalance())
								return 1;
							else if(accountOne.getBankAccount().getAccountBalance() == accountTwo.getBankAccount().getAccountBalance())
								return 0;
							else
							return  -1;
						}
					 
				
		 	});
		}
		return inputAccountList;
		
	}

	public List<CurrentAccount> sortAccountsByAccountNumber( List<CurrentAccount> inputAccountList ,int sortBy )
	{
		if(sortBy == 1)
		{
			Collections.sort(inputAccountList, new Comparator<CurrentAccount>()
				{
					@Override
					public int compare(CurrentAccount accountOne, CurrentAccount accountTwo) {
					if(accountOne.getBankAccount().getAccountNumber() < accountTwo.getBankAccount().getAccountNumber())
						return -1;
					else if(accountOne.getBankAccount().getAccountNumber() == accountTwo.getBankAccount().getAccountNumber())
						return 0;
					else
					return  1;
					}
			
				}
			);
			return inputAccountList;
		}
		else if(sortBy == 2)
		{
			Collections.sort(inputAccountList, new Comparator<CurrentAccount>()
		 	{

						@Override
						public int compare(CurrentAccount accountOne, CurrentAccount accountTwo) {
							if(accountOne.getBankAccount().getAccountNumber() < accountTwo.getBankAccount().getAccountNumber())
								return 1;
							else if(accountOne.getBankAccount().getAccountNumber() == accountTwo.getBankAccount().getAccountNumber())
								return 0;
							else
							return  -1;
						}
					 
				
		 	});
		}
		return inputAccountList;
	}
	
	private List<CurrentAccount> sortAccountsByAccountHolderName(List<CurrentAccount> inputAccountList, int sortBy) {
		if(sortBy == 1)
		{
			Collections.sort(inputAccountList, new Comparator<CurrentAccount>()
				{
					@Override
					public int compare(CurrentAccount accountOne, CurrentAccount accountTwo) {
					return  accountOne.getBankAccount().getAccountHolderName().compareToIgnoreCase(accountTwo.getBankAccount().getAccountHolderName());
					}
			
				}
			);
			return inputAccountList;
		}
		else if(sortBy == 2)
		{
			Collections.sort(inputAccountList, new Comparator<CurrentAccount>()
			{
				@Override
				public int compare(CurrentAccount accountOne, CurrentAccount accountTwo) {
				return  -accountOne.getBankAccount().getAccountHolderName().compareToIgnoreCase(accountTwo.getBankAccount().getAccountHolderName());
				}
			});
		}
		return inputAccountList;
	}

	@Override
	public CurrentAccount updateAccountInfo(CurrentAccount CurrentAccount) throws ClassNotFoundException, SQLException, AccountNotFoundException {

		return CurrentAccountDAO.updateAccountInfo(CurrentAccount);
	}

	@Override
	public List<CurrentAccount> getAccountByBalRange(double minimumBalance,
			double maxBalance) throws ClassNotFoundException, SQLException {
		
		return CurrentAccountDAO.getAccountByBalRange( minimumBalance, maxBalance);
	}

	
}
