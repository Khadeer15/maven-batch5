package com.khadeer;


import java.sql.Connection;

import javax.sql.DataSource;

import schemacrawler.schema.Column;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schema.View;
import schemacrawler.schemacrawler.DatabaseConnectionOptions;
import schemacrawler.schemacrawler.ExcludeAll;
import schemacrawler.schemacrawler.RegularExpressionInclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;
import schemacrawler.utility.SchemaCrawlerUtility;

public final class ApiExample
{

  public static void main(final String[] args)
    throws Exception
  {
    // Create a database connection
    final DataSource dataSource = new DatabaseConnectionOptions("org.hsqldb.jdbcDriver",
                                                                "jdbc:hsqldb:hsql://localhost:9001/schemacrawler");
    final Connection connection = dataSource.getConnection("sa", "");

    // Create the options
    final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
    // Set what details are required in the schema - this affects the
    // time taken to crawl the schema
    options.setSchemaInfoLevel(SchemaInfoLevel.standard());
    options.setRoutineInclusionRule(new ExcludeAll());
    options
      .setSchemaInclusionRule(new RegularExpressionInclusionRule("PUBLIC.BOOKS"));

    // Get the schema definition
    final Catalog catalog = SchemaCrawlerUtility
      .getCatalog(connection, options);

    for (final Schema schema: catalog.getSchemas())
    {
      System.out.println(schema);
      for (final Table table: catalog.getTables(schema))
      {
        System.out.print("o--> " + table);
        if (table instanceof View)
        {
          System.out.println(" (VIEW)");
        }
        else
        {
          System.out.println();
        }

        for (final Column column: table.getColumns())
        {
          System.out.println("     o--> " + column + " ("
                             + column.getColumnDataType() + ")");
        }
      }
    }

  }

}
