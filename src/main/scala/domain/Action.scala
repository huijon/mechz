package domain

trait Action {}

trait Attack extends Action {
  def damage: Double
  def physical: Boolean
  def ranged: Boolean
  def spell: Boolean
}

case class Punch(damage: Double) extends Attack {
  val physical = true
  val ranged = false
  val spell = false
}

case class Shoot(damage: Double) extends Attack {
  val physical = true
  val ranged = true
  val spell = false
}

case class FrostBolt(damage: Double = 3) extends Attack {
  val physical = false
  val ranged = true
  val spell = true
}
