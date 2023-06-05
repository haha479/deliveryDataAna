package td

import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.storage.StorageLevel

/**
  *
  * 终端信息维表
  * 2023/05/06
  *
  * @author 479
  *
  *
  */
object td_terminal_info {

  //终端样例类
  case class Terminal(platform_id: Int, platform_name: String, terminal_id: Int, terminal_name: String)

  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "self479")
    val spark = SparkSession
      .builder()
      .enableHiveSupport()
      .appName("td_terminal_info")
      .master("local[*]")
      .getOrCreate()
    val terminal = spark.sparkContext.textFile("hdfs://hadoop102:8020/waimai/data/terminal.txt")
    val tmlRdd = terminal.map(_.split(" ")).filter(_.length == 4)
    tmlRdd.persist(StorageLevel.MEMORY_AND_DISK_SER)
    import spark.implicits._
    val tmlDF = tmlRdd.map(tml => Terminal(tml(0).toInt, tml(1), tml(2).toInt, tml(3))).toDF
    tmlDF.show()
    tmlDF.write.format("orc").mode(SaveMode.Overwrite).saveAsTable("waimai.td_terminal_info")
    spark.stop()
  }

}
