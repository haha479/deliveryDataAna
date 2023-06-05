package td

import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.functions._


/**
  * 城市信息维表
  * 2023/06/05
  * @author 479
  */
object td_city_info {
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "self479")
    val spark = SparkSession.builder()
      .appName("td_city_info")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()
    val city = spark.read.json("hdfs://hadoop102:8020/waimai/data/city.json")
    import spark.implicits._
    val cities = city.select($"province_id", $"province_name", explode($"citys").as("cities")).
      select($"province_id", $"province_name", $"cities.city_id", $"cities.city_name")
    cities.show()
    cities.write.format("orc").mode(SaveMode.Overwrite).saveAsTable("waimai.td_city_info")
  }
}
