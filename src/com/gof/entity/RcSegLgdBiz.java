package com.gof.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.his.HRcSegLgdBiz;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_RC_SEG_LGD_BIZ")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class RcSegLgdBiz implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 1874314758316989856L;

	@Id
	private String baseYymm;

	@Id
	private String segId;

	@Id
	private String applBizDv;

	@Id
	private String lgdCalcTypCd;


	private Double applLgd;

	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

}
