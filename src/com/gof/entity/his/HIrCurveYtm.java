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
@Table(name ="E_IRH_CURVE_YTM")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrCurveYtm implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 1340116167808300605L;

	@Id
	private String baseDate;
	@Id
	private int seq;
	@Id
	private String irCurveId;

	@Id
	private String matCd;

	private Double ytm;
	private String tickerNm;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;


	public static HIrCurveYtm convertFrom(com.gof.entity.IrCurveYtm from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseDate(from.getBaseDate())
				.irCurveId(from.getIrCurveId())
				.matCd(from.getMatCd())
				.tickerNm(from.getTickerNm())
				.ytm(from.getYtm())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(LocalDateTime.now())

						.build();
	}
}