package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HRcCorpPd;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_RC_CORP_PD")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class RcCorpPd implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -3833361109526416019L;

	@Id
	private String baseYymm;

	@Id
	private String crdEvalAgncyCd;

	@Id
	private String crdGrdCd;

	@Id
	private String matCd;

	private Double cumPd;
	private Double fwdPd;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static RcCorpPd convertFrom(HRcCorpPd from, String baseYymm, int seq) {
		return builder()
				.baseYymm(from.getBaseYymm())
				.crdEvalAgncyCd(from.getCrdEvalAgncyCd())
				.crdGrdCd(from.getCrdGrdCd())
				.matCd(from.getMatCd())
				.fwdPd(from.getFwdPd())
				.cumPd(from.getCumPd())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())

						.build();
	}
}


