package net.cucumbersome.rpgRoller.warhammer.player
import cats.data.{NonEmptyList, Validated, ValidatedNel}
import cats.data.Validated.{Invalid, Valid}
import cats.{Applicative, Apply, SemigroupK}
import cats.kernel.Semigroup
import cats.implicits._

import net.cucumbersome.rpgRoller.warhammer
import net.cucumbersome.rpgRoller.warhammer.player.CombatActor._
import net.cucumbersome.rpgRoller.warhammer.player.Statistics._
object CombatActorValidator {
  import net.cucumbersome.rpgRoller.warhammer.player.CombatActorConversions._
  implicit val nelSemigroup: Semigroup[NonEmptyList[CombatActorValidationError]] =
    SemigroupK[NonEmptyList].algebra[CombatActorValidationError]

  def validate(a: CombatActor): ValidatedNel[CombatActorValidationError, CombatActor] = {
    val s = a.statistics
    Apply[ValidatedNel[CombatActorValidationError, ?]].map13(
      validateStringStatistics(a.id.data, "id").toValidatedNel,
      validateStringStatistics(a.name.data, "name").toValidatedNel,
      validateIntStatistics(a.hp.data, "hp").toValidatedNel,
      validateIntStatistics(s.weaponSkill.data, "weaponSkill").toValidatedNel,
      validateIntStatistics(s.ballisticSkill.data, "ballisticSkill").toValidatedNel,
      validateIntStatistics(s.strength.data, "strength").toValidatedNel,
      validateIntStatistics(s.toughness.data, "toughness").toValidatedNel,
      validateIntStatistics(s.agility.data, "agility").toValidatedNel,
      validateIntStatistics(s.intelligence.data, "intelligence").toValidatedNel,
      validateIntStatistics(s.perception.data, "perception").toValidatedNel,
      validateIntStatistics(s.willPower.data, "willPower").toValidatedNel,
      validateIntStatistics(s.fellowship.data, "fellowship").toValidatedNel,
      validateIntStatistics(s.influence.data, "influence").toValidatedNel
    ) {case(id, name, hp, ws, bs, str, tg, agi, int, per, wp, fel, infl) =>
        CombatActor(
          id = id.toId,
          name = name.toName,
          statistics = Statistics(
            weaponSkill = ws.toWs,
            ballisticSkill = bs.toBs,
            strength = str.toStr,
            toughness = tg.toTg,
            agility = agi.toAg,
            intelligence = int.toIt,
            perception = per.toPer,
            willPower = wp.toWp,
            fellowship = fel.toFel,
            influence = infl.toInfl
          ),
          hp = hp.toHp
        )
    }
  }

  private def validateIntStatistics( value: Int, fieldName: String): Validated[CombatActorValidationError, Int] =
    if(value >= 0) Valid(value)
    else Invalid(InvalidValue(fieldName))

  private def validateStringStatistics(value: String, fieldName: String): Validated[CombatActorValidationError, String] =
    if(!value.isEmpty) Valid(value)
    else Invalid(InvalidValue(fieldName))
}


sealed abstract class CombatActorValidationError{
  def error: String
}
final case class InvalidValue(field: String) extends CombatActorValidationError {
  override def error: String = s"Missing field $field"
}


