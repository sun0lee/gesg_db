package com.gof.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.gof.entity.IrCurveSpot;
import com.gof.entity.IrParamAfnsCalc;
import com.gof.enums.EJob;
import com.gof.model.AFNelsonSiegel;
import com.gof.model.IrModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Esg710_SetAfnsInitParam extends Process {

	public static final Esg710_SetAfnsInitParam INSTANCE = new Esg710_SetAfnsInitParam();
	public static final String jobId = INSTANCE.getClass().getSimpleName().toUpperCase().substring(0, ENTITY_LENGTH);

	/** <p> AFNS  </br>
	 * @param
	 * */
	public static Map<String, List<IrParamAfnsCalc>> setAfnsInitParam(String bssd, String mode, List<IrCurveSpot> curveHisList, List<IrCurveSpot> curveBaseList, List<String> tenorList,
	           double dt, double initSigma, double ltfr, double ltfrT, int prjYear, double errorTolerance, int itrMax, double confInterval, double epsilon)
	{

		Map<String, List<IrParamAfnsCalc>>  resultMap  = new TreeMap<String, List<IrParamAfnsCalc>>();
		List<IrParamAfnsCalc> irAfnsInitParam    = new ArrayList<IrParamAfnsCalc>();


		AFNelsonSiegel afns = new AFNelsonSiegel(IrModel.stringToDate(bssd), mode, null, curveHisList, curveBaseList,
                true, 'D', dt, initSigma, DCB_MON_DIF, ltfr, 0, (int) ltfrT, 0.0, 1.0 / 12,
                0.05, 2.0, 3, prjYear, errorTolerance, itrMax, confInterval, epsilon);


		afns.getinitialAfnsParas();


		irAfnsInitParam.addAll(afns.setAfnsInitParamList());

//		irAfnsInitParam.stream().forEach(s -> log.info("zzzz : {},{}", s.toString()));
		irAfnsInitParam.stream().forEach(s -> s.setLastModifiedBy(jobId));


		log.info("{}({}) creates {} results. They are inserted into [{}] Table", jobId, EJob.valueOf(jobId).getJobName(), irAfnsInitParam.size(), toPhysicalName(IrParamAfnsCalc.class.getSimpleName()));

		resultMap.put("PARAM", irAfnsInitParam);

		return resultMap;
	}


}
