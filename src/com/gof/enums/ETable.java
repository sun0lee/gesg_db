package com.gof.enums;

public enum ETable {

	E_IR_DCNT_RATE_BIZ  		( 1, false, "E_IR_DCNT_RATE_BIZ" , 		"E_IRH_DCNT_RATE_BIZ", 		"HIrDcntRateBiz")
	, E_IR_PARAM_HW_BIZ  		( 1, false, "E_IR_PARAM_HW_BIZ" , 		"E_IRH_PARAM_HW_BIZ", 		"HIrParamHwBiz")
	, E_IR_SPRD_LP_BIZ  		( 1, false, "E_IR_SPRD_LP_BIZ" , 		"E_IRH_SPRD_LP_BIZ", 		"HIrSprdLpBiz")
	, E_IR_PARAM_MODEL_BIZ  	( 1, true, 	"E_IR_PARAM_MODEL_BIZ" , 	"E_IRH_PARAM_MODEL_BIZ", 	"HIrParamModelBiz")
	, E_IR_SPRD_AFNS_BIZ  		( 1, true, 	"E_IR_SPRD_AFNS_BIZ" , 		"E_IRH_SPRD_AFNS_BIZ", 		"HIrSprdAfnsBiz")
	, E_IR_PARAM_AFNS_BIZ  		( 1, true, 	"E_IR_PARAM_AFNS_BIZ" , 	"E_IRH_PARAM_AFNS_BIZ", 	"HIrParamAfnsBiz")
	, E_IR_DCNT_SCE_STO_BIZ  	( 1, false, "E_IR_DCNT_SCE_STO_BIZ" , 	"E_IRH_DCNT_SCE_STO_BIZ", 	"HIrDcntSceStoBiz")
	, E_IR_DCNT_SCE_DET_BIZ  	( 1, false, "E_IR_DCNT_SCE_DET_BIZ" , 	"E_IRH_DCNT_SCE_DET_BIZ", 	"HIrDcntSceDetBiz")
	, E_IR_CURVE_SCE_BIZ  		( 1, false, "E_IR_CURVE_SCE_BIZ" , 		"E_IRH_CURVE_SCE_BIZ", 		"HIrCurvSceBiz")
	, E_RC_CORP_PD_BIZ  		( 1, false, "E_RC_CORP_PD_BIZ" , 		"E_RCH_CORP_PD_BIZ", 		"HRcCorpPdBiz")
	, E_RC_SEG_LGD_BIZ  		( 1, false, "E_RC_SEG_LGD_BIZ" , 		"E_RCH_SEG_LGD_BIZ", 		"HRcSegLgdBiz")
	, E_RC_INFLATION_BIZ  		( 1, false, "E_RC_INFLATION_BIZ" , 		"E_RCH_INFLATION_BIZ", 		"HRcInflationBiz")
	, E_RC_SEG_PREP_RATE_BIZ  	( 1, false, "E_RC_SEG_PREP_RATE_BIZ" , 	"E_RCH_SEG_PREP_RATE_BIZ", 	"HRcSegPrepRateBiz")
	;

	private int order;
	private boolean isAllBiz;
	private String toTableName;
	private String fromTableName;
	private String fromEntityName;

	private ETable(int order, boolean isAllBiz, String toTableName, String fromTableName, String fromEntityName) {
		this.order = order;
		this.isAllBiz= isAllBiz;
		this.toTableName = toTableName;
		this.fromTableName = fromTableName;
		this.fromEntityName = fromEntityName;
	}

	public int getOrder() {
		return order;
	}

	public boolean isAllBiz() {
		return isAllBiz;
	}
	public String getToTableName() {
		return toTableName;
	}
	public String getFromTableName() {
		return fromTableName;
	}
	public String getFromEntityName() {
		return fromEntityName;
	}


}
