insert into SAPR3.CUOB(MANDT,KNTAB,KNOBJ,KNNUM,ADZHL,SHAD_UPDATE_TS,SHAD_UPDATE_IND) 
	select distinct '100'
		,'STPO'
		,stpo.knobj
		,'0000000656'
		,'0000'
		,'2017-09-19 00:00:00.000000'
		,'I'
	from sapr3.mast mast
	join sapr3.stpo stpo on stpo.mandt = mast.mandt and stpo.stlnr = mast.stlnr
	join sapr3.mara mara on mara.mandt = mast.mandt and mara.matnr = stpo.idnrk
	left outer join sapr3.cuob cuob on cuob.mandt = mast.mandt and cuob.knobj = stpo.knobj
	left outer join sapr3.cukb cukb on cukb.mandt = mast.mandt and cukb.knnum = cuob.knnum
	where mast.mandt = '100'
	  and mara.mtart in ('ZZEE')
	  and mast.werks like '17%'
	  and cukb.knnam is null
	  and stpo.knobj <> '000000000000000000'
	  and stpo.stlkn not in (
		select stas.stlkn
		from sapr3.stas stas
		where stas.stlnr = mast.stlnr and stas.lkenz = 'X'
	  )
;
  
insert into SAPR3.CUOB(MANDT,KNTAB,KNOBJ,KNNUM,ADZHL,SHAD_UPDATE_TS,SHAD_UPDATE_IND) 
  select distinct '100'
		,'STPO'
		,stpo.knobj
		,'0000000699'
		,'0000'
		,'2017-09-19 00:00:00.000000'
		,'I'
	from sapr3.mast mast
	join sapr3.stpo stpo on stpo.mandt = mast.mandt and stpo.stlnr = mast.stlnr
	join sapr3.mara mara on mara.mandt = mast.mandt and mara.matnr = stpo.idnrk
	left outer join sapr3.cuob cuob on cuob.mandt = mast.mandt and cuob.knobj = stpo.knobj
	left outer join sapr3.cukb cukb on cukb.mandt = mast.mandt and cukb.knnum = cuob.knnum
	where mast.mandt = '100'
	  and mara.mtart in ('ZGRP')
	  and mast.werks like '17%'
	  and cukb.knnam is null
	  and stpo.knobj <> '000000000000000000'
	  and stpo.stlkn not in (
		select stas.stlkn
		from sapr3.stas stas
		where stas.stlnr = mast.stlnr and stas.lkenz = 'X'
	  )
  ;
  
insert into SAPR3.CUOB(MANDT,KNTAB,KNOBJ,KNNUM,ADZHL,SHAD_UPDATE_TS,SHAD_UPDATE_IND) 
  select distinct '100'
		,'STPO'
		,stpo.knobj
		,'0000000694'
		,'0000'
		,'2017-09-19 00:00:00.000000'
		,'I'
	from sapr3.mast mast
	join sapr3.stpo stpo on stpo.mandt = mast.mandt and stpo.stlnr = mast.stlnr
	join sapr3.mara mara on mara.mandt = mast.mandt and mara.matnr = stpo.idnrk
	left outer join sapr3.cuob cuob on cuob.mandt = mast.mandt and cuob.knobj = stpo.knobj
	left outer join sapr3.cukb cukb on cukb.mandt = mast.mandt and cukb.knnum = cuob.knnum
	where mast.mandt = '100'
	  and mara.mtart in ('ZOSP')
	  and mast.werks like '17%'
	  and cukb.knnam is null
	  and stpo.knobj <> '000000000000000000'
	  and stpo.stlkn not in (
		select stas.stlkn
		from sapr3.stas stas
		where stas.stlnr = mast.stlnr and stas.lkenz = 'X'
	  )
  ;