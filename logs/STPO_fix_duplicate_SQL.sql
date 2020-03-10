Update sapr3.stpo stpo1 set stpo1.mandt='RQK'
WHERE EXISTS ( SELECT *
    FROM sapr3.stpo stpo2
    join sapr3.mast mast on mast.mandt=stpo2.mandt and mast.stlnr=stpo2.stlnr 
    join sapr3.mara mara on mast.mandt=mara.mandt and mast.matnr=mara.matnr
    WHERE stpo1.mandt = stpo2.mandt
    AND stpo1.stlnr = stpo2.stlnr
    AND stpo1.idnrk = stpo2.idnrk
    AND stpo1.stlkn > stpo2.stlkn -- tie-breaker...
    and mara.mtart in ('ZSWO','ZGRP') 
    AND not exists (select 1 from sapr3.stas stas where stas.mandt=stpo2.mandt and stas.stlnr=stpo2.stlnr and stas.stlkn=stpo2.stlkn
                    and stpo2.lkenz='X')
    )
and stpo1.mandt='100';

Update sapr3.stas stas set stas.mandt='RQK'
where exists (select * from sapr3.stpo stpo where stpo.mandt='RQK' and stpo.stlnr=stas.stlnr and stpo.stlkn=stas.stlkn and stas.mandt='100');