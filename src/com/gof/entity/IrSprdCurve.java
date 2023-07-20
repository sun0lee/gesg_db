package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HIrSprdCurve;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IR_SPRD_CURVE")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrSprdCurve implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 8770367862233153559L;

	@Id
	private String baseYymm;

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

	public static IrSprdCurve convertFrom(HIrSprdCurve from, String baseYymm, int seq) {
		return builder()
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
