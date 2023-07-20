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
@Table(name ="E_IRH_DCNT_SCE_STO_GNR")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrDcntSceStoGnr implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -2494093547313432332L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
	@Id
	private String applBizDv;

    @Id
	private String irModelId;

	@Id
	private String irCurveId;

    @Id
	private Integer irCurveSceNo;

	@Id
	private Integer sceNo;

	@Id
	private String matCd;

	private Double spotRate;
	private Double fwdRate;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static HIrDcntSceStoGnr convertFrom(com.gof.entity.IrDcntSceStoGnr from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(from.getBaseYymm())
				.applBizDv(from.getApplBizDv())
				.irModelId(from.getIrModelId())
				.irCurveId(from.getIrCurveId())
				.irCurveSceNo(from.getIrCurveSceNo())
				.sceNo(from.getSceNo())
				.matCd(from.getMatCd())
				.spotRate(from.getSpotRate())
				.fwdRate(from.getFwdRate())
				.lastUpdateDate(from.getLastUpdateDate())
				.lastModifiedBy(from.getLastModifiedBy())

						.build();
	}
}