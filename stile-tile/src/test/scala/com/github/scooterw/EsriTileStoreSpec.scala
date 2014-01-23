package com.github.scooterw

import org.specs2.mutable._
import scala.io.Source

class EsriTileStoreSpec extends Specification {
  val tilePath = getClass.getResource("/waldo_canyon").getPath
  val store = new EsriTileStore(tilesetPath = tilePath)

  val testTilePath = getClass.getResource("/waldo_canyon/Layers/_alllayers/L16/R000061e8/C0000356a.png").getPath
  val testTile = Source.fromFile(testTilePath, "iso-8859-1").map(_.toByte).toArray

  "EsriTileStore" should {
    "return None when tile not found" in {
      val badCoord = new Coordinate(zoom = 25, column = 38000000, row = 15000000)
      val tile = store.get(badCoord)

      tile must beNone
    }

    "return Some(tile) when tile found" in {
      val coord = new Coordinate(row = 25064, column = 13674, zoom = 16)
      val tile = store.get(coord)

      tile must beSome
    }
  }
}

