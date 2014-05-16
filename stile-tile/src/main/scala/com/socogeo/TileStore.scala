package com.socogeo

trait TileStore {
  def get(coord: Coordinate): Option[Tile]
}

