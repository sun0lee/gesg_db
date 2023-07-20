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
@Table(name ="E_IR_DISC_RATE")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrDiscRate implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -4252300668894647002L;

	@Id
	private String baseYymm;

	@Id
	private String intRateCd;
	
	@Id
	private String discRateCalcTyp;
	
	@Id
	private String matCd;

	private Double mgtYield;
	private Double exBaseIr;
	private Double exBaseIrWght;
	
	private Double baseDiscRate;
	private Double adjRate;
	private Double discRate;
	private Double vol;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;



}