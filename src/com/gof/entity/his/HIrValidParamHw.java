package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.IrValidParamHw;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IRH_VALID_PARAM_HW")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrValidParamHw implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -7621117603455851091L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
    @Id
	private String irModelId;

	@Id
	private String irCurveId;

	@Id
	private Double swpnMatNum;

	@Id
	private Double swapTenNum;

	@Id
	private String validDv;

	private Double validVal1;
	private Double validVal2;
	private Double validVal3;
	private Double validVal4;
	private Double validVal5;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static HIrValidParamHw convertFrom(IrValidParamHw from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(from.getBaseYymm())
				.irModelId(from.getIrModelId())
				.irCurveId(from.getIrCurveId())
				.swpnMatNum(from.getSwpnMatNum())
				.swapTenNum(from.getSwapTenNum())
				.validDv(from.getValidDv())
				.validVal1(from.getValidVal1())
				.validVal2(from.getValidVal2())
				.validVal3(from.getValidVal3())
				.validVal4(from.getValidVal4())
				.validVal5(from.getValidVal5())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())

						.build();
	}

}