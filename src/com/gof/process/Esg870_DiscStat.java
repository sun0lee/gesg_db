package com.gof.process;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import com.gof.entity.IrDcntRateBiz;
import com.gof.entity.IrDiscMgtRate;
import com.gof.entity.IrDiscRate;
import com.gof.entity.IrDiscRateStat;
import com.gof.enums.EJob;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Esg870_DiscStat extends Process {	
	
	public static final Esg870_DiscStat INSTANCE = new Esg870_DiscStat();
	public static final String jobId = INSTANCE.getClass().getSimpleName().toUpperCase().substring(0, ENTITY_LENGTH);	
	
	public static List<IrDiscRateStat> createDiscStat(String bssd, List<IrDiscRate> discRateList, List<IrDcntRateBiz> irCurveList, List<IrDiscMgtRate> invCostList) {	
		List<IrDiscRateStat> rst  = new ArrayList<IrDiscRateStat>();
		
		int regSize 		= 36;
		int invCostAvgSize 	= 36; 
		
		List<IrDiscRate> currList =  discRateList.stream().filter(s-> s.getBaseYymm().equals(bssd)).collect(toList());
		
//		discRateList.stream().filter(s-> s.getBaseYymm().equals(bssd)).forEach(s-> log.info("zzzz : {}", s.toString()));
		
		double invCost = invCostList.stream().sorted(Collections.reverseOrder()).limit(invCostAvgSize)
											.mapToDouble(s -> s.getInvCostRate())
											.average()
											.orElse(0.0)
											;
		
		Map<String, Double> irMap = irCurveList.stream().sorted(Collections.reverseOrder())
												.limit(regSize)
												.sorted()
												.collect(toMap(IrDcntRateBiz::getBaseYymm, IrDcntRateBiz::getSpotRate));
		double currIntRate =  irMap.getOrDefault(bssd, 0.0);
		
		for (IrDiscRate disc : currList) {
//			log.info("In the Esg870 : {}" , disc.toString());
			
//			rst.add( createDiscRateKics(disc, discRateList, invCost));	
			rst.add( createDiscRateKicsNew(disc, discRateList, invCost));
			
			rst.add( createDiscRateIbiz(disc, discRateList, currIntRate));	
			rst.add( createDiscRateIfrs(disc, discRateList, irMap));	
		}
		
		log.info("{}({}) creates coefficients.  They are inserted into [{}] Table", jobId, EJob.valueOf(jobId).getJobName(), rst.size() );
		
		return rst;		
	}
	



	
	
//	for IFRS : base Disc Rate regresion!!!!
	private static IrDiscRateStat createDiscRateIfrs(IrDiscRate currDisc, List<IrDiscRate> discList , Map<String, Double> irMap) {
		
		Map<String, Double> baseDiscMap = discList.stream().filter(s-> s.getIntRateCd().equals(currDisc.getIntRateCd()))
												.collect(toMap(IrDiscRate::getBaseYymm, IrDiscRate::getBaseDiscRate));
		
		
		SimpleRegression linerRegression = new SimpleRegression(true);
		
		for(Map.Entry<String, Double> aa : irMap.entrySet()) {
			if(baseDiscMap.containsKey(aa.getKey())) {
				linerRegression.addData( aa.getValue(), baseDiscMap.get(aa.getKey()));
			}
		}
		
		return IrDiscRateStat.builder()
				.baseYymm(currDisc.getBaseYymm())
				.applBizDv("I")
				.intRateCd(currDisc.getIntRateCd())
				.indpVariable("KTB_1Y")
				.avgMonNum(1)
				.regrCoef(linerRegression.getSlope())
				.regrConstant(linerRegression.getIntercept())
				.adjRate(currDisc.getAdjRate())
				.vol(0.0)
				.remark("Linear Regression ")
				.lastModifiedBy(EJob.ESG870.getJobName())
				.lastUpdateDate(LocalDateTime.now())
				.build();
	}
	
	
	
	private static IrDiscRateStat createDiscRateIbiz(IrDiscRate currDisc, List<IrDiscRate> discList, double currIntRate) {
		return IrDiscRateStat.builder()
						.baseYymm(currDisc.getBaseYymm())
						.applBizDv("Z")
						.intRateCd(currDisc.getIntRateCd())
						.indpVariable("KTB_1Y")
						.avgMonNum(1)
						.regrCoef(1.0)
//				.regrCoef(intRate==0.0? 1.0: currDisc.getBaseDiscRate() / intRate )
//				.regrConstant(0.0)
						.regrCoef(1.0)
						.regrConstant(currDisc.getBaseDiscRate() - currIntRate)
						.adjRate(currDisc.getAdjRate())
						.vol(0.0)
						.remark("Current Spread ")
						.lastModifiedBy(EJob.ESG870.getJobName())
						.lastUpdateDate(LocalDateTime.now())
						.build();
	}
	
	
	
	/**
	 *  <p> KICS 4.0 기준의  공시이율 추정 모형
	 *  <p> KICS 4.0 에서 제시하는 방법론으로 공시기준이율 산출시 외부 지표금리는 배제하고 자산운용 수익률 요인만 고려함.
	 *  <p>    1. 자산운용 수익률과 공시이율과의 비율의 3년 평균으로 조정률 결정
	 *  <p>    2. 자산운용 수익률의 미래 추정치는 조정 무위험 금리의 1M Forward 에서 투자관리비용을 차감하여 결정함.  
	 *  <p>	     2.1 별도의 통계모형을 적용하지 않으므로 독립변수는 KTB1M, 상수항에 투자관리비용, 계수항에 1.0 을 설정함. 
	 *  <p>    3. 만일 사용자가 지정한 통계모형이 있으면 우선적으로 적용함.  
	 *  
	 *  
	 * @author takion77@gofconsulting.co.kr 
	 * @version 1.0
	 */
	private static IrDiscRateStat createDiscRateKics(IrDiscRate currDisc, List<IrDiscRate> discList, double invCost) {
		
		int adjRateAvgSize 	= 36;
		
		
		//  공시기준이율, 자산운용율의 비율의  최근 3 년 평균을 조정률로 설정함.
		double avgAdjRate = discList.stream().filter(s-> s.getIntRateCd().equals(currDisc.getIntRateCd()))
										.sorted(Collections.reverseOrder())
										.limit(adjRateAvgSize)
										.peek(s-> log.info("avg :  {}, {}", s.getAdjRatio(), s.toString()))
										.mapToDouble(s -> s.getAdjRatio())
										.average()
										.orElse(0.0)
										;

		
		return IrDiscRateStat.builder()
						.baseYymm(currDisc.getBaseYymm())
						.applBizDv("K")
						.intRateCd(currDisc.getIntRateCd())
						.indpVariable("KTB_1M")
						.avgMonNum(1)
						.regrCoef(1.0)
						.regrConstant(invCost)
						.adjRate(avgAdjRate)
						.vol(0.0)
						.remark("KICS")
						.lastModifiedBy(EJob.ESG870.getJobName())
						.lastUpdateDate(LocalDateTime.now())
						.build();
	}
	
	
	/**
	 *  <p> KICS 별표 22 기준의  공시이율 추정 모형
	 *  <p> KICS 별표 22 기준의 미래공시이율 및 확률론적 시나리오 모형
	 *  <p>    1. 미래 기준이율 = 외부지표 금리 * 가중치  + 자산운용이익률 시나리오 * (1-가중치)
	 *  <p>    2. 자산운용이익률 시나리오 = 확률론적 시나리오 - 투자관리 비용률  
	 *  <p>	   3. 조정률은  경영자 행동모형으로 설정한 가정사항임. 현재값 적용함.
	 *  <p>    4. 결국 미래 공시이률 시나리오 = 조정률  * 미래 기준이율 시나리오 = 조정률 * ( 외부지표금리 * 가중치 + (확률론적 시나리오 - 투자관리비용율)* (1-가중치)
	 *  <p>                           = 조정률 * ( 확률론적 시나리오 *  (1-가중치) +  외부지표금리 * 가중치  - 투자관리비용율* (1-가중치) )
	 *  
	 *  <p>   여기서 미래 공시율 시나리오에 적용할 확률론적 시나리오의 Tenor 를 결정해야하는데 기준서에서는 명시적으로 언급하고 있지 않음. ==> 기존  KICS 에서 1M 로 설정함. 즉 독립변수에 KTB_1M 를 지정함.
	 *   
	 *  <p>   예외적으로 외부지표, 자산운용이익률의 가중평균으로 산출하지 않는 공시이율에 대해서는 현재값 적용 가능함.
	 *  
	 *  
	 * @author takion77@gofconsulting.co.kr 
	 * @version 1.0
	 */
	private static IrDiscRateStat createDiscRateKicsNew(IrDiscRate currDisc, List<IrDiscRate> discList, double invCost) {
		
		double regrConstant = currDisc.getExBaseIr() * currDisc.getExBaseIrWght() - invCost * ( 1.0- currDisc.getExBaseIrWght());

		if(currDisc.getMgtYield()== null || currDisc.getMgtYield()==0.0) {
			return IrDiscRateStat.builder()
					.baseYymm(currDisc.getBaseYymm())
					.applBizDv("K")
					.intRateCd(currDisc.getIntRateCd())
					.indpVariable("KTB_1M")
					.avgMonNum(1)
					.regrCoef(0.0)
					.regrConstant(currDisc.getBaseDiscRate())
					.adjRate(currDisc.getAdjRate())
					.vol(0.0)
					.remark("KICS_ASIS")
					.lastModifiedBy(EJob.ESG870.getJobName())
					.lastUpdateDate(LocalDateTime.now())
					.build();
		}
		
		else {
			return IrDiscRateStat.builder()
					.baseYymm(currDisc.getBaseYymm())
					.applBizDv("K")
					.intRateCd(currDisc.getIntRateCd())
					.indpVariable("KTB_1M")
					.avgMonNum(1)
					.regrCoef(1.0 - currDisc.getExBaseIrWght())
					.regrConstant(regrConstant)
					.adjRate(currDisc.getAdjRate())
					.vol(0.0)
					.remark("KICS")
					.lastModifiedBy(EJob.ESG870.getJobName())
					.lastUpdateDate(LocalDateTime.now())
					.build();
			
		}
	}
}

