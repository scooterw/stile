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

  def get(coord: Coordinate): Option[Tile] = {
    val row = flipY(coord.zoom, coord.row)

    val mimeType = getTileFormat match {
      case Some(format) => format
      case None => "image/png"
    }

    val tileData = getTileData(coord.zoom, coord.column, row)

    tileData match {
      case Some(data) => Some(new Tile(data, mimeType))
      case _ => None
    }
  }

  def put(coord: Coordinate, tileData: Array[Byte]) = {
    db withSession {
      implicit session =>
        tiles.ddl.create
        tiles += (coord.zoom, coord.column, flipY(coord.zoom, coord.row), tileData)
    }
  }

  def delete(coord: Coordinate) = {
    val query = tiles.filter(_.zoom === coord.zoom).filter(_.col === coord.column).filter(_.row === flipY(coord.zoom, coord.row))

    db withSession {
      implicit session =>
        query.delete
    }
  }

  private def flipY(zoom: Int, y: Int) = {
    (math.pow(2, zoom).toInt - 1) - y
  }

  private def getTileData(zoom: Int, column: Int, row: Int): Option[Array[Byte]] = {
    val query = tiles.filter(_.zoom === zoom).filter(_.col === column).filter(_.row === row).take(1).map(_.data)

    try {
      db withSession {
        implicit session =>
          Some(query.list.head)
      }
    } catch {
      case ex: java.util.NoSuchElementException => None
    }
  }

  private def getTileFormat: Option[String] = {
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

