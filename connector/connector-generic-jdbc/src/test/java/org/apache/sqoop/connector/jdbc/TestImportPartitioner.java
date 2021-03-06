/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sqoop.connector.jdbc;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.sqoop.common.MutableContext;
import org.apache.sqoop.common.MutableMapContext;
import org.apache.sqoop.common.SqoopException;
import org.apache.sqoop.connector.jdbc.configuration.ConnectionConfiguration;
import org.apache.sqoop.connector.jdbc.configuration.ImportJobConfiguration;
import org.apache.sqoop.job.Constants;
import org.apache.sqoop.job.etl.Partition;
import org.apache.sqoop.job.etl.Partitioner;
import org.apache.sqoop.job.etl.PartitionerContext;

public class TestImportPartitioner extends TestCase {

  private static final int START = -5;
  private static final int NUMBER_OF_ROWS = 11;

  public void testIntegerEvenPartition() throws Exception {
    MutableContext context = new MutableMapContext();
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_COLUMNNAME,
        "ICOL");
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_COLUMNTYPE,
        String.valueOf(Types.INTEGER));
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MINVALUE,
        String.valueOf(START));
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MAXVALUE,
        String.valueOf(START + NUMBER_OF_ROWS - 1));

    ConnectionConfiguration connConf = new ConnectionConfiguration();
    ImportJobConfiguration jobConf = new ImportJobConfiguration();

    Partitioner partitioner = new GenericJdbcImportPartitioner();
    PartitionerContext partitionerContext = new PartitionerContext(context, 5);
    List<Partition> partitions = partitioner.getPartitions(partitionerContext, connConf, jobConf);

    verifyResult(partitions, new String[] {
        "-5 <= ICOL AND ICOL < -3",
        "-3 <= ICOL AND ICOL < -1",
        "-1 <= ICOL AND ICOL < 1",
        "1 <= ICOL AND ICOL < 3",
        "3 <= ICOL AND ICOL <= 5"
    });
  }

  public void testIntegerUnevenPartition() throws Exception {
    MutableContext context = new MutableMapContext();
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_COLUMNNAME,
        "ICOL");
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_COLUMNTYPE,
        String.valueOf(Types.INTEGER));
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MINVALUE,
        String.valueOf(START));
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MAXVALUE,
        String.valueOf(START + NUMBER_OF_ROWS - 1));

    ConnectionConfiguration connConf = new ConnectionConfiguration();
    ImportJobConfiguration jobConf = new ImportJobConfiguration();

    Partitioner partitioner = new GenericJdbcImportPartitioner();
    PartitionerContext partitionerContext = new PartitionerContext(context, 3);
    List<Partition> partitions = partitioner.getPartitions(partitionerContext, connConf, jobConf);

    verifyResult(partitions, new String[] {
        "-5 <= ICOL AND ICOL < -1",
        "-1 <= ICOL AND ICOL < 2",
        "2 <= ICOL AND ICOL <= 5"
    });
  }

  public void testIntegerOverPartition() throws Exception {
    MutableContext context = new MutableMapContext();
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_COLUMNNAME,
        "ICOL");
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_COLUMNTYPE,
        String.valueOf(Types.INTEGER));
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MINVALUE,
        String.valueOf(START));
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MAXVALUE,
        String.valueOf(START + NUMBER_OF_ROWS - 1));

    ConnectionConfiguration connConf = new ConnectionConfiguration();
    ImportJobConfiguration jobConf = new ImportJobConfiguration();

    Partitioner partitioner = new GenericJdbcImportPartitioner();
    PartitionerContext partitionerContext = new PartitionerContext(context, 13);
    List<Partition> partitions = partitioner.getPartitions(partitionerContext, connConf, jobConf);

    verifyResult(partitions, new String[] {
        "-5 <= ICOL AND ICOL < -4",
        "-4 <= ICOL AND ICOL < -3",
        "-3 <= ICOL AND ICOL < -2",
        "-2 <= ICOL AND ICOL < -1",
        "-1 <= ICOL AND ICOL < 0",
        "0 <= ICOL AND ICOL < 1",
        "1 <= ICOL AND ICOL < 2",
        "2 <= ICOL AND ICOL < 3",
        "3 <= ICOL AND ICOL < 4",
        "4 <= ICOL AND ICOL <= 5"
    });
  }

  public void testFloatingPointEvenPartition() throws Exception {
    MutableContext context = new MutableMapContext();
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_COLUMNNAME,
        "DCOL");
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_COLUMNTYPE,
        String.valueOf(Types.DOUBLE));
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MINVALUE,
        String.valueOf((double)START));
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MAXVALUE,
        String.valueOf((double)(START + NUMBER_OF_ROWS - 1)));

    ConnectionConfiguration connConf = new ConnectionConfiguration();
    ImportJobConfiguration jobConf = new ImportJobConfiguration();

    Partitioner partitioner = new GenericJdbcImportPartitioner();
    PartitionerContext partitionerContext = new PartitionerContext(context, 5);
    List<Partition> partitions = partitioner.getPartitions(partitionerContext, connConf, jobConf);

    verifyResult(partitions, new String[] {
        "-5.0 <= DCOL AND DCOL < -3.0",
        "-3.0 <= DCOL AND DCOL < -1.0",
        "-1.0 <= DCOL AND DCOL < 1.0",
        "1.0 <= DCOL AND DCOL < 3.0",
        "3.0 <= DCOL AND DCOL <= 5.0"
    });
  }

  public void testFloatingPointUnevenPartition() throws Exception {
    MutableContext context = new MutableMapContext();
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_COLUMNNAME,
        "DCOL");
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_COLUMNTYPE,
        String.valueOf(Types.DOUBLE));
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MINVALUE,
        String.valueOf((double)START));
    context.setString(
        GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MAXVALUE,
        String.valueOf((double)(START + NUMBER_OF_ROWS - 1)));

    ConnectionConfiguration connConf = new ConnectionConfiguration();
    ImportJobConfiguration jobConf = new ImportJobConfiguration();

    Partitioner partitioner = new GenericJdbcImportPartitioner();
    PartitionerContext partitionerContext = new PartitionerContext(context, 3);
    List<Partition> partitions = partitioner.getPartitions(partitionerContext, connConf, jobConf);

    verifyResult(partitions, new String[] {
        "-5.0 <= DCOL AND DCOL < -1.6666666666666665",
        "-1.6666666666666665 <= DCOL AND DCOL < 1.666666666666667",
        "1.666666666666667 <= DCOL AND DCOL <= 5.0"
    });
  }

  public void testNumericEvenPartition() throws Exception {
    MutableContext context = new MutableMapContext();
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_COLUMNNAME, "ICOL");
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_COLUMNTYPE, String.valueOf(Types.NUMERIC));
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MINVALUE, String.valueOf(START));
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MAXVALUE, String.valueOf(START + NUMBER_OF_ROWS - 1));

    ConnectionConfiguration connConf = new ConnectionConfiguration();
    ImportJobConfiguration jobConf = new ImportJobConfiguration();

    Partitioner partitioner = new GenericJdbcImportPartitioner();
    PartitionerContext partitionerContext = new PartitionerContext(context, 5);
    List<Partition> partitions = partitioner.getPartitions(partitionerContext, connConf, jobConf);

    verifyResult(partitions, new String[] {
        "-5 <= ICOL AND ICOL < -3",
        "-3 <= ICOL AND ICOL < -1",
        "-1 <= ICOL AND ICOL < 1",
        "1 <= ICOL AND ICOL < 3",
        "3 <= ICOL AND ICOL <= 5"
    });
  }

  public void testNumericUnevenPartition() throws Exception {
    MutableContext context = new MutableMapContext();
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_COLUMNNAME, "DCOL");
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_COLUMNTYPE, String.valueOf(Types.NUMERIC));
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MINVALUE, String.valueOf(new BigDecimal(START)));
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MAXVALUE, String.valueOf(new BigDecimal(START + NUMBER_OF_ROWS - 1)));

    ConnectionConfiguration connConf = new ConnectionConfiguration();
    ImportJobConfiguration jobConf = new ImportJobConfiguration();

    Partitioner partitioner = new GenericJdbcImportPartitioner();
    PartitionerContext partitionerContext = new PartitionerContext(context, 3);
    List<Partition> partitions = partitioner.getPartitions(partitionerContext, connConf, jobConf);

    verifyResult(partitions, new String[]{
      "-5 <= DCOL AND DCOL < -2",
      "-2 <= DCOL AND DCOL < 1",
      "1 <= DCOL AND DCOL <= 5"
    });
  }

  public void testNumericSinglePartition() throws Exception {
    MutableContext context = new MutableMapContext();
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_COLUMNNAME, "DCOL");
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_COLUMNTYPE, String.valueOf(Types.NUMERIC));
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MINVALUE, String.valueOf(new BigDecimal(START)));
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MAXVALUE, String.valueOf(new BigDecimal(START)));

    ConnectionConfiguration connConf = new ConnectionConfiguration();
    ImportJobConfiguration jobConf = new ImportJobConfiguration();

    Partitioner partitioner = new GenericJdbcImportPartitioner();
    PartitionerContext partitionerContext = new PartitionerContext(context, 3);
    List<Partition> partitions = partitioner.getPartitions(partitionerContext, connConf, jobConf);

    verifyResult(partitions, new String[]{
      "DCOL = -5",
    });
  }


  public void testDatePartition() throws Exception {
    MutableContext context = new MutableMapContext();
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_COLUMNNAME, "DCOL");
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_COLUMNTYPE, String.valueOf(Types.DATE));
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MINVALUE,
        Date.valueOf("2013-01-01").toString());
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_MAXVALUE, Date.valueOf("2013-12-31").toString());


    ConnectionConfiguration connConf = new ConnectionConfiguration();
    ImportJobConfiguration jobConf = new ImportJobConfiguration();

    Partitioner partitioner = new GenericJdbcImportPartitioner();
    PartitionerContext partitionerContext = new PartitionerContext(context,
        3);
    List<Partition> partitions = partitioner.getPartitions(partitionerContext, connConf, jobConf);
    verifyResult(partitions, new String[]{
        "'2013-01-01' <= DCOL AND DCOL < '2013-05-02'",
        "'2013-05-02' <= DCOL AND DCOL < '2013-08-31'",
        "'2013-08-31' <= DCOL AND DCOL <= '2013-12-31'",
    });

  }

  public void testTimePartition() throws Exception {
    MutableContext context = new MutableMapContext();
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_COLUMNNAME, "TCOL");
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_COLUMNTYPE, String.valueOf(Types.TIME));
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MINVALUE,
        Time.valueOf("01:01:01").toString());
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MAXVALUE,
        Time.valueOf("10:40:50").toString());


    ConnectionConfiguration connConf = new ConnectionConfiguration();
    ImportJobConfiguration jobConf = new ImportJobConfiguration();

    Partitioner partitioner = new GenericJdbcImportPartitioner();
    PartitionerContext partitionerContext = new PartitionerContext(context,
        3);
    List<Partition> partitions = partitioner.getPartitions(partitionerContext, connConf, jobConf);
    verifyResult(partitions, new String[]{
        "'01:01:01' <= TCOL AND TCOL < '04:14:17'",
        "'04:14:17' <= TCOL AND TCOL < '07:27:33'",
        "'07:27:33' <= TCOL AND TCOL <= '10:40:50'",
    });
  }

  public void testTimestampPartition() throws Exception {
    MutableContext context = new MutableMapContext();
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_COLUMNNAME, "TSCOL");
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_COLUMNTYPE, String.valueOf(Types.TIMESTAMP));
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MINVALUE,
        Timestamp.valueOf("2013-01-01 01:01:01.123").toString());
    context.setString(GenericJdbcConnectorConstants.CONNECTOR_JDBC_PARTITION_MAXVALUE,
        Timestamp.valueOf("2013-12-31 10:40:50.654").toString());

    ConnectionConfiguration connConf = new ConnectionConfiguration();
    ImportJobConfiguration jobConf = new ImportJobConfiguration();

    Partitioner partitioner = new GenericJdbcImportPartitioner();
    PartitionerContext partitionerContext = new PartitionerContext(context,
        3);
    List<Partition> partitions = partitioner.getPartitions(partitionerContext, connConf, jobConf);
    verifyResult(partitions, new String[]{
        "'2013-01-01 01:01:01.123' <= TSCOL AND TSCOL < '2013-05-02 12:14:17.634'",
        "'2013-05-02 12:14:17.634' <= TSCOL AND TSCOL < '2013-08-31 23:27:34.144'",
        "'2013-08-31 23:27:34.144' <= TSCOL AND TSCOL <= '2013-12-31 10:40:50.654'",
    });
  }

  public void testBooleanPartition() throws Exception {
    MutableContext context = new MutableMapContext();
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_COLUMNNAME, "BCOL");
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_COLUMNTYPE, String.valueOf(Types.BOOLEAN));
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_MINVALUE, "0");
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_MAXVALUE, "1");

    ConnectionConfiguration connConf = new ConnectionConfiguration();
    ImportJobConfiguration jobConf = new ImportJobConfiguration();

    Partitioner partitioner = new GenericJdbcImportPartitioner();
    PartitionerContext partitionerContext = new PartitionerContext(context,
        3);
    List<Partition> partitions = partitioner.getPartitions(partitionerContext, connConf, jobConf);
    verifyResult(partitions, new String[]{
      "BCOL = TRUE",
      "BCOL = FALSE",
    });
  }

  public void testVarcharPartition() throws Exception {
    MutableContext context = new MutableMapContext();
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_COLUMNNAME, "VCCOL");
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_COLUMNTYPE, String.valueOf(Types.VARCHAR));
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_MINVALUE, "A");
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_MAXVALUE, "Z");

    ConnectionConfiguration connConf = new ConnectionConfiguration();
    ImportJobConfiguration jobConf = new ImportJobConfiguration();

    Partitioner partitioner = new GenericJdbcImportPartitioner();
    PartitionerContext partitionerContext = new PartitionerContext(context,
        25);
    List<Partition> partitions = partitioner.getPartitions(partitionerContext, connConf, jobConf);

    verifyResult(partitions, new String[] {
        "'A' <= VCCOL AND VCCOL < 'B'",
        "'B' <= VCCOL AND VCCOL < 'C'",
        "'C' <= VCCOL AND VCCOL < 'D'",
        "'D' <= VCCOL AND VCCOL < 'E'",
        "'E' <= VCCOL AND VCCOL < 'F'",
        "'F' <= VCCOL AND VCCOL < 'G'",
        "'G' <= VCCOL AND VCCOL < 'H'",
        "'H' <= VCCOL AND VCCOL < 'I'",
        "'I' <= VCCOL AND VCCOL < 'J'",
        "'J' <= VCCOL AND VCCOL < 'K'",
        "'K' <= VCCOL AND VCCOL < 'L'",
        "'L' <= VCCOL AND VCCOL < 'M'",
        "'M' <= VCCOL AND VCCOL < 'N'",
        "'N' <= VCCOL AND VCCOL < 'O'",
        "'O' <= VCCOL AND VCCOL < 'P'",
        "'P' <= VCCOL AND VCCOL < 'Q'",
        "'Q' <= VCCOL AND VCCOL < 'R'",
        "'R' <= VCCOL AND VCCOL < 'S'",
        "'S' <= VCCOL AND VCCOL < 'T'",
        "'T' <= VCCOL AND VCCOL < 'U'",
        "'U' <= VCCOL AND VCCOL < 'V'",
        "'V' <= VCCOL AND VCCOL < 'W'",
        "'W' <= VCCOL AND VCCOL < 'X'",
        "'X' <= VCCOL AND VCCOL < 'Y'",
        "'Y' <= VCCOL AND VCCOL <= 'Z'",
    });
  }

  public void testVarcharPartitionWithCommonPrefix() throws Exception {
    MutableContext context = new MutableMapContext();
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_COLUMNNAME, "VCCOL");
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_COLUMNTYPE, String.valueOf(Types.VARCHAR));
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_MINVALUE, "AAA");
    context.setString(GenericJdbcConnectorConstants
        .CONNECTOR_JDBC_PARTITION_MAXVALUE, "AAF");

    ConnectionConfiguration connConf = new ConnectionConfiguration();
    ImportJobConfiguration jobConf = new ImportJobConfiguration();

    Partitioner partitioner = new GenericJdbcImportPartitioner();
    PartitionerContext partitionerContext = new PartitionerContext(context,
        5);

    List<Partition> partitions = partitioner.getPartitions(partitionerContext, connConf, jobConf);

    verifyResult(partitions, new String[] {
        "'AAA' <= VCCOL AND VCCOL < 'AAB'",
        "'AAB' <= VCCOL AND VCCOL < 'AAC'",
        "'AAC' <= VCCOL AND VCCOL < 'AAD'",
        "'AAD' <= VCCOL AND VCCOL < 'AAE'",
        "'AAE' <= VCCOL AND VCCOL <= 'AAF'",
    });

  }

  private void verifyResult(List<Partition> partitions,
      String[] expected) {
    assertEquals(expected.length, partitions.size());

    Iterator<Partition> iterator = partitions.iterator();
    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i],
          ((GenericJdbcImportPartition)iterator.next()).getConditions());
    }
  }
}
