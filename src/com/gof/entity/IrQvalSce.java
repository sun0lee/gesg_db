package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HIrQvalSce;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IR_QVAL_SCE")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class IrQvalSce implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 1660055914228437117L;

	@Id
	private String baseYymm;

	@Id
	private String applBizDv;

    @Id
	private String irModelId;

	@Id
	private String irCurveId;

    @Id
	private Integer irCurveSceNo;

	@Id
	private String qvalDv;

	@Id
	private Integer qvalSeq;

	private Double qval1;
	private Double qval2;
	private Double qval3;
	private Double qval4;
	private Double qval5;
	private Double qval6;
	private Double qval7;
	private Double qval8;
	private Double qval9;
	private Double qval10;
	private Double qval11;
	private Double qval12;
	private Double qval13;
	private Double qval14;
	private Double qval15;

	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static IrQvalSce convertFrom(HIrQvalSce from, String baseYymm, int seq) {
		return builder()
				.baseYymm(from.getBaseYymm())
				.applBizDv(from.getApplBizDv())
				.irModelId(from.getIrModelId())
				.irCurveSceNo(from.getIrCurveSceNo())
				.irCurveId(from.getIrCurveId())
				.qvalDv(from.getQvalDv())
				.qvalSeq(from.getQvalSeq())
				.qval1(from.getQval1())
				.qval2(from.getQval2())
				.qval3(from.getQval3())
				.qval4(from.getQval4())
				.qval5(from.getQval5())
				.qval6(from.getQval6())
				.qval7(from.getQval7())
				.qval8(from.getQval8())
				.qval9(from.getQval9())
				.qval10(from.getQval10())
				.qval11(from.getQval11())
				.qval12(from.getQval12())
				.qval13(from.getQval13())
				.qval14(from.getQval14())
				.qval15(from.getQval15())
				.lastUpdateDate(from.getLastUpdateDate())
				.lastModifiedBy(from.getLastModifiedBy())

						.build();
	}

	public HIrQvalSce convertToHis(String baseYymm, int seq) {
		return HIrQvalSce.builder().seq(seq)
				.baseYymm(this.getBaseYymm())
				.applBizDv(this.getApplBizDv())
				.irModelId(this.getIrModelId())
				.irCurveSceNo(this.getIrCurveSceNo())
				.irCurveId(this.getIrCurveId())
				.qvalDv(this.getQvalDv())
				.qvalSeq(this.getQvalSeq())
				.qval1(this.getQval1())
				.qval2(this.getQval2())
				.qval3(this.getQval3())
				.qval4(this.getQval4())
				.qval5(this.getQval5())
				.qval6(this.getQval6())
				.qval7(this.getQval7())
				.qval8(this.getQval8())
				.qval9(this.getQval9())
				.qval10(this.getQval10())
				.qval11(this.getQval11())
				.qval12(this.getQval12())
				.qval13(this.getQval13())
				.qval14(this.getQval14())
				.qval15(this.getQval15())
				.lastUpdateDate(this.getLastUpdateDate())
				.lastModifiedBy(this.getLastModifiedBy())

						.build();
	}

}