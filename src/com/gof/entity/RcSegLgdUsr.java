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
@Table(name ="E_RC_SEG_LGD_USR")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RcSegLgdUsr implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 1874314758316989856L;

	@Id
	private String applStYymm;

	@Id
	private String lgdCalcTypCd;

	@Id
	private String segId;

	@Column(name = "APPL_ED_YYMM") private String applEdYymm;
	@Column(name = "LGD") private Double lgd;

	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;
	
	public RcSegLgdBiz convertToBiz(String bssd, String bizDv) {
		return RcSegLgdBiz.builder()
					.baseYymm(bssd)
					.applBizDv(bizDv)
					.segId(segId)
					.lgdCalcTypCd(lgdCalcTypCd)
					.applLgd(lgd)
					.lastModifiedBy("GESG")
					.lastUpdateDate(LocalDateTime.now())
					.build();
	}

}
