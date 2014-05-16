package com.socogeo

import scala.slick.driver.SQLiteDriver.simple._
import scala.slick.lifted.ProvenShape

case class Metadata(tag: Tag) extends Table[(String, String)](tag, "metadata") {
  def name: Column[String] = column[String]("name")
  def value: Column[String] = column[String]("value")

  def * : ProvenShape[(String, String)] = (name, value)
}

