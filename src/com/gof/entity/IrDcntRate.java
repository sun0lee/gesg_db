package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HIrDcntRate;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IR_DCNT_RATE")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrDcntRate implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -4252300668894647002L;

	@Id
	private String baseYymm;

	@Id
	private String applBizDv;

	@Id
	private String irCurveId;

	@Id
	private Integer irCurveSceNo;

	@Id
	private String matCd;

	private Double spotRate;
	private Double fwdRate;
	private Double adjSpotRate;
	private Double adjFwdRate;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public IrDcntRateBiz convertAdj() {

		IrDcntRateBiz adjDcnt = new IrDcntRateBiz();

		adjDcnt.setBaseYymm(this.baseYymm);
		adjDcnt.setApplBizDv(this.applBizDv  + "_L");
		adjDcnt.setIrCurveId(this.irCurveId);
		adjDcnt.setIrCurveSceNo(this.irCurveSceNo);
		adjDcnt.setMatCd(this.matCd);
		adjDcnt.setSpotRate(this.adjSpotRate);
		adjDcnt.setFwdRate(this.adjFwdRate);
		adjDcnt.setLastModifiedBy("GESG_" + this.getClass().getSimpleName());
		adjDcnt.setLastUpdateDate(LocalDateTime.now());

		return adjDcnt;
	}


	public IrDcntRateBiz convertBase() {

		IrDcntRateBiz baseDcnt = new IrDcntRateBiz();

		baseDcnt.setBaseYymm(this.baseYymm);
		baseDcnt.setApplBizDv(this.applBizDv + "_A");
		baseDcnt.setIrCurveId(this.irCurveId);
		baseDcnt.setIrCurveSceNo(this.irCurveSceNo);
		baseDcnt.setMatCd(this.matCd);
		baseDcnt.setSpotRate(this.spotRate);
		baseDcnt.setFwdRate(this.fwdRate);
		baseDcnt.setLastModifiedBy("GESG_" + this.getClass().getSimpleName());
		baseDcnt.setLastUpdateDate(LocalDateTime.now());

		return baseDcnt;
	}


	public IrDcntRate deepCopy(IrDcntRate org) {
		IrDcntRate copy = new IrDcntRate();

		copy.setBaseYymm(org.getBaseYymm());
		copy.setApplBizDv(org.getApplBizDv());
		copy.setIrCurveId(org.getIrCurveId());
		copy.setIrCurveSceNo(org.getIrCurveSceNo());
		copy.setMatCd(org.getMatCd());
		copy.setSpotRate(org.getSpotRate());
		copy.setFwdRate(org.getFwdRate());
		copy.setAdjSpotRate(org.getAdjSpotRate());
		copy.setAdjFwdRate(org.getAdjFwdRate());

		return copy;
	}


	public IrCurveSpot convertAdjSpot() {

		IrCurveSpot adjSpot = new IrCurveSpot();

		adjSpot.setBaseDate(this.baseYymm);
		adjSpot.setIrCurveId(this.irCurveId);
		adjSpot.setMatCd(this.matCd);
		adjSpot.setSpotRate(this.adjSpotRate);
		adjSpot.setLastModifiedBy("GESG_" + this.getClass().getSimpleName());
		adjSpot.setLastUpdateDate(LocalDateTime.now());

		return adjSpot;
	}


	public IrCurveSpot convertBaseSpot() {

		IrCurveSpot spot = new IrCurveSpot();

		spot.setBaseDate(this.baseYymm);
		spot.setIrCurveId(this.irCurveId);
		spot.setMatCd(this.matCd);
		spot.setSpotRate(this.spotRate);
		spot.setLastModifiedBy("GESG_" + this.getClass().getSimpleName());
		spot.setLastUpdateDate(LocalDateTime.now());

		return spot;
	}

	public static IrDcntRate convertFrom(HIrDcntRate from, String baseYymm, int seq) {
		return builder()
				.baseYymm(from.getBaseYymm())
				.irCurveId(from.getIrCurveId())
				.applBizDv(from.getApplBizDv())
				.irCurveSceNo(from.getIrCurveSceNo())
				.matCd(from.getMatCd())
				.spotRate(from.getSpotRate())
				.adjSpotRate(from.getAdjSpotRate())
				.fwdRate(from.getFwdRate())
				.adjFwdRate(from.getAdjFwdRate())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())

						.build();
	}

	public HIrDcntRate convertToHis(String baseYymm, int seq) {
		return HIrDcntRate.builder().seq(seq)
				.baseYymm(this.getBaseYymm())
				.irCurveId(this.getIrCurveId())
				.applBizDv(this.getApplBizDv())
				.irCurveSceNo(this.getIrCurveSceNo())
				.matCd(this.getMatCd())
				.spotRate(this.getSpotRate())
				.adjSpotRate(this.getAdjSpotRate())
				.fwdRate(this.getFwdRate())
				.adjFwdRate(this.getAdjFwdRate())
				.lastModifiedBy(this.getLastModifiedBy())
				.lastUpdateDate(this.getLastUpdateDate())

						.build();
	}
}