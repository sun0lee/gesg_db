package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HIrSprdAfnsCalc;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IR_SPRD_AFNS_CALC")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrSprdAfnsCalc implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 6332350473680597191L;

	@Id
	private String baseYymm;

	@Id
	private String irModelId;

	@Id
	private String irCurveId;

	@Id
	private Integer irCurveSceNo;

	@Id
	private String matCd;

	private Double shkSprdCont;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;


	public IrSprdAfnsBiz convert() {

		IrSprdAfnsBiz biz = new IrSprdAfnsBiz();

		biz.setBaseYymm(this.baseYymm);
		biz.setIrModelId(this.irModelId);
		biz.setIrCurveId(this.irCurveId);
		biz.setIrCurveSceNo(this.irCurveSceNo);
		biz.setMatCd(this.matCd);
		biz.setShkSprdCont(this.shkSprdCont);
		biz.setLastModifiedBy("GESG_" + this.getClass().getSimpleName());
		biz.setLastUpdateDate(LocalDateTime.now());

		return biz;
	}

	public static IrSprdAfnsCalc convertFrom(HIrSprdAfnsCalc from, String baseYymm, int seq) {
		return builder()
				.baseYymm(from.getBaseYymm())
				.irModelId(from.getIrModelId())
				.irCurveId(from.getIrCurveId())
				.irCurveSceNo(from.getIrCurveSceNo())
				.matCd(from.getMatCd())
				.shkSprdCont(from.getShkSprdCont())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())

						.build();
	}
}
