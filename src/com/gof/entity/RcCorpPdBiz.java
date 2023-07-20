package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HRcCorpPdBiz;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_RC_CORP_PD_BIZ")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class RcCorpPdBiz implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -5770104017516099415L;

	@Id
	private String baseYymm;

	@Id
	private String applBizDv;

	@Id
	private String crdGrdCd;

	@Id
	private String matCd;

	private Double cumPd;
	private Double fwdPd;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static RcCorpPdBiz convertFrom(HRcCorpPdBiz from, String baseYymm, int seq) {
		return builder()
				.baseYymm(from.getBaseYymm())
				.applBizDv(from.getApplBizDv())
				.crdGrdCd(from.getCrdGrdCd())
				.matCd(from.getMatCd())
				.cumPd(from.getCumPd())
				.fwdPd(from.getFwdPd())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())

						.build();
	}

}
