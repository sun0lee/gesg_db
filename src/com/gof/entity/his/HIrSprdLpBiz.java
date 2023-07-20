package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.gof.entity.IrSprdLpBiz;
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
@Table(name ="E_IRH_SPRD_LP_BIZ")
@FilterDef(name="eqBaseYymm", parameters= @ParamDef(name ="bssd",  type="string"))
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrSprdLpBiz implements Serializable, EntityIdentifier, HisEntity<IrSprdLpBiz> {

	private static final long serialVersionUID = -2721793562033729088L;

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

	@Id
	private String matCd;

	private Double liqPrem;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static HIrSprdLpBiz convertFrom(com.gof.entity.IrSprdLpBiz from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(from.getBaseYymm())
				.applBizDv(from.getApplBizDv())
				.irCurveId(from.getIrCurveId())
				.matCd(from.getMatCd())
				.irCurveSceNo(from.getIrCurveSceNo())
				.liqPrem(from.getLiqPrem())
				.lastUpdateDate(from.getLastUpdateDate())
				.lastModifiedBy(from.getLastModifiedBy())

						.build();
	}


	public IrSprdLpBiz convertToBiz( String baseYymm, int seq) {
		return IrSprdLpBiz.builder()
				.baseYymm(this.getBaseYymm())
				.applBizDv(this.getApplBizDv())
				.irCurveId(this.getIrCurveId())
				.matCd(this.getMatCd())
				.irCurveSceNo(this.getIrCurveSceNo())
				.liqPrem(this.getLiqPrem())
//				.lastUpdateDate(this.getLastUpdateDate())
				.lastUpdateDate(LocalDateTime.now())
				.lastModifiedBy(this.getLastModifiedBy()+"_"+seq)
				.build();
	}
}
