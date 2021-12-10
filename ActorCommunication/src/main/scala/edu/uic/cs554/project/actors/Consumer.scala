package edu.uic.cs554
package project.actors

import akka.actor.{Actor, ActorLogging, Props}
import com.fasterxml.jackson.dataformat.yaml.YAMLParser

object Consumer {
  case object OrderItem
  case object CancelItem

  def props(yamlParser: YAMLParser): Props = Props(new Consumer(yamlParser))
}

class Consumer(yamlParser: YAMLParser) extends Actor with ActorLogging {
  override def receive: Receive = ???
}
