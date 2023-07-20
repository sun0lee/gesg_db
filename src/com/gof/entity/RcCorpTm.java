package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HRcCorpTm;
import com.gof.enums.ECrdGrd;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_RC_CORP_TM")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class RcCorpTm implements Serializable, EntityIdentifier, Comparable<RcCorpTm> {

	private static final long serialVersionUID = -4080286022399238155L;

	@Id
	private String baseYymm;

	@Id
	private String crdEvalAgncyCd;

	@Id
	private String fromCrdGrdCd;

	@Id
	private String toCrdGrdCd;

	private double transProb;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public ECrdGrd getFromGradeEnum() {
		return ECrdGrd.getECrdGrd(fromCrdGrdCd) ;
	}

	public ECrdGrd getToGradeEnum() {
		return ECrdGrd.getECrdGrd(toCrdGrdCd) ;
	}

	@Override
	public int compareTo(RcCorpTm other) {
		return 100 * ( this.getFromGradeEnum().getOrder() - other.getFromGradeEnum().getOrder())
				   + ( this.getToGradeEnum().getOrder()  - other.getToGradeEnum().getOrder())
				   ;
	}

	public  static RcCorpTm convertFrom(HRcCorpTm from, String baseYymm, int seq) {
		return builder()
				.baseYymm(from.getBaseYymm())
				.crdEvalAgncyCd(from.getCrdEvalAgncyCd())
				.fromCrdGrdCd(from.getFromCrdGrdCd())
				.toCrdGrdCd(from.getToCrdGrdCd())
				.transProb(from.getTransProb())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())

						.build();
	}
}
