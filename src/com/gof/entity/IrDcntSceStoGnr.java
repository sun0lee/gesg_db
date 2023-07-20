package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HIrDcntSceStoGnr;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IR_DCNT_SCE_STO_GNR")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrDcntSceStoGnr implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -2494093547313432332L;

	@Id
	private String baseYymm;

	@Id
	private String applBizDv;

    @Id
	private String irModelId;

	@Id
	private String irCurveId;

    @Id
	private Integer irCurveSceNo;

	@Id
	private Integer sceNo;

	@Id
	private String matCd;

	private Double spotRate;
	private Double fwdRate;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static IrDcntSceStoGnr convertFrom(HIrDcntSceStoGnr from, String baseYymm, int seq) {
		return builder()
				.baseYymm(from.getBaseYymm())
				.applBizDv(from.getApplBizDv())
				.irModelId(from.getIrModelId())
				.irCurveId(from.getIrCurveId())
				.irCurveSceNo(from.getIrCurveSceNo())
				.sceNo(from.getSceNo())
				.matCd(from.getMatCd())
				.spotRate(from.getSpotRate())
				.fwdRate(from.getFwdRate())
				.lastUpdateDate(from.getLastUpdateDate())
				.lastModifiedBy(from.getLastModifiedBy())

						.build();
	}
	public HIrDcntSceStoGnr convertToHis(String baseYymm, int seq) {
		return HIrDcntSceStoGnr.builder().seq(seq)
				.baseYymm(this.getBaseYymm())
				.applBizDv(this.getApplBizDv())
				.irModelId(this.getIrModelId())
				.irCurveId(this.getIrCurveId())
				.irCurveSceNo(this.getIrCurveSceNo())
				.sceNo(this.getSceNo())
				.matCd(this.getMatCd())
				.spotRate(this.getSpotRate())
				.fwdRate(this.getFwdRate())
				.lastUpdateDate(this.getLastUpdateDate())
				.lastModifiedBy(this.getLastModifiedBy())

						.build();
	}
}