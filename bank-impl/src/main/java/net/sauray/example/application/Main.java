package net.sauray.example.application;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;

import net.sauray.example.domain.BankAccountEntity;
import net.sauray.example.domain.commands.*;

public class Main {
  public static void main(String[] args) {
    System.out.println("Hello"); 
    ActorSystem system = ActorSystem.create();

    Inbox inbox = Inbox.create(system);

    Props props1 = Props.create(BankAccountEntity.class);
    ActorRef ref = system.actorOf(props1, "bank-account-1");

    inbox.watch(ref);

    ref.tell(new PutMoneyOnAccount(10l), ActorRef.noSender());
    ref.tell(new RemoveMoneyFromAccount(1l), ActorRef.noSender());

    inbox.send(ref, new GetAccountBalance());
    try {
		System.out.println(inbox.receive(Duration.ofSeconds(3)));
	} catch (TimeoutException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

  }
}
