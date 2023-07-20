package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HIrCurveSpotWeek;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IR_CURVE_SPOT_WEEK")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrCurveSpotWeek implements Serializable, EntityIdentifier, Comparable<IrCurveSpotWeek>{

	private static final long serialVersionUID = 8687612876394929135L;

	@Id
	private String baseDate;

	@Id
	private String irCurveId;

	@Id
	private String matCd;

	private Double spotRate;
	private String dayOfWeek;
	private String bizDayType;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;
	

	
	

	public IrCurveSpotWeek(String baseDate, String matCd, Double intRate) {

		this.baseDate = baseDate;
		this.matCd = matCd;
		this.spotRate = intRate;
	}

	public IrCurveSpotWeek(String bssd, IrCurveSpotWeek curveHis) {

		this.baseDate = curveHis.getBaseDate();
		this.irCurveId = curveHis.getIrCurveId();
		this.matCd = curveHis.getMatCd();
		this.spotRate = curveHis.getSpotRate();
	}

	public IrCurveSpotWeek(IrCurveSpotWeek curveHis) {

		this.baseDate = curveHis.baseDate;
		this.irCurveId = curveHis.irCurveId;
		this.matCd = curveHis.matCd;
		this.spotRate = curveHis.spotRate;
		this.dayOfWeek = curveHis.dayOfWeek;
		this.bizDayType = curveHis.bizDayType;
		this.lastModifiedBy = curveHis.lastModifiedBy;
		this.lastUpdateDate = curveHis.lastUpdateDate;
	}

	public IrCurveSpot convertToHis() {
		IrCurveSpot rst = new IrCurveSpot();

		rst.setBaseDate(this.baseDate);
		rst.setIrCurveId(this.irCurveId);
		rst.setMatCd(this.matCd);
//		rst.setSpotRate(this.spotRate);
		rst.setSpotRate(this.spotRate);
		rst.setLastModifiedBy("GESG_" + this.getClass().getSimpleName());
		rst.setLastUpdateDate(LocalDateTime.now());
		return rst;
	}

	
	public IrCurveSpot convertToPercentHis() {
		IrCurveSpot rst = new IrCurveSpot();

		rst.setBaseDate(this.baseDate);
		rst.setIrCurveId(this.irCurveId);
		rst.setMatCd(this.matCd);
//		rst.setSpotRate(this.spotRate);
		rst.setSpotRate(this.spotRate *100);
		rst.setLastModifiedBy("GESG_" + this.getClass().getSimpleName());
		rst.setLastUpdateDate(LocalDateTime.now());
		return rst;
	}
	
//	@Override
//	public IrCurveWeek clone() throws CloneNotSupportedException {
//		return (IrCurveWeek) super.clone();
//	}

	public static IrCurveSpotWeek convertFrom(HIrCurveSpotWeek from, String baseYymm, int seq) {
		return builder()
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

	@Override
	public int compareTo(IrCurveSpotWeek other) {
		return this.baseDate.compareTo(other.baseDate);
	}
}
