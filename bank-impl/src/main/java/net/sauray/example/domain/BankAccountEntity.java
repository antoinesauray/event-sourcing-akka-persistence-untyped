package net.sauray.example.domain;

import akka.japi.pf.ReceiveBuilder;
import akka.persistence.AbstractPersistentActor;
import akka.persistence.Recovery;
import akka.persistence.SnapshotOffer;
import akka.persistence.SnapshotSelectionCriteria;
import net.sauray.example.domain.commands.*;
import net.sauray.example.domain.events.*;
import net.sauray.example.domain.state.BankState;

/*
 * BankAccountEntity.java
 * Copyright (C) 2020 antoinesauray <sauray.antoine@gmail.com>
 *
 * Distributed under terms of the MIT license.
 */

public class BankAccountEntity extends AbstractPersistentActor
{

  private BankState state = new BankState(0l);

	public BankAccountEntity() {
		
	}

	@Override
	public String persistenceId() {
		return "bank-account";
	}

  public ReceiveBuilder receiveBuilderEvents() {
    return receiveBuilder()
      .match(MoneyRemovedFromAccount.class, state::update)
      .match(MoneyAddedOnAccount.class, state::update);
  }

  @Override
  public Recovery recovery() {
    // disable snapshot recovery
    return Recovery.create(SnapshotSelectionCriteria.none());
  }

	@Override
	public Receive createReceive() {
    return receiveBuilder()
      .match(PutMoneyOnAccount.class, this::handleTransferMoneyCommand)
      .match(RemoveMoneyFromAccount.class, this::handleWithdrawMoneyCommand)
      .match(GetAccountBalance.class, this::handleGetAccountBalance)
      .build();
	}

	@Override
	public Receive createReceiveRecover() {
    return ReceiveBuilder.create()
      .match(MoneyRemovedFromAccount.class, state::update)
      .match(MoneyAddedOnAccount.class, state::update)
      .match(SnapshotOffer.class, ss -> state = (BankState) state)
      .build();
	}

  private void handleGetAccountBalance(GetAccountBalance cmd) {
    getSender().tell(state, getSelf());
  }

  private void handleTransferMoneyCommand(PutMoneyOnAccount cmd) {
    MoneyAddedOnAccount event = new MoneyAddedOnAccount(cmd.getAmountCents());
    persist(event, this::handleMoneyTransferredEvent);
    getSender().tell(event, getSelf());
  }

  private void handleWithdrawMoneyCommand(RemoveMoneyFromAccount cmd) {
    if(state.getAmountCents() - cmd.getAmountCents() > 0) {
      MoneyRemovedFromAccount event = new MoneyRemovedFromAccount(cmd.getAmountCents());
      persist(event, this::handleMoneyWithdrawnEvent);
      getSender().tell(event, getSelf());
    } else {
      // dont know if this is the correct way to reject a command
      throw new IllegalArgumentException();
    }
  }

  private void handleMoneyWithdrawnEvent(MoneyRemovedFromAccount event) {
    state.update(event);
    // snapshot if you want here
  }

  private void handleMoneyTransferredEvent(MoneyAddedOnAccount event) {
    state.update(event);
    // snapshot if you want here
  }
}

