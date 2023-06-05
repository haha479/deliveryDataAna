package td

import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  *
  * 新老用户标识维表
  * 2023/05/06
  *
  * @author zhanghao
  *
  */
object td_new_old_info {
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "self479")
    val spark = SparkSession
      .builder()
      .enableHiveSupport()
      .appName("td_new_old_info")
      .master("local[*]")
      .getOrCreate()

    val newOldDF = spark.read.
      format("jdbc")
      .option("url", "jdbc:mysql://hadoop102:3306/waimai")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("dbtable", "td_new_old_info")
      .option("user", "root")
      .option("password", "123479")
      .load()

    newOldDF.write.mode(SaveMode.Append).format("orc").saveAsTable("waimai.td_new_old_info")


  }

}
