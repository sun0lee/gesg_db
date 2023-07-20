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
@Table(name ="E_RC_INFLATION_USR")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RcInflationUsr implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 1874314758316989856L;

	@Id
	private String settingYymm;
	@Id
	private String baseYymm;


	@Column(name = "INFLATION_INDEX") private Double inflationIndex;
	@Column(name = "IFRS_TGT_INDEX") private Double ifrsTgtIndex;
	@Column(name = "KICS_TGT_INDEX") private Double kicsTgtIndex;

	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;
	
	public RcInflationBiz convertToBiz(String bssd, String bizDv) {
		return RcInflationBiz.builder()
					.baseYymm(bssd)
					.applBizDv(bizDv)
					.inflationId("UD")
					.inflationIndex(bizDv.equals("KICS")? kicsTgtIndex: ifrsTgtIndex)
					.lastModifiedBy("GESG")
					.lastUpdateDate(LocalDateTime.now())
					.build();
	}

}
