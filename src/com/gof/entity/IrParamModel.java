package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HIrParamModel;
import com.gof.enums.EBoolean;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IR_PARAM_MODEL")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrParamModel implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -3967105002517595201L;

	@Id
	private String irModelId;

	private String irModelNm;

	@Id
	private String irCurveId;

	private Integer totalSceNo;
	private Integer rndSeed;
	private Double itrTol;

	@Enumerated(EnumType.STRING)
	private EBoolean useYn;

	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static IrParamModel convertFrom(HIrParamModel from, String baseYymm, int seq) {
		return builder()
				.irModelId(from.getIrModelId())
				.irCurveId(from.getIrCurveId())
				.irModelNm(from.getIrModelNm())
				.totalSceNo(from.getTotalSceNo())
				.rndSeed(from.getRndSeed())
				.itrTol(from.getItrTol())
				.useYn(from.getUseYn())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())

						.build();
	}
}