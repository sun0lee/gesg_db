package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.IrParamHwBiz;
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
@Table(name ="E_IRH_PARAM_HW_BIZ")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrParamHwBiz implements Serializable, EntityIdentifier , HisEntity<IrParamHwBiz>{

	private static final long serialVersionUID = 1524655691890282755L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
	@Id
	private String applBizDv;

	@Id
	private String irModelId;

	@Id
	private String irCurveId;

	@Id
	private String matCd;

	@Id
	private String paramTypCd;

	private Double paramVal;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static HIrParamHwBiz convertFrom(IrParamHwBiz from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(from.getBaseYymm())
				.applBizDv(from.getApplBizDv())
				.irModelId(from.getIrModelId())
				.irCurveId(from.getIrCurveId())
				.matCd(from.getMatCd())
				.paramTypCd(from.getParamTypCd())
				.paramVal(from.getParamVal())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())

						.build();
	}


	@Override
	public IrParamHwBiz convertToBiz(String baseYymm, int seq) {
		return IrParamHwBiz.builder()
				.baseYymm(this.getBaseYymm())
				.applBizDv(this.getApplBizDv())
				.irModelId(this.getIrModelId())
				.irCurveId(this.getIrCurveId())
				.matCd(this.getMatCd())
				.paramTypCd(this.getParamTypCd())
				.paramVal(this.getParamVal())
				.lastModifiedBy(this.getLastModifiedBy()+ "_" +seq)
				.lastUpdateDate(this.getLastUpdateDate())

						.build();
	}



}


