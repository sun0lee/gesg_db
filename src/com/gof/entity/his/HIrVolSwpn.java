package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.gof.entity.IrVolSwpn;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_IRH_VOL_SWPN")
@FilterDef(name="eqBaseDate", parameters= @ParamDef(name ="bssd",  type="string"))
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class HIrVolSwpn implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = -4010423589739075634L;

	@Id
	private String baseYymm;
	@Id
	private int seq;
	@Id
	private String irCurveId;

	@Id
	private Integer swpnMatNum;

	@Id
	private Integer swapTenNum;

	private Double vol;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;
	
	public static HIrVolSwpn convertFrom(IrVolSwpn from, String baseYymm, int seq) {
		return builder().seq(seq)
				.baseYymm(from.getBaseYymm())
				.irCurveId(from.getIrCurveId())
				.swapTenNum(from.getSwapTenNum())
				.swpnMatNum(from.getSwpnMatNum())
				.vol(from.getVol())
				.lastModifiedBy(from.getLastModifiedBy())
				.lastUpdateDate(LocalDateTime.now())
				.build();
	}
}