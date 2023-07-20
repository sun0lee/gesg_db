package com.gof.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.gof.enums.EJob;
import com.gof.entity.IrParamAfnsCalc;
import com.gof.entity.IrCurveSpot;
import com.gof.model.AFNelsonSiegel;
import com.gof.model.IrModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Esg720_optAfnsParam extends Process {

	public static final Esg720_optAfnsParam INSTANCE = new Esg720_optAfnsParam();
	public static final String jobId = INSTANCE.getClass().getSimpleName().toUpperCase().substring(0, ENTITY_LENGTH);

	/** <p> AFNS 紐⑥닔 理쒖쟻�솕 (kalmanFiltering) </br>
	 * @param
	 * @See getAfnsResultList
	 * */
	public static Map<String, List<IrParamAfnsCalc>> optimizationParas(String bssd, String mode, List<IrCurveSpot> curveHisList, List<IrCurveSpot> curveBaseList, List<String> tenorList,
	           double dt, double initSigma, double ltfr, double ltfrT, int prjYear, double errorTolerance, int itrMax, double confInterval, double epsilon
	           , List<IrParamAfnsCalc> initParam // add input
	           )
    {

		Map<String, List<IrParamAfnsCalc>>  resultMap  = new TreeMap<String, List<IrParamAfnsCalc>>();
		List<IrParamAfnsCalc> irOptParam    = new ArrayList<IrParamAfnsCalc>();


		AFNelsonSiegel afns = new AFNelsonSiegel(IrModel.stringToDate(bssd), mode, null, curveHisList, curveBaseList,
                true, 'D', dt, initSigma, DCB_MON_DIF, ltfr, 0, (int) ltfrT, 0.0, 1.0 / 12,
                0.05, 2.0, 3, prjYear, errorTolerance, itrMax, confInterval, epsilon);


		afns.optimizationParas(initParam);


		irOptParam.addAll(afns.getAfnsParamList());


		// fk 媛� 異붽�
		irOptParam.stream().forEach(s -> s.setLastModifiedBy(jobId));

		resultMap.put("PARAM",  irOptParam);

		log.info("{}({}) creates {} results. They are inserted into [{}] Table", jobId, EJob.valueOf(jobId).getJobName(), irOptParam.size(), toPhysicalName(IrParamAfnsCalc.class.getSimpleName()));

		return resultMap;
	}


}
