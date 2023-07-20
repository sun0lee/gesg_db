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
@Table(name ="E_IRH_SPRD_CURVE")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrSprdCurve implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 8770367862233153559L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
	@Id
	private String irCurveId;

	@Id
	private String irTypDvCd;

	@Id
	private String matCd;

	private Double intRate;
	private Double crdSprd;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static HIrSprdCurve convertFrom(com.gof.entity.IrSprdCurve from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(from.getBaseYymm())
				.irCurveId(from.getIrCurveId())
				.irTypDvCd(from.getIrTypDvCd())
				.matCd(from.getMatCd())
				.intRate(from.getIntRate())
				.crdSprd(from.getCrdSprd())
				.lastUpdateDate(from.getLastUpdateDate())
				.lastModifiedBy(from.getLastModifiedBy())

						.build();
	}
}
