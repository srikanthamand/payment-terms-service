package com.tmt.payment_terms.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmt.payment_terms.entity.PaymentTerm;
import com.tmt.payment_terms.exception.BadRequestException;
import com.tmt.payment_terms.exception.RecordNotFoundException;
import com.tmt.payment_terms.repository.PaymentTermsRepository;

@RestController
@RequestMapping("/api")
public class PaymentTermController {

	private static final Logger logger = LoggerFactory.getLogger(PaymentTermController.class);

	@Autowired
	PaymentTermsRepository paymentTermsRepository;

	// Get All PaymentTerms
	@GetMapping("/paymentTerms")
	public List<PaymentTerm> getAllPaymentTerms() {
		logger.info("GET: paymentTerms api called");
		return paymentTermsRepository.findAll();
	}

	// Create a new PaymentTerm
	@PostMapping("/paymentTerms")
	public ResponseEntity<PaymentTerm> createPaymentTerm(@Valid @RequestBody PaymentTerm paymentTerm) {
		logger.info("post: paymentTerms api called");
		if (paymentTerm.getDays() < paymentTerm.getRemindBeforeDays()) {
			throw new BadRequestException("RemindBeforeDays should not greater than Days field");
		}
		paymentTerm = paymentTermsRepository.save(paymentTerm);
		return new ResponseEntity<PaymentTerm>(paymentTerm, HttpStatus.OK);
	}

	// Get a Single PaymentTerm
	@GetMapping("/paymentTerms/{id}")
	public ResponseEntity<PaymentTerm> getPaymentTermById(@PathVariable(value = "id") Long paymentTermId) {
		PaymentTerm paymentTerm = paymentTermsRepository.findById(paymentTermId)
				.orElseThrow(() -> new RecordNotFoundException("PaymentTerm id : " + paymentTermId + " does no exist"));
		return new ResponseEntity<PaymentTerm>(paymentTerm, HttpStatus.OK);
	}

	// Update a PaymentTerm
	@PutMapping("/paymentTerms/{id}")
	public ResponseEntity<PaymentTerm> updatePaymentTerm(@PathVariable(value = "id") Long paymentTermId,
			@RequestBody PaymentTerm paymentTermDetails) {

		PaymentTerm paymentTerm = paymentTermsRepository.findById(paymentTermId)
				.orElseThrow(() -> new RecordNotFoundException("PaymentTerm id : " + paymentTermId + " does no exist"));

		if (!StringUtils.isEmpty(paymentTermDetails.getCode())) {
			paymentTerm.setCode(paymentTermDetails.getCode());
		}
		if (!StringUtils.isEmpty(paymentTermDetails.getDescription())) {
			paymentTerm.setDescription(paymentTermDetails.getDescription());
		}
		if (paymentTermDetails.getDays() != null) {
			paymentTerm.setDays(paymentTermDetails.getDays());
		}

		if (paymentTermDetails.getRemindBeforeDays() != null) {
			paymentTerm.setRemindBeforeDays(paymentTermDetails.getRemindBeforeDays());
		}

		if (paymentTerm.getDays() < paymentTerm.getRemindBeforeDays()) {
			throw new BadRequestException("RemindBeforeDays should not greater than Days field");
		}

		PaymentTerm updatedPaymentTerm = paymentTermsRepository.save(paymentTerm);
		return new ResponseEntity<PaymentTerm>(updatedPaymentTerm, HttpStatus.OK);
	}

	// Delete a PaymentTerm
	@DeleteMapping("/paymentTerms/{id}")
	public ResponseEntity<?> deletePaymentTerm(@PathVariable(value = "id") Long paymentTermId) {
		PaymentTerm paymentTerm = paymentTermsRepository.findById(paymentTermId)
				.orElseThrow(() -> new RecordNotFoundException("PaymentTerm id : " + paymentTermId + " does no exist"));
		paymentTermsRepository.delete(paymentTerm);

		return ResponseEntity.ok().build();
	}

}
