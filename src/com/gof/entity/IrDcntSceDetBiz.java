package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HIrDcntSceDetBiz;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IR_DCNT_SCE_DET_BIZ")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrDcntSceDetBiz implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 3917757332051508436L;

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
	private String matCd;

	private Double spotRate;
	private Double fwdRate;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static IrDcntSceDetBiz convertFrom(HIrDcntSceDetBiz from, String baseYymm, int seq) {
		return builder()
				.baseYymm(from.getBaseYymm())
				.applBizDv(from.getApplBizDv())
				.irModelId(from.getIrModelId())
				.irCurveId(from.getIrCurveId())
				.irCurveSceNo(from.getIrCurveSceNo())
				.matCd(from.getMatCd())
				.spotRate(from.getSpotRate())
				.fwdRate(from.getFwdRate())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())

						.build();
	}
}