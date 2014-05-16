package com.socogeo

import org.specs2.mutable._
import scala.io.Source

class MBTileStoreSpec extends Specification {
  val tilePath = getClass.getResource("/geography-class.mbtiles").getPath
  val store = new MBTileStore(tilePath)

  val testTilePath = getClass.getResource("/geo_0_0_0.png").getPath
  val testTile = Source.fromFile(testTilePath, "iso-8859-1").map(_.toByte).toArray

  "MBTileStore" should {
    "return None when tile not found" in {
      val badCoord = new Coordinate(zoom = 25, column = 38000000, row = 15000000)
      val tile = store.get(badCoord)

      tile must beNone
    }

    "return Some(tile) when tile found" in {
      val coord = new Coordinate(0, 0, 0)
      val tile = store.get(coord)

      tile must beSome
      tile.get.data must be equalTo testTile
    }
  }
}

