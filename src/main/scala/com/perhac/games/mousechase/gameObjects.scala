package com.perhac.games.mousechase

import com.perhac.games.mousechase.MouseChaseGame.RESOURCES_ROOT
import pixiscalajs.PIXI
import pixiscalajs.PIXI.{DisplayObject, Point, Sprite}
import pixiscalajs.extensions.Implicits._
import pixiscalajs.extensions.Vector2
import pixiscalajs.extensions.Vector2.Zero

import scala.util.Random

abstract class GameObject(x: Double, y: Double) {
  var alive = true

  var acceleration: Vector2 = Zero
  var position: Vector2 = Vector2(x, y)
  var speed: Vector2 = Zero
  var world: World = _

  def displayObject: PIXI.DisplayObject

  def update(deltaTime: Long): Unit = {
    speed = acceleration * deltaTime * 0.15
    position += speed

    if (position.x < -20) position.x = world.width
    if (position.y < -30) position.y = world.height
    if (position.x > world.width + 20) position.x = -20
    if (position.y > world.height + 30) position.y = -30
  }

  def collides(origin: GameObject, point: Vector2): Boolean = false

  def setWorld(world_ : World): Unit = this.world = world_

  def destroy(): Unit = {
    alive = false
  }

}

abstract class SpriteGameObject(image: String, x: Double, y: Double, scale: Double = 1.0) extends GameObject(x, y) {
  val sprite: Sprite = PIXI.Sprite.fromImage(s"$RESOURCES_ROOT/images/$image")
  sprite.anchor = Point(0.5, 0.5)
  sprite.scale = Point(scale, scale)
  sprite.name = this.toString

  override val displayObject: DisplayObject = sprite

  override def update(deltaTime: Long): Unit = {
    super.update(deltaTime)
    sprite.x = position.x
    sprite.y = position.y
  }

  override def destroy(): Unit = {
    super.destroy()
    world.removeFromStage(this)
  }

  override def collides(origin: GameObject, point: Vector2): Boolean =
    sprite.containsPoint(point)

}

case class Cat(catNumber: Int, x: Double, y: Double)
  extends SpriteGameObject(
    s"cat$catNumber.png",
    x: Double,
    y: Double, 0.4) {

  override def update(deltaTime: Long): Unit = {
    if (this.acceleration != Zero) {
      this.acceleration = this.heading
    }
    super.update(deltaTime)
  }

  def heading: Vector2 = Vector2.Right.rotateRadians(sprite.rotation)

  override def collides(origin: GameObject, point: Vector2): Boolean = false
}

case class Mouse(mouseNumber: Int, x: Double, y: Double, scale: Double = 1.0) extends SpriteGameObject(s"mouse$mouseNumber.png", x: Double, y: Double, scale: Double) {
  val clockwise: Boolean = Random.nextBoolean()
  val rotationSpeed: Int = Random.nextInt(30) + 10
  acceleration = Vector2.Random

  override def update(deltaTime: Long): Unit = {
    super.update(deltaTime)
    sprite.rotation += 0.001 * rotationSpeed * (if (clockwise) 1 else -1)
  }

}

object Mouse {
  def randomMouse(w: Int, h: Int): Mouse =
    Mouse(Random.nextInt(5) + 1, Random.nextInt(w), Random.nextInt(h), Random.nextDouble / 4 + 0.15)
}
