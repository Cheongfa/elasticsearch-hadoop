# Elasticsearch-Hadoop
based on [ES-Hadoop](https://www.elastic.co/products/hadoop) plugin for ES official

## 中文版本 [English for official](https://www.elastic.co/products/hadoop)

* 首先确保你的环境中ES集群，Hadoop集群，Hive工作正常，并[下载](https://www.elastic.co/downloads/hadoop)相应的ES-Hadoop的版本。（本文只介绍ES -> HDFS以及ES -> Hive、Hive -> ES）

* ES导出至HDFS的代码在本仓库中，请自行查阅。

## ES和Hive互导

* **ES -> Hive**

1.  ADD JAR /path/elasticsearch-hadoop.jar;

2.  create table in hive
    ```
    example here:
    
    CREATE TABLE zju_ycf(
    id BIGINT,
    createtime TIMESTAMP,
    ...)
    
    ```
    
3.  create external table in hive
    
    ```
    CREATE EXTERNAL TABLE ex_zju_ycf(
    id BIGINT,
    createtime TIMESTAMP,
    ...)
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES('es.resource' = 'radio/artists','es.nodes' = '10.10.10.10', 'es.query' = '?q=me*');
    ```
    
4.  insert your table from external table,你也可以通过hive直接对ES进行查询操作

    ```
    insert overwrite table $YOUR_TABLE_NAME select * from $YOUR_EXTERNAL_TABLE_NAME
    ```
    
* **Hive -> ES**

1.  假设Hive中要导入的表名为zju_ycf

2.  创建外表

    ```
    CREATE EXTERNAL TABLE ex_zju_ycf(
    id BIGINT,
    createtime TIMESTAMP,
    ...)
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES('es.resource' = 'radio/artists', 'es.nodes' = '10.10.10.10');
    ```
    
3.  写入到ES
    
    ```
    INSERT OVERWRITE TABLE ex_zju_ycf SELECT * FROM zju_ycf;
    ```
    
* 以上方式可以通过ES-Hadoop来实现ES和Hive之间数据的互导

* **注意事项**

* 由于ES-Hadoop是通过rest接口与ES进行数据交互，所以在数据量巨大的时候出现互导失败的概率非常大，在导数据时必须确认ES集群的负载情况。
