package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IRH_PARAM_MODEL_CALC")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrParamModelCalc implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 3998120244491066027L;

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



	public static HIrParamModelCalc convertFrom(com.gof.entity.IrParamModelCalc from, String baseYymm, int seq) {
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
}


