package com.nw.banktransfer.service;

import java.util.List;

import com.nw.banktransfer.model.Account;
import com.nw.banktransfer.model.Transaction;

/**
 * This interface is a facade to the service layer for managing the actual service layer
 * @author Mallik Kumar
 *
 */
public interface BankTransferService {

	public void saveAccounts(List<Account> accounts);
	public List<Account> getAllAccounts();
	public Account findByAccountNumber(String accountNumber);
	public List<Transaction> getAllTransactions();
	public boolean performBankTransfer(Transaction transaction);
}
