package sttp.model

/**
  * Represents query parameters, where each parameter can have 0, 1, or more values.
  * All query parameters are assumed to be decoded.
  */
class QueryParams(ps: Seq[(String, Seq[String])]) {
  def toMap: Map[String, String] = toSeq.toMap
  def toMultiMap: Map[String, Seq[String]] = ps.toMap
  def toSeq: Seq[(String, String)] = ps.flatMap { case (k, vs) => vs.map((k, _)) }
  def toMultiSeq: Seq[(String, Seq[String])] = ps

  def get(s: String): Option[String] = ps.find(_._1 == s).flatMap(_._2.headOption)
  def getMulti(s: String): Option[Seq[String]] = ps.find(_._1 == s).map(_._2)

  def param(k: String, v: String): QueryParams = new QueryParams(ps :+ (k -> List(v)))
  def param(k: String, v: Seq[String]): QueryParams = new QueryParams(ps :+ (k -> v))
  def param(p: Map[String, String]): QueryParams = new QueryParams(ps ++ p.map { case (k, v) => (k -> List(v)) })
}

object QueryParams {
  def apply() = new QueryParams(Nil)
  def fromMap(m: Map[String, String]): QueryParams = new QueryParams(m.mapValues(List(_)).toSeq)
  def fromSeq(s: Seq[(String, String)]): QueryParams =
    new QueryParams(s.groupBy(_._1).mapValues(_.map(_._2)).toSeq)
  def fromMultiMap(m: Map[String, Seq[String]]): QueryParams = new QueryParams(m.toSeq)
  def fromMultiSeq(m: Seq[(String, Seq[String])]): QueryParams = new QueryParams(m)
}
