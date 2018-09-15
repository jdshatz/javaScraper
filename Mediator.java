public interface SearchMediator {
  public void send(String Query, Colleague colleague);
}

public abstract Colleague{
  private Mediator mediator;
  public Colleague(Mediator m) {
    mediator = m;
  }
  //send a message via the mediator
  public void send(String message) {
    mediator.send(message, this);
  }
  //get access to the mediator
  public Mediator getMediator() {return mediator;}
  public abstract void receive(String message);
}

public class ApplicationMediator implements Mediator {
  private ArrayList<Colleague> colleagues;
  public ApplicationMediator() {
    colleagues = new ArrayList<Colleague>();
  }
  public void addColleague(Colleague colleague) {
    colleagues.add(colleague);
  }
  public void send(String message, Colleague originator) {
    //let all other screens know that this screen has changed
    for(Colleague colleague: colleagues) {
      //don't tell ourselves
      if(colleague != originator) {
        colleage.receive(message);
      }
    }
  }
}

public class ConcreteColleague extends Colleague {
  public void receive(String message) {
    System.out.println("Colleague Received: " + message);
  }
}

public class MobileColleague extends Colleague {
  public void receive(String message) {
    System.out.println("Mobile Received: " + message);
  }
}