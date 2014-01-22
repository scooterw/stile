stile - a scala map tile library

To use (MBTiles example):

```scala
val coords = new com.github.scooterw.Coordinate(0, 1, 1)
//coords: com.github.scooterw.Coordinate = Coordinate(0, 1, 1)

val store = new com.github.scooterw.MBTileStore("/Users/scooter/Downloads/geography-class")
//store: com.github.scooterw.MBTileStore = com.github.scooterw.MBTileStore@b66ddf3

val tile = store.get(coords)
//tile: com.github.scooterw.Tile = Tile([B@2249af0c,image/png)
```

