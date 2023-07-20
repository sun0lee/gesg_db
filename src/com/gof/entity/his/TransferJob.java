package com.gof.entity.his;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gof.entity.RcCorpTm;
import com.gof.enums.ECrdGrd;
import com.gof.enums.ETable;
import com.gof.interfaces.EntityIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name ="E_CO_BIZ_JOB_LIST")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class TransferJob implements Serializable, EntityIdentifier  {

	private static final long serialVersionUID = -4080286022399238155L;

	@Id
	private String baseYymm;

	@Id
	private String applBizDv;

	@Id
	@Enumerated(EnumType.STRING)
	private ETable toTable;

	private String fromTable;

	private int  seq;


//	private String desc;
	private String lastModifiedBy;
	private LocalDateTime lastUpdateDate;




}
