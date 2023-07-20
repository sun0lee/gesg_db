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
@Table(name ="E_IRH_PARAM_HW_RND")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrParamHwRnd implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -4252300668894647002L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
	@Id
	private String irModelId;

	@Id
	private String irCurveId;

	@Id
	private Integer sceNo;

	@Id
	private String matCd;

	private Double rndNum;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;


	public static HIrParamHwRnd convertFrom(com.gof.entity.IrParamHwRnd from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(from.getBaseYymm())
				.irModelId(from.getIrModelId())
				.irCurveId(from.getIrCurveId())
				.sceNo(from.getSceNo())
				.matCd(from.getMatCd())
				.rndNum(from.getRndNum())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())

						.build();
	}
}