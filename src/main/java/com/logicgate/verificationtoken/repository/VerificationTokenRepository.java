package com.logicgate.verificationtoken.repository;

import com.logicgate.verificationtoken.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long>, VerificationTokenRepositoryCustom {
}
