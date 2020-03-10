db2 connect to PPRDSHAD
echo `date` '#Start to run df_insert_CABN.sql'
db2 -tvf df_insert_CABN.sql >>df_insert_CABN.log
echo `date` '#Start to run df_update_CABN.sql'
db2 -tvf df_update_CABN.sql >>df_update_CABN.log
echo `date` '#Start to run df_insert_CAWN.sql'
db2 -tvf df_insert_CAWN.sql >>df_insert_CAWN.log
echo `date` '#Start to run df_update_CAWN.sql'
db2 -tvf df_update_CAWN.sql >>df_update_CAWN.log
echo `date` '#Start to run df_insert_STPOESW.sql'
db2 -tvf df_insert_STPOESW.sql >>df_insert_STPOESW.log
echo `date` '#Start to run df_update_STPOESW.sql'
db2 -tvf df_update_STPOESW.sql >>df_update_STPOESW.log
echo `date` '#Start to run STPO_Fix_SQL.sql'
db2 -tvf STPO_Fix_SQL.sql >>STPO_Fix_SQL.log
echo `date` '#Start to run STPO_fix_duplicate_SQL.sql'
db2 -tvf STPO_fix_duplicate_SQL.sql >>STPO_fix_duplicate_SQL.log
echo `date` '#Finish to run all the sql'
