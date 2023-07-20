package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.FxRateHis;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="E_FXH_RATE")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class HFxRateHis implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 7749489060564117278L;

	@Id
	private String baseDate;

	@Id
	private int seq;

	@Id
	private String curCd;

	private Double bslBseRt;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

	public static HFxRateHis convertFrom(FxRateHis from, String baseYymm, int seq) {
		return builder().seq(seq)
						.baseDate(from.getBaseDate())
						.curCd(from.getCurCd())
						.bslBseRt(from.getBslBseRt())
						.lastModifiedBy(from.getLastModifiedBy())
						.lastUpdateDate(LocalDateTime.now())
						.build();
	}

}
