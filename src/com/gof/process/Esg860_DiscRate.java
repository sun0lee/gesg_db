package com.gof.process;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.groupingBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import com.gof.dao.RcDao;
import com.gof.entity.IrDcntRateBiz;
import com.gof.entity.IrDiscExMst;
import com.gof.entity.IrDiscExRateUsr;
import com.gof.entity.IrDiscExRateWghtUsr;
import com.gof.entity.IrDiscMgtRate;
import com.gof.entity.IrDiscRate;
import com.gof.entity.IrDiscRateStat;
import com.gof.entity.IrDiscRateUsr;
import com.gof.entity.IrSprdAfnsBiz;
import com.gof.enums.EJob;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Esg860_DiscRate extends Process {	
	
	public static final Esg860_DiscRate INSTANCE = new Esg860_DiscRate();
	public static final String jobId = INSTANCE.getClass().getSimpleName().toUpperCase().substring(0, ENTITY_LENGTH);	
	
	public static List<IrDiscRate> createDiscRate(String bssd,List<IrDiscRateUsr> discRateUsr, List<IrDiscExMst> discExMst , List<IrDiscExRateUsr> discExRateUsr, List<IrDiscExRateWghtUsr> discExRateWghtUsr , List<IrSprdAfnsBiz> shockSpread) {	
		List<IrDiscRate> rstList = new ArrayList<IrDiscRate>();
		
		String stBssd = "202011";
		String endBssd = "202101";
		getUpAvgExRate(stBssd, endBssd, discExRateUsr, shockSpread).entrySet().forEach(s-> log.info("zqqqq : {},{}", s.getKey(), s.getValue()));
		
		
		
		return rstList;
	}

	public static Map<String, Double> getBaseAvgExRate(String stBssd, String endBssd, List<IrDiscExRateUsr> discExRateUsr, List<IrSprdAfnsBiz> shockSpread){
		List<IrSprdAfnsBiz> shockList = shockSpread.stream().filter(s-> s.getIrCurveSceNo()== 1).collect(toList());
		log.info("BASE");
		return getAvgExRate(stBssd, endBssd, discExRateUsr, shockList);
	}
	
	public static Map<String, Double> getUpAvgExRate(String stBssd, String endBssd, List<IrDiscExRateUsr> discExRateUsr, List<IrSprdAfnsBiz> shockSpread){
		List<IrSprdAfnsBiz> shockList = shockSpread.stream().filter(s-> s.getIrCurveSceNo()==3).collect(toList());
		log.info("UP");
		return getAvgExRate(stBssd, endBssd, discExRateUsr, shockList);
	}
	
	
	public static Map<String, Double> getDownAvgExRate(String stBssd, String endBssd, List<IrDiscExRateUsr> discExRateUsr, List<IrSprdAfnsBiz> shockSpread){
		List<IrSprdAfnsBiz> shockList = shockSpread.stream().filter(s-> s.getIrCurveSceNo()==4).collect(toList());
		log.info("DOWN");
		return getAvgExRate(stBssd, endBssd, discExRateUsr, shockList);
	}
	
	
	private static Map<String, Double> getAvgExRate(String stBssd, String endBssd, List<IrDiscExRateUsr> discExRateUsr, List<IrSprdAfnsBiz> shockSpread){	
		
		Map<String, Double> rstMap = new HashMap<String, Double>();
		Map<String, List<IrDiscExRateUsr>>   exMap = discExRateUsr.stream()
													.filter(s-> s.getBaseYymm().compareTo(stBssd) >=0 )
													.filter(s-> s.getBaseYymm().compareTo(endBssd) < 1 )
													.collect(groupingBy(IrDiscExRateUsr::getExRatePk, toList()));
		
		Map<String, Double> shockByMat = shockSpread.stream().collect(toMap(IrSprdAfnsBiz::getMatCd, IrSprdAfnsBiz::getShkSprdCont));
		
		shockByMat.entrySet().forEach(s-> log.info(" Shock Spread : {},{}", s.getKey(), s.getValue()));
		
		double temp =0.0;
		double shock =0.0;
		
		for(Map.Entry<String , List<IrDiscExRateUsr>> entry : exMap.entrySet()) {
			for(IrDiscExRateUsr aa : entry.getValue()) {
				shock = shockByMat.getOrDefault(aa.getMatCd(), 0.0);
				if(entry.getValue().size()==3) {
					log.info("Avg Ex Rate1 : {},  {},{},{}", aa.getBaseYymm(), aa.getExRatePk(), aa.getExRateIr(), shock);
					
					if(aa.getBaseYymm().equals(stBssd)){
						temp = temp + aa.getExRateIr() * 1.0 /6.0 ;
					}
					else if(aa.getBaseYymm().equals(endBssd)){
						temp = temp + aa.getExRateIr()* 3.0 /6.0;
					}
					else {
						temp = temp + aa.getExRateIr()* 2.0 /6.0;
					}
				}
				
			}
			log.info("Avg Ex Rate 2:  {},{},{},{},{}", entry.getKey(),   temp, shock, temp + shock * 100);
			rstMap.put(entry.getKey(), temp + shock * 100);
			temp = 0.0;
		}
			
		return rstMap;
	}

}


