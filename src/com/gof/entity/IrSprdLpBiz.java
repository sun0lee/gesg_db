package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.gof.entity.his.HIrSprdLpBiz;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IR_SPRD_LP_BIZ")
@FilterDef(name="eqBaseYymm", parameters= @ParamDef(name ="bssd",  type="string"))
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrSprdLpBiz implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -2721793562033729088L;

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

	private Double liqPrem;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static IrSprdLpBiz convertFrom(HIrSprdLpBiz from, String baseYymm, int seq) {
		return builder()
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

}
