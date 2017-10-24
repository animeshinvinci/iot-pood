package iot.pood.management
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.UpdateWriteResult

import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}
import reactivemongo.bson.{BSONDocumentReader, BSONDocumentWriter, Macros, document}

import scala.io.StdIn
import scala.util.{Failure, Success}



/**
  * Created by rafik on 16.10.2017.
  */
object MongoApp extends App{

//  // My settings (see available connection options)
//  val mongoUri = "mongodb://localhost:27017"
//
//  import ExecutionContext.Implicits.global // use any appropriate context
//
//  // Connect to the database: Must be done only once per application
//  val driver = MongoDriver()
//  val parsedUri = MongoConnection.parseURI(mongoUri)
//  val connection = parsedUri.map(driver.connection(_))
//
//  Future.fromTry(connection)
//  // Database and collections: Get references
//  val futureConnection = Future.fromTry(connection)
//  def db1: Future[DefaultDB] = futureConnection.flatMap(_.database("pood"))
//  def collections: Future[BSONCollection] = db1.map(_.collection("user"))
//
//  // Write Documents: insert or update
//
//  implicit def personWriter: BSONDocumentWriter[Person] = Macros.writer[Person]
//  // or provide a custom one
//
//  val p = Person("first","lazxcst",10)
//
//  StdIn.readLine()
//  createPerson(p)
//
//  def createPerson(person: Person) = {
//    collections.flatMap(_.insert(person)).onComplete(result => result match {
//      case Success(success) => {
//        println(s"Suceess: $success")
//        println(success.ok)
//        println(success.n)
//      }
//      case Failure(e) => println(s"Error: $e",e)
//    })
//  }
//
////  def updatePerson(person: Person): Future[Int] = {
////    val selector = document(
////      "firstName" -> person.firstName,
////      "lastName" -> person.lastName
////    )
////
////    // Update the matching person
////    personCollection.flatMap(_.update(selector, person).map(_.n))
////  }
//
//  implicit def personReader: BSONDocumentReader[Person] = Macros.reader[Person]
//  // or provide a custom one
//
////  def findPersonByAge(age: Int): Future[List[Person]] =
////    personCollection.flatMap(_.find(document("age" -> age)). // query builder
////      cursor[Person]().collect[List]()) // collect using the result cursor
////  // ... deserializes the document using personReader
//
//  // Custom persistent types
//  case class Person(firstName: String, lastName: String, age: Int)

}
