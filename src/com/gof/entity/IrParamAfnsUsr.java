package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.enums.EAfnsParamTypCd;
import com.gof.interfaces.EntityIdentifier;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IR_PARAM_AFNS_USR")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class IrParamAfnsUsr implements Serializable, EntityIdentifier {
	
	private static final long serialVersionUID = -4846720080900879865L;

	@Id
	private String baseYymm; 
	
	@Id
	private String irModelId;
	
	@Id
	private String irCurveId;
	
	@Id
	@Enumerated(EnumType.STRING)
//	private String paramTypCd;
	private EAfnsParamTypCd paramTypCd;
	
	private Double paramVal;
	
	private String lastModifiedBy;

	private LocalDateTime lastUpdateDate;	
	
	
	public IrParamAfnsCalc convertToCalc() {
		return IrParamAfnsCalc.builder()
			.baseYymm(this.baseYymm)
			.irModelId(this.irModelId)
			.irCurveId(this.irCurveId)
			.paramTypCd(this.paramTypCd)
			.paramVal(this.paramVal)
			.lastModifiedBy(this.lastModifiedBy)
			.lastUpdateDate(LocalDateTime.now())
			.build() ;
	}

}
