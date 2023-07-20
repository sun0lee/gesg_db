package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IRH_SPRD_LP")
@FilterDef(name="eqBaseYymm", parameters= @ParamDef(name ="bssd",  type="string"))
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrSprdLp implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 1004575080756955856L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
	@Id
	private String dcntApplModelCd;

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



	public static HIrSprdLp convertFrom(com.gof.entity.IrSprdLp from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(from.getBaseYymm())
				.applBizDv(from.getApplBizDv())
				.irCurveId(from.getIrCurveId())
				.dcntApplModelCd(from.getDcntApplModelCd())
				.irCurveSceNo(from.getIrCurveSceNo())
				.matCd(from.getMatCd())
				.liqPrem(from.getLiqPrem())
				.lastUpdateDate(from.getLastUpdateDate())
				.lastModifiedBy(from.getLastModifiedBy())

						.build();
	}

}
