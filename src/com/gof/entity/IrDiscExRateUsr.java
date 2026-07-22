package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
@Table(name ="E_IR_DISC_EX_RATE_USR")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrDiscExRateUsr implements Serializable, EntityIdentifier, Comparable<IrDiscExRateUsr> {

	private static final long serialVersionUID = -4252300668894647002L;

	@Id
	private String baseYymm;

	@Id
	private String exRateId;
	
	@Id
	private String avgTyp;
	
	private String exRateNm;
	
	private String avgStDate;
	private String avgEndDate;
	
	private String calcEndYymm;
	private String matCd;
	
	private Double exRateIr;

	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public String getExRatePk() {
		return exRateId+ avgTyp;
	}

	@Override
	public int compareTo(IrDiscExRateUsr other) {
		return baseYymm.compareTo(other.getBaseYymm());
	}
	
	public String getCalcStYymm() {
		LocalDate aa = LocalDate.parse(getCalcEndYymm()+"01",DateTimeFormatter.BASIC_ISO_DATE);
		return aa.minusMonths(2).format(DateTimeFormatter.BASIC_ISO_DATE).substring(0,6);
	}
	
	
	public double getExRateIrReal() {
		return exRateIr /100.0;
	}
	
	public String getCalcEndYymm() {
		return calcEndYymm ==null ?  getAdjDate(baseYymm, 2)  : calcEndYymm; 
		// 국고채(5년), 회사채(무보증 3년, AA-) 및 통화안정증권(1년) 수익률과 양도성예금증서(91일) 유통수익률은 공시기준이율 적용시점의 전전월말 직전3개월 가중이동평균을 통해 산출한다.
	}
	
//	public String getAdjCalcEndYymm() {
//		return calcEndYymm ==null ? getAdjDate(baseYymm, 2) : calcEndYymm; 
//	}
	
	private String getAdjDate(String yyyymm, int adjNum) {
		LocalDate aa = LocalDate.parse(yyyymm +"01",DateTimeFormatter.BASIC_ISO_DATE);
		return aa.minusMonths(adjNum).format(DateTimeFormatter.BASIC_ISO_DATE).substring(0,6);
	}
}