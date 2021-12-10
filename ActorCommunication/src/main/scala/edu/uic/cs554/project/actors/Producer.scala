package edu.uic.cs554.project.actors

import akka.actor.{Actor, ActorLogging, Props}
import edu.uic.cs554.project.actors.Producer.FetchItem

object Producer {
  case object FetchItem

  def props(): Props = Props(new Producer())
}

class Producer extends Actor with ActorLogging {
  override def receive: Receive = {
    case FetchItem =>

  }
}
