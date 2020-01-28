package net.sauray.example.domain.commands;

import net.sauray.example.domain.Utils;

/*
 * WithdrawMoney.java
 * Copyright (C) 2020 antoinesauray <sauray.antoine@gmail.com>
 *
 * Distributed under terms of the MIT license.
 */

public class RemoveMoneyFromAccount implements BankCommand
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
  private final Long amountCents;

	public RemoveMoneyFromAccount(Long amountCents) {
    Utils.validateIsPositive(amountCents);
    this.amountCents = amountCents;	
	}

	public Long getAmountCents() {
		return amountCents;
	}
}

