@echo off

set MYSQL_PATH="/usr/local/mysql/bin/mysql"

%MYSQL_PATH%\mysql -u root -p hotel < create_tables_ddl.sql

%MYSQL_PATH%\mysql -u root -p hotel < filling_tables_dml.sql

pause
