package net.sauray.example.domain.events;

/*
 * MoneyWithdrawn.java
 * Copyright (C) 2020 antoinesauray <sauray.antoine@gmail.com>
 *
 * Distributed under terms of the MIT license.
 */

public class MoneyAddedOnAccount implements BankEvent
{
  /**
	 *
	 */
	private static final long serialVersionUID = 1L;

  private final Long amountCents;

	public MoneyAddedOnAccount(Long amountCents) {
    	this.amountCents = amountCents;	
	}

	public Long getAmountCents() {
		return amountCents;
	}
}

