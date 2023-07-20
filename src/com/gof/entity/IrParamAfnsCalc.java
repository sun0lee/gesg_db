package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HIrParamAfnsCalc;
import com.gof.enums.EAfnsParamTypCd;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IR_PARAM_AFNS_CALC")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrParamAfnsCalc implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -4843148071754518205L;

	@Id
	private String baseYymm;

	@Id
	private String irModelId;

	@Id
	private String irCurveId;

	@Id
//	private String paramTypCd;
	@Enumerated(EnumType.STRING)
	private EAfnsParamTypCd paramTypCd;


	private Double paramVal;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static IrParamAfnsCalc convertFrom(HIrParamAfnsCalc from, String baseYymm, int seq) {
		return builder()
				.baseYymm(from.getBaseYymm())
				.irModelId(from.getIrModelId())
				.irCurveId(from.getIrCurveId())
				.paramTypCd(from.getParamTypCd())
				.paramVal(from.getParamVal())
				.lastUpdateDate(from.getLastUpdateDate())
				.lastModifiedBy(from.getLastModifiedBy())

						.build();
	}
	
	public IrParamAfnsCalc getInitParam() {
		
		return builder()
				.baseYymm(this.getBaseYymm())
				.irModelId(this.getIrModelId())
				.irCurveId(this.getIrCurveId())
				.paramTypCd(this.getParamTypCd())
//				.paramVal(EAfnsParamTypCd.INIT_EPSILON.equals(this.getParamTypCd())? 1.0: this.getParamVal())
				.paramVal(this.getParamVal())
				.lastUpdateDate(this.getLastUpdateDate())
				.lastModifiedBy(this.getLastModifiedBy())

						.build();
	}
	
	public IrParamAfnsBiz converToBiz() {
		
		return IrParamAfnsBiz.builder()
				.baseYymm(this.getBaseYymm())
				.irModelId(this.getIrModelId())
				.irCurveId(this.getIrCurveId())
				.paramVal(this.getParamVal())
				.lastUpdateDate(this.getLastUpdateDate())
				.lastModifiedBy(this.getLastModifiedBy())
				.build();

	}
}
