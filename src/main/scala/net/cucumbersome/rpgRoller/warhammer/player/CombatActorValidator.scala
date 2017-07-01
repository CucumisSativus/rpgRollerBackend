package net.cucumbersome.rpgRoller.warhammer.player
import cats.data.Validated.{Invalid, Valid}
import cats.data.{NonEmptyList, Validated, ValidatedNel}
import cats.kernel.Semigroup
import cats.{Apply, SemigroupK}
object CombatActorValidator {
  import net.cucumbersome.rpgRoller.warhammer.player.CombatActorConversions._
  implicit val nelSemigroup: Semigroup[NonEmptyList[CombatActorValidationError]] =
    SemigroupK[NonEmptyList].algebra[CombatActorValidationError]

  def validate(a: CombatActorPresenter): ValidatedNel[CombatActorValidationError, CombatActor] = {
    val s = a.statistics
    Apply[ValidatedNel[CombatActorValidationError, ?]].map13(
      validateStringStatistics(a.id, "id").toValidatedNel,
      validateStringStatistics(a.name, "name").toValidatedNel,
      validateIntStatistics(a.hp, "hp").toValidatedNel,
      validateIntStatistics(s.weaponSkill, "weaponSkill").toValidatedNel,
      validateIntStatistics(s.ballisticSkill, "ballisticSkill").toValidatedNel,
      validateIntStatistics(s.strength, "strength").toValidatedNel,
      validateIntStatistics(s.toughness, "toughness").toValidatedNel,
      validateIntStatistics(s.agility, "agility").toValidatedNel,
      validateIntStatistics(s.intelligence, "intelligence").toValidatedNel,
      validateIntStatistics(s.perception, "perception").toValidatedNel,
      validateIntStatistics(s.willPower, "willPower").toValidatedNel,
      validateIntStatistics(s.fellowship, "fellowship").toValidatedNel,
      validateIntStatistics(s.influence, "influence").toValidatedNel
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


