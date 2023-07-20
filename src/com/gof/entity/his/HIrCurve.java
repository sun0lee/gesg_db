package com.gof.entity.his;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.IrCurve;
import com.gof.enums.EBoolean;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name ="E_IRH_CURVE")
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrCurve implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -7079607534247603390L;

	@Id
	private String irCurveId;
	@Id
	private String baseYymm;
	@Id
	private int seq;

	private String irCurveNm;
	private String curCd;
	private String applMethDv;
	private String crdGrdCd;
	private String intpMethCd;

	@Enumerated(EnumType.STRING)
	private EBoolean useYn;

	public static HIrCurve convertFrom(IrCurve from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(baseYymm)
				.useYn(from.getUseYn())
				.applMethDv(from.getApplMethDv())
				.irCurveNm(from.getIrCurveNm())
				.irCurveId(from.getIrCurveId())
				.crdGrdCd(from.getCrdGrdCd())
				.curCd(from.getCurCd())
//				.lastModifiedBy(from.getLastModifiedBy())
//				.lastUpdateDate(from.getLastUpdateDate())
						.build();
	}

}
