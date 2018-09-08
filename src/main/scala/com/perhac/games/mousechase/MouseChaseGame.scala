package com.perhac.games.mousechase

import org.scalajs.dom.html
import pixiscalajs.PIXI
import pixiscalajs.PIXI.RendererOptions

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("com.perhac.games.mousechase.MouseChaseGame")
class MouseChaseGame {

  var world: World = _

  @JSExport
  def main(canvas: html.Canvas): Unit = {
    println("Loading renderer")
    val w = canvas.width
    val h = canvas.height

    val renderer = new PIXI.CanvasRenderer(w, h, RendererOptions(canvas))

    println("Creating world")

    val cat = Cat(0, w / 2, h / 2)

    world = new World(renderer, cat)
    world.add(cat)
    (1 to 10).toList.map { _ =>
      println("adding mouse")
      world.add(Mouse.randomMouse(w, h))
    }

    world.loop.run()
  }

}

object MouseChaseGame {
  val RESOURCES_ROOT = "assets"
}