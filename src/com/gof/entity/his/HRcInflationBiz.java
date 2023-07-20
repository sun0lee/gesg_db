package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.RcCorpPdBiz;
import com.gof.entity.RcCorpTm;
import com.gof.entity.RcInflationBiz;
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
@Table(name ="E_RCH_INFLATION_BIZ")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HRcInflationBiz implements Serializable, EntityIdentifier, HisEntity<RcInflationBiz>{

	private static final long serialVersionUID = 1874314758316989856L;

	@Id
	private String baseYymm;
	@Id
	private int seq;

	@Id
	private String applBizDv;

	@Id
	private String inflationId;

	private Double inflationIndex;
	private Double mgmtTargetLowerVal;
	private Double mgmtTargetUpperVal;

	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;


	public static HRcInflationBiz convertFrom(RcInflationBiz from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(from.getBaseYymm())
				.applBizDv(from.getApplBizDv())
				.inflationId(from.getInflationId())
				.inflationIndex(from.getInflationIndex())
				.mgmtTargetLowerVal(from.getMgmtTargetLowerVal())
				.mgmtTargetUpperVal(from.getMgmtTargetUpperVal())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())

						.build();
	}

	@Override
	public RcInflationBiz convertToBiz(String baseYymm, int seq) {
		return RcInflationBiz.builder()
				.baseYymm(this.getBaseYymm())
				.applBizDv(this.getApplBizDv())
				.inflationId(this.getInflationId())
				.inflationIndex(this.getInflationIndex())
				.mgmtTargetLowerVal(this.getMgmtTargetLowerVal())
				.mgmtTargetUpperVal(this.getMgmtTargetUpperVal())
				.lastModifiedBy(this.getLastModifiedBy()+"_"+seq)
//				.lastUpdateDate(this.getLastUpdateDate())
				.lastUpdateDate(LocalDateTime.now())
				.build();
	}
}
