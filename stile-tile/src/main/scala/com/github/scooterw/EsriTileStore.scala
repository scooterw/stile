package com.github.scooterw

import scala.io.Source

class EsriTileStore(tilesetPath: String, format: String = "image/png") extends TileStore {
  def get(coord: Coordinate): Option[Tile] = {
    val zoom = coord.zoom.toString.padTo(2, "0").reverse.mkString
    val column = s"C${toEsriHex(coord.column)}"
    val row = s"R${toEsriHex(coord.row)}"

    val tileData = getTileData(s"$tilesetPath/Layers/_alllayers/L$zoom/$row/$column")

    tileData match {
      case Some(tile) => Some(new Tile(tile, format))
      case _ => None
    }
  }

  private def getTileData(tilePath: String): Option[Array[Byte]] = {
    val source = try {
      Some(Source.fromFile(tilePath, "UTF-8"))
    } catch {
      case ex: java.io.FileNotFoundException => None
    }

    source match {
      case Some(s) => {
        val tile = Some(s.map(_.toByte).toArray)
        s.close
        tile
      }
      case _ => None

    }
  }

  private def toEsriHex(value: Int) = {
    value.toHexString.reverse.padTo(8, "0").reverse.mkString
  }
}

