stile - a scala map tile library

To use (MBTiles example):

```scala
val coords = new com.socogeo.Coordinate(0, 1, 1)
//coords: com.socogeo.Coordinate = Coordinate(0, 1, 1)

val store = new com.socogeo.MBTileStore("/Users/scooter/Downloads/geography-class.mbtiles")
//store: com.socogeo.MBTileStore = com.socogeo.MBTileStore@b66ddf3

val tile = store.get(coords)
//tile: com.socogeo.Tile = Tile([B@2249af0c,image/png)
```

