package domain

/**
  * Created by jono on 15/02/2017.
  */

case object AllPhases {
  val order = List(RegenPhase, FatiguePhase, InteractPhase, EndPhase)
}

trait FightPhase {
  def order: Int
  def actions: List[PlayerAction]
  def execute: PhaseResult
  def fhp: FightingHeroPair
}

//val regennedPair = Hero.regenHeroes(fightingPair)
//val fatiguedPair = Hero.fatigueHeroes(regennedPair, rounds.size)

case class RegenPhase(fhp: FightingHeroPair, actions: List[PlayerAction] = List()) extends FightPhase {
  val order = 0
  def execute = {
    PhaseResult(Hero.regenHeroes(fhp))
  }
}
case class FatiguePhase(fhp: FightingHeroPair, roundCount: Int, actions: List[PlayerAction] = List()) extends FightPhase {
  val order = 1
  def execute = {
    PhaseResult(Hero.fatigueHeroes(fhp, roundCount))
  }
}
case class InteractPhase(fhp: FightingHeroPair, actions: List[PlayerAction] = List()) extends FightPhase {
  val order = 2
  def execute = {
    PhaseResult(fhp)
  }
}
case class EndPhase(fhp: FightingHeroPair, actions: List[PlayerAction] = List()) extends FightPhase {
  val order = 3
  def execute = {
    PhaseResult(fhp)
  }
}

//case class ExecutePhase(phase: FightPhase) {
//  val phaseResult = phase.execute
//  phase match {
//    case p: RegenPhase =>
//    case p: FatiguePhase =>
//    case p: InteractPhase =>
//    case p: EndPhase =>
//  }
//}

//stuff that the server needs to return to the users
case class PhaseResult(fhp: FightingHeroPair)

// actions carried out, e.g. hero1 attacks hero2
// hero1 casts heal or armour up
// hero2 plays dodge card
case class PlayerAction()