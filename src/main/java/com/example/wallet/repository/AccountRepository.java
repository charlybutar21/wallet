package com.example.wallet.repository;

import com.example.wallet.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findFirstByToken(String token);
    Optional<Account> findByUsername(String username);

    @Query("SELECT a FROM Account a WHERE " +
            "(:keyword IS NULL OR LOWER(a.accountHolderName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:accountNumber IS NULL OR a.accountNumber = :accountNumber)")
    Page<Account> searchByKeywordAndAccountNumber(
            @Param("keyword") String keyword,
            @Param("accountNumber") Long accountNumber,
            Pageable pageable
    );

}
