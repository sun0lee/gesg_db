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
@Table(name ="E_IRH_CURVE_SPOT_WEEK")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrCurveSpotWeek implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 8687612876394929135L;

	@Id
	private String baseDate;
	@Id
	private int seq;

	@Id
	private String irCurveId;

	@Id
	private String matCd;

	private Double spotRate;
	private String dayOfWeek;
	private String bizDayType;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;


//	@Override
//	public IrCurveWeek clone() throws CloneNotSupportedException {
//		return (IrCurveWeek) super.clone();
//	}

	public static HIrCurveSpotWeek convertFrom(com.gof.entity.IrCurveSpotWeek from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseDate(from.getBaseDate())
				.irCurveId(from.getIrCurveId())
				.matCd(from.getMatCd())
				.spotRate(from.getSpotRate())
				.bizDayType(from.getBizDayType())
				.dayOfWeek(from.getDayOfWeek())
				.lastUpdateDate(from.getLastUpdateDate())
				.lastModifiedBy(from.getLastModifiedBy())

						.build();
	}
}
