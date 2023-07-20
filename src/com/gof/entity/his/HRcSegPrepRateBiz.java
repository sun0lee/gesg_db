package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.RcInflationBiz;
import com.gof.entity.RcSegLgdBiz;
import com.gof.entity.RcSegPrepRateBiz;
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
@Table(name ="E_RCH_SEG_PREP_RATE_BIZ")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HRcSegPrepRateBiz implements Serializable, EntityIdentifier, HisEntity<RcSegPrepRateBiz> {

	private static final long serialVersionUID = 1874314758316989856L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
	@Id
	private String applBizDv;
	@Id
	private String segId;
	private Double applPrepRate;

	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static HRcSegPrepRateBiz convertFrom(RcSegPrepRateBiz from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(from.getBaseYymm())
				.applBizDv(from.getApplBizDv())
				.segId(from.getSegId())
				.applPrepRate(from.getApplPrepRate())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())
				.build();
	}

	@Override
	public RcSegPrepRateBiz convertToBiz(String baseYymm, int seq) {
		return RcSegPrepRateBiz.builder()
				.baseYymm(this.getBaseYymm())
				.applBizDv(this.getApplBizDv())
				.segId(this.segId)
				.applPrepRate(this.applPrepRate)
				.lastModifiedBy(this.getLastModifiedBy()+"_"+seq)
//				.lastUpdateDate(this.getLastUpdateDate())
				.lastUpdateDate(LocalDateTime.now())
				.build();
	}
}