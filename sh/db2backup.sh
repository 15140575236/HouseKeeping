db2 connect to PPRDSHAD
db2 "delete from SAPR3.CUOB where mandt='100' and KNOBJ>'990000000000000000' and KNOBJ<'991000000000000000'"
db2 "update SAPR3.CABN set KNOBJ='000000000000000000' where mandt='100' and KNOBJ>'990000000000000000' and KNOBJ<'991000000000000000'"
db2 "delete from SAPR3.CUOB where mandt='100' and KNOBJ>'991000000000000000' and KNOBJ<'992000000000000000' with ur"
db2 "update SAPR3.CAWN set KNOBJ='000000000000000000' where mandt='100' and KNOBJ>'991000000000000000' and KNOBJ<'992000000000000000'"
db2 "delete from SAPR3.CUOB where mandt='100' and KNOBJ>'992000000000000000' and KNOBJ<'993000000000000000' with ur"
db2 "update SAPR3.STPO set KNOBJ='000000000000000000' where mandt='100' and KNOBJ>'992000000000000000' and KNOBJ<'993000000000000000'"
db2 "delete from SAPR3.CUOB where mandt='100' and SHAD_UPDATE_TS='2017-09-19 00:00:00.000000'"
db2 "update SAPR3.STPO set mandt='100' where mandt='RQK'"
db2 "update SAPR3.stas set mandt='100' where mandt='RQK'"