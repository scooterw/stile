package com.github.scooterw

trait TileStore {
  def get(coord: Coordinate): Option[Tile]
}

