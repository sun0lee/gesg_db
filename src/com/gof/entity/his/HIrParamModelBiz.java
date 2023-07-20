package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.IrParamModelBiz;
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
@Table(name ="E_IRH_PARAM_MODEL_BIZ")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrParamModelBiz implements Serializable, EntityIdentifier, HisEntity<IrParamModelBiz> {

	private static final long serialVersionUID = -1938042374180430559L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
	@Id
	private String irModelId;

	@Id
	private String irCurveId;

	@Id
	private String paramTypCd;

	private Double paramVal;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static HIrParamModelBiz convertFrom(com.gof.entity.IrParamModelBiz from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(from.getBaseYymm())
				.irModelId(from.getIrModelId())
				.irCurveId(from.getIrCurveId())
				.paramTypCd(from.getParamTypCd())
				.paramVal(from.getParamVal())
				.lastUpdateDate(from.getLastUpdateDate())
				.lastModifiedBy(from.getLastModifiedBy())

						.build();
	}

	@Override
	public IrParamModelBiz convertToBiz(String baseYymm, int seq) {
		return IrParamModelBiz.builder()
				.baseYymm(this.getBaseYymm())
				.irModelId(this.getIrModelId())
				.irCurveId(this.getIrCurveId())
				.paramTypCd(this.getParamTypCd())
				.paramVal(this.getParamVal())
				.lastUpdateDate(this.getLastUpdateDate())
				.lastModifiedBy(this.getLastModifiedBy()+"_"+seq)

						.build();
	}
}


