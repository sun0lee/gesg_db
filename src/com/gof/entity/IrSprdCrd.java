package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HIrSprdCrd;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="E_IR_SPRD_CRD")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class IrSprdCrd implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 76762312409390492L;

	@Id
	private String baseYymm;

	@Id
	private String crdGrdCd;

	@Id
	private String crdGrdNm;

	@Id
	private String matCd;

	private Double crdSprd;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static IrSprdCrd convertFrom(HIrSprdCrd from, String baseYymm, int seq) {
		return builder()
				.baseYymm(from.getBaseYymm())
				.crdGrdCd(from.getCrdGrdCd())
				.crdGrdNm(from.getCrdGrdNm())
				.matCd(from.getMatCd())
				.crdSprd(from.getCrdSprd())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(from.getLastUpdateDate())

						.build();
	}

}


