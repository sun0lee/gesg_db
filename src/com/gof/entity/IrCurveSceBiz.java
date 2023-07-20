package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HIrCurveSceBiz;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="E_IR_CURVE_SCE_BIZ")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class IrCurveSceBiz implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 4458482460359847563L;

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

	public static IrCurveSceBiz convertFrom(HIrCurveSceBiz from, String baseYymm, int seq) {
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
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())
						.build();
	}

}


