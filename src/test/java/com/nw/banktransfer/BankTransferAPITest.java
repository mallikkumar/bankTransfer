
package com.nw.banktransfer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nw.banktransfer.exception.InsufficientFundsException;
import com.nw.banktransfer.exception.UnknownAccountException;
import com.nw.banktransfer.model.Account;
import com.nw.banktransfer.model.Transaction;
import com.nw.banktransfer.service.BankTransferServiceImpl;


@RunWith(SpringRunner.class)
@DataJpaTest
public class BankTransferAPITest {
    @TestConfiguration
    static class BankTransferTestContextConfiguration {
        @Bean
        public BankTransferServiceImpl bankTransferTest() {
            return new BankTransferServiceImpl();
        }
    }

    @Autowired
    private BankTransferServiceImpl bankTransferService;

    List<Account> accounts = Stream.of(new Account(1L, "22", "Ram", new BigDecimal(250)), new Account(2L, "33", "Krishna", new BigDecimal(100))).collect(Collectors.toList());
    @Test
    public void completeTransactionTest() {

//        saveAccounts is tested with 2 accounts
        bankTransferService.saveAccounts(accounts);  

    	//Test the getAllAccounts()
        assertEquals(2, bankTransferService.getAllAccounts().size());

        //Test findByAccountNumber()
    	assertNotNull(bankTransferService.findByAccountNumber("22"));

    	//Test performBankTransfer() 
        Transaction transaction = new Transaction("22", "33", new BigDecimal(45));
        assertTrue(bankTransferService.performBankTransfer(transaction));
        
        //Test getAllTransactions()
        assertThat(bankTransferService.getAllTransactions().size()).isEqualTo(1);
        
    }
    
    @Test(expected = InsufficientFundsException.class)
    public void testInsufficientFunds() {
        bankTransferService.saveAccounts(accounts);  
    	bankTransferService.performBankTransfer(new Transaction("22", "33", new BigDecimal(45444)));
    }
    
    @Test(expected = UnknownAccountException.class)
    public void testUnknownAccountException() {
        bankTransferService.findByAccountNumber("23434");  
    }
}
