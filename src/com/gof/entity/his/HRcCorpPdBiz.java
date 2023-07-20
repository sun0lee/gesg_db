package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.RcCorpPdBiz;
import com.gof.interfaces.EntityIdentifier;
import com.gof.interfaces.HisEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_RCH_CORP_PD_BIZ")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HRcCorpPdBiz implements Serializable, EntityIdentifier, HisEntity<RcCorpPdBiz> {

	private static final long serialVersionUID = -5770104017516099415L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
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

	public static HRcCorpPdBiz convertFrom(RcCorpPdBiz from, String baseYymm, int seq) {
		return builder().seq(seq)
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

	@Override
	public RcCorpPdBiz convertToBiz(String baseYymm, int seq) {
		return RcCorpPdBiz.builder()
				.baseYymm(this.getBaseYymm())
				.applBizDv(this.getApplBizDv())
				.crdGrdCd(this.getCrdGrdCd())
				.matCd(this.getMatCd())
				.cumPd(this.getCumPd())
				.fwdPd(this.getFwdPd())
				.lastModifiedBy(this.getLastModifiedBy()+"_"+seq)
//				.lastUpdateDate(this.getLastUpdateDate())
				.lastUpdateDate(LocalDateTime.now())
				.build();
	}
}
