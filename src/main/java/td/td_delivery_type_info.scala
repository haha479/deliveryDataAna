package td

import org.apache.spark.sql.SparkSession

/**
  *
  * 配送维度信息表
  * 2023/05/06
  *
  * @author 479
  */
object td_delivery_type_info {
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "self479")
    val spark = SparkSession
      .builder()
      .enableHiveSupport()
      .appName("td_delivery_info")
      .master("local[*]")
      .getOrCreate()

    val delivery = spark.read.json("hdfs://hadoop102:8020/waimai/data/delivery.json")
    delivery.show()
    delivery.write.format("orc").saveAsTable("waimai.td_delivery_type_info")
    spark.stop()


  }
}
