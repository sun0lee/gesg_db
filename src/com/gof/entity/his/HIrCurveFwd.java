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

@Entity
@Table(name ="E_IRH_CURVE_FWD")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class HIrCurveFwd implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -2709553445772419321L;

	@Id
	private String baseDate;

	@Id
	private int seq;

	@Id
	private String irCurveId;

	@Id
	private String fwdMatCd;

	@Id
	private String matCd;

	private Double intRate;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static HIrCurveFwd convertFrom(com.gof.entity.IrCurveFwd from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseDate(from.getBaseDate())
				.irCurveId(from.getIrCurveId())
				.fwdMatCd(from.getFwdMatCd())
				.matCd(from.getMatCd())
				.intRate(from.getIntRate())
				.lastUpdateDate(from.getLastUpdateDate())
				.lastModifiedBy(from.getLastModifiedBy())
						.build();
	}
}
