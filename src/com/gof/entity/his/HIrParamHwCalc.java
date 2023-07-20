package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
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
@Table( name ="E_IRH_PARAM_HW_CALC")
@FilterDef(name="FILTER", parameters= { @ParamDef(name="baseYymm", type="string") })
@Filters( { @Filter(name ="FILTER", condition="BASE_YYMM = :baseYymm") } )
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrParamHwCalc implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -3199922647182076353L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
	@Id
	private String irModelId;

	@Id
	private String irCurveId;

	@Id
	private String matCd;

	@Id
	private String paramTypCd;

	private Double paramVal;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;


	public static HIrParamHwCalc convertFrom(com.gof.entity.IrParamHwCalc from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(from.getBaseYymm())
				.irModelId(from.getIrModelId())
				.irCurveId(from.getIrCurveId())
				.matCd(from.getMatCd())
				.paramTypCd(from.getParamTypCd())
				.paramVal(from.getParamVal())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())

						.build();
	}
}


