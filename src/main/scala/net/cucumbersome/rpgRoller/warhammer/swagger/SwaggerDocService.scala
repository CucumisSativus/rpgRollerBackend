package net.cucumbersome.rpgRoller.warhammer.swagger

import com.github.swagger.akka.SwaggerHttpService
import com.github.swagger.akka.model.Info
import net.cucumbersome.rpgRoller.warhammer.combat.CombatController
import net.cucumbersome.rpgRoller.warhammer.player.ActorsController


class SwaggerDocService(domain: String, port: Int) extends SwaggerHttpService {
  override val apiClasses = Set(classOf[ActorsController], classOf[CombatController])
  override val host = s"$domain:$port"
  override val basePath = "/"
  override val apiDocsPath = "api"
  override val info = Info()
}