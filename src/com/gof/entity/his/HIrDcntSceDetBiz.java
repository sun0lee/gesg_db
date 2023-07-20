package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.IrDcntSceDetBiz;
import com.gof.interfaces.EntityIdentifier;
import com.gof.interfaces.HisEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IRH_DCNT_SCE_DET_BIZ")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrDcntSceDetBiz implements Serializable, EntityIdentifier, HisEntity<IrDcntSceDetBiz> {

	private static final long serialVersionUID = 3917757332051508436L;

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
	private String matCd;

	private Double spotRate;
	private Double fwdRate;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static HIrDcntSceDetBiz convertFrom(com.gof.entity.IrDcntSceDetBiz from, String baseYymm, int seq) {
		return builder().seq(seq)
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

	@Override
	public  IrDcntSceDetBiz convertToBiz(String baseYymm, int seq) {
		return IrDcntSceDetBiz.builder()
				.baseYymm(this.getBaseYymm())
				.applBizDv(this.getApplBizDv())
				.irModelId(this.getIrModelId())
				.irCurveId(this.getIrCurveId())
				.irCurveSceNo(this.getIrCurveSceNo())
				.matCd(this.getMatCd())
				.spotRate(this.getSpotRate())
				.fwdRate(this.getFwdRate())
				.lastModifiedBy(this.getLastModifiedBy()+"_"+seq)
				.lastUpdateDate(this.getLastUpdateDate())

						.build();
	}
}