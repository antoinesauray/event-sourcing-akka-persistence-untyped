package net.sauray.example.domain.state;

import java.io.Serializable;

import net.sauray.example.domain.events.*;

/*
 * BankState.java
 * Copyright (C) 2020 antoinesauray <sauray.antoine@gmail.com>
 *
 * Distributed under terms of the MIT license.
 */

public final class BankState implements Serializable
{
  /**
   * Serialization id
   */
  private static final long serialVersionUID = 1L;
  private Long amountCents;

  public BankState(Long amountCents) {
    this.amountCents = amountCents;
  }

  public Long getAmountCents() {
    return amountCents;
  }

public void update(MoneyRemovedFromAccount event) {
    this.amountCents += event.getAmountCents();
  }

  public void update(MoneyAddedOnAccount event) {
    this.amountCents += event.getAmountCents();
  }

  @Override
  public String toString() {
    return String.format("BankAccount: balance is %d", this.amountCents);
  }

}

