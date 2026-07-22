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
@Table(name ="E_IR_BIZ_APLY_DISC_RATE_STAT")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrDiscRateStat implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -4252300668894647002L;

	@Id
	private String baseYymm;
	
	@Id
	private String applBizDv;

	@Id
	private String intRateCd;
	
	private String indpVariable;
	private Integer avgMonNum;
	
	private Double regrConstant;
	private Double regrCoef;
	private Double adjRate;
	private Double vol;
	private String remark;
	
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	



}