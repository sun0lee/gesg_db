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
@Table(name ="E_IR_DISC_EX_RATE_WGHT_USR")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrDiscExRateWghtUsr implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -4252300668894647002L;

	@Id
	private String baseYymm;

	@Id
	private String acctDvCd;

	@Id
	private String exRateId;
	
	@Id
	private String avgTyp;
	
	private String exRateNm;
	
	private Double exRateWght;

	
	private String calcEndYymm;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;
	
	public String getPk() {
		return  exRateId + avgTyp + acctDvCd;
	}

	public double getExRateWghtReal() {
		return exRateWght /100.0;
	}

}