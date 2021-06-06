package agario.domain.actor

import akka.actor.SupervisorStrategy.{Resume, Stop}
import akka.actor.{ActorInitializationException, ActorKilledException, DeathPactException, OneForOneStrategy, SupervisorStrategy, SupervisorStrategyConfigurator}

class SupervisorConfigurator extends SupervisorStrategyConfigurator {
  override def create(): SupervisorStrategy = OneForOneStrategy() {
    case _: ActorInitializationException => Stop
    case _: ActorKilledException => Stop
    case _: DeathPactException => Stop
    case _: Exception => Resume
  }
}
