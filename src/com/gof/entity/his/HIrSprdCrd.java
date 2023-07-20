package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="E_IRH_SPRD_CRD")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class HIrSprdCrd implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 76762312409390492L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
	@Id
	private String crdGrdCd;

	@Id
	private String crdGrdNm;

	@Id
	private String matCd;

	private Double crdSprd;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static HIrSprdCrd convertFrom(com.gof.entity.IrSprdCrd from, String baseYymm, int seq) {
		return builder().seq(seq)
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


