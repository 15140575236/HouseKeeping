--CABN
db2 connect to PPRDSHAD user oimdev using 2016oimdev
db2 "select count(*) from SAPR3.CUOB where mandt='100' and KNOBJ>'990000000000000000' and KNOBJ<'991000000000000000' with ur"
db2 "select count(*) from SAPR3.CABN where mandt='100' and KNOBJ>'990000000000000000' and KNOBJ<'991000000000000000' with ur"
--CAWN
db2 "select count(*) from SAPR3.CUOB where mandt='100' and KNOBJ>'991000000000000000' and KNOBJ<'992000000000000000' with ur"
db2 "select count(*) from SAPR3.CAWN where mandt='100' and KNOBJ>'991000000000000000' and KNOBJ<'992000000000000000' with ur"
--STPOESW
db2 "select count(*) from SAPR3.CUOB where mandt='100' and KNOBJ>'992000000000000000' and KNOBJ<'993000000000000000' with ur"
db2 "select count(*) from SAPR3.STPO where mandt='100' and KNOBJ>'992000000000000000' and KNOBJ<'993000000000000000' with ur"
--STPO
db2 "select count(*) from SAPR3.CUOB where mandt='100' and SHAD_UPDATE_TS='2017-09-19 00:00:00.000000'"