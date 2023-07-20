package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.RcCorpTm;
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
@Table(name ="E_RCH_CORP_TM")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HRcCorpTm implements Serializable, EntityIdentifier  {

	private static final long serialVersionUID = -4080286022399238155L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
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


	public static HRcCorpTm convertFrom(RcCorpTm from, String baseYymm, int seq) {
		return builder().seq(seq)
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
