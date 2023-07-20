package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.IrValidRnd;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IRH_VALID_RND")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrValidRnd implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -4899640834780900414L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
    @Id
	private String irModelId;

	@Id
	private String irCurveId;

	@Id
	private String validDv;

	@Id
	private Integer validSeq;

	private Double validVal1;
	private Double validVal2;
	private Double validVal3;
	private Double validVal4;
	private Double validVal5;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static HIrValidRnd convertFrom(IrValidRnd from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(from.getBaseYymm())
				.irModelId(from.getIrModelId())
				.irCurveId(from.getIrCurveId())
				.validDv(from.getValidDv())
				.validSeq(from.getValidSeq())
				.validVal1(from.getValidVal1())
				.validVal2(from.getValidVal2())
				.validVal3(from.getValidVal3())
				.validVal4(from.getValidVal4())
				.validVal5(from.getValidVal5())
				.lastUpdateDate(from.getLastUpdateDate())
				.lastModifiedBy(from.getLastModifiedBy())

						.build();
	}

}