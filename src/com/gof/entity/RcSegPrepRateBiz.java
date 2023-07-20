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
@Table(name ="E_RC_SEG_PREP_RATE_BIZ")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class RcSegPrepRateBiz implements Serializable, EntityIdentifier {

	private static final long serialVersionUID = 1874314758316989856L;

	@Id
	private String baseYymm;
	@Id
	private String applBizDv;
	@Id
	private String segId;
	private Double applPrepRate;

	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;

}
