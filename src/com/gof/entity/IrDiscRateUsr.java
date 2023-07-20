package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HIrDcntRate;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IR_USER_DISC_RATE_HIS")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrDiscRateUsr implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -4252300668894647002L;

	@Id
	private String baseYymm;

	@Id
	private String intRateCd;

	private Double mgtAsstYield;
	private Double exBaseIr;
	private Double exBaseIrWght;
	private Double baseDiscRate;
	private Double discRateAdjRate;
	private Double applDiscRate;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public IrDiscRate convertToDiscRate() {

		return IrDiscRate.builder()
				.baseYymm(baseYymm)
				.intRateCd(intRateCd)
				.discRateCalcTyp("UD")
				.matCd("M0003")
				.mgtYield(mgtAsstYield)
				.exBaseIr(exBaseIr)
				.exBaseIrWght(exBaseIrWght)
				.baseDiscRate(baseDiscRate)
				.adjRate(discRateAdjRate)
				.discRate(applDiscRate)
				.vol(0.0)
				.lastModifiedBy("GESG")
				.lastUpdateDate(LocalDateTime.now())
				.build();
	}



}