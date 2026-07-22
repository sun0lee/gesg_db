package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name ="E_IR_DISC_EX_MST")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
@Slf4j
public class IrDiscExMst implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -4252300668894647002L;


	@Id
	private String intRateCd;

	
	@Id
	private String exRateId;
	
	@Id
	private String avgTyp;
	
	private String exRateNm;
	private String acctDvCd;
	
	private Double exRateWght;
	private Double addSpread;
	
	private Double roundDigit;
	
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public String getExRatePk() {
		return exRateId + avgTyp;
	}

	public IrDiscExMst updateWeight(Map<String, Double> wghtMap) {
		double tempWght =0.0;
		if(acctDvCd != null) {
			tempWght = wghtMap.getOrDefault(exRateId+avgTyp+acctDvCd, 0.0) / 100.0;		 // USER INPUT DATA AS % VALUE!!! 
		}
		else {
			tempWght = this.exRateWght;	
		}
		
		return IrDiscExMst.builder()
				.intRateCd(intRateCd)
				.exRateId(exRateId)
				.avgTyp(avgTyp)
				.exRateNm(exRateNm)
				.exRateWght(tempWght)
				.addSpread(addSpread)
				.roundDigit(roundDigit)
				.lastModifiedBy(lastModifiedBy)
				.lastUpdateDate(LocalDateTime.now())
				.build();
		
	}	

	
	public double getAdjAddSpread() {
		return addSpread == null? 0.0: addSpread /100.0;
	}
	
	public double getRound(double exRate) {
		double temp = 0.0;
		if(roundDigit == null) {
			return exRate;
		}
		else {
			temp  = Math.pow(10.0, roundDigit + 2);
//			log.info("Mst : {},{},{},{},{}", intRateCd, exRateId, exRate, temp, Math.round( exRate  * temp ) / temp);
			
			return Math.round( exRate  * temp ) / temp;
		}
	}

}