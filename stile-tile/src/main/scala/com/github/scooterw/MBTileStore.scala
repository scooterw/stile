package com.github.scooterw

import scala.slick.jdbc.JdbcBackend.Database
import scala.slick.jdbc.{ GetResult, StaticQuery => Q }
import Q.interpolation
import java.sql.Blob
import com.jcraft.jzlib.{ Deflater, Inflater }

class MBTileStore(dbName: String, extension: String = "mbtiles") extends TileStore {
  lazy val db = Database.forURL(
    s"jdbc:sqlite:$dbName.$extension",
    driver = "org.sqlite.JDBC"
  )

  implicit val getStringResult = GetResult(r => r.nextString)
  implicit val getByteArrayResult = GetResult(r => r.nextBytes)

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
    val sql = "select tile_data from tiles where zoom_level = %s and tile_column = %s and tile_row = %s".format(zoom, column, row)

    db withSession {
      implicit session =>
        Q.queryNA[Array[Byte]](sql).first
    }
  }

  def getTileFormat: Option[String] = {
    val sql = "select value from metadata where name = 'format'"

    try {
      db withSession {
        implicit session =>
          Q.queryNA[String](sql).firstOption
      }
    } catch {
      case ex: java.util.NoSuchElementException => None
    }
  }
}

