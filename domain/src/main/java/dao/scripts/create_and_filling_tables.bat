#!/bin/bash

PGSQL_PATH="/usr/local/bin/psql"

$PGSQL_PATH -U postgres -d hotel -f create_tables_ddl.sql

$PGSQL_PATH -U postgres -d hotel -f filling_tables_dml.sql

read -p "Press Enter to exit"
