package com.github.scooterw

trait TileStore {
  def get(coord: Coordinate, format: Option[String] = None): Tile
}

