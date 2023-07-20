package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.gof.entity.IrDcntRateBu;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IRH_DCNT_RATE_BU")
@NoArgsConstructor
@FilterDef(name="IR_FILTER", parameters= { @ParamDef(name="baseYymm", type="string"), @ParamDef(name="irCurveId", type="string") })
@Filters( { @Filter(name ="IR_FILTER", condition="BASE_YYMM = :baseYymm"),  @Filter(name ="IR_FILTER", condition="IR_CURVE_ID like :irCurveId") } )
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrDcntRateBu implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -4644199390958760035L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
	@Id
	private String applBizDv;

	@Id
	private String irCurveId;

	@Id
	private Integer irCurveSceNo;

	@Id
	private String matCd;

	private Double spotRateDisc;
	private Double spotRateCont;
	private Double liqPrem;
	private Double adjSpotRateDisc;
	private Double adjSpotRateCont;
	private Double addSprd;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;


	public static HIrDcntRateBu convertFrom(IrDcntRateBu from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(from.getBaseYymm())
				.applBizDv(from.getApplBizDv())
				.irCurveId(from.getIrCurveId())
				.irCurveSceNo(from.getIrCurveSceNo())
				.matCd(from.getMatCd())
				.spotRateDisc(from.getSpotRateDisc())
				.liqPrem(from.getLiqPrem())
				.addSprd(from.getAddSprd())
				.adjSpotRateDisc(from.getAdjSpotRateDisc())
				.adjSpotRateCont(from.getAdjSpotRateCont())
				.spotRateCont(from.getSpotRateCont())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())

						.build();
	}
}