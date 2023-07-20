package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.interfaces.EntityIdentifier;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_RC_SEG_PREP_RATE_USR")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RcSegPrepRateUsr implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 1874314758316989856L;

	@Id
	private String applStYymm;
	@Id
	private String applBizDv;
	@Id
	private String segId;

	private String applEdYymm;


	@Column(name = "APPL_PREP_RATE") private Double applPrepRate;

	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;
	
	
	public RcSegPrepRateBiz convertToBiz(String bssd, String bizDv) {
		return RcSegPrepRateBiz.builder()
					.baseYymm(bssd)
					.applBizDv(bizDv)
					.segId(segId)
					.applPrepRate(applPrepRate)
					.lastModifiedBy("GESG")
					.lastUpdateDate(LocalDateTime.now())
					.build();
	}

}
