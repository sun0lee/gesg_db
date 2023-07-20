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
@Table(name ="E_IR_DISC_RATE_ASST_REVN_RATE")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrDiscMgtRate implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -4252300668894647002L;

	@Id
	private String baseYymm;

	@Id
	private String acctDvCd;
	

	private Double mgtAsstAmt;
	private Double invRevnAmt;
	private Double ociAssetEvalPl;
	private Double ociRelCorpStkEvalPl;
	private Double ociFxRevalPl;
	private Double ociRevalRevnAmt;
	
	private Double ociOtherAmt;
	private Double unrealizedPlSumAmt;
	private Double mgtAsstRevnRate;
	private Double invCostAmt;
	private Double invCostRate;
	private Double mgtAsstYield;

	private Double vol;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;



}