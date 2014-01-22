package com.github.scooterw

import scala.slick.jdbc.JdbcBackend.Database
import scala.slick.driver.SQLiteDriver.simple._
import com.jcraft.jzlib.{ Deflater, Inflater }

class MBTileStore(dbName: String, extension: String = "mbtiles") extends TileStore {
  lazy val db = Database.forURL(
    s"jdbc:sqlite:$dbName.$extension",
    driver = "org.sqlite.JDBC"
  )

  val tiles: TableQuery[Tiles] = TableQuery[Tiles]
  val metadata: TableQuery[Metadata] = TableQuery[Metadata]

  def get(coord: Coordinate, format: Option[String] = Some("image/png")): Tile = {
    val row = flipY(coord.zoom, coord.row)

    val mimeType = getTileFormat match {
      case Some(f) => f
      case None => "image/png"
    }

    val tileData = getTileData(coord.zoom, coord.column, row)

    new Tile(tileData, mimeType)
  }

  def flipY(zoom: Int, y: Int) = {
    (math.pow(2, zoom).toInt - 1) - y
  }

  def getTileData(zoom: Int, column: Int, row: Int): Array[Byte] = {
    val query = tiles.filter(_.zoom === zoom).filter(_.col === column).filter(_.row === row).take(1).map(_.data)

    db withSession {
      implicit session =>
        query.list.head
    }
  }

  def getTileFormat: Option[String] = {
    val query = metadata.filter(_.name === "format").take(1).map(_.value)

    try {
      db withSession {
        implicit session =>
          Some(query.list.head)
      }
    } catch {
      case ex: java.util.NoSuchElementException => None
    }
  }
}

