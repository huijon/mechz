import domain._

import scalaz._, Scalaz._

/**
 * Created by jono on 13/02/17.
 */

object Mechz {
  def main(args: Array[String]): Unit = {
    println("Start mechz")
    val jono = Warrior("NiMR0d")
    val neopy = Hunter("Neopy")
    val heroes = NewHeroPair(jono, neopy)
    //val results = (0 until 10000).map(count => fight(heroes))
    val results = (0 until 10000).map(count => fightInteractive(heroes))
    val draws = results.count(result => result.resultPair.result.isRight)
    val warrior = results.count(result => result.resultPair.result match {
      case -\/(WinnerLoserPair(w: Warrior, _)) => true
      case _ => false
    })
    val hunter = results.count(result => result.resultPair.result match {
      case -\/(WinnerLoserPair(h: Hunter, _)) => true
      case _ => false
    })
    val mage = results.count(result => result.resultPair.result match {
      case -\/(WinnerLoserPair(m: Mage, _)) => true
      case _ => false
    })
    println(s"draws: ${draws}, warrior: ${warrior}, hunter: ${hunter}, mage: ${mage}")
  }

  def fight(heroes: HeroPair, rounds: List[RoundHeroPair] = List()): FightResult = {
    val fightingPair = FightingHeroPair(heroes._1, heroes._2)
    val regennedPair = Hero.regenHeroes(fightingPair)
    val fatiguedPair = Hero.fatigueHeroes(regennedPair, rounds.size)
    Hero.calculateFightResult(heroes) match {
      case Some(resultPair) => FightResult(resultPair, rounds)
      case None =>
        val firstAttack = Hero.computeAttack(fatiguedPair)
        val afterAttack = Hero.computeAttack(firstAttack.swap)
        fight(afterAttack.swap, rounds :+ afterAttack.toRoundHeroPair)
    }
  }

  def fightInteractive(heroes: HeroPair, rounds: List[RoundHeroPair] = List()): FightResult = {
    val fhp = FightingHeroPair(heroes._1, heroes._2)
    val regenPhase = RegenPhase(fhp)
    val regenResult = regenPhase.execute

    println("fatiguing")
    val fatiguePhase = FatiguePhase(regenResult.fhp, rounds.size)
    val fatigueResult = fatiguePhase.execute

    Hero.calculateFightResult(fatigueResult.fhp) match {
      case Some(resultPair) => FightResult(resultPair, rounds)
      case None =>
        // get player actions here
        val interactPhase = InteractPhase(fatigueResult.fhp, List())
        val interactResult = interactPhase.execute

        val endPhase = EndPhase(interactResult.fhp)
        val endResult = endPhase.execute

        fightInteractive(endResult.fhp, rounds :+ endResult.fhp.toRoundHeroPair)
    }
  }
}





