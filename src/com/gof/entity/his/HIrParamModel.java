package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name ="E_IRH_PARAM_MODEL")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrParamModel implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -3967105002517595201L;

	@Id
	private String irModelId;
	@Id
	private String baseYymm;

	@Id
	private int seq;
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

	public static HIrParamModel convertFrom(com.gof.entity.IrParamModel from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(baseYymm)
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
