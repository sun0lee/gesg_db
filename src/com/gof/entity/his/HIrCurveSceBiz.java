package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.IrCurveSceBiz;
import com.gof.interfaces.EntityIdentifier;
import com.gof.interfaces.HisEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="E_IRH_CURVE_SCE_BIZ")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class HIrCurveSceBiz implements Serializable, EntityIdentifier, HisEntity<IrCurveSceBiz> {

	private static final long serialVersionUID = 4458482460359847563L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
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

	public static HIrCurveSceBiz convertFrom(com.gof.entity.IrCurveSceBiz from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(from.getBaseYymm())
				.applBizDv(from.getApplBizDv())
				.irModelId(from.getIrModelId())
				.irCurveId(from.getIrCurveId())
				.irCurveSceNo(from.getIrCurveSceNo())
				.sceNo(from.getSceNo())
				.matCd(from.getMatCd())
				.spotRate(from.getSpotRate())
				.fwdRate(from.getFwdRate())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())
						.build();
	}

	@Override
	public IrCurveSceBiz convertToBiz(String baseYymm, int seq) {
		return IrCurveSceBiz.builder()
				.baseYymm(this.getBaseYymm())
				.applBizDv(this.getApplBizDv())
				.irModelId(this.getIrModelId())
				.irCurveId(this.getIrCurveId())
				.irCurveSceNo(this.getIrCurveSceNo())
				.sceNo(this.getSceNo())
				.matCd(this.getMatCd())
				.spotRate(this.getSpotRate())
				.fwdRate(this.getFwdRate())
				.lastModifiedBy(this.getLastModifiedBy())
				.lastUpdateDate(this.getLastUpdateDate())
						.build();
	}
}


