package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IRH_PARAM_SW")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrParamSw implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -4175243759288891655L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
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

	public static HIrParamSw convertFrom(com.gof.entity.IrParamSw from, String baseYymm, int seq) {
		return builder().seq(seq)
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


