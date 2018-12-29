package com.moneymoney.account.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.dao.SavingsAccountDAO;
import com.moneymoney.account.dao.SavingsAccountDAOImpl;
import com.moneymoney.account.factory.AccountFactory;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;
import com.moneymoney.exception.InsufficientFundsException;
import com.moneymoney.exception.InvalidInputException;

public class SavingsAccountServiceImpl implements SavingsAccountService {

	private AccountFactory factory;
	private SavingsAccountDAO savingsAccountDAO;

	public SavingsAccountServiceImpl() {
		factory = AccountFactory.getInstance();
		savingsAccountDAO = new SavingsAccountDAOImpl();
	}

	@Override
	public SavingsAccount createNewAccount(String accountHolderName, double accountBalance, boolean salary)
			throws ClassNotFoundException, SQLException {
		SavingsAccount account = factory.createNewSavingsAccount(accountHolderName, accountBalance, salary);
		int accountId=savingsAccountDAO.createNewAccount(account);
		account=new SavingsAccount(accountId,account.getBankAccount().getAccountHolderName(),
				account.getBankAccount().getAccountBalance(),account.isSalary());
		return account;
	}

	@Override
	public List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException {
		return savingsAccountDAO.getAllSavingsAccount();
	}

	@Override
	public void deposit(SavingsAccount account, double amount) throws ClassNotFoundException, SQLException {
		if (amount > 0) {
			double currentBalance = account.getBankAccount().getAccountBalance();
			currentBalance += amount;
			savingsAccountDAO.updateBalance(account.getBankAccount().getAccountNumber(), currentBalance);
			//savingsAccountDAO.commit();
		}else {
			throw new InvalidInputException("Invalid Input Amount!");
		}
	}
	@Override
	public void withdraw(SavingsAccount account, double amount) throws ClassNotFoundException, SQLException {
		double currentBalance = account.getBankAccount().getAccountBalance();
		if (amount > 0 && currentBalance >= amount) {
			currentBalance -= amount;
			savingsAccountDAO.updateBalance(account.getBankAccount().getAccountNumber(), currentBalance);
			//savingsAccountDAO.commit();
		} else {
			throw new InsufficientFundsException("Invalid Input or Insufficient Funds!");
		}
	}

	@Override
	public void fundTransfer(SavingsAccount sender, SavingsAccount receiver, double amount)
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
	public SavingsAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		return savingsAccountDAO.getAccountById(accountNumber);
	}

	@Override
	public boolean deleteAccount(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		 
		return savingsAccountDAO.deleteAccount(accountNumber);
	}

	@Override
	public double getAccountBalance(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		
 		return savingsAccountDAO.getAccountBalance(accountNumber);
	}

	@Override
	public SavingsAccount getAccountByName(String accountHolderName) throws ClassNotFoundException, AccountNotFoundException, SQLException {

		return savingsAccountDAO.getAccountByName(accountHolderName);
 
	}

	

	@Override
	public List<SavingsAccount> sortAllAccount(int option, int sortBy) throws ClassNotFoundException, SQLException {
		List<SavingsAccount> inputAccountList = new ArrayList<SavingsAccount>();
		inputAccountList = getAllSavingsAccount();
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
	
	

	private List<SavingsAccount> sortAccountsByAccountBalance(List<SavingsAccount> inputAccountList, int sortBy) {
		if(sortBy == 1)
		{
			Collections.sort(inputAccountList, new Comparator<SavingsAccount>()
				{
					@Override
					public int compare(SavingsAccount accountOne, SavingsAccount accountTwo) {
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
			Collections.sort(inputAccountList, new Comparator<SavingsAccount>()
		 	{

						@Override
						public int compare(SavingsAccount accountOne, SavingsAccount accountTwo) {
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

	public List<SavingsAccount> sortAccountsByAccountNumber( List<SavingsAccount> inputAccountList ,int sortBy )
	{
		if(sortBy == 1)
		{
			Collections.sort(inputAccountList, new Comparator<SavingsAccount>()
				{
					@Override
					public int compare(SavingsAccount accountOne, SavingsAccount accountTwo) {
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
			Collections.sort(inputAccountList, new Comparator<SavingsAccount>()
		 	{

						@Override
						public int compare(SavingsAccount accountOne, SavingsAccount accountTwo) {
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
	
	private List<SavingsAccount> sortAccountsByAccountHolderName(List<SavingsAccount> inputAccountList, int sortBy) {
		if(sortBy == 1)
		{
			Collections.sort(inputAccountList, new Comparator<SavingsAccount>()
				{
					@Override
					public int compare(SavingsAccount accountOne, SavingsAccount accountTwo) {
					return  accountOne.getBankAccount().getAccountHolderName().compareToIgnoreCase(accountTwo.getBankAccount().getAccountHolderName());
					}
			
				}
			);
			return inputAccountList;
		}
		else if(sortBy == 2)
		{
			Collections.sort(inputAccountList, new Comparator<SavingsAccount>()
			{
				@Override
				public int compare(SavingsAccount accountOne, SavingsAccount accountTwo) {
				return  -accountOne.getBankAccount().getAccountHolderName().compareToIgnoreCase(accountTwo.getBankAccount().getAccountHolderName());
				}
			});
		}
		return inputAccountList;
	}

	@Override
	public SavingsAccount updateAccountInfo(SavingsAccount savingsAccount) throws ClassNotFoundException, SQLException, AccountNotFoundException {

		return savingsAccountDAO.updateAccountInfo(savingsAccount);
	}

	@Override
	public List<SavingsAccount> getAccountByBalRange(double minimumBalance,
			double maxBalance) throws ClassNotFoundException, SQLException {
		
		return savingsAccountDAO.getAccountByBalRange( minimumBalance, maxBalance);
	}

	
}
