package com.perhac.games.mousechase

import pixiscalajs.PIXI
import pixiscalajs.PIXI.{Container, SystemRenderer}
import pixiscalajs.extensions.{DefineLoop, KeyBinding, Keyboard, Vector2}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


class World(renderer: SystemRenderer, cat: Cat) {

  val width: Double = renderer.width
  val height: Double = renderer.height

  val right: KeyBinding = Keyboard.bind(39)
  val left: KeyBinding = Keyboard.bind(37)
  val up: KeyBinding = Keyboard.bind(38)
  val down: KeyBinding = Keyboard.bind(40)
  val space: KeyBinding = Keyboard.bind(32)

  var objects: ArrayBuffer[GameObject] = mutable.ArrayBuffer[GameObject]()

  val stage: Container = new PIXI.Container() {
    width = renderer.width
    height = renderer.height
  }

  val loop = DefineLoop {
    if (right.isDown) cat.sprite.rotation += Vector2.DEG_TO_RADS * 3
    if (left.isDown) cat.sprite.rotation -= Vector2.DEG_TO_RADS * 3
    if (up.isDown) cat.acceleration = cat.heading
    if (down.isDown) cat.acceleration = Vector2.Zero
    if (space.isDown) objects.foreach(_.speed = Vector2.Zero)
    updateAllChildren()
    renderer.render(stage)
  }

  var latestTimestamp = 0L

  def updateAllChildren() {
    val deltaTime = System.currentTimeMillis() - latestTimestamp
    latestTimestamp = System.currentTimeMillis()

    objects = objects.filter(_.alive)
    objects.foreach(_.update(deltaTime))
  }

  def add(gameObject: GameObject): World = {
    gameObject.setWorld(this)
    stage.addChild(gameObject.displayObject)
    objects += gameObject
    this
  }

  def removeFromStage(gameObject: GameObject): Unit = {
    stage.removeChild(gameObject.displayObject)
  }

  def checkCollision(origin: GameObject, position: Vector2): Seq[GameObject] = {
    objects.filter { o =>
      (o == origin) && o.collides(origin, position)
    }
  }

}
