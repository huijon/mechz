package domain

import java.text.DecimalFormat
import scalaz.{\/, \/-, -\/}

trait Hero {
  def name: String
  def `class`: String
  def regen: Double
  def hp: Double
  def maxHp: Double
  def attack: Attack
  def armour: Double
  def crit: Double
  override def toString = {
    val df = new DecimalFormat("#.##")
    val hpStr = df.format(hp)
    s"$name (${`class`}) $hpStr"
  }
}

case class Warrior(name: String, hp: Double = 100) extends Hero {
  val `class` = "Warrior"
  val attack = Punch(4.0)
  val armour = 3.0
  val regen = 1.0
  val maxHp = 100.0
  val crit = 0.00
}

case class Hunter(name: String, hp: Double = 100) extends Hero {
  val `class` = "Hunter"
  val attack = Shoot(5.0)
  val regen = 0.5
  val armour = 2.0
  val maxHp = 100.0
  val crit = 0.1
}

case class Mage(name: String, hp: Double = 100) extends Hero {
  val `class` = "Mage"
  val attack = FrostBolt(6.0)
  val regen = 0.05
  val armour = 0.15
  val maxHp = 100.0
  val crit = 0.3
}

trait HeroPair {
  def _1: Hero
  def _2: Hero
  override def toString = {
    s"${this._1}\n${this._2}"
  }
}

case class NewHeroPair(_1: Hero, _2: Hero) extends HeroPair

case class FightingHeroPair(attacker: Hero, defender: Hero) extends HeroPair {
  def _1 = attacker
  def _2 = defender
  def swap: FightingHeroPair = {
    FightingHeroPair(defender, attacker)
  }
  def toRoundHeroPair = {
    RoundHeroPair(_1, _2)
  }
}

trait FightResultPairTrait {
  def result: HeroPair \/ HeroPair
}

case class WinnerLoserPair(winner: Hero, loser: Hero) extends HeroPair {
  def _1 = winner
  def _2 = loser
}

case class DrawPair(_1: Hero, _2: Hero) extends HeroPair

case class FightResultPair(result: WinnerLoserPair \/ DrawPair) extends HeroPair with FightResultPairTrait {
  def _1 = result match {
    case -\/(winloss) => winloss.winner
    case \/-(draw) => draw._1
  }

  def _2 = result match {
    case -\/(winloss) => winloss.loser
    case \/-(draw) => draw._2
  }

  def display(): Unit = {
    val str = result match {
      case -\/(winloss) => s"Winner: ${winloss.winner}\nLoser: ${winloss.loser}"
      case \/-(draw) => s"Draw! ${draw._1} and ${draw._2}"
    }
    println(s"\n$str")
  }
}

case class RoundHeroPair(_1: Hero, _2: Hero) extends HeroPair

case class FightResult(resultPair: FightResultPair, rounds: List[RoundHeroPair]) {
  def display() = {
    rounds.indices.foreach(num => {
      println(s"-----$num-----\n${rounds(num)}\n")
    })
    resultPair.display()
  }
}

case class Damage(base: Double, crit: Double) {
  val total = base + crit
}

case object Hero {

  def calculatDamage(attacker: Hero): Damage = {
    if (Math.random() < attacker.crit) {
      Damage(attacker.attack.damage, attacker.attack.damage)
    } else {
      Damage(attacker.attack.damage, 0)
    }
  }

  def computeAttack(heroes: FightingHeroPair): FightingHeroPair  = {
    val attacker = heroes.attacker
    val defender = heroes.defender
    val damage = calculatDamage(attacker)
    val newDefender = defender match {
      case w: Warrior => w.copy(hp = w.hp - damage.total + w.armour)
      case h: Hunter => h.copy(hp = h.hp - damage.total + h.armour)
      case m: Mage => m.copy(hp = m.hp - damage.total + m.armour)
    }
    FightingHeroPair(attacker, newDefender)
  }

  def calculateFightResult(heroes: HeroPair): Option[FightResultPair] = {
    if (heroes._1.hp <= 0 && heroes._2.hp > 0) {
      Some(FightResultPair(-\/(WinnerLoserPair(heroes._2, heroes._1))))
    }
    else if (heroes._2.hp <= 0 && heroes._1.hp > 0) {
      Some(FightResultPair(-\/(WinnerLoserPair(heroes._1, heroes._2))))
    }
    else if (heroes._1.hp <= 0 && heroes._2.hp <= 0) {
      Some(FightResultPair(\/-(DrawPair(heroes._1, heroes._2))))
    }
    else None
  }

  def regenHeroes(heroes: HeroPair): FightingHeroPair = {
    FightingHeroPair(this.regen(heroes._1), this.regen(heroes._2))
  }

  def regen(hero: Hero): Hero = {
    hero match {
      case w: Warrior => w.copy(hp = Math.min(w.hp + w.regen, w.maxHp))
      case h: Hunter => h.copy(hp = Math.min(h.hp + h.regen, h.maxHp))
      case m: Mage => m.copy(hp = Math.min(m.hp + m.regen, m.maxHp))
    }
  }

  def fatigueHeroes(heroes: HeroPair, level: Int): FightingHeroPair = {
    FightingHeroPair(this.fatigue(heroes._1, level), this.fatigue(heroes._2, level))
  }

  val FATIGUE_LEVEL_STARTS_AT = 100

  def fatigue(hero: Hero, level: Int): Hero = {
    if (level > FATIGUE_LEVEL_STARTS_AT) {
      val fatigueDamage = Math.pow(level - FATIGUE_LEVEL_STARTS_AT, 1.8)
      hero match {
        case w: Warrior => w.copy(hp = Math.min(w.hp - fatigueDamage, w.maxHp))
        case h: Hunter => h.copy(hp = Math.min(h.hp - fatigueDamage, h.maxHp))
        case m: Mage => m.copy(hp = Math.min(m.hp - fatigueDamage, m.maxHp))
      }
    } else hero
  }
}
