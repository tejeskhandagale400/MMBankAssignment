package com.moneymoney.account.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.moneymoney.account.CurrentAccount;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;

public class CurrentAccountDAOImpl implements CurrentAccountDAO {

	public int createNewAccount(CurrentAccount account) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
				("INSERT INTO ACCOUNT VALUES(?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setInt(1, account.getBankAccount().getAccountNumber());
		preparedStatement.setString(2, account.getBankAccount().getAccountHolderName());
		preparedStatement.setDouble(3, account.getBankAccount().getAccountBalance());
		preparedStatement.setObject(4, null);
		preparedStatement.setDouble(5, account.getOdLimit());
		preparedStatement.setString(6, "CA");
		int i = preparedStatement.executeUpdate();
		int accountId=0;
		if(i==1)
		{
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	            	accountId=(int) (generatedKeys.getInt(1));
	            	System.out.println(accountId);
	            }
	            else {
	                throw new SQLException("Creating user failed, no ID obtained.");
	            }
	        }
		}
		//System.out.println(i);
		preparedStatement.close();
		DBUtil.commit();
		return accountId;
	}

	public List<CurrentAccount> getAllCurrentAccount() throws ClassNotFoundException, SQLException {
		List<CurrentAccount> CurrentAccounts = new ArrayList<>();
		Connection connection = DBUtil.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM ACCOUNT WHERE type like");
		while (resultSet.next()) {// Check if row(s) is present in table
			int accountNumber = resultSet.getInt(1);
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			double odLimit = resultSet.getDouble("od_limit");
			CurrentAccount CurrentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,
					odLimit);
			CurrentAccounts.add(CurrentAccount);
		}
		DBUtil.commit();
		return CurrentAccounts;
	}
	@Override
	public void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(false);
		PreparedStatement preparedStatement = connection.prepareStatement
				("UPDATE ACCOUNT SET account_balance=? where account_number=?");
		preparedStatement.setDouble(1, currentBalance);
		preparedStatement.setInt(2, accountNumber);
		preparedStatement.executeUpdate();
	}
	
	@Override
	public CurrentAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
				("SELECT * FROM account where account_number=?");
		preparedStatement.setInt(1, accountNumber);
		ResultSet resultSet = preparedStatement.executeQuery();
		CurrentAccount CurrentAccount = null;
		if(resultSet.next()) {
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			double odLimit = resultSet.getDouble("od_limit");
			CurrentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,
					odLimit);
			return CurrentAccount;
		}
		throw new AccountNotFoundException("Account with account number "+accountNumber+" does not exist.");
	}
	
	@Override
	public CurrentAccount getAccountByName(String accountHolderName) throws AccountNotFoundException, SQLException, ClassNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
				("SELECT * FROM account where account_hn like ?");
		preparedStatement.setString(1, accountHolderName);
		ResultSet resultSet = preparedStatement.executeQuery();
		CurrentAccount CurrentAccount = null;
		if(resultSet.next()) {
			int accountNumber = resultSet.getInt(1);
			double accountBalance = resultSet.getDouble(3);
			double odLimit = resultSet.getDouble("od_limit");
			CurrentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,
					odLimit);
			return CurrentAccount;
		}
		throw new AccountNotFoundException("Account with account holder name "+accountHolderName+" does not exist.");
	}
	
	
	 

	

	@Override
	public boolean deleteAccount(int accountNumber) throws SQLException, ClassNotFoundException, AccountNotFoundException {
	Connection connection = DBUtil.getConnection();
	connection.setAutoCommit(false);
	if(  getAccountById(accountNumber).getBankAccount().getAccountNumber() == accountNumber)
	{	
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE From account  WHERE account_number = ?");
		preparedStatement.setInt(1, accountNumber);
		preparedStatement.executeUpdate();
		DBUtil.commit();
		return true;
	}
	else  return false;
	}


	@Override
	public double getAccountBalance(int accountNumber) throws SQLException, AccountNotFoundException, ClassNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
				("SELECT * FROM account where account_number=?");
		preparedStatement.setInt(1, accountNumber);
		ResultSet resultSet = preparedStatement.executeQuery();
		 
		if(resultSet.next()) {
			double accountBalance = resultSet.getDouble(3);
			return accountBalance;
		}
		throw new AccountNotFoundException("Account with account number "+accountNumber+" does not exist.");
		 
	}

	

	@Override
	public CurrentAccount updateAccountInfo(CurrentAccount CurrentAccount) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
				("UPDATE account SET account_hn=? , od_limit=? WHERE account_number=?");
		preparedStatement.setString(1, CurrentAccount.getBankAccount().getAccountHolderName());
		preparedStatement.setDouble(2, CurrentAccount.getOdLimit());
		preparedStatement.setInt(3, CurrentAccount.getBankAccount().getAccountNumber());
		preparedStatement.executeUpdate();
		return getAccountById(CurrentAccount.getBankAccount().getAccountNumber());
	}

	@Override
	public List<CurrentAccount> getAccountByBalRange(double minimumBalance,
			double maxBalance) throws ClassNotFoundException, SQLException {
		List<CurrentAccount> CurrentAccounts = new ArrayList<>();
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
				("SELECT * FROM account WHERE account_balance BETWEEN  ? AND ? ");
		preparedStatement.setDouble(1,minimumBalance);
		preparedStatement.setDouble(2, maxBalance);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {// Check if row(s) is present in table
			int accountNumber = resultSet.getInt(1);
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			double odLimit = resultSet.getDouble("od_limit");
			CurrentAccount CurrentAccount = new CurrentAccount(accountNumber, accountHolderName, accountBalance,
					odLimit);
			CurrentAccounts.add(CurrentAccount);
		}
		
		return CurrentAccounts;
	}


	

}
