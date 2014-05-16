package com.socogeo

import scala.slick.driver.SQLiteDriver.simple._
import scala.slick.lifted.ProvenShape

case class Tiles(tag: Tag) extends Table[(Int, Int, Int, Array[Byte])](tag, "tiles") {
  def zoom: Column[Int] = column[Int]("zoom_level")
  def col: Column[Int] = column[Int]("tile_column")
  def row: Column[Int] = column[Int]("tile_row")
  def data: Column[Array[Byte]] = column[Array[Byte]]("tile_data")

  def * : ProvenShape[(Int, Int, Int, Array[Byte])] = (zoom, col, row, data)
}

