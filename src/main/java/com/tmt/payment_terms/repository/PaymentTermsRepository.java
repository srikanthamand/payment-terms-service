package com.tmt.payment_terms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tmt.payment_terms.entity.PaymentTerm;

@Repository
public interface PaymentTermsRepository extends JpaRepository<PaymentTerm, Long>{
	
	PaymentTerm findByCode(String String);
}
