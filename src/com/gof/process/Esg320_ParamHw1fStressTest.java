package com.gof.process;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gof.entity.IrCurveSpot;
import com.gof.entity.IrParamHwCalc;
import com.gof.entity.IrVolSwpn;
import com.gof.enums.EJob;
import com.gof.model.Hw1fCalibrationKics;
import com.gof.model.entity.SwpnVolInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Esg320_ParamHw1fStressTest extends Process {
	
	public static final Esg320_ParamHw1fStressTest INSTANCE = new Esg320_ParamHw1fStressTest();
	public static final String jobId = INSTANCE.getClass().getSimpleName().toUpperCase().substring(0, ENTITY_LENGTH);
	
	public static List<IrParamHwCalc> createParamHw1fNonSplitMapValid(String bssd, String irModelId, String irCurveId, List<IrCurveSpot> spotList, List<IrVolSwpn> swpnVolList, double[] initParas, Integer freq, double errTol, int[] alphaPiece, int[] sigmaPiece) {
		
		List<IrParamHwCalc> paramCalc = new ArrayList<IrParamHwCalc>();
				
		freq = Math.max(freq, 1);		
		List<SwpnVolInfo> volInfo  = swpnVolList.stream().map(s-> SwpnVolInfo.convertFrom(s)).collect(toList());		
		
		Hw1fCalibrationKics calib = new Hw1fCalibrationKics(bssd, spotList, volInfo, alphaPiece, sigmaPiece, initParas, freq, errTol);
		paramCalc                 = calib.getHw1fCalibrationResultList().stream().map(s -> s.convertNonSplit(irModelId, irCurveId))
																			     .flatMap(s-> s.stream())
																			     .collect(toList());

		paramCalc.stream().forEach(s -> s.setLastModifiedBy(jobId));
		paramCalc.stream().forEach(s -> s.setLastUpdateDate(LocalDateTime.now()));		

		log.info("{}({}) creates {} results of [MODEL: {}]. They are inserted into [{}] Table", jobId, EJob.valueOf(jobId).getJobName(), paramCalc.size(), irModelId, toPhysicalName(IrParamHwCalc.class.getSimpleName()));
		
		return paramCalc;
	}	
	
}