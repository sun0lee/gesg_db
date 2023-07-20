package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.gof.entity.his.HIrCurveYtm;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@IdClass(IrCurveYtmId.class)
@Table(name ="E_IR_CURVE_YTM")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrCurveYtm implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 1340116167808300605L;

	@Id
	private String baseDate;

	@Id
	private String irCurveId;

	@Id
	private String matCd;

	private Double ytm;
	private String tickerNm;

	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public IrCurveSpot convertSimple() {

		IrCurveSpot spot = new IrCurveSpot();

		spot.setBaseDate(this.baseDate);
		spot.setIrCurveId(this.irCurveId);
		spot.setMatCd(this.matCd);
		spot.setSpotRate(this.ytm);
		spot.setLastModifiedBy("GESG_" + this.getClass().getSimpleName());
		spot.setLastUpdateDate(LocalDateTime.now());

		return spot;
	}

	public static IrCurveYtm convertFrom(HIrCurveYtm from, String baseYymm, int seq) {
		return builder()
				.baseDate(from.getBaseDate())
				.irCurveId(from.getIrCurveId())
				.matCd(from.getMatCd())
				.tickerNm(from.getTickerNm())
				.ytm(from.getYtm())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(LocalDateTime.now())

						.build();
	}



	public IrCurveYtm addSpread(double ytmSpread) {

		IrCurveYtm ytm = new IrCurveYtm();

		ytm.setBaseDate(this.baseDate);
		ytm.setIrCurveId(this.irCurveId);
		ytm.setMatCd(this.matCd);
		ytm.setYtm(this.ytm + ytmSpread) ;
		ytm.setLastModifiedBy("GESG_" + this.getClass().getSimpleName());
		ytm.setLastUpdateDate(LocalDateTime.now());

		return ytm;
	}
	
	public String getPk() {
		return baseDate + irCurveId + matCd;
	}

}