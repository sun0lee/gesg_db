package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HIrDcntRateBiz;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IR_DCNT_RATE_BIZ")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrDcntRateBiz implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 9213714569868056834L;

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
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;


	public int getMatNum() {
		return Integer.parseInt(matCd.substring(1));
	}

	public double getDf() {
		return Math.pow(1+spotRate, -1.0 * getMatNum()/12);
	}

	public double getContForwardRate() {
		return Math.log(1+fwdRate);
	}

	public static IrDcntRateBiz convertFrom(HIrDcntRateBiz from, String baseYymm, int seq) {
		return builder()
				.baseYymm(from.getBaseYymm())
				.applBizDv(from.getApplBizDv())
				.irCurveId(from.getIrCurveId())
				.irCurveSceNo(from.getIrCurveSceNo())
				.matCd(from.getMatCd())
				.spotRate(from.getSpotRate())
				.fwdRate(from.getFwdRate())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())

						.build();
	}

	public HIrDcntRateBiz convertToHis(String baseYymm, int seq) {
		return HIrDcntRateBiz.builder().seq(seq)
				.baseYymm(this.getBaseYymm())
				.applBizDv(this.getApplBizDv())
				.irCurveId(this.getIrCurveId())
				.irCurveSceNo(this.getIrCurveSceNo())
				.matCd(this.getMatCd())
				.spotRate(this.getSpotRate())
				.fwdRate(this.getFwdRate())
				.lastModifiedBy(this.getLastModifiedBy())
				.lastUpdateDate(this.getLastUpdateDate())

						.build();
	}
}
