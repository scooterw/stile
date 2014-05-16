package com.socogeo

case class Coordinate(x: Int, y: Int, z: Int) {

  def row = y
  def column = x
  def zoom = z

}

