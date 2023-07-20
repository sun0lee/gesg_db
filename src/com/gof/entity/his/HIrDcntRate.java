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
@Table(name ="E_IRH_DCNT_RATE")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrDcntRate implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -4252300668894647002L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
	@Id
	private String applBizDv;

	@Id
	private String irCurveId;

	@Id
	private Integer irCurveSceNo;

	@Id
	private String matCd;

	private Double spotRate;
	private Double fwdRate;
	private Double adjSpotRate;
	private Double adjFwdRate;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;



	public static HIrDcntRate convertFrom(com.gof.entity.IrDcntRate from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(from.getBaseYymm())
				.irCurveId(from.getIrCurveId())
				.applBizDv(from.getApplBizDv())
				.irCurveSceNo(from.getIrCurveSceNo())
				.matCd(from.getMatCd())
				.spotRate(from.getSpotRate())
				.adjSpotRate(from.getAdjSpotRate())
				.fwdRate(from.getFwdRate())
				.adjFwdRate(from.getAdjFwdRate())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())

						.build();
	}
}