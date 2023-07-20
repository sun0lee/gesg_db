package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
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
@Table(name ="E_IRH_CURVE_SPOT")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@ToString
public class HIrCurveSpot implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 8405894865559378104L;

	@Id
	private String baseDate;

	@Id
	private int seq;
	@Id
	@Column(name ="IR_CURVE_ID")
	private String irCurveId;

	@Id
	private String matCd;

	private Double spotRate;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;




	public static HIrCurveSpot convertFrom(com.gof.entity.IrCurveSpot from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseDate(from.getBaseDate())
				.irCurveId(from.getIrCurveId())
				.matCd(from.getMatCd())
				.spotRate(from.getSpotRate())
				.lastUpdateDate(from.getLastUpdateDate())
				.lastModifiedBy(from.getLastModifiedBy())
						.build();
	}
}
