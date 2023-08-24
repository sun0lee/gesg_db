package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HIrParamSw;
import com.gof.interfaces.EntityIdentifier;
import com.gof.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IR_PARAM_SW")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrParamSw implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -4175243759288891655L;

	@Id
	private String baseYymm;

	@Id
	private String applBizDv;

	@Id
	private String irCurveId;

	@Id
	private Integer irCurveSceNo;

	private String irCurveSceNm;
	private String curCd;
	private Integer freq;
	private Integer llp;
	private Double ltfr;
	private Integer ltfrCp;
	private Double liqPrem;
	private String liqPremApplDv;
	private Integer shkSprdSceNo;
	private Double swAlphaYtm;
	private String stoSceGenYn;
	private String fwdMatCd;
	private Double multIntRate;
	private Double addSprd;
	private Double ytmAddSprd;
	private String pvtRateMatCd;
	private Double multPvtRate;

	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public double getYtmSpread() {
		if(ytmAddSprd == null) {
			return 0.0;
		}
		return ytmAddSprd;
	}

	// default 처리 
	// TODO: FSS '24.03 llp : 20 -> 30 조정 예정.
	public Integer getLlp() {
		return llp = StringUtil.objectToPrimitive(llp, 20);
	}
//	public Integer getFreq() {
//		return freq = StringUtil.objectToPrimitive(freq, 2) ;
//	}
//	public String getFwdMatCd() {
//		return fwdMatCd = StringUtil.objectToPrimitive(fwdMatCd, "M0000");
//	}
//	public String getLiqPremApplDv() {
//		return liqPremApplDv = StringUtil.objectToPrimitive(liqPremApplDv, "1");
//	}
//	public Double getSwAlphaYtm() {
//		return swAlphaYtm = StringUtil.objectToPrimitive(swAlphaYtm, 0.1) ; 
//	}		
//	public Double getMultIntRate() {
//		return multIntRate = StringUtil.objectToPrimitive(multIntRate,1.0);
//	}
//	public Double getAddSprd() {
//		return addSprd =  StringUtil.objectToPrimitive(addSprd, 0.0);
//	}
//	public String getPvtRateMatCd() {
//		return pvtRateMatCd = StringUtil.objectToPrimitive(pvtRateMatCd , "M0000");
//	}
//	public Double getMultPvtRate() {
//		return multPvtRate = StringUtil.objectToPrimitive(multPvtRate , 1.0  );
//	}
	
	
	public static IrParamSw convertFrom(HIrParamSw from, String baseYymm, int seq) {
		return builder()
				.baseYymm(from.getBaseYymm())
				.applBizDv(from.getApplBizDv())
				.irCurveId(from.getIrCurveId())
				.curCd(from.getCurCd())
				.liqPremApplDv(from.getLiqPremApplDv())
				.stoSceGenYn(from.getStoSceGenYn())
				.irCurveSceNm(from.getIrCurveSceNm())
				.llp(from.getLlp())
				.ltfrCp(from.getLtfrCp())
				.irCurveSceNo(from.getIrCurveSceNo())
				.swAlphaYtm(from.getSwAlphaYtm())
				.liqPrem(from.getLiqPrem())
				.fwdMatCd(from.getFwdMatCd())
				.shkSprdSceNo(from.getShkSprdSceNo())
				.pvtRateMatCd(from.getPvtRateMatCd())
				.multIntRate(from.getMultIntRate())
				.addSprd(from.getAddSprd())
				.multPvtRate(from.getMultPvtRate())
				.ltfr(from.getLtfr())
				.freq(from.getFreq())
				.ytmAddSprd(from.getYtmAddSprd())
				.lastUpdateDate(from.getLastUpdateDate())
				.lastModifiedBy(from.getLastModifiedBy())
						.build();
	}
}


