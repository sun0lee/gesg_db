package com.gof.process;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hibernate.Session;

import com.gof.dao.CoEsgMetaDao;
import com.gof.dao.CoJobListDao;
import com.gof.dao.DaoUtil;
import com.gof.dao.FxRateDao;
import com.gof.dao.HisDao;
import com.gof.dao.IrCurveDao;
import com.gof.dao.IrCurveSceDao;
import com.gof.dao.IrCurveSpotDao;
import com.gof.dao.IrCurveSpotWeekDao;
import com.gof.dao.IrCurveYtmDao;
import com.gof.dao.IrDcntRateDao;
import com.gof.dao.IrParamAfnsDao;
import com.gof.dao.IrParamHwDao;
import com.gof.dao.IrParamHwRndDao;
import com.gof.dao.IrParamModelDao;
import com.gof.dao.IrParamSwDao;
import com.gof.dao.IrSprdDao;
import com.gof.dao.IrValidDao;
import com.gof.dao.IrVolSwpnDao;
import com.gof.dao.RcCorpPdDao;
import com.gof.dao.RcDao;
import com.gof.entity.CoEsgMeta;
import com.gof.entity.CoJobInfo;
import com.gof.entity.CoJobList;
import com.gof.entity.IrCurve;
import com.gof.entity.IrCurveSpot;
import com.gof.entity.IrCurveSpotWeek;
import com.gof.entity.IrCurveYtm;
import com.gof.entity.IrDcntRate;
import com.gof.entity.IrDcntRateBiz;
import com.gof.entity.IrDcntRateBu;
import com.gof.entity.IrDcntRateBuIm;
import com.gof.entity.IrDcntSceIm;
import com.gof.entity.IrDcntSceStoBiz;
import com.gof.entity.IrDcntSceStoGnr;
import com.gof.entity.IrDiscRate;
import com.gof.entity.IrParamAfnsCalc;
import com.gof.entity.IrParamHwBiz;
import com.gof.entity.IrParamHwCalc;
import com.gof.entity.IrParamHwRnd;
import com.gof.entity.IrParamModel;
import com.gof.entity.IrParamModelBiz;
import com.gof.entity.IrParamModelCalc;
import com.gof.entity.IrParamModelRnd;
import com.gof.entity.IrParamSw;
import com.gof.entity.IrParamSwUsr;
import com.gof.entity.IrQvalSce;
import com.gof.entity.IrSprdAfnsBiz;
import com.gof.entity.IrSprdAfnsCalc;
import com.gof.entity.IrSprdLp;
import com.gof.entity.IrSprdLpBiz;
import com.gof.entity.IrValidParamHw;
import com.gof.entity.IrValidRnd;
import com.gof.entity.IrValidSceSto;
import com.gof.entity.IrVolSwpn;
import com.gof.entity.RcCorpPd;
import com.gof.entity.RcCorpPdBiz;
import com.gof.entity.RcCorpTm;
import com.gof.entity.RcInflationBiz;
import com.gof.entity.RcSegLgdBiz;
import com.gof.entity.RcSegPrepRateBiz;
import com.gof.entity.StdAsstIrSceSto;
import com.gof.entity.his.HFxRateHis;
import com.gof.entity.his.HIrCurveFwd;
import com.gof.entity.his.HIrCurveSpot;
import com.gof.entity.his.HIrCurveSpotWeek;
import com.gof.entity.his.HIrCurveYtm;
import com.gof.entity.his.HIrDcntSceDetBiz;
import com.gof.entity.his.HIrDcntSceStoBiz;
import com.gof.entity.his.HIrDcntSceStoGnr;
import com.gof.entity.his.HIrParamAfnsBiz;
import com.gof.entity.his.HIrParamAfnsCalc;
import com.gof.entity.his.HIrParamHwBiz;
import com.gof.entity.his.HIrParamHwCalc;
import com.gof.entity.his.HIrParamHwRnd;
import com.gof.entity.his.HIrParamModel;
import com.gof.entity.his.HIrParamModelBiz;
import com.gof.entity.his.HIrParamModelCalc;
import com.gof.entity.his.HIrParamSw;
import com.gof.entity.his.HIrSprdAfnsBiz;
import com.gof.entity.his.HIrSprdAfnsCalc;
import com.gof.entity.his.HIrSprdCrd;
import com.gof.entity.his.HIrSprdCurve;
import com.gof.entity.his.HIrSprdLp;
import com.gof.entity.his.HIrSprdLpBiz;
import com.gof.entity.his.HIrVolSwpn;
import com.gof.entity.his.HRcCorpPd;
import com.gof.entity.his.HRcCorpPdBiz;
import com.gof.entity.his.HRcCorpTm;
import com.gof.entity.his.HRcInflationBiz;
import com.gof.entity.his.HRcSegLgdBiz;
import com.gof.entity.his.HRcSegPrepRateBiz;
import com.gof.entity.his.TransferJob;
import com.gof.enums.EJob;
import com.gof.enums.ERunArgument;
import com.gof.util.AesCrypto;
import com.gof.util.DateUtil;
import com.gof.util.EsgConstant;
import com.gof.util.FinUtils;
import com.gof.util.HibernateUtil;
import com.gof.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

	private static Map<ERunArgument, String> argInputMap  = new LinkedHashMap<>();
	private static Map<String, String>       argInDBMap   = new LinkedHashMap<>();
	private static List<String>              jobList      = new ArrayList<String>();

	private static Session   session;
	private static String    bssd;

	private static int       projectionYear              = 120;
	private static long      cnt                         = 0;
	private static int       flushSize                   = 10000;
	private static int       logSize                     = 100000;

	private static List<String>           irCurveIdList  = new ArrayList<String>();
	private static Map<String, IrCurve>   irCurveMap     = new TreeMap<String, IrCurve>();
	private static Map<String, IrParamSw> irCurveSwMap   = new TreeMap<String, IrParamSw>();

	private static Map<String, Map<Integer, IrParamSw>> kicsSwMap = new TreeMap<String, Map<Integer, IrParamSw>>();
	private static Map<String, Map<Integer, IrParamSw>> kicsSwMap2 = new TreeMap<String, Map<Integer, IrParamSw>>();
	private static List<IrParamSw> ytmSpreadList = new ArrayList<IrParamSw>();

	private static Map<String, Map<Integer, IrParamSw>> ifrsSwMap = new TreeMap<String, Map<Integer, IrParamSw>>();
	private static Map<String, Map<Integer, IrParamSw>> ifrsSwMap2 = new TreeMap<String, Map<Integer, IrParamSw>>();
	private static Map<String, Map<Integer, IrParamSw>> ibizSwMap = new TreeMap<String, Map<Integer, IrParamSw>>();
	private static Map<String, Map<Integer, IrParamSw>> ibizSwMap2 = new TreeMap<String, Map<Integer, IrParamSw>>();
	private static Map<String, Map<Integer, IrParamSw>> saasSwMap = new TreeMap<String, Map<Integer, IrParamSw>>();
	private static Map<String, Map<Integer, IrParamSw>> saasSwMap2 = new TreeMap<String, Map<Integer, IrParamSw>>();

	private static double    hw1fInitAlpha               = 0.05;
	private static double    hw1fInitSigma               = 0.007;
	private static double    targetDuration              = 3.0;
	private static int[]     hwAlphaPieceSplit           = new int[] {10};
	private static int[]     hwAlphaPieceNonSplit        = new int[] {20};
	private static int[]     hwSigmaPiece                = new int[] {1, 2, 3, 5, 7, 10};
	private static double    significanceLevel           = 0.05;
	private static int       cirAvgMonth                 = 36;
	private static int       cirPrjYear                  = 30;
//	private static String    iRateHisStBaseDate          = "20100101";
	private static String    iRateHisStBaseDate          = "20100101";
	private static int    	 afnsHisDateNum          	 =  0;
	private static String    bizApplyYn          		 = "N";



	public static void main(String[] args) {

// ****************************************************************** Run Argument &  Common Data Setting ********************************

		init(args);		// Input Data Setting

// ****************************************************************** Pre-Process and Input Setting      ********************************

		job110();       // Job 110: Set Smith-Wilson Attribute
		job120();       // Job 120: Set Swaption Volatility
		job130();       // Job 130: Set YTM TermStructure

		job150();       // Job 110: YTM to SPOT by Smith-Wilson Method
		job151();       // Job 111: YTM to SPOT by Smith-Wilson Method Migration

// ****************************************************************** Deterministic Scenario with LP      ********************************

		job210();       // Job 210: AFNS Weekly Input TermStructure Setup
		job211();       // Job 210: AFNS Weekly Input TermStructure Setup
		job220();       // Job 220: AFNS Shock Spread
		job230();       // Job 230: Biz Applied AFNS Shock Spread

		job240();		// job 240: Set Liquidity Premium
		job250();		// job 250: Biz Applied Liquidity Premium

		job260();		// job 260: BottomUp Risk Free TermStructure with Liquidity Premium
		job261();		// job 260: BottomUp Risk Free TermStructure with Liquidity Premium
		job270();		// job 270: Interpolated TermStructure by SW
		job271();		// job 270: Interpolated TermStructure by SW
		job280();		// job 280: Biz Applied TermStructure by SW

// ****************************************************************** Stochastic Scenario with LP         ********************************

		job310();		// Job 310: Calibration and Validation of HW1F Model Parameter
		job320();		// job 320: Calibration Stress Test of HW1F Model Parameter
		job330();       // job 330: Biz Applied HW1F Model Parameter

		job340();       // job 340: HW1F Discount Rate Scenario of Biz TermStructure
		job350();       // job 350: HW1F Bond Yield Scenario of Biz TermStructure
		job360();       // job 360: Validation for Random number of HW1F Scenario
		job370();       // job 370: Validation for Market Consistency of HW1F Scenario

// ****************************************************************** Biz Scenario of CIR Model           ********************************

		job410();		// Job 410: Calibration of CIR Forcasting Model
		job420();		// Job 420: Biz Applied CIR Forcasting Model Parameter
		job430();		// Job 430: Stochastic Scenario of CIR Forcasting Model

// ****************************************************************** INTERNAL MODEL Job                              ********************************
		job710();
		job711();
		job720();
		job721();
		job730();
		job740();

		job760();
		job770();

// ****************************************************************** RC Job                              ********************************

		job810();		// Job 810: Set Transition Matrix
		job820();		// Job 820: Corporate PD from Transition Matrix
		job830();		// Job 830: Seg Lgd
		job840();		// Job 840: Seg Inflation
		job850();		// Job 850: Seg Prepay
		job860();		// Job 860: Disclosure Rate
		job870();		// Job 870: Investment Cost


		job901();		// Job 901: Save Seq Data
		job902();		// Job 902: Save Seq Data
		job903();		// Job 903: Save Seq Data
		job904();		// Job 904: Save Seq Data
		job905();		// Job 905: Save Seq Data
		job906();		// Job 906: Save Seq Data
//		job907();		// Job 907: Save Seq Data

		job910();		// Job 910: Apply Biz Data

// ****************************************************************** End Job                             ********************************

		hold();
		HibernateUtil.shutdown();
		System.exit(0);
	}


	private static void init(String[] args) {

		for (String aa : args) {
			for (ERunArgument bb : ERunArgument.values()) {
				if (aa.split("=")[0].toLowerCase().contains(bb.name())) {
					argInputMap.put(bb, aa.split("=")[1]);
					break;
				}
			}
		}

		try {
			if(argInputMap.containsKey(ERunArgument.encrypt) && argInputMap.get(ERunArgument.encrypt).toString().trim().toUpperCase().equals("Y")) {

				Scanner sc = new Scanner(System.in);

				System.out.println("=======================================================");
				System.out.println("==========  [GESG Text Encryption Process]   ==========");
				System.out.println("=======================================================");
				System.out.print("Enter Plain Text To Encrypt: ");
				String plainPwd = sc.next();

				AesCrypto aes128 = new AesCrypto();
				String encryptPwd = aes128.AesCBCEncode(plainPwd);
//				log.info("Aes128 Encode: {}, Decode: {}", plainPwd, encryptPwd);

				System.out.println("");
				System.out.println("Encrypted Text:  " + encryptPwd);
				System.out.println("=======================================================");

				sc.close();
				System.exit(0);
			}
		}
		catch (Exception e) {
			log.error("Argument Error: [-Dencrypt] in Text Encryption Process");
			System.exit(0);
		}


		for (String aa : args) log.info("Input Arguments : {}", aa);
//		log.info("argInputMap: {}", argInputMap);

		try {
			bssd = argInputMap.get(ERunArgument.time).replace("-", "").replace("/", "").substring(0, 6);
		} catch (Exception e) {
			log.error("Argument Error: [-Dtime]" );
			System.exit(0);
		}

		Properties properties = new Properties();
		try {
			FileInputStream fis = new FileInputStream(argInputMap.get(ERunArgument.properties));
			properties.load(new BufferedInputStream(fis));
			EsgConstant.TABLE_SCHEMA = properties.getOrDefault("schema", "GESG").toString().trim().toUpperCase();

			if(properties.containsKey("encrypt") && properties.getProperty("encrypt").toString().trim().toUpperCase().equals("Y")) {

				AesCrypto aes128 = new AesCrypto();
				String decodePwd = aes128.AesCBCDecode(properties.getProperty("password"));
				properties.setProperty("password", decodePwd);
			}

			if(properties.contains("bizApplyYn") && properties.getProperty("bizApplyYn").toString().trim().toUpperCase().equals("Y")) {
				bizApplyYn ="Y";
			}
		} catch (Exception e) {
			log.error("Error in Properties Loading : {}", e);
			System.exit(0);
		}
		
//TODO!!!!!
//		session = HibernateUtil.getSessionFactory(properties).openSession();
//		for local test!!!!
		session = HibernateUtil.getSessionFactory().openSession();
		log.info("End of session call");


		argInDBMap = CoEsgMetaDao.getCoEsgMeta("PROPERTIES").stream().collect(toMap(s->s.getParamKey(), s->s.getParamValue()));
		log.info("argInDBMap: {}", argInDBMap);

		try {
			if(argInputMap.containsKey(ERunArgument.job) && argInputMap.get(ERunArgument.job).toUpperCase().equals("CIR")) {
				CoJobListDao.getCoJobList("CIR").stream().forEach(s -> log.info("JOB LIST: {}, {}", s.getJobId().trim(), s.getJobNm().trim()));
				jobList = CoJobListDao.getCoJobList("CIR").stream().map(s -> s.getJobId().trim()).collect(Collectors.toList());
			}
			else {
//				Map<String, String> jobListMap = CoJobListDao.getCoJobList().stream().collect(Collectors.toMap(CoJobList::getJobId, CoJobList::getJobNm, (k, v) -> k, TreeMap::new));
				CoJobListDao.getCoJobList().stream().forEach(s -> log.info("JOB LIST: {}, {}", s.getJobId().trim(), s.getJobNm().trim()));
				jobList    = CoJobListDao.getCoJobList().stream().map(s -> s.getJobId().trim()).collect(Collectors.toList());
			}

			hw1fInitAlpha                = Double.parseDouble(argInDBMap.getOrDefault("HW1F_ALPHA_INIT", "0.05" ).toString());
			hw1fInitSigma                = Double.parseDouble(argInDBMap.getOrDefault("HW1F_SIGMA_INIT", "0.007").toString());

			String hwAlphaPieceStr       = argInDBMap.getOrDefault("HW1F_ALPHA_PIECE", "10").toString();
			String hwSigmaPieceStr       = argInDBMap.getOrDefault("HW1F_SIGMA_PIECE", "1, 2, 3, 5, 7, 10").toString();
			hwAlphaPieceSplit            = Arrays.stream(hwAlphaPieceStr.split(",")).map(s -> s.trim()).map(Integer::parseInt).mapToInt(Integer::intValue).toArray();
			hwSigmaPiece                 = Arrays.stream(hwSigmaPieceStr.split(",")).map(s -> s.trim()).map(Integer::parseInt).mapToInt(Integer::intValue).toArray();

			
			afnsHisDateNum	             = Integer.parseInt(argInDBMap.getOrDefault("AFNS_IM_HIS_NUM", "0").toString());
			iRateHisStBaseDate           = argInDBMap.getOrDefault("IR_HIS_START_DATE", "20100101").toString().trim().toUpperCase();
			
			projectionYear 	             = Integer.parseInt(argInDBMap.getOrDefault("PROJECTION_YEAR", "120").toString());



			targetDuration	             = Double.parseDouble(argInDBMap.getOrDefault("BOND_YIELD_TGT_DURATION", "3.0").toString());
			significanceLevel            = Double.parseDouble(argInDBMap.getOrDefault("SIGNIFICANCE_LEVEL", "0.05").toString());

			cirAvgMonth                  = Integer.parseInt(argInDBMap.getOrDefault("CIR_AVG_MONTH", "36").toString());
			cirPrjYear                   = Integer.parseInt(argInDBMap.getOrDefault("CIR_PROJECTION_YEAR", "30").toString());

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Check Input Setting in [{}] or [{}] table", Process.toPhysicalName(CoJobList.class.getSimpleName()), Process.toPhysicalName(CoEsgMeta.class.getSimpleName()));
			System.exit(0);
		}
// Job For Biz Apply Data Transfer
		if(bizApplyYn.equals("Y")) {
			jobList.clear();
			jobList.add("910");
		}

//		for local test
		jobList.clear();
//		jobList.add("110");
//		jobList.add("120");
//		jobList.add("130");
//		jobList.add("150");
//		jobList.add("151");
//		jobList.add("210");
//		jobList.add("211");
//		jobList.add("220");
//		jobList.add("230");
//		jobList.add("240");
//		jobList.add("250");
//		jobList.add("260");
//		jobList.add("261");
		jobList.add("270");
		jobList.add("271");
		jobList.add("280");
//		jobList.add("330");
//		jobList.add("340");
//		jobList.add("370");
//		jobList.add("710");
//		jobList.add("720");
//		jobList.add("711");
//		jobList.add("721");
//		jobList.add("730");
//		jobList.add("740");
//		jobList.add("760");
//		jobList.add("770");
//		jobList.add("810");
//		jobList.add("820");
//		jobList.add("830");
//		jobList.add("840");
//		jobList.add("850");
//		jobList.add("860");
		
//		jobList.add("900");
//		jobList.add("901");
//		jobList.add("902");
//		jobList.add("903");
//		jobList.add("904");
//		jobList.add("905");
//		jobList.add("906");
//		jobList.add("907");
//		jobList.add("910");
	}

	//TODO: Start from E_IR_PARAM_SW_USR
	private static void job110() {
//		if(jobList.contains("110")) {
		if(true) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG110);

			try {
				irCurveMap    = IrCurveDao.getIrCurveList().stream().collect(Collectors.toMap(s->s.getIrCurveId(), Function.identity()));
				irCurveIdList = irCurveMap.keySet().stream().collect(Collectors.toList());

				if(irCurveIdList.isEmpty()) {
					log.error("No Active Interest Rate Curve in [{}] Table", Process.toPhysicalName(IrCurve.class.getSimpleName()));
					throw new Exception();
				}
				else {
					log.info("Active Interest Rate Curve: [{}]", irCurveIdList);
				}

				int delNum = session.createQuery("delete IrParamSw a where a.baseYymm=:param").setParameter("param", bssd).executeUpdate();
				log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", Process.toPhysicalName(IrParamSw.class.getSimpleName()), jobLog.getJobId(), bssd, delNum);

				List<IrParamSwUsr> paramSwUsrList = IrParamSwDao.getIrParamSwUsrList(bssd, irCurveIdList);
//				paramSwUsrList.forEach(s -> log.info("paramSwUsrList: {}", s));
				log.info("Active PARAM_SW_USR SIZE in [{}]: [{}]", bssd, paramSwUsrList.size());

				Set<String>  applBizDvSet    = paramSwUsrList.stream().map(s -> s.getApplBizDv())   .collect(Collectors.toSet());
				Set<String>  irCurveIdSet    = paramSwUsrList.stream().map(s -> s.getIrCurveId())   .collect(Collectors.toSet());
				Set<Integer> irCurveSceNoSet = paramSwUsrList.stream().map(s -> s.getIrCurveSceNo()).collect(Collectors.toSet());
//				irCurveSceNoSet.forEach(s -> log.info("irCurveSceNoSet: {}", s));

				List<IrParamSw> paramSwList = new ArrayList<IrParamSw>();

				for(String biz : applBizDvSet) {
					for(String curve : irCurveIdSet) {
						for(Integer sceNo : irCurveSceNoSet) {
							List<IrParamSw> sw = IrParamSwDao.getIrParamSwList(bssd, biz, curve, sceNo);
							paramSwList.addAll(sw);
						}
					}
				}
//				paramSwList.forEach(s -> log.info("paramSwList: {}", s));
				log.info("Active PARAM_SW     SIZE in [{}]: [{}]", bssd, paramSwList.size());
				if(paramSwList.size() != paramSwUsrList.size()) {
					log.warn("Check Smith-Wilson Attribute in [{}] Table for [{}]", Process.toPhysicalName(IrParamSwUsr.class.getSimpleName()), bssd);
				}

				paramSwList.stream().forEach(s -> session.save(s));
				log.info("[{}] has been Created from [{}] in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", Process.toPhysicalName(IrParamSw.class.getSimpleName()), Process.toPhysicalName(IrParamSwUsr.class.getSimpleName()), jobLog.getJobId(), bssd, paramSwList.size());

				irCurveSwMap  = paramSwList.stream().filter(s -> s.getIrCurveSceNo().equals(1) && s.getApplBizDv().equals("KICS"))
						                            .collect(Collectors.toMap(IrParamSw::getIrCurveId, Function.identity()));

				for(IrParamSw irParamSw : paramSwList.stream().filter(s -> s.getIrCurveSceNo().equals(1) && !s.getApplBizDv().equals("KICS")).collect(Collectors.toList())) {
					irCurveSwMap.putIfAbsent(irParamSw.getIrCurveId(), irParamSw);
				}

				if(irCurveSwMap.isEmpty()) {
					log.error("Check Smith-Wilson Attribute in [{}] Table for [{}]", Process.toPhysicalName(IrParamSw.class.getSimpleName()), bssd);
					throw new Exception();
				}
				
				ytmSpreadList = paramSwList.stream().filter(s-> s.getYtmSpread() != 0.0).collect(toList());

				kicsSwMap = paramSwList.stream().filter(s -> s.getApplBizDv().equals("KICS"))
												.collect(Collectors.groupingBy(IrParamSw::getIrCurveId, TreeMap::new, Collectors.toMap(IrParamSw::getIrCurveSceNo, Function.identity(), (k, v) -> k, TreeMap::new)));


				
				kicsSwMap2 = paramSwList.stream().filter(s -> s.getApplBizDv().equals("KICS")).filter(s-> s.getYtmSpread() != 0.0)
								.collect(groupingBy(IrParamSw::getIrCurveId, TreeMap::new, toMap(IrParamSw::getIrCurveSceNo, Function.identity(), (k, v) -> k, TreeMap::new)));

				ifrsSwMap = paramSwList.stream().filter(s -> s.getApplBizDv().equals("IFRS"))
	                                            .collect(Collectors.groupingBy(IrParamSw::getIrCurveId, TreeMap::new, Collectors.toMap(IrParamSw::getIrCurveSceNo, Function.identity(), (k, v) -> k, TreeMap::new)));

				ifrsSwMap2 = paramSwList.stream().filter(s -> s.getApplBizDv().equals("IFRS")).filter(s-> s.getYtmSpread() != 0.0)
						.collect(groupingBy(IrParamSw::getIrCurveId, TreeMap::new, toMap(IrParamSw::getIrCurveSceNo, Function.identity(), (k, v) -> k, TreeMap::new)));
				
				ibizSwMap = paramSwList.stream().filter(s -> s.getApplBizDv().equals("IBIZ"))
												.collect(Collectors.groupingBy(IrParamSw::getIrCurveId, TreeMap::new, Collectors.toMap(IrParamSw::getIrCurveSceNo, Function.identity(), (k, v) -> k, TreeMap::new)));

				
				ibizSwMap2 = paramSwList.stream().filter(s -> s.getApplBizDv().equals("IBIZ")).filter(s-> s.getYtmSpread() != 0.0)
										.collect(groupingBy(IrParamSw::getIrCurveId, TreeMap::new, toMap(IrParamSw::getIrCurveSceNo, Function.identity(), (k, v) -> k, TreeMap::new)));
							

				saasSwMap = paramSwList.stream().filter(s -> s.getApplBizDv().equals("SAAS"))
												.collect(Collectors.groupingBy(IrParamSw::getIrCurveId, TreeMap::new, Collectors.toMap(IrParamSw::getIrCurveSceNo, Function.identity(), (k, v) -> k, TreeMap::new)));
				
				saasSwMap2 = paramSwList.stream().filter(s -> s.getApplBizDv().equals("SAAS")).filter(s-> s.getYtmSpread() != 0.0)
										.collect(groupingBy(IrParamSw::getIrCurveId, TreeMap::new, toMap(IrParamSw::getIrCurveSceNo, Function.identity(), (k, v) -> k, TreeMap::new)));

				for(Map.Entry<String, Map<Integer, IrParamSw>> crv : kicsSwMap.entrySet()) log.info("SW Input Set: [KICS], [IR_CURVE_ID: {}, Num of SCENARIO: {}]", crv.getKey(), crv.getValue().size());
				for(Map.Entry<String, Map<Integer, IrParamSw>> crv : ifrsSwMap.entrySet()) log.info("SW Input Set: [IFRS], [IR_CURVE_ID: {}, Num of SCENARIO: {}]", crv.getKey(), crv.getValue().size());
				for(Map.Entry<String, Map<Integer, IrParamSw>> crv : ibizSwMap.entrySet()) log.info("SW Input Set: [IBIZ], [IR_CURVE_ID: {}, Num of SCENARIO: {}]", crv.getKey(), crv.getValue().size());
				for(Map.Entry<String, Map<Integer, IrParamSw>> crv : saasSwMap.entrySet()) log.info("SW Input Set: [SAAS], [IR_CURVE_ID: {}, Num of SCENARIO: {}]", crv.getKey(), crv.getValue().size());

				session.flush();
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);

				session.saveOrUpdate(jobLog);
				session.getTransaction().commit();
				System.exit(0);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job120() {
		if(jobList.contains("120")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG120);

			try {
				for(Map.Entry<String, IrCurve> irCrv : irCurveMap.entrySet()) {

					if(!irCurveSwMap.containsKey(irCrv.getKey())) {
						log.warn("No Ir Curve Data [{}] in Smith-Wilson Map for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					int delNum = session.createQuery("delete IrVolSwpn a where a.baseYymm = :param1 and a.irCurveId = :param2")
										.setParameter("param1", bssd)
										.setParameter("param2", irCrv.getKey()).executeUpdate();

					log.info("[{}] has been Deleted in Job:[{}] [COUNT: {}]", Process.toPhysicalName(IrVolSwpn.class.getSimpleName()), jobLog.getJobId(), delNum);

					List<IrVolSwpn> swpnVol = Esg120_SetVolSwpn.createVolSwpnFromUsr(bssd, irCrv.getKey());
					swpnVol.stream().forEach(s -> session.save(s));
				}

				session.flush();
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job130() {
		if(jobList.contains("130")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG130);

			try {
				for(Map.Entry<String, IrCurve> irCrv : irCurveMap.entrySet()) {
					log.info("Job 130 : {},{},{}", irCrv.getKey(), irCrv.getValue());
					if(!irCurveSwMap.containsKey(irCrv.getKey())) {
						log.warn("No Ir Curve Data [{}] in Smith-Wilson Map for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					int delNum = session.createQuery("delete IrCurveYtm a where a.baseDate like :param1 and a.irCurveId = :param2")
										.setParameter("param1", bssd+"%")
										.setParameter("param2", irCrv.getKey()).executeUpdate();

					log.info("[{}] has been Deleted in Job:[{}] [COUNT: {}]", Process.toPhysicalName(IrCurveYtm.class.getSimpleName()), jobLog.getJobId(), delNum);

					List<IrCurveYtm> ytmUsrHis = Esg130_SetYtm.createYtmFromUsrHis(bssd, irCrv.getKey());
					ytmUsrHis.stream().forEach(s -> session.saveOrUpdate(s));
//					ytmUsrHis.stream().forEach(s -> log.info("{}", s));
					session.flush();
					
					Map<String, IrCurveYtm> ytmMap = ytmUsrHis.stream().collect(toMap(IrCurveYtm::getPk, Function.identity()));

					
					List<IrCurveYtm> ytmUsr    = Esg130_SetYtm.createYtmFromUsr(bssd, irCrv.getKey());
					
//					ytmUsr.stream().forEach(s -> session.saveOrUpdate(s));
//					ytmUsr.stream().filter(s-> !session.contains(s)).forEach(s -> session.saveOrUpdate(s));
//					ytmUsr.stream().forEach(s -> log.info("{}", s));
					
					for( IrCurveYtm  aa : ytmUsr) {
						if(!ytmMap.containsKey(aa.getPk())) {
							session.saveOrUpdate(aa);
						}
					}
				}

				session.flush();
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job150() {
		if(jobList.contains("150")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG150);


			try {
				for(Map.Entry<String, IrCurve> irCrv : irCurveMap.entrySet()) {
					if(!irCurveSwMap.containsKey(irCrv.getKey())) {
						log.warn("No Ir Curve Data [{}] in Smith-Wilson Map for [{}]", irCrv.getKey(), bssd);
						continue;
					}

//					IrCurveSpotDao.deleteIrCurveSpotMonth(bssd, irCrv.getKey());
					List<IrCurveYtm> ytmRstList = IrCurveYtmDao.getIrCurveYtmMonth(bssd, irCrv.getKey());
//					List<IrCurveYtm> ytmRstList = IrCurveYtmDao.getIrCurveYtmAll(bssd, irCrv.getKey());
					if(ytmRstList.size()==0) {
						log.warn("No Historical YTM Data exist for [{}, {}]", bssd, irCrv.getKey());
						continue;
					}

					TreeMap<String, List<IrCurveYtm>> ytmRstMap = new TreeMap<String, List<IrCurveYtm>>();
					ytmRstMap = ytmRstList.stream().collect(Collectors.groupingBy(s -> s.getBaseDate(), TreeMap::new, Collectors.toList()));

					for(Map.Entry<String, List<IrCurveYtm>> ytmRst : ytmRstMap.entrySet()) {

//						log.info("ytmRst: {}, {}, {}, {}, {}, {}", ytmRst.getKey(), irCrv.getKey(), irCurveSwMap.get(irCrv.getKey()).getSwAlphaYtm(), irCurveSwMap.get(irCrv.getKey()).getFreq(), ytmRst.getValue(), ytmRst);

						List<IrCurveSpot> rst = new ArrayList<IrCurveSpot>();

						rst = Esg150_YtmToSpotSw.createIrCurveSpot(ytmRst.getKey(), irCrv.getKey(), ytmRst.getValue(),
								                                   irCurveSwMap.get(irCrv.getKey()).getSwAlphaYtm(), irCurveSwMap.get(irCrv.getKey()).getFreq());

						if(rst.isEmpty()) throw new Exception();
						rst.forEach(s -> session.saveOrUpdate(s));
					}
					session.flush();
					session.clear();
				}
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				e.printStackTrace();
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}

	//TODO: for temporary use only
	private static void job151() {
		if(jobList.contains("151")) {
//		if(true) {
			session.beginTransaction();
//			CoJobInfo jobLog = startJogLog(EJob.ESG151);

//			irCurveIdList.add("1010000");
//			List<IrParamSw> paramSwList = IrParamSwDao.getIrParamSwList(bssd, "KICS", "1010000", 1);
//			irCurveSwMap  = paramSwList.stream().filter(s -> s.getIrCurveSceNo().equals(1) && s.getApplBizDv().equals("KICS"))
//                    .collect(Collectors.toMap(IrParamSw::getIrCurveId, Function.identity()));
			
			log.info("Job 151 : {},{}", irCurveIdList.size());

			for(String irCrv : irCurveIdList) {
				try {
					String stBssd = "201012";
					String edBssd = "202212";
					LocalDate sttDate = DateUtil.convertFrom(DateUtil.toEndOfMonth(stBssd));
					LocalDate endDate = DateUtil.convertFrom(DateUtil.toEndOfMonth(edBssd));
					int monthDiff = DateUtil.monthBetween(stBssd, edBssd);

					log.info("{}. {}, monthDiff: {}", sttDate, endDate, monthDiff);

					TreeSet<LocalDate> bssdList = new TreeSet<LocalDate>();

					for(int i=0; i<monthDiff+1; i++) {
						LocalDate curDate = sttDate.plusMonths(i);
						bssdList.add(curDate);
					}

					log.info("{}", bssdList);

					for(LocalDate date : bssdList) {

						String bssd1 = DateUtil.dateToString(date).substring(0, 6);
						IrCurveSpotDao.deleteIrCurveSpotMonth(bssd1, irCrv);

						List<IrCurveYtm> ytmRstList = IrCurveYtmDao.getIrCurveYtmMonth(bssd1, irCrv);

						if(ytmRstList.size()==0) {
							log.warn("No Historical YTM Data exist for [{}, {}]", bssd, irCrv);
							continue;
						}

						TreeMap<String, List<IrCurveYtm>> ytmRstMap = new TreeMap<String, List<IrCurveYtm>>();
						ytmRstMap = ytmRstList.stream().collect(Collectors.groupingBy(s -> s.getBaseDate(), TreeMap::new, Collectors.toList()));

						for(Map.Entry<String, List<IrCurveYtm>> ytmRst : ytmRstMap.entrySet()) {
//							Esg150_YtmToSpotSw.createIrCurveSpot(ytmRst.getKey(), irCrv, ytmRst.getValue()).forEach(s -> session.save(s));
							Esg150_YtmToSpotSw.createIrCurveSpot(ytmRst.getKey(), irCrv, ytmRst.getValue(), irCurveSwMap.get(irCrv).getSwAlphaYtm(), irCurveSwMap.get(irCrv).getFreq()).forEach(s -> session.save(s));
						}
					}
					session.flush();
					session.clear();
//					completeJob("SUCCESS", jobLog);

				} catch (Exception e) {
					log.error("ERROR: {}", e);
//					completeJob("ERROR", jobLog);
				}
			}
//			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job210() {
		if(jobList.contains("210")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG210);

			try {
				for(Map.Entry<String, IrCurve> irCrv : irCurveMap.entrySet()) {
					if(!irCurveSwMap.containsKey(irCrv.getKey())) {
						log.warn("No Ir Curve Data [{}] in Smith-Wilson Map for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), irCurveSwMap.get(irCrv.getKey()).getLlp());
//					List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), Math.min(StringUtil.objectToPrimitive(irCurveSwMap.get(irCrv.getKey()).getLlp()), 20));
					log.info("TenorList in [{}]: ID: [{}], llp: [{}], matCd: {}", jobLog.getJobId(), irCrv.getKey(), irCurveSwMap.get(irCrv.getKey()).getLlp(), tenorList);

					if(tenorList.isEmpty()) {
						log.warn("No Ir Curve Data [{}] at [{}] in Table [{}]", irCrv.getKey(), bssd, Process.toPhysicalName(IrCurveSpot.class.getSimpleName()));
						continue;
					}

					int delNum = session.createQuery("delete IrCurveSpotWeek a where a.baseDate >= :param1 and a.baseDate <= :param2 and a.irCurveId = :param3")
										.setParameter("param1", iRateHisStBaseDate)
										.setParameter("param2", bssd+31)
							            .setParameter("param3", irCrv.getKey()).executeUpdate();

					log.info("[{}] has been Deleted in Job:[{}] [IR_CURVE_ID: {}, COUNT: {}]", Process.toPhysicalName(IrCurveSpotWeek.class.getSimpleName()), jobLog.getJobId(), irCrv.getKey(), delNum);

					List<IrCurveSpotWeek> spotWeek = Esg210_SpotWeek.setupIrCurveSpotWeek(bssd, iRateHisStBaseDate, irCrv.getKey(), tenorList);
					spotWeek.stream().forEach(s -> session.save(s));

//					spotWeek.stream().forEach(s -> log.info("aaaa : {}", s.toString()));
					session.flush();
					session.clear();
				}
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}

	private static void job211() {
		if(jobList.contains("211")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG210);

			List<IrParamSw> paramSwList = IrParamSwDao.getIrParamSwList(bssd, "KICS", "1010000", 1);
			paramSwList.addAll(IrParamSwDao.getIrParamSwList(bssd, "KICS", "RF_USD", 1));

			irCurveMap    = IrCurveDao.getIrCurveList().stream().collect(Collectors.toMap(s->s.getIrCurveId(), Function.identity()));
			irCurveSwMap  = paramSwList.stream().filter(s -> s.getIrCurveSceNo().equals(1) && s.getApplBizDv().equals("KICS"))
                    .collect(Collectors.toMap(IrParamSw::getIrCurveId, Function.identity()));
			try {
				for(Map.Entry<String, IrCurve> irCrv : irCurveMap.entrySet()) {
					if(!irCurveSwMap.containsKey(irCrv.getKey())) {
						log.warn("No Ir Curve Data [{}] in Smith-Wilson Map for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), irCurveSwMap.get(irCrv.getKey()).getLlp());
//					List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), Math.min(StringUtil.objectToPrimitive(irCurveSwMap.get(irCrv.getKey()).getLlp()), 20));
					log.info("TenorList in [{}]: ID: [{}], llp: [{}], matCd: {}", jobLog.getJobId(), irCrv.getKey(), irCurveSwMap.get(irCrv.getKey()).getLlp(), tenorList);

					if(tenorList.isEmpty()) {
						log.warn("No Ir Curve Data [{}] at [{}] in Table [{}]", irCrv.getKey(), bssd, Process.toPhysicalName(IrCurveSpot.class.getSimpleName()));
						continue;
					}

//					int delNum = session.createQuery("delete IrCurveSpotWeek a where a.baseDate >= :param1 and a.baseDate <= :param2 and a.irCurveId = :param3")
//										.setParameter("param1", iRateHisStBaseDate)
//										.setParameter("param2", bssd+31)
//							            .setParameter("param3", irCrv.getKey()).executeUpdate();

//					log.info("[{}] has been Deleted in Job:[{}] [IR_CURVE_ID: {}, COUNT: {}]", Process.toPhysicalName(IrCurveSpotWeek.class.getSimpleName()), jobLog.getJobId(), irCrv.getKey(), delNum);

					List<IrCurveSpotWeek> spotWeek = Esg210_SpotWeek.setupIrCurveSpotWeek(bssd, iRateHisStBaseDate, irCrv.getKey(), tenorList);
					spotWeek.stream().forEach(s -> session.saveOrUpdate(s));

//					spotWeek.stream().forEach(s -> log.info("aaaa : {}", s.toString()));
					session.flush();
					session.clear();
				}
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}

	private static void job220() {
		if(jobList.contains("220")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG220);

			String irModelId       = argInDBMap.getOrDefault("AFNS_MODE"         , "AFNS"    ).trim().toUpperCase();
			int    weekDay         = Integer.valueOf((String) argInDBMap.getOrDefault("AFNS_WEEK_DAY"        , "5"));
			double confInterval    = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_CONF_INTERVAL"   , "0.995"));

			double dt              = 1.0 / 52.0;   //weekly only
			int    kalmanItrMax    = Integer.valueOf((String) argInDBMap.getOrDefault("AFNS_KALMAN_ITR_MAX"  , "100"));
			double sigmaInit       = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_SIGMA_INIT"      , "0.05"));
			double epsilonInit     = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_EPSILON_INIT"    , "0.001"));


			List<IrParamModel> modelMst = IrParamModelDao.getParamModelList(irModelId);
//			modelMst.forEach(s-> log.info("zzz : {},{}", s.toString()));
			Map<String, IrParamModel> modelMstMap = modelMst.stream().collect(Collectors.toMap(IrParamModel::getIrCurveId, Function.identity()));
			log.info("IrParamModel: {}", modelMstMap.toString());

			try {
				for(Map.Entry<String, IrCurve> irCrv : irCurveMap.entrySet()) {
					if(!irCurveSwMap.containsKey(irCrv.getKey())) {
						log.warn("No Ir Curve Data [{}] in Smith-Wilson Map for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					if(!modelMstMap.containsKey(irCrv.getKey())) {
						log.warn("No Model Attribute of [{}] for [{}] in [{}] Table", irModelId, irCrv.getKey(), Process.toPhysicalName(IrParamModel.class.getSimpleName()));
						continue;
					}

					log.info("AFNS Shock Spread (Cont) for [{}({}, {})]", irCrv.getKey(), irCrv.getValue().getIrCurveNm(), irCrv.getValue().getCurCd());

					List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), Math.min(StringUtil.objectToPrimitive(irCurveSwMap.get(irCrv.getKey()).getLlp()), 20));
//					tenorList.remove("M0048"); tenorList.remove("M0084"); tenorList.remove("M0180");  //FOR CHECK w/ FSS
//					log.info("{}", tenorList);
					//TODO:
//					tenorList.remove("M0180");

					log.info("TenorList in [{}]: ID: [{}], llp: [{}], matCd: {}", jobLog.getJobId(), irCrv.getKey(), irCurveSwMap.get(irCrv.getKey()).getLlp(), tenorList);
					if(tenorList.isEmpty()) {
						log.warn("No Spot Rate Data [ID: {}] for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					int delNum1 = session.createQuery("delete IrParamAfnsCalc a where baseYymm=:param1 and a.irModelId=:param2 and a.irCurveId=:param3")
		                     			 .setParameter("param1", bssd)
		                     			 .setParameter("param2", irModelId)
		                     			 .setParameter("param3", irCrv.getKey())
		                     			 .executeUpdate();

					log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, IR_CURVE_ID: {}, COUNT: {}]", Process.toPhysicalName(IrParamAfnsCalc.class.getSimpleName()), jobLog.getJobId(), irModelId, irCrv.getKey(), delNum1);

					int delNum2 = session.createQuery("delete IrSprdAfnsCalc a where baseYymm=:param1 and a.irModelId=:param2 and a.irCurveId=:param3")
					                     .setParameter("param1", bssd)
								  		 .setParameter("param2", irModelId)
										 .setParameter("param3", irCrv.getKey())
										 .executeUpdate();

					log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, IR_CURVE_ID: {}, COUNT: {}]", Process.toPhysicalName(IrSprdAfnsCalc.class.getSimpleName()), jobLog.getJobId(), irModelId, irCrv.getKey(), delNum2);

					List<IrCurveSpotWeek> weekHisList    = IrCurveSpotWeekDao.getIrCurveSpotWeekHis(bssd, iRateHisStBaseDate, irCrv.getKey(), tenorList, weekDay, false);
					List<IrCurveSpotWeek> weekHisBizList = IrCurveSpotWeekDao.getIrCurveSpotWeekHis(bssd, iRateHisStBaseDate, irCrv.getKey(), tenorList, weekDay, true);
					log.info("weekHisList: [{}], [TOTAL: {}, BIZDAY: {}], [from {} to {}, weekDay:{}]", irCrv.getKey(), weekHisList.size(), weekHisBizList.size(), iRateHisStBaseDate.substring(0,6), bssd, weekDay);

					//for ensuring enough input size
					if(weekHisList.size() < 1000) {
						log.warn("Weekly SpotRate Data is not Enough [ID: {}, SIZE: {}] for [{}]", irCrv.getKey(), weekHisList.size(), bssd);
						continue;
					}

					List<IrCurveSpot> curveHisList = weekHisList.stream().map(s->s.convertToHis()).collect(toList());
//					curveHisList = curveHisList.stream().filter(s -> Integer.valueOf(s.getBaseDate()) >= 20110701).collect(toList());

					//Any curveBaseList result in same parameters and spreads.
					List<IrCurveSpot> curveBaseList = IrCurveSpotDao.getIrCurveSpot(bssd, irCrv.getKey(), tenorList);

					if(curveBaseList.size()==0) {
						log.warn("No IR Curve Data [IR_CURVE_ID: {}] for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					double errorTolerance = StringUtil.objectToPrimitive(modelMstMap.get(irCrv.getKey()).getItrTol(), 1E-8);

					Map<String, List<?>> irShockSenario = new TreeMap<String, List<?>>();
					irShockSenario = Esg220_ShkSprdAfns.createAfnsShockScenario(FinUtils.toEndOfMonth(bssd), irModelId, curveHisList, curveBaseList, tenorList, dt, sigmaInit
													                          , irCurveSwMap.get(irCrv.getKey()).getLtfr()
														                      , irCurveSwMap.get(irCrv.getKey()).getLtfrCp()
														                      , projectionYear
														                      , errorTolerance
														                      , kalmanItrMax
														                      , confInterval
														                      , epsilonInit);

//					//for input Paras(currently null)
//					irShockSenario = Esg220_AfnsShkSprd.createAfnsShockScenarioByParam(FinUtils.toEndOfMonth(bssd), irModelId, null, curveBaseList, tenorList, dt, sigmaInit
//																                       , irCurveSwMap.get(irCrv.getKey()).getLtfr()
//																                       , irCurveSwMap.get(irCrv.getKey()).getLtfrCp()
//																                       , projectionYear
//																                       , errorTolerance
//																                       , kalmanItrMax
//																                       , confInterval
//																                       , epsilonInit);

					for(Map.Entry<String, List<?>> rslt : irShockSenario.entrySet()) {
//						if(!rslt.getKey().equals("CURVE")) rslt.getValue().forEach(s -> log.info("{}, {}", s.toString()));
						rslt.getValue().forEach(s -> session.save(s));

						session.flush();
						session.clear();
					}
				}
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job230() {
		if(jobList.contains("230")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG230);

			String irModelId = argInDBMap.getOrDefault("AFNS_MODE", "AFNS").trim().toUpperCase();

			try {
				for(Map.Entry<String, IrCurve> irCrv : irCurveMap.entrySet()) {
					if(!irCurveSwMap.containsKey(irCrv.getKey())) {
						log.warn("No Ir Curve Data [{}] in Smith-Wilson Map for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					int delNum = session.createQuery("delete IrSprdAfnsBiz a where baseYymm=:param1 and a.irModelId=:param2 and a.irCurveId=:param3")
		                     			.setParameter("param1", bssd)
		                     			.setParameter("param2", irModelId)
		                     			.setParameter("param3", irCrv.getKey())
		                     			.executeUpdate();

					log.info("[{}] has been Deleted in Job:[{}] [IR_CURVE_ID: {}, COUNT: {}]", Process.toPhysicalName(IrSprdAfnsBiz.class.getSimpleName()), jobLog.getJobId(), irCrv.getKey(), delNum);

					List<IrSprdAfnsBiz> afnsBizList = Esg230_BizSprdAfns.createBizAfnsShockScenario(bssd, irModelId, irCrv.getKey());
					afnsBizList.stream().forEach(s -> session.save(s));
				}
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job240() {
		if(jobList.contains("240")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG240);

			try {
				int delNum = session.createQuery("delete IrSprdLp a where a.baseYymm=:param").setParameter("param", bssd).executeUpdate();
				log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", Process.toPhysicalName(IrSprdLp.class.getSimpleName()), jobLog.getJobId(), bssd, delNum);

				List<IrSprdLp> kicsSpread1 = Esg240_LpSprd.setLpFromSwMap(bssd, "KICS", kicsSwMap);
				kicsSpread1.stream().forEach(s -> session.save(s));

				List<IrSprdLp> ifrsSpread1 = Esg240_LpSprd.setLpFromSwMap(bssd, "IFRS", ifrsSwMap);
				ifrsSpread1.stream().forEach(s -> session.save(s));

				List<IrSprdLp> ibizSpread1 = Esg240_LpSprd.setLpFromSwMap(bssd, "IBIZ", ibizSwMap);
				ibizSpread1.stream().forEach(s -> session.save(s));

				List<IrSprdLp> saasSpread1 = Esg240_LpSprd.setLpFromSwMap(bssd, "SAAS", saasSwMap);
				saasSpread1.stream().forEach(s -> session.save(s));


				String lpCurveId = argInDBMap.getOrDefault("LP_CURVE_ID", "5010110");

				List<IrSprdLp> kicsSpread2 = Esg240_LpSprd.setLpFromCrdSprd(bssd, "KICS", kicsSwMap, lpCurveId);
				kicsSpread2.stream().forEach(s -> session.save(s));

				List<IrSprdLp> ifrsSpread2 = Esg240_LpSprd.setLpFromCrdSprd(bssd, "IFRS", ifrsSwMap, lpCurveId);
				ifrsSpread2.stream().forEach(s -> session.save(s));

				List<IrSprdLp> ibizSpread2 = Esg240_LpSprd.setLpFromCrdSprd(bssd, "IBIZ", ibizSwMap, lpCurveId);
				ibizSpread2.stream().forEach(s -> session.save(s));

				List<IrSprdLp> saasSpread2 = Esg240_LpSprd.setLpFromCrdSprd(bssd, "SAAS", saasSwMap, lpCurveId);
				saasSpread2.stream().forEach(s -> session.save(s));


				List<IrSprdLp> kicsSpread3 = Esg240_LpSprd.setLpFromUsr(bssd, "KICS", kicsSwMap);
				kicsSpread3.stream().forEach(s -> session.save(s));

				List<IrSprdLp> ifrsSpread3 = Esg240_LpSprd.setLpFromUsr(bssd, "IFRS", ifrsSwMap);
				ifrsSpread3.stream().forEach(s -> session.save(s));

				List<IrSprdLp> ibizSpread3 = Esg240_LpSprd.setLpFromUsr(bssd, "IBIZ", ibizSwMap);
				ibizSpread3.stream().forEach(s -> session.save(s));

				List<IrSprdLp> saasSpread3 = Esg240_LpSprd.setLpFromUsr(bssd, "SAAS", saasSwMap);
				saasSpread3.stream().forEach(s -> session.save(s));


				session.flush();
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job250() {
		if(jobList.contains("250")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG250);

			try {
				int delNum = session.createQuery("delete IrSprdLpBiz a where a.baseYymm=:param").setParameter("param", bssd).executeUpdate();
				log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", Process.toPhysicalName(IrSprdLpBiz.class.getSimpleName()), jobLog.getJobId(), bssd, delNum);

				List<IrSprdLpBiz> kicsSpread = Esg250_BizLpSprd.setLpSprdBiz(bssd, "KICS", kicsSwMap);
				kicsSpread.stream().forEach(s -> session.save(s));

				List<IrSprdLpBiz> ifrsSpread = Esg250_BizLpSprd.setLpSprdBiz(bssd, "IFRS", ifrsSwMap);
				ifrsSpread.stream().forEach(s -> session.save(s));

				List<IrSprdLpBiz> ibizSpread = Esg250_BizLpSprd.setLpSprdBiz(bssd, "IBIZ", ibizSwMap);
				ibizSpread.stream().forEach(s -> session.save(s));

				List<IrSprdLpBiz> saasSpread = Esg250_BizLpSprd.setLpSprdBiz(bssd, "SAAS", saasSwMap);
				saasSpread.stream().forEach(s -> session.save(s));

				session.flush();
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job260() {
		if(jobList.contains("260")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG260);

			try {
				int delNum = session.createQuery("delete IrDcntRateBu a where a.baseYymm=:param").setParameter("param", bssd).executeUpdate();
				log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", Process.toPhysicalName(IrDcntRateBu.class.getSimpleName()), jobLog.getJobId(), bssd, delNum);

				String irModelId = "AFNS";		//for acquiring AFNS Shock Spread

				List<IrDcntRateBu> kicsDcntRateBu = Esg260_IrDcntRateBu.setIrDcntRateBu(bssd, irModelId, "KICS", kicsSwMap);
				kicsDcntRateBu.stream().forEach(s -> session.save(s));

				List<IrDcntRateBu> ifrsDcntRateBu = Esg260_IrDcntRateBu.setIrDcntRateBu(bssd, irModelId, "IFRS", ifrsSwMap);
				ifrsDcntRateBu.stream().forEach(s -> session.save(s));

				List<IrDcntRateBu> ibizDcntRateBu = Esg260_IrDcntRateBu.setIrDcntRateBu(bssd, irModelId, "IBIZ", ibizSwMap);
				ibizDcntRateBu.stream().forEach(s -> session.save(s));

				//forward curve or manual shift of term structure is treated here
				List<IrDcntRateBu> saasDcntRateBu = Esg260_IrDcntRateBu.setIrDcntRateBu(bssd, irModelId, "SAAS", saasSwMap);
				saasDcntRateBu.stream().forEach(s -> session.save(s));

				session.flush();
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job270() {
		if(jobList.contains("270")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG270);

			try {
				int delNum = session.createQuery("delete IrDcntRate a where a.baseYymm=:param").setParameter("param", bssd).executeUpdate();
				log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", Process.toPhysicalName(IrDcntRate.class.getSimpleName()), jobLog.getJobId(), bssd, delNum);

//				List<IrDcntRate> userDcntRate = IrDcntRateDao.getIrDcntRateUsrList(bssd).stream().map(s -> s.convert()).collect(Collectors.toList());
//				userDcntRate.stream().forEach(s -> session.save(s));
				
				
				List<IrDcntRate> kicsDcntRate = Esg270_IrDcntRate.createIrDcntRate(bssd, "KICS", kicsSwMap, projectionYear);
//				if(kicsDcntRate.isEmpty()) throw new Exception();
				kicsDcntRate.stream().forEach(s -> session.save(s));

				List<IrDcntRate> ifrsDcntRate = Esg270_IrDcntRate.createIrDcntRate(bssd, "IFRS", ifrsSwMap, projectionYear);
				ifrsDcntRate.stream().forEach(s -> session.save(s));

				
				List<IrDcntRate> ibizDcntRate = Esg270_IrDcntRate.createIrDcntRate(bssd, "IBIZ", ibizSwMap, projectionYear);
				ibizDcntRate.stream().forEach(s -> session.save(s));

				List<IrDcntRate> saasDcntRate = Esg270_IrDcntRate.createIrDcntRate(bssd, "SAAS", saasSwMap, projectionYear);
				saasDcntRate.stream().forEach(s -> session.save(s));

				
//				Map<String, Map<Integer, IrParamSw>> temp = new TreeMap<String, Map<Integer, IrParamSw>>();
//				temp = ytmSpreadList.stream().filter(s->s.getIrCurveId().equals("RF_NZD"))
//				.filter(s->s.getIrCurveSceNo() > 60 )
//				.collect(Collectors.groupingBy(IrParamSw::getIrCurveId, TreeMap::new, Collectors.toMap(IrParamSw::getIrCurveSceNo, Function.identity(), (k, v) -> k, TreeMap::new)));
//				Esg270_IrDcntRate.createIrDcntRate(bssd, "IBIZ", temp, projectionYear).forEach(s-> save(s));
				
				
				
				session.flush();
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
//				log.error("Check User Defined Dcnt Rate Data in [{}] Table", Process.toPhysicalName(IrDcntRateUsr.class.getSimpleName()));
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	// ytm spread 瑜� �쟻�슜�븳 �븷�씤�쑉 �궛異�
	private static void job261() {
		if(jobList.contains("261")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG261);

			try {

				String irModelId = "AFNS";		//for acquiring AFNS Shock Spread


				for(Map.Entry<String, Map<Integer,IrParamSw>> entry :  kicsSwMap2.entrySet()) {
					for( Map.Entry<Integer, IrParamSw> innerEntry : entry .getValue().entrySet()) {
						log.info("zzzz : {},{}", innerEntry.getValue().getIrCurveSceNo(), innerEntry.getValue().getYtmSpread());
					}
				}
				List<IrDcntRateBu> kicsDcntRateBu = Esg261_IrDcntRateBu_Ytm.setIrDcntRateBu(bssd, irModelId, "KICS",  kicsSwMap2);
				kicsDcntRateBu.stream().forEach(s -> session.saveOrUpdate(s));
				session.flush();
				
				for(Map.Entry<String, Map<Integer,IrParamSw>> entry :  ibizSwMap2.entrySet()) {
					for( Map.Entry<Integer, IrParamSw> innerEntry : entry .getValue().entrySet()) {
						log.info("ibiz param Sw with ytm Spread : {},{}", innerEntry.getValue().getIrCurveSceNo(), innerEntry.getValue().getYtmSpread());
					}

				}
				
				List<IrDcntRateBu> ibizDcntRateBu = Esg261_IrDcntRateBu_Ytm.setIrDcntRateBu(bssd, irModelId, "IBIZ",  ibizSwMap2);
				ibizDcntRateBu.stream().forEach(s -> session.saveOrUpdate(s));
				session.flush();

				for(Map.Entry<String, Map<Integer,IrParamSw>> entry :  ifrsSwMap2.entrySet()) {
					for( Map.Entry<Integer, IrParamSw> innerEntry : entry .getValue().entrySet()) {
						log.info("ibiz param Sw with ytm Spread : {},{}", innerEntry.getValue().getIrCurveSceNo(), innerEntry.getValue().getYtmSpread());
					}
				}
				
				Esg261_IrDcntRateBu_Ytm.setIrDcntRateBu(bssd, irModelId, "IFRS",  ifrsSwMap2).forEach(s -> session.saveOrUpdate(s));
				session.flush();
				
				
				for(Map.Entry<String, Map<Integer,IrParamSw>> entry :  saasSwMap2.entrySet()) {
					for( Map.Entry<Integer, IrParamSw> innerEntry : entry .getValue().entrySet()) {
						log.info("ibiz param Sw with ytm Spread : {},{}", innerEntry.getValue().getIrCurveSceNo(), innerEntry.getValue().getYtmSpread());
					}

				}
				
				Esg261_IrDcntRateBu_Ytm.setIrDcntRateBu(bssd, irModelId, "SAAS",  saasSwMap2).forEach(s -> session.saveOrUpdate(s));
				session.flush();
				
				
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job271() {
	if(jobList.contains("271")) {
		session.beginTransaction();
		CoJobInfo jobLog = startJogLog(EJob.ESG271);

		try {

//			String irModelId = "AFNS";		//for acquiring AFNS Shock Spread
//			YTM SPREAD 媛� �꽕�젙�맂 SW �꽭�똿留� �븘�꽣留�
			
			Map<Double, List<IrParamSw>> spMap = ytmSpreadList.stream().filter(s-> s.getYtmSpread() != 0.0).collect(groupingBy(IrParamSw::getYtmSpread, toList()));
			
			

			for(Map.Entry<Double, List<IrParamSw>> entry : spMap.entrySet()) {
				Map<String, Map<Integer, IrParamSw>> kicsSwMap3 = entry.getValue().stream()
								.collect(groupingBy(IrParamSw::getIrCurveId, TreeMap::new, Collectors.toMap(IrParamSw::getIrCurveSceNo, Function.identity(), (k, v) -> k, TreeMap::new)));

				entry.getValue().forEach(s-> log.info("Entry in Job271 : {},{},{}", entry.getKey(), s.getIrCurveSceNo(), s.getShkSprdSceNo()));

//				List<IrDcntRate> kicsDcntRate = Esg271_IrDcntRate.createIrDcntRate(bssd, "ALL", kicsSwMap3, projectionYear);
				List<IrDcntRate> kicsDcntRate = Esg271_IrDcntRate.createIrDcntRate(bssd,  kicsSwMap3, projectionYear);
				kicsDcntRate.stream().forEach(s -> saveOrUpdate(s));

				session.flush();
				session.clear();
			}

			completeJob("SUCCESS", jobLog);

		} catch (Exception e) {
//			log.error("Check User Defined Dcnt Rate Data in [{}] Table", Process.toPhysicalName(IrDcntRateUsr.class.getSimpleName()));
			log.error("ERROR: {}", e);
			completeJob("ERROR", jobLog);
		}
		session.saveOrUpdate(jobLog);
		session.getTransaction().commit();
	}
}

	private static void job280() {
		if(jobList.contains("280")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG280);

			try {
				int delNum = session.createQuery("delete IrDcntRateBiz a where a.baseYymm=:param").setParameter("param", bssd).executeUpdate();
				log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", Process.toPhysicalName(IrDcntRateBiz.class.getSimpleName()), jobLog.getJobId(), bssd, delNum);

				IrDcntRateDao.getIrDcntRateBizAdjSpotList (bssd, "KICS").forEach(s -> session.save(s));
				IrDcntRateDao.getIrDcntRateBizBaseSpotList(bssd, "KICS").forEach(s -> session.save(s));
				session.flush();
				
				IrDcntRateDao.getIrDcntRateBizAdjSpotList (bssd, "IFRS").forEach(s -> session.save(s));
				IrDcntRateDao.getIrDcntRateBizBaseSpotList(bssd, "IFRS").forEach(s -> session.save(s));
				session.flush();
				
				IrDcntRateDao.getIrDcntRateBizAdjSpotList (bssd, "IBIZ").forEach(s -> session.save(s));
				IrDcntRateDao.getIrDcntRateBizBaseSpotList(bssd, "IBIZ").forEach(s -> session.save(s));
				session.flush();
				
				IrDcntRateDao.getIrDcntRateBizAdjSpotList (bssd, "SAAS").forEach(s -> session.save(s));
				IrDcntRateDao.getIrDcntRateBizBaseSpotList(bssd, "SAAS").forEach(s -> session.save(s));
				session.flush();
				
				
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job310() {
		if(jobList.contains("310")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG310);

			String irModelId    = argInDBMap.getOrDefault("HW_MODE", "HW1F").trim().toUpperCase();
			String irModelIdNsp = irModelId + "_NSP";
			String irModelIdSp  = irModelId + "_SP";

			List<IrParamModel> modelMst = IrParamModelDao.getParamModelList(irModelId);
			Map<String, IrParamModel> modelMstMap = modelMst.stream().collect(Collectors.toMap(IrParamModel::getIrCurveId, Function.identity()));
			log.info("IrParamModel: {}", modelMstMap.toString());

			try {
				for(Map.Entry<String, IrCurve> irCrv : irCurveMap.entrySet()) {
					if(!irCurveSwMap.containsKey(irCrv.getKey())) {
						log.warn("No Ir Curve Data [{}] in Smith-Wilson Map for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					if(!modelMstMap.containsKey(irCrv.getKey())) {
						log.warn("No Model Attribute of [{}] for [{}] in [{}] Table", irModelId, irCrv.getKey(), Process.toPhysicalName(IrParamModel.class.getSimpleName()));
						continue;
					}

//					List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), irCurveSwMap.get(irCrv.getKey()).getLlp());
					List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), 20);
					log.info("TenorList in [{}]: ID: [{}], llp: [{}], matCd: {}", jobLog.getJobId(), irCrv.getKey(), irCurveSwMap.get(irCrv.getKey()).getLlp(), tenorList);
					if(tenorList.isEmpty()) {
						log.warn("No Spot Rate Data [ID: {}] for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					int delNum1 = session.createQuery("delete IrParamHwCalc a where baseYymm=:param1 and a.irModelId=:param2 and a.irCurveId =:param3")
								 		 .setParameter("param1", bssd)
		                     			 .setParameter("param2", irModelIdNsp)
		                     			 .setParameter("param3", irCrv.getKey())
		                     			 .executeUpdate();

					log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, IR_CURVE_ID: {}, COUNT: {}]", Process.toPhysicalName(IrParamHwCalc.class.getSimpleName()), jobLog.getJobId(), irModelIdNsp, irCrv.getKey(), delNum1);


					int delNum2 = session.createQuery("delete IrParamHwCalc a where baseYymm=:param1 and a.irModelId=:param2 and a.irCurveId=:param3")
								 		 .setParameter("param1", bssd)
			                			 .setParameter("param2", irModelIdSp)
			                			 .setParameter("param3", irCrv.getKey())
			                			 .executeUpdate();

					log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, IR_CURVE_ID: {}, COUNT: {}]", Process.toPhysicalName(IrParamHwCalc.class.getSimpleName()), jobLog.getJobId(), irModelIdSp , irCrv.getKey(), delNum2);


					int delNum3 = session.createQuery("delete IrValidParamHw a where baseYymm=:param1 and a.irModelId=:param2 and a.irCurveId=:param3")
										 .setParameter("param1", bssd)
										 .setParameter("param2", irModelIdNsp)
										 .setParameter("param3", irCrv.getKey())
										 .executeUpdate();

					log.info("[{}] has been Deleted in Job:[{}] [IR_CURVE_ID: {}, COUNT: {}]", Process.toPhysicalName(IrValidParamHw.class.getSimpleName()), jobLog.getJobId(), irCrv.getKey(), delNum3);

					List<IrCurveSpot> spotList = IrCurveSpotDao.getIrCurveSpot(bssd, irCrv.getKey(), tenorList);

					log.info("SPOT RATE: [ID: {}], [SIZE: {}]", irCrv.getKey(), spotList.size());
					if(spotList.size()==0) {
						log.warn("No IR Curve Data [IR_CURVE_ID: {}] for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					List<IrVolSwpn> swpnVolList = IrVolSwpnDao.getSwpnVol(bssd, irCrv.getKey());

					log.info("SWAPNTION VOL: [ID: {}], [SIZE: {}]", irCrv.getKey(), swpnVolList.size());
					if(swpnVolList.size()==0 || swpnVolList.size() != 36) {
						log.warn("Check SWAPTION VOL Data [IR_CURVE_ID: {}] for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					Integer freq = StringUtil.objectToPrimitive(irCurveSwMap.get(irCrv.getKey()).getFreq(), 2);
					double errTol = StringUtil.objectToPrimitive(modelMstMap.get(irCrv.getKey()).getItrTol(), 1E-8);
					log.info("freq: {}, errTol: {}", freq, errTol);

					double[] hwInitParam  = new double[] {hw1fInitAlpha, hw1fInitAlpha, hw1fInitSigma, hw1fInitSigma, hw1fInitSigma, hw1fInitSigma, hw1fInitSigma, hw1fInitSigma};
//					double[] hwInitParam  = new double[] {0.03, 0.06, 0.007, 0.006, 0.005, 0.004, 0.005, 0.006};

					Map<String, List<?>> irParamHw1fNonSplitMap = new TreeMap<String, List<?>>();
					irParamHw1fNonSplitMap = Esg310_ParamHw1f.createParamHw1fNonSplitMap(bssd, irModelIdNsp, irCrv.getKey(), spotList, swpnVolList, hwInitParam, freq, errTol, hwAlphaPieceNonSplit, hwSigmaPiece);
//					irParamHw1fNonSplitMap = Esg310_ParamHw1f.createParamHw1fNonSplitMap(bssd, irModelIdNsp, irCrv.getKey(), spotList, swpnVolList, hwInitParamNsp, freq, errTol, hwAlphaPieceNonSplit, hwSigmaPiece);

					for(Map.Entry<String, List<?>> rslt : irParamHw1fNonSplitMap.entrySet()) {
//						if(rslt.getKey().equals("VALID")) rslt.getValue().forEach(s -> session.save(s));
						rslt.getValue().forEach(s -> session.save(s));
						session.flush();
						session.clear();
					}

					Map<String, List<?>> irParamHw1fSplitMap = new TreeMap<String, List<?>>();
					irParamHw1fSplitMap = Esg310_ParamHw1f.createParamHw1fSplitMap(bssd, irModelIdSp, irCrv.getKey(), spotList, swpnVolList, hwInitParam, freq, errTol, hwAlphaPieceSplit, hwSigmaPiece);

					for(Map.Entry<String, List<?>> rslt : irParamHw1fSplitMap.entrySet()) {
						rslt.getValue().forEach(s -> session.save(s));
						session.flush();
						session.clear();
					}
				}
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job320() {
		if(jobList.contains("320")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG320);

			String irModelId    = argInDBMap.getOrDefault("HW_MODE", "HW1F").trim().toUpperCase();
			String irModelIdNsp = irModelId + "_NSP";

			List<IrParamModel> modelMst = IrParamModelDao.getParamModelList(irModelId);
			Map<String, IrParamModel> modelMstMap = modelMst.stream().collect(Collectors.toMap(IrParamModel::getIrCurveId, Function.identity()));
			log.info("IrParamModel: {}", modelMstMap.toString());

			try {
				for(Map.Entry<String, IrCurve> irCrv : irCurveMap.entrySet()) {
					if(!irCurveSwMap.containsKey(irCrv.getKey())) {
						log.warn("No Ir Curve Data [{}] in Smith-Wilson Map for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					if(!modelMstMap.containsKey(irCrv.getKey())) {
						log.warn("No Model Attribute of [{}] for [{}] in [{}] Table", irModelId, irCrv.getKey(), Process.toPhysicalName(IrParamModel.class.getSimpleName()));
						continue;
					}

//					List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), irCurveSwMap.get(irCrv.getKey()).getLlp());
					List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), 20);

					log.info("TenorList in [{}]: ID: [{}], llp: [{}], matCd: {}", jobLog.getJobId(), irCrv.getKey(), irCurveSwMap.get(irCrv.getKey()).getLlp(), tenorList);
					if(tenorList.isEmpty()) {
						log.warn("No Spot Rate Data [ID: {}] for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					int delNum = session.createQuery("delete IrParamHwCalc a where baseYymm=:param1 and a.irModelId like :param2 and a.irCurveId =:param3 and a.lastModifiedBy=:param4")
								 		.setParameter("param1", bssd)
		                     			.setParameter("param2", "%"+irModelIdNsp+"%")
		                     			.setParameter("param3", irCrv.getKey())
		                     			.setParameter("param4", jobLog.getJobId())
		                     			.executeUpdate();

					log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, IR_CURVE_ID: {}, COUNT: {}]", Process.toPhysicalName(IrParamHwCalc.class.getSimpleName()), jobLog.getJobId(), irModelIdNsp, irCrv.getKey(), delNum);

					List<IrCurveSpot> spotList = IrCurveSpotDao.getIrCurveSpot(bssd, irCrv.getKey(), tenorList);

					log.info("SPOT RATE: [ID: {}], [SIZE: {}]", irCrv.getKey(), spotList.size());
					if(spotList.size()==0) {
						log.warn("No IR Curve Data [IR_CURVE_ID: {}] for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					List<IrVolSwpn> swpnVolList = IrVolSwpnDao.getSwpnVol(bssd, irCrv.getKey());

					log.info("SWAPNTION VOL: [ID: {}], [SIZE: {}]", irCrv.getKey(), swpnVolList.size());
					if(swpnVolList.size()==0) {
						log.warn("No SWAPTION VOL Data [IR_CURVE_ID: {}] for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					Integer freq = StringUtil.objectToPrimitive(irCurveSwMap.get(irCrv.getKey()).getFreq(), 2);
					double errTol = StringUtil.objectToPrimitive(modelMstMap.get(irCrv.getKey()).getItrTol(), 1E-8);
					log.info("freq: {}, errTol: {}", freq, errTol);

					//TODO: Initial Parameter(Sigma) Stability Test for SIGMA: [0.001, 0.010, 0.020, 0.030, 0.040, 0.050]
					double validSigma = 0.0;
					for(int i=0; i<6; i++) {

						if(i==0) validSigma = 0.001;
						if(i==1) validSigma = 0.010;
						if(i==2) validSigma = 0.020;
						if(i==3) validSigma = 0.030;
						if(i==4) validSigma = 0.040;
						if(i==5) validSigma = 0.050;

						double[] hwInitParamSigma  = new double[] {hw1fInitAlpha, hw1fInitAlpha, validSigma, validSigma, validSigma, validSigma, validSigma, validSigma};

						List<IrParamHwCalc> hwParamCalcValid = Esg320_ParamHw1fStressTest.createParamHw1fNonSplitMapValid(bssd, irModelIdNsp + "_INIT_" + String.valueOf(validSigma), irCrv.getKey(), spotList, swpnVolList, hwInitParamSigma, freq, errTol, hwAlphaPieceNonSplit, hwSigmaPiece);
						hwParamCalcValid.forEach(s -> session.save(s));
					}


					//TODO: Market Data(Spot and Swaption Vol Stability Test for [Spot +1bp, Spot -1bp, Swaption Vol +1bp, Swaption Vol -1bp]
					double[] hwInitParamMkt = new double[] {hw1fInitAlpha, hw1fInitAlpha, hw1fInitSigma, hw1fInitSigma, hw1fInitSigma, hw1fInitSigma, hw1fInitSigma, hw1fInitSigma};

					List<IrCurveSpot> spotListUp = IrCurveSpotDao.getIrCurveSpot(bssd, irCrv.getKey(), tenorList);
					spotListUp.stream().forEach(s -> s.setSpotRate(s.getSpotRate() + 0.0001));
					List<IrParamHwCalc> hwParamCalcSpotUp = Esg320_ParamHw1fStressTest.createParamHw1fNonSplitMapValid(bssd, irModelIdNsp + "_SPOT_UP", irCrv.getKey(), spotListUp, swpnVolList, hwInitParamMkt, freq, errTol, hwAlphaPieceNonSplit, hwSigmaPiece);
					hwParamCalcSpotUp.forEach(s -> session.save(s));


					List<IrCurveSpot> spotListDn = IrCurveSpotDao.getIrCurveSpot(bssd, irCrv.getKey(), tenorList);
					spotListDn.stream().forEach(s -> s.setSpotRate(s.getSpotRate() - 0.0001));
					List<IrParamHwCalc> hwParamCalcSpotDn = Esg320_ParamHw1fStressTest.createParamHw1fNonSplitMapValid(bssd, irModelIdNsp + "_SPOT_DN", irCrv.getKey(), spotListDn, swpnVolList, hwInitParamMkt, freq, errTol, hwAlphaPieceNonSplit, hwSigmaPiece);
					hwParamCalcSpotDn.forEach(s -> session.save(s));


					List<IrVolSwpn> swpnVolListUp = IrVolSwpnDao.getSwpnVol(bssd, irCrv.getKey());
					swpnVolListUp.stream().forEach(s -> s.setVol(s.getVol() + 0.0001));
					List<IrParamHwCalc> hwParamCalcSwpnUp = Esg320_ParamHw1fStressTest.createParamHw1fNonSplitMapValid(bssd, irModelIdNsp + "_SWPN_UP", irCrv.getKey(), spotList, swpnVolListUp, hwInitParamMkt, freq, errTol, hwAlphaPieceNonSplit, hwSigmaPiece);
					hwParamCalcSwpnUp.forEach(s -> session.save(s));


					List<IrVolSwpn> swpnVolListDn = IrVolSwpnDao.getSwpnVol(bssd, irCrv.getKey());
					swpnVolListDn.stream().forEach(s -> s.setVol(s.getVol() - 0.0001));
					List<IrParamHwCalc> hwParamCalcSwpnDn = Esg320_ParamHw1fStressTest.createParamHw1fNonSplitMapValid(bssd, irModelIdNsp + "_SWPN_DN", irCrv.getKey(), spotList, swpnVolListDn, hwInitParamMkt, freq, errTol, hwAlphaPieceNonSplit, hwSigmaPiece);
					hwParamCalcSwpnDn.forEach(s -> session.save(s));

				}
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job330() {
		if(jobList.contains("330")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG330);

			String irModelId = argInDBMap.getOrDefault("HW_MODE", "HW1F").trim().toUpperCase();

			try {
				for(Map.Entry<String, IrCurve> irCrv : irCurveMap.entrySet()) {
					if(!irCurveSwMap.containsKey(irCrv.getKey())) {
						log.warn("No Ir Curve Data [{}] in Smith-Wilson Map for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					int delNum = session.createQuery("delete IrParamHwBiz a where baseYymm=:param1 and a.irModelId=:param2 and a.irCurveId=:param3")
										.setParameter("param1", bssd)
		                     			.setParameter("param2", irModelId)
		                     			.setParameter("param3", irCrv.getKey())
		                     			.executeUpdate();

					log.info("[{}] has been Deleted in Job:[{}] [IR_CURVE_ID: {}, COUNT: {}]", Process.toPhysicalName(IrParamHwBiz.class.getSimpleName()), jobLog.getJobId(), irCrv.getKey(), delNum);

					int hwAlphaAvgNum = -1 * Integer.parseInt(argInDBMap.getOrDefault("HW_ALPHA_AVG_NUM", "120").toString());
					int hwSigmaAvgNum = -1 * Integer.parseInt(argInDBMap.getOrDefault("HW_SIGMA_AVG_NUM", "120").toString());

					String hwAlphaAvgMatCd = argInDBMap.getOrDefault("HW_ALPHA_AVG_MAT_CD", "M0240").trim().toUpperCase();
					String hwSigmaAvgMatCd = argInDBMap.getOrDefault("HW_SIGMA_AVG_MAT_CD", "M0120").trim().toUpperCase();

					Esg330_BizParamHw1f.createBizHw1fParam(bssd, "KICS", irModelId, irCrv.getKey(), hwAlphaAvgNum, hwAlphaAvgMatCd, hwSigmaAvgNum, hwSigmaAvgMatCd).forEach(s -> session.save(s));
					Esg330_BizParamHw1f.createBizHw1fParam(bssd, "IFRS", irModelId, irCrv.getKey(), hwAlphaAvgNum, hwAlphaAvgMatCd, hwSigmaAvgNum, hwSigmaAvgMatCd).forEach(s -> session.save(s));
					Esg330_BizParamHw1f.createBizHw1fParam(bssd, "IBIZ", irModelId, irCrv.getKey(), hwAlphaAvgNum, hwAlphaAvgMatCd, hwSigmaAvgNum, hwSigmaAvgMatCd).forEach(s -> session.save(s));
					Esg330_BizParamHw1f.createBizHw1fParam(bssd, "SAAS", irModelId, irCrv.getKey(), hwAlphaAvgNum, hwAlphaAvgMatCd, hwSigmaAvgNum, hwSigmaAvgMatCd).forEach(s -> session.save(s));

					session.flush();
					session.clear();
				}
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}

	private static void job340() {
		if(jobList.contains("340")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG340);

			String irModelId = argInDBMap.getOrDefault("HW_MODE", "HW1F").trim().toUpperCase();

			List<IrParamModel> modelMst = IrParamModelDao.getParamModelList(irModelId);
			Map<String, IrParamModel> modelMstMap = modelMst.stream().collect(Collectors.toMap(IrParamModel::getIrCurveId, Function.identity()));
			log.info("IrParamModel: {}", modelMstMap.toString());

			Map<String, Map<String, Map<Integer, IrParamSw>>> totalSwMap = new LinkedHashMap<String, Map<String, Map<Integer, IrParamSw>>>();
//			totalSwMap.put("KICS",  kicsSwMap);
			totalSwMap.put("IFRS",  ifrsSwMap);
//			totalSwMap.put("IBIZ",  ibizSwMap);
//			totalSwMap.put("SAAS",  saasSwMap);


//			int stoBizSeq = HisDao.getSeq("HIrDcntSceStoBiz", bssd);
//			int hwRndSeq = HisDao.getSeq("HIrParamHwRnd", bssd);

//						 + "  where BASE_YYMM=:param1 and IR_MODEL_ID=:param2 ";
//
//			String query2 = " delete " + schema + ".E_IR_PARAM_HW_RND partition (PT_E" + bssd + ") "
//					  + "  where BASE_YYMM=:param1 and IR_MODEL_ID=:param2 ";
//
//			int delNum = session.createNativeQuery(query)
//								.setParameter("param1", bssd)
//								.setParameter("param2", irModelId)
//								.executeUpdate();

			try {
				int delNum1 = session.createQuery("delete IrDcntSceStoBiz a where a.baseYymm=:param1 and a.irModelId=:param2")
							 		 .setParameter("param1", bssd)
									 .setParameter("param2", irModelId)
									 .executeUpdate();

				log.info("[{}] has been Deleted in Job:[{}] [COUNT: {}]", Process.toPhysicalName(IrDcntSceStoBiz.class.getSimpleName()), jobLog.getJobId(), delNum1);

				int delNum2 = session.createQuery("delete IrParamHwRnd a where baseYymm=:param1 and a.irModelId=:param2")
									 .setParameter("param1", bssd)
									 .setParameter("param2", irModelId)
									 .executeUpdate();

				log.info("[{}] has been Deleted in Job:[{}] [COUNT: {}]", Process.toPhysicalName(IrParamHwRnd.class.getSimpleName()), jobLog.getJobId(), delNum2);

				int delNum3 = session.createQuery("delete IrValidSceSto a where baseYymm=:param1 and a.irModelId=:param2 and a.lastModifiedBy=:param3")
									 .setParameter("param1", bssd)
									 .setParameter("param2", irModelId)
									 .setParameter("param3", jobLog.getJobId())
									 .executeUpdate();

				log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, COUNT: {}]", Process.toPhysicalName(IrValidSceSto.class.getSimpleName()), jobLog.getJobId(), irModelId, delNum3);


				for(Map.Entry<String, Map<String, Map<Integer, IrParamSw>>> biz : totalSwMap.entrySet()) {

					for(Map.Entry<String, Map<Integer, IrParamSw>> curveSwMap : biz.getValue().entrySet()) {
						for(Map.Entry<Integer, IrParamSw> swSce : curveSwMap.getValue().entrySet()) {

//							if(!biz.getKey().equals("KICS") || !swSce.getKey().equals(1)) continue;
							log.info("[{}] BIZ: [{}], IR_CURVE_ID: [{}], IR_CURVE_SCE_NO: [{}]", jobLog.getJobId(), biz.getKey(), curveSwMap.getKey(), swSce.getKey());
							Map<String, List<?>> hw1fResult = Esg340_BizScenHw1f.createScenHw1f(bssd, biz.getKey(), irModelId, curveSwMap.getKey(), swSce.getKey(), biz.getValue(), modelMstMap, projectionYear);

							@SuppressWarnings("unchecked")
							List<IrDcntSceStoBiz> stoSceList = (List<IrDcntSceStoBiz>) hw1fResult.get("SCE");
							@SuppressWarnings("unchecked")
							List<IrParamHwRnd>    randHwList = (List<IrParamHwRnd>) hw1fResult.get("RND");

							TreeMap<Integer, TreeMap<Integer, Double>> stoSceMap = new TreeMap<Integer, TreeMap<Integer, Double>>();
							stoSceMap = stoSceList.stream().collect(Collectors.groupingBy(s -> Integer.valueOf(s.getMatCd().substring(1))
													               , TreeMap::new, Collectors.toMap(s -> Integer.valueOf(s.getSceNo()), IrDcntSceStoBiz::getFwdRate, (k, v) -> k, TreeMap::new)));

							Esg340_BizScenHw1f.createQuantileValue(bssd, biz.getKey(), irModelId, curveSwMap.getKey(), swSce.getKey(), stoSceMap).forEach(s -> session.save(s));

							int sceCnt = 1;
							for (IrDcntSceStoBiz sce : stoSceList) {
								session.save(sce);
								if (sceCnt % 50 == 0) {
									session.flush();
									session.clear();
								}
								if (sceCnt % logSize == 0) {
									log.info("Stochastic TermStructure of [{}] [BIZ: {}, ID: {}, SCE: {}] is processed {}/{} in Job:[{}]", irModelId, biz.getKey(), curveSwMap.getKey(), swSce.getKey(), sceCnt, stoSceList.size(), jobLog.getJobId());
								}
								sceCnt++;
							}


							if(biz.getKey().equals("KICS")) {
								int rndCnt = 1;
								for (IrParamHwRnd rnd : randHwList) {
									session.save(rnd);
									if (rndCnt % 50 == 0) {
										session.flush();
										session.clear();
									}
									if (rndCnt % logSize == 0) {
										log.info("Stochastic Random Number of [{}] [BIZ: {}, ID: {}, SCE: {}] is processed {}/{} in Job:[{}]", irModelId, biz.getKey(), curveSwMap.getKey(), swSce.getKey(), rndCnt, randHwList.size(), jobLog.getJobId());
									}
									rndCnt++;
								}
							}

							session.flush();
							session.clear();
						}
					}
				}
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job341() {
		if(jobList.contains("341")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG340);

			String irModelId = argInDBMap.getOrDefault("HW_MODE", "HW1F").trim().toUpperCase();

			List<IrParamModel> modelMst = IrParamModelDao.getParamModelList(irModelId);
			Map<String, IrParamModel> modelMstMap = modelMst.stream().collect(Collectors.toMap(IrParamModel::getIrCurveId, Function.identity()));
			log.info("IrParamModel: {}", modelMstMap.toString());

			Map<String, Map<String, Map<Integer, IrParamSw>>> totalSwMap = new LinkedHashMap<String, Map<String, Map<Integer, IrParamSw>>>();
			totalSwMap.put("KICS",  kicsSwMap);
			totalSwMap.put("IFRS",  ifrsSwMap);
			totalSwMap.put("IBIZ",  ibizSwMap);
			totalSwMap.put("SAAS",  saasSwMap);

			try {

				for(Map.Entry<String, Map<String, Map<Integer, IrParamSw>>> biz : totalSwMap.entrySet()) {

					for(Map.Entry<String, Map<Integer, IrParamSw>> curveSwMap : biz.getValue().entrySet()) {
						for(Map.Entry<Integer, IrParamSw> swSce : curveSwMap.getValue().entrySet()) {

//							if(!biz.getKey().equals("KICS") || !swSce.getKey().equals(1)) continue;
							log.info("[{}] BIZ: [{}], IR_CURVE_ID: [{}], IR_CURVE_SCE_NO: [{}]", jobLog.getJobId(), biz.getKey(), curveSwMap.getKey(), swSce.getKey());
							Map<String, List<?>> hw1fResult = Esg340_BizScenHw1f.createScenHw1f(bssd, biz.getKey(), irModelId, curveSwMap.getKey(), swSce.getKey(), biz.getValue(), modelMstMap, projectionYear);

							@SuppressWarnings("unchecked")
							List<IrDcntSceStoBiz> stoSceList = (List<IrDcntSceStoBiz>) hw1fResult.get("SCE");
							@SuppressWarnings("unchecked")
							List<IrParamHwRnd>    randHwList = (List<IrParamHwRnd>) hw1fResult.get("RND");

							TreeMap<Integer, TreeMap<Integer, Double>> stoSceMap = new TreeMap<Integer, TreeMap<Integer, Double>>();
							stoSceMap = stoSceList.stream().collect(Collectors.groupingBy(s -> Integer.valueOf(s.getMatCd().substring(1))
													               , TreeMap::new, Collectors.toMap(s -> Integer.valueOf(s.getSceNo()), IrDcntSceStoBiz::getFwdRate, (k, v) -> k, TreeMap::new)));

							Esg340_BizScenHw1f.createQuantileValue(bssd, biz.getKey(), irModelId, curveSwMap.getKey(), swSce.getKey(), stoSceMap).forEach(s -> session.save(s));

							int sceCnt = 1;
							for (IrDcntSceStoBiz sce : stoSceList) {
								session.save(sce);
								if (sceCnt % 50 == 0) {
									session.flush();
									session.clear();
								}
								if (sceCnt % logSize == 0) {
									log.info("Stochastic TermStructure of [{}] [BIZ: {}, ID: {}, SCE: {}] is processed {}/{} in Job:[{}]", irModelId, biz.getKey(), curveSwMap.getKey(), swSce.getKey(), sceCnt, stoSceList.size(), jobLog.getJobId());
								}
								sceCnt++;
							}

							if(biz.getKey().equals("KICS")) {
								int rndCnt = 1;
								for (IrParamHwRnd rnd : randHwList) {
									session.save(rnd);
									if (rndCnt % 50 == 0) {
										session.flush();
										session.clear();
									}
									if (rndCnt % logSize == 0) {
										log.info("Stochastic Random Number of [{}] [BIZ: {}, ID: {}, SCE: {}] is processed {}/{} in Job:[{}]", irModelId, biz.getKey(), curveSwMap.getKey(), swSce.getKey(), rndCnt, randHwList.size(), jobLog.getJobId());
									}
									rndCnt++;
								}
							}

							session.flush();
							session.clear();
						}
					}
				}
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();

		}
	}

	private static void job342() {
		if(jobList.contains("342")) {

		}
	}


	private static void job350() {
		if(jobList.contains("350")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG350);

			String irModelId = argInDBMap.getOrDefault("HW_MODE", "HW1F").trim().toUpperCase();

			List<IrParamModel> modelMst = IrParamModelDao.getParamModelList(irModelId);
			Map<String, IrParamModel> modelMstMap = modelMst.stream().collect(Collectors.toMap(IrParamModel::getIrCurveId, Function.identity()));
			log.info("IrParamModel: {}", modelMstMap.toString());

			Map<String, Map<String, Map<Integer, IrParamSw>>> totalSwMap = new LinkedHashMap<String, Map<String, Map<Integer, IrParamSw>>>();
			totalSwMap.put("KICS",  kicsSwMap);
			totalSwMap.put("IFRS",  ifrsSwMap);
			totalSwMap.put("IBIZ",  ibizSwMap);
			totalSwMap.put("SAAS",  saasSwMap);

			try {
				int delNum = session.createQuery("delete StdAsstIrSceSto a where a.baseYymm=:param1")
									.setParameter("param1", bssd)
									.executeUpdate();

				log.info("[{}] has been Deleted in Job:[{}] [COUNT: {}]", Process.toPhysicalName(StdAsstIrSceSto.class.getSimpleName()), jobLog.getJobId(), delNum);

				for(Map.Entry<String, Map<String, Map<Integer, IrParamSw>>> biz : totalSwMap.entrySet()) {

					for(Map.Entry<String, Map<Integer, IrParamSw>> curveSwMap : biz.getValue().entrySet()) {
						for(Map.Entry<Integer, IrParamSw> swSce : curveSwMap.getValue().entrySet()) {

							log.info("[{}] BIZ: [{}], IR_CURVE_ID: [{}], IR_CURVE_SCE_NO: [{}]", jobLog.getJobId(), biz.getKey(), curveSwMap.getKey(), swSce.getKey());
							List<StdAsstIrSceSto> bondYieldList = Esg350_BizBondYieldHw1f.createBondYieldHw1f(bssd, biz.getKey(), irModelId, curveSwMap.getKey(), swSce.getKey(), biz.getValue(), modelMstMap, projectionYear, targetDuration);

							int sceCnt = 1;
							for (StdAsstIrSceSto sce : bondYieldList) {
								session.save(sce);
								if (sceCnt % 50 == 0) {
									session.flush();
									session.clear();
								}
								if (sceCnt % logSize == 0) {
									log.info("Stochastic Bond Yield of [{}] [BIZ: {}, ASST: {}, SCE: {}] is processed {}/{} in Job:[{}]", irModelId, biz.getKey(), curveSwMap.getKey(), swSce.getKey(), sceCnt, bondYieldList.size(), jobLog.getJobId());
								}
								sceCnt++;
							}

							session.flush();
							session.clear();
						}
					}
				}
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}

			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job360() {
		if(jobList.contains("360")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG360);

			String irModelId = argInDBMap.getOrDefault("HW_MODE", "HW1F").trim().toUpperCase();

			List<IrParamModel> modelMst = IrParamModelDao.getParamModelList(irModelId);
			Map<String, IrParamModel> modelMstMap = modelMst.stream().collect(Collectors.toMap(IrParamModel::getIrCurveId, Function.identity()));
			log.info("IrParamModel: {}", modelMstMap.toString());

			Map<String, Map<String, Map<Integer, IrParamSw>>> totalSwMap = new LinkedHashMap<String, Map<String, Map<Integer, IrParamSw>>>();
			totalSwMap.put("KICS",  kicsSwMap);
//			totalSwMap.put("IFRS",  ifrsSwMap);
//			totalSwMap.put("IBIZ",  ibizSwMap);
//			totalSwMap.put("SAAS",  saasSwMap);

			try {
				int delNum = session.createQuery("delete IrValidRnd a where baseYymm=:param1 and a.irModelId=:param2")
									.setParameter("param1", bssd)
									.setParameter("param2", irModelId)
									.executeUpdate();

				log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, COUNT: {}]", Process.toPhysicalName(IrValidRnd.class.getSimpleName()), jobLog.getJobId(), irModelId, delNum);

				for(Map.Entry<String, Map<String, Map<Integer, IrParamSw>>> biz : totalSwMap.entrySet()) {

					for(Map.Entry<String, Map<Integer, IrParamSw>> curveSwMap : biz.getValue().entrySet()) {
						for(Map.Entry<Integer, IrParamSw> swSce : curveSwMap.getValue().entrySet()) {

							if(!biz.getKey().equals("KICS") || !swSce.getKey().equals(1)) continue;
//							if(!curveSwMap.getKey().equals("1010000")) continue;

							log.info("[{}] BIZ: [{}], IR_CURVE_ID: [{}], IR_CURVE_SCE_NO: [{}]", jobLog.getJobId(), biz.getKey(), curveSwMap.getKey(), swSce.getKey());
							List<IrParamHwRnd> randHwList = Esg360_ValidRandHw1f.createValidInputHw1f(bssd, biz.getKey(), irModelId, curveSwMap.getKey(), swSce.getKey(), biz.getValue(), modelMstMap, projectionYear, targetDuration);

							TreeMap<Integer, TreeMap<Integer, Double>> randNumMap = new TreeMap<Integer, TreeMap<Integer, Double>>();
							randNumMap = randHwList.stream().collect(Collectors.groupingBy(s -> Integer.valueOf(s.getMatCd().substring(1))
													               , TreeMap::new, Collectors.toMap(s -> Integer.valueOf(s.getSceNo()), IrParamHwRnd::getRndNum, (k, v) -> k, TreeMap::new)));

//							log.info("rand: {}", randNumMap.firstEntry().getValue());

							Esg360_ValidRandHw1f.testRandNormality    (bssd, irModelId, curveSwMap.getKey(), randNumMap, significanceLevel).forEach(s -> session.save(s));
							Esg360_ValidRandHw1f.testRandIndependency (bssd, irModelId, curveSwMap.getKey(), randNumMap, significanceLevel).forEach(s -> session.save(s));

							session.flush();
							session.clear();
						}
					}
				}
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job370() {
		if(jobList.contains("370")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG370);

			String irModelId = argInDBMap.getOrDefault("HW_MODE", "HW1F").trim().toUpperCase();

			List<IrParamModel> modelMst = IrParamModelDao.getParamModelList(irModelId);
			Map<String, IrParamModel> modelMstMap = modelMst.stream().collect(Collectors.toMap(IrParamModel::getIrCurveId, Function.identity()));
			log.info("IrParamModel: {}", modelMstMap.toString());

			Map<String, Map<String, Map<Integer, IrParamSw>>> totalSwMap = new LinkedHashMap<String, Map<String, Map<Integer, IrParamSw>>>();
			totalSwMap.put("KICS",  kicsSwMap);
			totalSwMap.put("IFRS",  ifrsSwMap);
			totalSwMap.put("IBIZ",  ibizSwMap);
			totalSwMap.put("SAAS",  saasSwMap);

			try {
				int delNum = session.createQuery("delete IrValidSceSto a where baseYymm=:param1 and a.irModelId=:param2 and a.lastModifiedBy=:param3")
									.setParameter("param1", bssd)
									.setParameter("param2", irModelId)
									.setParameter("param3", jobLog.getJobId())
									.executeUpdate();

				log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, COUNT: {}]", Process.toPhysicalName(IrValidSceSto.class.getSimpleName()), jobLog.getJobId(), irModelId, delNum);

				for(Map.Entry<String, Map<String, Map<Integer, IrParamSw>>> biz : totalSwMap.entrySet()) {

					for(Map.Entry<String, Map<Integer, IrParamSw>> curveSwMap : biz.getValue().entrySet()) {
						for(Map.Entry<Integer, IrParamSw> swSce : curveSwMap.getValue().entrySet()) {
//							if(!biz.getKey().equals("KICS") || !swSce.getKey().equals(1)) continue;
//							if(!curveSwMap.getKey().equals("1010000")) continue;

							log.info("[{}] BIZ: [{}], IR_CURVE_ID: [{}], IR_CURVE_SCE_NO: [{}]", jobLog.getJobId(), biz.getKey(), curveSwMap.getKey(), swSce.getKey());
							Map<String, List<?>> hw1fResult = Esg370_ValidScenHw1f.createValidInputHw1f(bssd, biz.getKey(), irModelId, curveSwMap.getKey(), swSce.getKey(), biz.getValue(), modelMstMap, projectionYear, targetDuration);

							@SuppressWarnings("unchecked")
							List<IrDcntSceStoBiz> stoSceList = (List<IrDcntSceStoBiz>) hw1fResult.get("SCE");
							@SuppressWarnings("unchecked")
							List<StdAsstIrSceSto> stoYldList = (List<StdAsstIrSceSto>) hw1fResult.get("YLD");

							TreeMap<Integer, TreeMap<Integer, Double>> stoSceMap = new TreeMap<Integer, TreeMap<Integer, Double>>();
							stoSceMap = stoSceList.stream().collect(Collectors.groupingBy(s -> Integer.valueOf(s.getMatCd().substring(1))
													               , TreeMap::new, Collectors.toMap(s -> Integer.valueOf(s.getSceNo()), IrDcntSceStoBiz::getFwdRate, (k, v) -> k, TreeMap::new)));

							TreeMap<Integer, TreeMap<Integer, Double>> stoYldMap = new TreeMap<Integer, TreeMap<Integer, Double>>();
							stoYldMap = stoYldList.stream().collect(Collectors.groupingBy(s -> Integer.valueOf(s.getMatCd().substring(1))
													               , TreeMap::new, Collectors.toMap(s -> Integer.valueOf(s.getSceNo()), StdAsstIrSceSto::getAsstYield, (k, v) -> k, TreeMap::new)));

//							log.info("dcnt  : {}", stoBizMap.get(2).entrySet());
//							log.info("yield : {}", stoYldMap.get(2).entrySet());

							Esg370_ValidScenHw1f.testMarketConsistency(bssd, biz.getKey(), irModelId, curveSwMap.getKey(), swSce.getKey(), stoSceMap, stoYldMap, significanceLevel).forEach(s -> session.save(s));

							session.flush();
							session.clear();
						}
					}
				}
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job410() {
		if(jobList.contains("410")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG410);

			String irModelId = "CIR";

			List<IrParamModel> modelMst = IrParamModelDao.getParamModelList(irModelId);
			Map<String, Map<String, IrParamModel>> modelMstMap = modelMst.stream().collect(Collectors.groupingBy(IrParamModel::getIrModelId
					                                                                    , TreeMap::new, Collectors.toMap(IrParamModel::getIrCurveId, Function.identity(), (k, v) -> k, TreeMap::new)));
			log.info("IrParamModel: {}", modelMstMap.toString());

			int delNum = session.createQuery("delete IrParamModelCalc a where baseYymm=:param1 and a.irModelId like :param2")
			 		    		.setParameter("param1", bssd)
        			            .setParameter("param2", "%"+irModelId+"%")
        			            .executeUpdate();

			log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, COUNT: {}]", Process.toPhysicalName(IrParamModelCalc.class.getSimpleName()), jobLog.getJobId(), irModelId, delNum);

			try {
				for(Map.Entry<String, Map<String, IrParamModel>> modelMap : modelMstMap.entrySet()) {
					for(Map.Entry<String, IrParamModel> model : modelMap.getValue().entrySet()) {

						String  cirTenor  = String.format("%s%04d", "M", Integer.valueOf(modelMap.getKey().substring(5)) * 12);
						Double  dt        = 1.0 / 250;

						List<IrCurveSpot> spotList = IrCurveYtmDao.getIrCurveYtmHis(bssd, model.getKey(), -cirAvgMonth, cirTenor).stream().map(s -> s.convertSimple()).collect(Collectors.toList());
//						log.info("{}", spotList);
						if(spotList.size()==0) {
							log.warn("No Historical YTM Data exist for [{}, {}] in [{}]", bssd, model.getKey(), jobLog.getJobId());
							continue;
						}

						List<IrParamModelCalc> cirParamList = Esg410_ParamCirForecast.createCirParam(bssd, modelMap.getKey(), model.getKey(), dt, spotList, model.getValue().getItrTol());
						cirParamList.stream().forEach(s -> session.save(s));
					}
				}

				session.flush();
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job420() {
		if(jobList.contains("420")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG420);

			String irModelId = "CIR";

			List<IrParamModel> modelMst = IrParamModelDao.getParamModelList(irModelId);
			Map<String, Map<String, IrParamModel>> modelMstMap = modelMst.stream().collect(Collectors.groupingBy(IrParamModel::getIrModelId
					                                                                    , TreeMap::new, Collectors.toMap(IrParamModel::getIrCurveId, Function.identity(), (k, v) -> k, TreeMap::new)));
			log.info("IrParamModel: {}", modelMstMap.toString());

			int delNum = session.createQuery("delete IrParamModelBiz a where baseYymm=:param1 and a.irModelId like :param2")
			 		    		.setParameter("param1", bssd)
        			            .setParameter("param2", "%"+irModelId+"%")
        			            .executeUpdate();

			log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, COUNT: {}]", Process.toPhysicalName(IrParamModelBiz.class.getSimpleName()), jobLog.getJobId(), irModelId, delNum);

			try {
				for(Map.Entry<String, Map<String, IrParamModel>> modelMap : modelMstMap.entrySet()) {
					for(Map.Entry<String, IrParamModel> model : modelMap.getValue().entrySet()) {
						Esg420_BizParamCirForecast.createBizCirForecastParam(bssd, modelMap.getKey(), model.getKey()).forEach(s -> session.save(s));
					}
				}

				session.flush();
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job430() {
		if(jobList.contains("430")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG430);

			String irModelId = "CIR";

			List<IrParamModel> modelMst = IrParamModelDao.getParamModelList(irModelId);
			Map<String, Map<String, IrParamModel>> modelMstMap = modelMst.stream().collect(Collectors.groupingBy(IrParamModel::getIrModelId
					                                                                    , TreeMap::new, Collectors.toMap(IrParamModel::getIrCurveId, Function.identity(), (k, v) -> k, TreeMap::new)));
			log.info("IrParamModel: {}", modelMstMap.toString());

			int delNum1 = session.createQuery("delete IrDcntSceStoGnr a where baseYymm=:param1 and a.irModelId like :param2")
			 		 	 		 .setParameter("param1", bssd)
        			             .setParameter("param2", "%"+irModelId+"%")
        			             .executeUpdate();

			log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, COUNT: {}]", Process.toPhysicalName(IrDcntSceStoGnr.class.getSimpleName()), jobLog.getJobId(), irModelId, delNum1);

			int delNum2 = session.createQuery("delete IrParamModelRnd a where baseYymm=:param1 and a.irModelId like :param2")
								 .setParameter("param1", bssd)
								 .setParameter("param2", "%"+irModelId+"%")
								 .executeUpdate();

			log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, COUNT: {}]", Process.toPhysicalName(IrParamModelRnd.class.getSimpleName()), jobLog.getJobId(), irModelId, delNum2);

			int delNum3 = session.createQuery("delete IrQvalSce a where baseYymm=:param1 and a.irModelId like :param2")
					 .setParameter("param1", bssd)
					 .setParameter("param2", "%"+irModelId+"%")
					 .executeUpdate();

			log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, COUNT: {}]", Process.toPhysicalName(IrQvalSce.class.getSimpleName()), jobLog.getJobId(), irModelId, delNum3);


			try {
				for(Map.Entry<String, Map<String, IrParamModel>> modelMap : modelMstMap.entrySet()) {
					for(Map.Entry<String, IrParamModel> model : modelMap.getValue().entrySet()) {

						Double dt = 1.0 / 12;
						List<IrParamModelBiz> cirParamList = IrParamModelDao.getParamModelBizList(bssd, modelMap.getKey(), model.getKey());
						log.info("CIR Param: {}", cirParamList);

						List<IrDcntSceStoGnr> cirSceList = Esg430_BizScenCirForecast.createScenCir(bssd, modelMap.getKey(), model.getKey(), cirParamList, dt, cirPrjYear, model.getValue().getTotalSceNo(), model.getValue().getRndSeed());

						TreeMap<Integer, TreeMap<Integer, Double>> cirSceMap = new TreeMap<Integer, TreeMap<Integer, Double>>();
						cirSceMap = cirSceList.stream().collect(Collectors.groupingBy(s -> Integer.valueOf(s.getMatCd().substring(1))
												               , TreeMap::new, Collectors.toMap(s -> Integer.valueOf(s.getSceNo()), IrDcntSceStoGnr::getFwdRate, (k, v) -> k, TreeMap::new)));

						Esg430_BizScenCirForecast.createQuantileValue(bssd, "IBIZ", modelMap.getKey(), model.getKey(), 1, cirSceMap).forEach(s -> session.save(s));

						int sceCnt = 1;
						for (IrDcntSceStoGnr sce : cirSceList) {
							session.save(sce);

							if (sceCnt % 50 == 0) {
								session.flush();
								session.clear();
							}
							if (sceCnt % logSize == 0) {
								log.info("CIR Interest Rate of [{}] [ID: {}] is processed {}/{} in Job:[{}]", modelMap.getKey(), model.getKey(), sceCnt, cirSceList.size(), jobLog.getJobId());
							}
							sceCnt++;
						}

						if(cirParamList.size() == 0) continue;
						List<IrParamModelRnd> randNumList = Esg430_BizScenCirForecast.createRandCir(bssd, modelMap.getKey(), model.getKey(), cirPrjYear, model.getValue().getTotalSceNo(), model.getValue().getRndSeed());

						int rndCnt = 1;
						for (IrParamModelRnd rnd : randNumList) {
							session.save(rnd);
							if (rndCnt % 50 == 0) {
								session.flush();
								session.clear();
							}
							if (rndCnt % logSize == 0) {
								log.info("CIR Random Number of [{}] [ID: {}] is processed {}/{} in Job:[{}]", modelMap.getKey(), model.getKey(), rndCnt, randNumList.size(), jobLog.getJobId());
							}
							rndCnt++;
						}

						session.flush();
						session.clear();
					}
				}
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}



	private static void job710() {
			if(jobList.contains("710")) {
				session.beginTransaction();
				CoJobInfo jobLog = startJogLog(EJob.ESG710);

				String irModelId       = argInDBMap.getOrDefault("AFNS_MODE"         , "AFNS_IM"    ).trim().toUpperCase();
				int    weekDay         = Integer.valueOf((String) argInDBMap.getOrDefault("AFNS_WEEK_DAY"        , "5"));
				double confInterval    = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_CONF_INTERVAL"   , "0.995"));

				double dt              = 1.0 / 52.0;   //weekly only
				int    kalmanItrMax    = Integer.valueOf((String) argInDBMap.getOrDefault("AFNS_KALMAN_ITR_MAX"  , "100"));
				double sigmaInit       = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_SIGMA_INIT"      , "0.05"));
				double epsilonInit     = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_EPSILON_INIT"    , "0.001"));


				List<IrParamModel> modelMst = IrParamModelDao.getParamModelList(irModelId);
				Map<String, IrParamModel> modelMstMap = modelMst.stream().collect(Collectors.toMap(IrParamModel::getIrCurveId, Function.identity()));
				log.info("IrParamModel: {}", modelMstMap.toString());

				try {
					for(Map.Entry<String, IrCurve> irCrv : irCurveMap.entrySet()) {
						if(!irCurveSwMap.containsKey(irCrv.getKey())) {
							log.warn("No Ir Curve Data [{}] in Smith-Wilson Map for [{}]", irCrv.getKey(), bssd);
							continue;
						}

						if(!modelMstMap.containsKey(irCrv.getKey())) {
							log.warn("No Model Attribute of [{}] for [{}] in [{}] Table", irModelId, irCrv.getKey(), Process.toPhysicalName(IrParamModel.class.getSimpleName()));
							continue;
						}

						log.info("AFNS Shock Spread (Cont) for [{}({}, {})]", irCrv.getKey(), irCrv.getValue().getIrCurveNm(), irCrv.getValue().getCurCd());

						List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), Math.min(StringUtil.objectToPrimitive(irCurveSwMap.get(irCrv.getKey()).getLlp()), 20));
//						List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), StringUtil.objectToPrimitive(irCurveSwMap.get(irCrv.getKey()).getLlp())));

//						
						iRateHisStBaseDate =  IrCurveSpotWeekDao.getIrCurveSpotWeekHisDate(bssd, irCrv.getKey(), weekDay)
																				.stream().limit(afnsHisDateNum)
																				.sorted()
																				.limit(1)
																				.map(s->s.getBaseDate())
																				.findFirst().orElse(iRateHisStBaseDate)
																				;
						
						// TENOR FILTERING
						tenorList.remove("M0003");
						tenorList.remove("M0006");
						tenorList.remove("M0009");
						tenorList.remove("M0018");
						tenorList.remove("M0030");
						tenorList.remove("M0048");
						tenorList.remove("M0072");
						tenorList.remove("M0084");
						tenorList.remove("M0096");
						tenorList.remove("M0108");
						tenorList.remove("M0180");  //FOR CHECK w/ FSS


						log.info("TenorList in [{}]: ID: [{}], llp: [{}], DATE :  {}, matCd: {}", jobLog.getJobId(), irCrv.getKey(), irCurveSwMap.get(irCrv.getKey()).getLlp(), iRateHisStBaseDate, tenorList);
						
						if(tenorList.isEmpty()) {
							log.warn("No Spot Rate Data [ID: {}] for [{}]", irCrv.getKey(), bssd);
							continue;
						}

						int delNum1 = session.createQuery("delete IrParamAfnsCalc a where baseYymm=:param1 and a.irModelId=:param2 and a.irCurveId=:param3")
			                     			 .setParameter("param1", bssd)
			                     			 .setParameter("param2", irModelId)
			                     			 .setParameter("param3", irCrv.getKey())
			                     			 .executeUpdate();

						log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, IR_CURVE_ID: {}, COUNT: {}]", Process.toPhysicalName(IrParamAfnsCalc.class.getSimpleName()), jobLog.getJobId(), irModelId, irCrv.getKey(), delNum1);

						List<IrCurveSpotWeek> weekHisList    = IrCurveSpotWeekDao.getIrCurveSpotWeekHis(bssd, iRateHisStBaseDate, irCrv.getKey(), tenorList, weekDay, false);
						List<IrCurveSpotWeek> weekHisBizList = IrCurveSpotWeekDao.getIrCurveSpotWeekHis(bssd, iRateHisStBaseDate, irCrv.getKey(), tenorList, weekDay, true);
						log.info("weekHisList: [{}], [TOTAL: {}, BIZDAY: {}], [from {} to {}, weekDay:{}]", irCrv.getKey(), weekHisList.size(), weekHisBizList.size(), iRateHisStBaseDate.substring(0,6), bssd, weekDay);

//						weekHisList.stream().filter(s-> s.getMatCd().equals("M0012")).forEach(s-> log.info("weekHis aaa: {},{}", s.toString()));

						//for ensuring enough input size
						if(weekHisList.size() < 1000) {
							log.warn("Weekly SpotRate Data is not Enough [ID: {}, SIZE: {}] for [{}]", irCrv.getKey(), weekHisList.size(), bssd);
							continue;
						}


						List<IrCurveSpot> curveHisList = weekHisList.stream().map(s->s.convertToHis()).collect(toList());
//						curveHisList = curveHisList.stream().filter(s -> Integer.valueOf(s.getBaseDate()) >= 20110701).collect(toList());

						//Any curveBaseList result in same parameters and spreads.
						List<IrCurveSpot> curveBaseList = IrCurveSpotDao.getIrCurveSpot(bssd, irCrv.getKey(), tenorList);
						
//						curveHisList.stream().filter(s->s.getIrCurveId().equals("RF_USD")).forEach( s-> log.info("aaa : {}", s.toString()));
//						curveBaseList.stream().filter(s->s.getIrCurveId().equals("RF_USD")).forEach( s-> log.info("bbb : {}", s.toString()));


						if(curveBaseList.size()==0) {
							log.warn("No IR Curve Data [IR_CURVE_ID: {}] for [{}]", irCrv.getKey(), bssd);
							continue;
						}

						double errorTolerance = StringUtil.objectToPrimitive(modelMstMap.get(irCrv.getKey()).getItrTol(), 1E-8);

						Map<String, List<IrParamAfnsCalc>> resultMap = new TreeMap<String, List<IrParamAfnsCalc>>();
						resultMap = Esg710_SetAfnsInitParam.setAfnsInitParam(FinUtils.toEndOfMonth(bssd), irModelId, curveHisList, curveBaseList, tenorList, dt, sigmaInit
														                          , irCurveSwMap.get(irCrv.getKey()).getLtfr()
															                      , irCurveSwMap.get(irCrv.getKey()).getLtfrCp()
															                      , projectionYear
															                      , errorTolerance
															                      , kalmanItrMax
															                      , confInterval
															                      , epsilonInit);


						for(Map.Entry<String, List<IrParamAfnsCalc>> rslt : resultMap.entrySet()) {
	//						if(!rslt.getKey().equals("CURVE")) rslt.getValue().forEach(s -> log.info("{}, {}", s.toString()));
							rslt.getValue().forEach(s -> session.save(s));

							session.flush();
							session.clear();
						}
					}
					completeJob("SUCCESS", jobLog);

				} catch (Exception e) {
					log.error("ERROR: {}", e);
					completeJob("ERROR", jobLog);
				}
				session.saveOrUpdate(jobLog);
				session.getTransaction().commit();
			}
		}
	
	private static void job711() {
		if(jobList.contains("711")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG710);

			String irModelId       = argInDBMap.getOrDefault("AFNS_MODE"         , "AFNS_IM"    ).trim().toUpperCase();
			int    weekDay         = Integer.valueOf((String) argInDBMap.getOrDefault("AFNS_WEEK_DAY"        , "5"));
			double confInterval    = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_CONF_INTERVAL"   , "0.995"));

			double dt              = 1.0 / 52.0;   //weekly only
			int    kalmanItrMax    = Integer.valueOf((String) argInDBMap.getOrDefault("AFNS_KALMAN_ITR_MAX"  , "100"));
			double sigmaInit       = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_SIGMA_INIT"      , "0.05"));
			double epsilonInit     = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_EPSILON_INIT"    , "0.001"));


			List<IrParamModel> modelMst = IrParamModelDao.getParamModelList(irModelId);
			Map<String, IrParamModel> modelMstMap = modelMst.stream().collect(Collectors.toMap(IrParamModel::getIrCurveId, Function.identity()));
			log.info("IrParamModel: {}", modelMstMap.toString());

			try {
				for(Map.Entry<String, IrCurve> irCrv : irCurveMap.entrySet()) {
					if(!irCurveSwMap.containsKey(irCrv.getKey())) {
						log.warn("No Ir Curve Data [{}] in Smith-Wilson Map for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					if(!modelMstMap.containsKey(irCrv.getKey())) {
						log.warn("No Model Attribute of [{}] for [{}] in [{}] Table", irModelId, irCrv.getKey(), Process.toPhysicalName(IrParamModel.class.getSimpleName()));
						continue;
					}

					log.info("AFNS Shock Spread (Cont) for [{}({}, {})]", irCrv.getKey(), irCrv.getValue().getIrCurveNm(), irCrv.getValue().getCurCd());

					List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), Math.min(StringUtil.objectToPrimitive(irCurveSwMap.get(irCrv.getKey()).getLlp()), 20));

					// TENOR FILTERING
					tenorList.remove("M0003");
					tenorList.remove("M0006");
					tenorList.remove("M0009");
					tenorList.remove("M0018");
					tenorList.remove("M0030");
					tenorList.remove("M0048");
					tenorList.remove("M0072");
					tenorList.remove("M0084");
					tenorList.remove("M0096");
					tenorList.remove("M0108");
					tenorList.remove("M0180");  //FOR CHECK w/ FSS


					log.info("TenorList in [{}]: ID: [{}], llp: [{}], matCd: {}", jobLog.getJobId(), irCrv.getKey(), irCurveSwMap.get(irCrv.getKey()).getLlp(), tenorList);
					
					if(tenorList.isEmpty()) {
						log.warn("No Spot Rate Data [ID: {}] for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					int delNum1 = session.createQuery("delete IrParamAfnsCalc a where baseYymm=:param1 and a.irModelId=:param2 and a.irCurveId=:param3")
		                     			 .setParameter("param1", bssd)
		                     			 .setParameter("param2", irModelId)
		                     			 .setParameter("param3", irCrv.getKey())
		                     			 .executeUpdate();

					log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, IR_CURVE_ID: {}, COUNT: {}]", Process.toPhysicalName(IrParamAfnsCalc.class.getSimpleName()), jobLog.getJobId(), irModelId, irCrv.getKey(), delNum1);

					List<IrCurveSpotWeek> weekHisList    = IrCurveSpotWeekDao.getIrCurveSpotWeekHis(bssd, iRateHisStBaseDate, irCrv.getKey(), tenorList, weekDay, false);
					List<IrCurveSpotWeek> weekHisBizList = IrCurveSpotWeekDao.getIrCurveSpotWeekHis(bssd, iRateHisStBaseDate, irCrv.getKey(), tenorList, weekDay, true);
					log.info("weekHisList: [{}], [TOTAL: {}, BIZDAY: {}], [from {} to {}, weekDay:{}]", irCrv.getKey(), weekHisList.size(), weekHisBizList.size(), iRateHisStBaseDate.substring(0,6), bssd, weekDay);

//					weekHisList.stream().filter(s-> s.getMatCd().equals("M0012")).forEach(s-> log.info("weekHis aaa: {},{}", s.toString()));

					//for ensuring enough input size
					if(weekHisList.size() < 1000) {
						log.warn("Weekly SpotRate Data is not Enough [ID: {}, SIZE: {}] for [{}]", irCrv.getKey(), weekHisList.size(), bssd);
						continue;
					}

//					TODO?????/
					List<IrCurveSpot> curveHisList = weekHisList.stream().map(s->s.convertToPercentHis()).collect(toList());
//					curveHisList = curveHisList.stream().filter(s -> Integer.valueOf(s.getBaseDate()) >= 20110701).collect(toList());

//					TODO?????/						
					//Any curveBaseList result in same parameters and spreads.
					List<IrCurveSpot> curveBaseList = IrCurveSpotDao.getIrCurveSpot(bssd, irCrv.getKey(), tenorList)
											.stream().map(s->s.getPercentSpot()).collect(toList());

					if(curveBaseList.size()==0) {
						log.warn("No IR Curve Data [IR_CURVE_ID: {}] for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					double errorTolerance = StringUtil.objectToPrimitive(modelMstMap.get(irCrv.getKey()).getItrTol(), 1E-8);

					Map<String, List<IrParamAfnsCalc>> resultMap = new TreeMap<String, List<IrParamAfnsCalc>>();
					resultMap = Esg710_SetAfnsInitParam.setAfnsInitParam(FinUtils.toEndOfMonth(bssd), irModelId, curveHisList, curveBaseList, tenorList, dt, sigmaInit
													                          , irCurveSwMap.get(irCrv.getKey()).getLtfr()
														                      , irCurveSwMap.get(irCrv.getKey()).getLtfrCp()
														                      , projectionYear
														                      , errorTolerance
														                      , kalmanItrMax
														                      , confInterval
														                      , epsilonInit);


					for(Map.Entry<String, List<IrParamAfnsCalc>> rslt : resultMap.entrySet()) {
//						if(!rslt.getKey().equals("CURVE")) rslt.getValue().forEach(s -> log.info("{}, {}", s.toString()));
						rslt.getValue().forEach(s -> session.save(s));

						session.flush();
						session.clear();
					}
				}
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}

	private static void job720() {
			if(jobList.contains("720")) {
				session.beginTransaction();
				CoJobInfo jobLog = startJogLog(EJob.ESG720);

				String irModelId       = argInDBMap.getOrDefault("AFNS_MODE"         , "AFNS_IM"    ).trim().toUpperCase();
				int    weekDay         = Integer.valueOf((String) argInDBMap.getOrDefault("AFNS_WEEK_DAY"        , "5"));
				double confInterval    = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_CONF_INTERVAL"   , "0.995"));

				double dt              = 1.0 / 52.0;   //weekly only
				int    kalmanItrMax    = Integer.valueOf((String) argInDBMap.getOrDefault("AFNS_KALMAN_ITR_MAX"  , "100"));
				double sigmaInit       = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_SIGMA_INIT"      , "0.05"));
				double epsilonInit     = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_EPSILON_INIT"    , "0.001"));
//				double epsilonInit     = 1.0;


				List<IrParamModel> modelMst = IrParamModelDao.getParamModelList(irModelId);
				Map<String, IrParamModel> modelMstMap = modelMst.stream().collect(Collectors.toMap(IrParamModel::getIrCurveId, Function.identity()));
				log.info("IrParamModel: {}", modelMstMap.toString());

				try {
					for(Map.Entry<String, IrCurve> irCrv : irCurveMap.entrySet()) {
						if(!irCurveSwMap.containsKey(irCrv.getKey())) {
							log.warn("No Ir Curve Data [{}] in Smith-Wilson Map for [{}]", irCrv.getKey(), bssd);
							continue;
						}

						if(!modelMstMap.containsKey(irCrv.getKey())) {
							log.warn("No Model Attribute of [{}] for [{}] in [{}] Table", irModelId, irCrv.getKey(), Process.toPhysicalName(IrParamModel.class.getSimpleName()));
							continue;
						}

						log.info("AFNS Shock Spread (Cont) for [{}({}, {})]", irCrv.getKey(), irCrv.getValue().getIrCurveNm(), irCrv.getValue().getCurCd());

						List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), Math.min(StringUtil.objectToPrimitive(irCurveSwMap.get(irCrv.getKey()).getLlp()), 20));

						tenorList.remove("M0003");
						tenorList.remove("M0006");
						tenorList.remove("M0009");
						tenorList.remove("M0018");
						tenorList.remove("M0030");
						tenorList.remove("M0048");
						tenorList.remove("M0072");
						tenorList.remove("M0084");
						tenorList.remove("M0096");
						tenorList.remove("M0108");
						tenorList.remove("M0180");  //FOR CHECK w/ FSS


						log.info("TenorList in [{}]: ID: [{}], llp: [{}], matCd: {}", jobLog.getJobId(), irCrv.getKey(), irCurveSwMap.get(irCrv.getKey()).getLlp(), tenorList);
						if(tenorList.isEmpty()) {
							log.warn("No Spot Rate Data [ID: {}] for [{}]", irCrv.getKey(), bssd);
							continue;
						}

						int delNum1 = session.createQuery("delete IrParamAfnsCalc a where baseYymm=:param1 and a.irModelId=:param2 and a.irCurveId=:param3 and a.lastModifiedBy=:param4 ")
			                     			 .setParameter("param1", bssd)
			                     			 .setParameter("param2", irModelId)
			                     			 .setParameter("param3", irCrv.getKey())
			                     			 .setParameter("param4","ESG720")
			                     			 .executeUpdate();

						log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, IR_CURVE_ID: {}, COUNT: {}]", Process.toPhysicalName(IrParamAfnsCalc.class.getSimpleName()), jobLog.getJobId(), irModelId, irCrv.getKey(), delNum1);

						iRateHisStBaseDate =  IrCurveSpotWeekDao.getIrCurveSpotWeekHisDate(bssd, irCrv.getKey(), weekDay)
																.stream().limit(afnsHisDateNum)
																.sorted()
																.limit(1)
																.map(s->s.getBaseDate())
																.findFirst().orElse(iRateHisStBaseDate)
																;
						

						List<IrCurveSpotWeek> weekHisList    = IrCurveSpotWeekDao.getIrCurveSpotWeekHis(bssd, iRateHisStBaseDate, irCrv.getKey(), tenorList, weekDay, false);
						List<IrCurveSpotWeek> weekHisBizList = IrCurveSpotWeekDao.getIrCurveSpotWeekHis(bssd, iRateHisStBaseDate, irCrv.getKey(), tenorList, weekDay, true);
						log.info("weekHisList: [{}], [TOTAL: {}, BIZDAY: {}], [from {} to {}, weekDay:{}]", irCrv.getKey(), weekHisList.size(), weekHisBizList.size(), iRateHisStBaseDate.substring(0,6), bssd, weekDay);

						//for ensuring enough input size
						if(weekHisList.size() < 1000) {
							log.warn("Weekly SpotRate Data is not Enough [ID: {}, SIZE: {}] for [{}]", irCrv.getKey(), weekHisList.size(), bssd);
							continue;
						}

						List<IrCurveSpot> curveHisList = weekHisList.stream().map(s->s.convertToHis()).collect(toList());
	//					curveHisList = curveHisList.stream().filter(s -> Integer.valueOf(s.getBaseDate()) >= 20110701).collect(toList());

						//Any curveBaseList result in same parameters and spreads.
						List<IrCurveSpot> curveBaseList = IrCurveSpotDao.getIrCurveSpot(bssd, irCrv.getKey(), tenorList);


						if(curveBaseList.size()==0) {
							log.warn("No IR Curve Data [IR_CURVE_ID: {}] for [{}]", irCrv.getKey(), bssd);
							continue;
						}

						// enum�뿉 �젙�쓽�맂 �닚�꽌濡� �젙�젹�빐�꽌 諛쏆쓬 =>  紐⑥닔�뒗 enum�뿉 �젙�쓽�븳 �닚�꽌��濡� �떎瑜� �쓽誘몃�� 媛뽯뒗 媛믪씠湲� �븣臾몄뿉 �닚�꽌媛� 以묒슂�븿.
						List<IrParamAfnsCalc> initParam = IrParamAfnsDao.getIrParamAfnsCalcInitList(bssd, irModelId, irCrv.getKey()).stream()
							                                            .sorted(Comparator.comparingInt(p -> p.getParamTypCd().ordinal()))
							                                            .map(s -> s.getInitParam())
							                                            .collect(Collectors.toList());
						
//						initParam.forEach(s-> log.info("aaa : {}", s.toString()));
						
						double errorTolerance = StringUtil.objectToPrimitive(modelMstMap.get(irCrv.getKey()).getItrTol(), 1E-8);

						Map<String, List<IrParamAfnsCalc>> resultMap = new TreeMap<String, List<IrParamAfnsCalc>>();
						resultMap = Esg720_optAfnsParam.optimizationParas(FinUtils.toEndOfMonth(bssd), irModelId, curveHisList, curveBaseList, tenorList, dt, sigmaInit
														                          , irCurveSwMap.get(irCrv.getKey()).getLtfr()
															                      , irCurveSwMap.get(irCrv.getKey()).getLtfrCp()
															                      , projectionYear
															                      , errorTolerance
															                      , kalmanItrMax
															                      , confInterval
															                      , epsilonInit
															                      , initParam // add
															                      );


						for(Map.Entry<String, List<IrParamAfnsCalc>> rslt : resultMap.entrySet()) {
	//						if(!rslt.getKey().equals("CURVE")) rslt.getValue().forEach(s -> log.info("{}, {}", s.toString()));
							rslt.getValue().forEach(s -> session.save(s));

							session.flush();
							session.clear();
						}
					}
					completeJob("SUCCESS", jobLog);

				} catch (Exception e) {
					log.error("ERROR: {}", e);
					completeJob("ERROR", jobLog);
				}
				session.saveOrUpdate(jobLog);
				session.getTransaction().commit();
			}
		}

	private static void job721() {
		if(jobList.contains("721")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG720);

			String irModelId       = argInDBMap.getOrDefault("AFNS_MODE"         , "AFNS_IM"    ).trim().toUpperCase();
			int    weekDay         = Integer.valueOf((String) argInDBMap.getOrDefault("AFNS_WEEK_DAY"        , "5"));
			double confInterval    = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_CONF_INTERVAL"   , "0.995"));

			double dt              = 1.0 / 52.0;   //weekly only
			int    kalmanItrMax    = Integer.valueOf((String) argInDBMap.getOrDefault("AFNS_KALMAN_ITR_MAX"  , "100"));
			double sigmaInit       = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_SIGMA_INIT"      , "0.05"));
			double epsilonInit     = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_EPSILON_INIT"    , "0.001"));
//			double epsilonInit     = 1.0;


			List<IrParamModel> modelMst = IrParamModelDao.getParamModelList(irModelId);
			Map<String, IrParamModel> modelMstMap = modelMst.stream().collect(Collectors.toMap(IrParamModel::getIrCurveId, Function.identity()));
			log.info("IrParamModel: {}", modelMstMap.toString());

			try {
				for(Map.Entry<String, IrCurve> irCrv : irCurveMap.entrySet()) {
					if(!irCurveSwMap.containsKey(irCrv.getKey())) {
						log.warn("No Ir Curve Data [{}] in Smith-Wilson Map for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					if(!modelMstMap.containsKey(irCrv.getKey())) {
						log.warn("No Model Attribute of [{}] for [{}] in [{}] Table", irModelId, irCrv.getKey(), Process.toPhysicalName(IrParamModel.class.getSimpleName()));
						continue;
					}

					log.info("AFNS Shock Spread (Cont) for [{}({}, {})]", irCrv.getKey(), irCrv.getValue().getIrCurveNm(), irCrv.getValue().getCurCd());

					List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), Math.min(StringUtil.objectToPrimitive(irCurveSwMap.get(irCrv.getKey()).getLlp()), 20));

					tenorList.remove("M0003");
					tenorList.remove("M0006");
					tenorList.remove("M0009");
					tenorList.remove("M0018");
					tenorList.remove("M0030");
					tenorList.remove("M0048");
					tenorList.remove("M0072");
					tenorList.remove("M0084");
					tenorList.remove("M0096");
					tenorList.remove("M0108");
					tenorList.remove("M0180");  //FOR CHECK w/ FSS


					log.info("TenorList in [{}]: ID: [{}], llp: [{}], matCd: {}", jobLog.getJobId(), irCrv.getKey(), irCurveSwMap.get(irCrv.getKey()).getLlp(), tenorList);
					if(tenorList.isEmpty()) {
						log.warn("No Spot Rate Data [ID: {}] for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					int delNum1 = session.createQuery("delete IrParamAfnsCalc a where baseYymm=:param1 and a.irModelId=:param2 and a.irCurveId=:param3 and a.lastModifiedBy=:param4 ")
		                     			 .setParameter("param1", bssd)
		                     			 .setParameter("param2", irModelId)
		                     			 .setParameter("param3", irCrv.getKey())
		                     			 .setParameter("param4","ESG720")
		                     			 .executeUpdate();

					log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, IR_CURVE_ID: {}, COUNT: {}]", Process.toPhysicalName(IrParamAfnsCalc.class.getSimpleName()), jobLog.getJobId(), irModelId, irCrv.getKey(), delNum1);


					List<IrCurveSpotWeek> weekHisList    = IrCurveSpotWeekDao.getIrCurveSpotWeekHis(bssd, iRateHisStBaseDate, irCrv.getKey(), tenorList, weekDay, false);
					List<IrCurveSpotWeek> weekHisBizList = IrCurveSpotWeekDao.getIrCurveSpotWeekHis(bssd, iRateHisStBaseDate, irCrv.getKey(), tenorList, weekDay, true);
					log.info("weekHisList: [{}], [TOTAL: {}, BIZDAY: {}], [from {} to {}, weekDay:{}]", irCrv.getKey(), weekHisList.size(), weekHisBizList.size(), iRateHisStBaseDate.substring(0,6), bssd, weekDay);

					//for ensuring enough input size
					if(weekHisList.size() < 1000) {
						log.warn("Weekly SpotRate Data is not Enough [ID: {}, SIZE: {}] for [{}]", irCrv.getKey(), weekHisList.size(), bssd);
						continue;
					}

					List<IrCurveSpot> curveHisList = weekHisList.stream().map(s->s.convertToPercentHis()).collect(toList());
//					curveHisList = curveHisList.stream().filter(s -> Integer.valueOf(s.getBaseDate()) >= 20110701).collect(toList());

					//Any curveBaseList result in same parameters and spreads.
//					List<IrCurveSpot> curveBaseList = IrCurveSpotDao.getIrCurveSpot(bssd, irCrv.getKey(), tenorList);
//					TODO?????
					List<IrCurveSpot> curveBaseList = IrCurveSpotDao.getIrCurveSpot(bssd, irCrv.getKey(), tenorList)
							.stream().map(s->s.getPercentSpot()).collect(toList());

					if(curveBaseList.size()==0) {
						log.warn("No IR Curve Data [IR_CURVE_ID: {}] for [{}]", irCrv.getKey(), bssd);
						continue;
					}

					// enum�뿉 �젙�쓽�맂 �닚�꽌濡� �젙�젹�빐�꽌 諛쏆쓬 =>  紐⑥닔�뒗 enum�뿉 �젙�쓽�븳 �닚�꽌��濡� �떎瑜� �쓽誘몃�� 媛뽯뒗 媛믪씠湲� �븣臾몄뿉 �닚�꽌媛� 以묒슂�븿.
					List<IrParamAfnsCalc> initParam = IrParamAfnsDao.getIrParamAfnsCalcInitList(bssd, irModelId, irCrv.getKey()).stream()
						                                            .sorted(Comparator.comparingInt(p -> p.getParamTypCd().ordinal()))
						                                            .map(s -> s.getInitParam())
						                                            .collect(Collectors.toList());

					double errorTolerance = StringUtil.objectToPrimitive(modelMstMap.get(irCrv.getKey()).getItrTol(), 1E-8);

					Map<String, List<IrParamAfnsCalc>> resultMap = new TreeMap<String, List<IrParamAfnsCalc>>();
					resultMap = Esg720_optAfnsParam.optimizationParas(FinUtils.toEndOfMonth(bssd), irModelId, curveHisList, curveBaseList, tenorList, dt, sigmaInit
													                          , irCurveSwMap.get(irCrv.getKey()).getLtfr()
														                      , irCurveSwMap.get(irCrv.getKey()).getLtfrCp()
														                      , projectionYear
														                      , errorTolerance
														                      , kalmanItrMax
														                      , confInterval
														                      , epsilonInit
														                      , initParam // add
														                      );


					for(Map.Entry<String, List<IrParamAfnsCalc>> rslt : resultMap.entrySet()) {
//						if(!rslt.getKey().equals("CURVE")) rslt.getValue().forEach(s -> log.info("{}, {}", s.toString()));
						rslt.getValue().forEach(s -> session.save(s));

						session.flush();
						session.clear();
					}
				}
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}
	private static void job730() {
			if(jobList.contains("730")) {
				session.beginTransaction();
				CoJobInfo jobLog = startJogLog(EJob.ESG730);

				String irModelId       = argInDBMap.getOrDefault("AFNS_MODE"         , "AFNS_IM"    ).trim().toUpperCase();
				int    weekDay         = Integer.valueOf((String) argInDBMap.getOrDefault("AFNS_WEEK_DAY"        , "5"));
				double confInterval    = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_CONF_INTERVAL"   , "0.995"));

				double dt              = 1.0 / 52.0;   //weekly only
				int    kalmanItrMax    = Integer.valueOf((String) argInDBMap.getOrDefault("AFNS_KALMAN_ITR_MAX"  , "100"));
				double sigmaInit       = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_SIGMA_INIT"      , "0.05"));
				double epsilonInit     = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_EPSILON_INIT"    , "0.001"));


				List<IrParamModel> modelMst = IrParamModelDao.getParamModelList(irModelId);
				Map<String, IrParamModel> modelMstMap = modelMst.stream().collect(Collectors.toMap(IrParamModel::getIrCurveId, Function.identity()));
				log.info("IrParamModel: {}", modelMstMap.toString());

				try {
					for(Map.Entry<String, IrCurve> irCrv : irCurveMap.entrySet()) {
						if(!irCurveSwMap.containsKey(irCrv.getKey())) {
							log.warn("No Ir Curve Data [{}] in Smith-Wilson Map for [{}]", irCrv.getKey(), bssd);
							continue;
						}

						if(!modelMstMap.containsKey(irCrv.getKey())) {
							log.warn("No Model Attribute of [{}] for [{}] in [{}] Table", irModelId, irCrv.getKey(), Process.toPhysicalName(IrParamModel.class.getSimpleName()));
							continue;
						}

						log.info("AFNS Shock Spread (Cont) for [{}({}, {})]", irCrv.getKey(), irCrv.getValue().getIrCurveNm(), irCrv.getValue().getCurCd());

						List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), StringUtil.objectToPrimitive(irCurveSwMap.get(irCrv.getKey()).getLlp()));
					
//						tenorList.remove("M0003");
//						tenorList.remove("M0006");
//						tenorList.remove("M0009");
//						tenorList.remove("M0018");
//						tenorList.remove("M0030");
//						tenorList.remove("M0048");
//						tenorList.remove("M0072");
//						tenorList.remove("M0084");
//						tenorList.remove("M0096");
//						tenorList.remove("M0108");
//						tenorList.remove("M0180");  //FOR CHECK w/ FSS
						
//						List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), Math.min(StringUtil.objectToPrimitive(irCurveSwMap.get(irCrv.getKey()).getLlp()), 20));
	//					tenorList.remove("M0048"); tenorList.remove("M0084"); tenorList.remove("M0180");  //FOR CHECK w/ FSS
	//					log.info("{}", tenorList);
						//TODO:
	//					tenorList.remove("M0180");

						log.info("TenorList in [{}]: ID: [{}], llp: [{}], matCd: {}", jobLog.getJobId(), irCrv.getKey(), irCurveSwMap.get(irCrv.getKey()).getLlp(), tenorList);
						if(tenorList.isEmpty()) {
							log.warn("No Spot Rate Data [ID: {}] for [{}]", irCrv.getKey(), bssd);
							continue;
						}


						int delNum2 = session.createQuery("delete IrSprdAfnsCalc a where baseYymm=:param1 and a.irModelId=:param2 and a.irCurveId=:param3")
						                     .setParameter("param1", bssd)
									  		 .setParameter("param2", irModelId)
											 .setParameter("param3", irCrv.getKey())
											 .executeUpdate();

						log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, IR_CURVE_ID: {}, COUNT: {}]", Process.toPhysicalName(IrSprdAfnsCalc.class.getSimpleName()), jobLog.getJobId(), irModelId, irCrv.getKey(), delNum2);

						iRateHisStBaseDate =  IrCurveSpotWeekDao.getIrCurveSpotWeekHisDate(bssd, irCrv.getKey(), weekDay)
								.stream().limit(afnsHisDateNum)
								.sorted()
								.limit(1)
								.map(s->s.getBaseDate())
								.findFirst().orElse(iRateHisStBaseDate)
								;
						
						
						List<IrCurveSpotWeek> weekHisList    = IrCurveSpotWeekDao.getIrCurveSpotWeekHis(bssd, iRateHisStBaseDate, irCrv.getKey(), tenorList, weekDay, false);
						List<IrCurveSpotWeek> weekHisBizList = IrCurveSpotWeekDao.getIrCurveSpotWeekHis(bssd, iRateHisStBaseDate, irCrv.getKey(), tenorList, weekDay, true);
						log.info("weekHisList: [{}], [TOTAL: {}, BIZDAY: {}], [from {} to {}, weekDay:{}]", irCrv.getKey(), weekHisList.size(), weekHisBizList.size(), iRateHisStBaseDate.substring(0,6), bssd, weekDay);

						//for ensuring enough input size
						if(weekHisList.size() < 1000) {
							log.warn("Weekly SpotRate Data is not Enough [ID: {}, SIZE: {}] for [{}]", irCrv.getKey(), weekHisList.size(), bssd);
							continue;
						}

						List<IrCurveSpot> curveHisList = weekHisList.stream().map(s->s.convertToHis()).collect(toList());
	//					curveHisList = curveHisList.stream().filter(s -> Integer.valueOf(s.getBaseDate()) >= 20110701).collect(toList());

						//Any curveBaseList result in same parameters and spreads.
						List<IrCurveSpot> curveBaseList = IrCurveSpotDao.getIrCurveSpot(bssd, irCrv.getKey(), tenorList);

						if(curveBaseList.size()==0) {
							log.warn("No IR Curve Data [IR_CURVE_ID: {}] for [{}]", irCrv.getKey(), bssd);
							continue;
						}
						
						
						List<IrParamAfnsCalc> optParam = IrParamAfnsDao.getIrParamAfnsUsrList(bssd, irModelId , irCrv.getKey()).stream()
								.map(s-> s.convertToCalc())
								.sorted(Comparator.comparingInt(p -> p.getParamTypCd().ordinal()))
								.collect(Collectors.toList());
						
						if(optParam.size() ==0) {
							optParam = IrParamAfnsDao.getIrParamAfnsCalcList(bssd, irModelId , irCrv.getKey()).stream()
									.sorted(Comparator.comparingInt(p -> p.getParamTypCd().ordinal()))
									.collect(Collectors.toList());
							
							log.warn("Apply calucalted AFNS Parameter : {}, {}", optParam);
						}

						double errorTolerance = StringUtil.objectToPrimitive(modelMstMap.get(irCrv.getKey()).getItrTol(), 1E-8);

						Map<String, List<IrSprdAfnsCalc>> irShockSenario = new TreeMap<String, List<IrSprdAfnsCalc>>();
						irShockSenario = Esg730_ShkSprdAfns.createAfnsShockScenario(FinUtils.toEndOfMonth(bssd), irModelId, curveHisList, curveBaseList, tenorList, dt, sigmaInit
														                          , irCurveSwMap.get(irCrv.getKey()).getLtfr()
															                      , irCurveSwMap.get(irCrv.getKey()).getLtfrCp()
															                      , projectionYear
															                      , errorTolerance
															                      , kalmanItrMax
															                      , confInterval
															                      , epsilonInit
															                      , optParam // add
															                      );


						for(Map.Entry<String, List<IrSprdAfnsCalc>> rslt : irShockSenario.entrySet()) {
	//						if(!rslt.getKey().equals("CURVE")) rslt.getValue().forEach(s -> log.info("{}, {}", s.toString()));
							rslt.getValue().forEach(s -> session.save(s));

							session.flush();
							session.clear();
						}
					}
					completeJob("SUCCESS", jobLog);

				} catch (Exception e) {
					log.error("ERROR: {}", e);
					completeJob("ERROR", jobLog);
				}
				session.saveOrUpdate(jobLog);
				session.getTransaction().commit();
			}
		}


	private static void job740() {
			if(jobList.contains("740")) {
				session.beginTransaction();
				CoJobInfo jobLog = startJogLog(EJob.ESG740);

				String irModelId       = argInDBMap.getOrDefault("AFNS_MODE"         , "AFNS_STO"    ).trim().toUpperCase();
				String upperirModelId  = argInDBMap.getOrDefault("AFNS_MODE"         , "AFNS_IM"    ).trim().toUpperCase();
				int    weekDay         = Integer.valueOf((String) argInDBMap.getOrDefault("AFNS_WEEK_DAY"        , "5"));
				double confInterval    = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_CONF_INTERVAL"   , "0.995"));

				double dt              = 1.0 / 52.0;   //weekly only
				int    kalmanItrMax    = Integer.valueOf((String) argInDBMap.getOrDefault("AFNS_KALMAN_ITR_MAX"  , "100"));
				double sigmaInit       = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_SIGMA_INIT"      , "0.05"));
				double epsilonInit     = Double. valueOf((String) argInDBMap.getOrDefault("AFNS_EPSILON_INIT"    , "0.001"));


				List<IrParamModel> modelMst = IrParamModelDao.getParamModelList(upperirModelId); // AFNS
				Map<String, IrParamModel> modelMstMap = modelMst.stream().collect(Collectors.toMap(IrParamModel::getIrCurveId, Function.identity()));
				log.info("IrParamModel: {}", modelMstMap.toString());

				try {
					for(Map.Entry<String, IrCurve> irCrv : irCurveMap.entrySet()) {
						if(!irCurveSwMap.containsKey(irCrv.getKey())) {
							log.warn("No Ir Curve Data [{}] in Smith-Wilson Map for [{}]", irCrv.getKey(), bssd);
							continue;
						}

						if(!modelMstMap.containsKey(irCrv.getKey())) {
							log.warn("No Model Attribute of [{}] for [{}] in [{}] Table", irModelId, irCrv.getKey(), Process.toPhysicalName(IrParamModel.class.getSimpleName()));
							continue;
						}

						log.info("AFNS Shock Spread (Cont) for [{}({}, {})]", irCrv.getKey(), irCrv.getValue().getIrCurveNm(), irCrv.getValue().getCurCd());

//						List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), Math.min(StringUtil.objectToPrimitive(irCurveSwMap.get(irCrv.getKey()).getLlp()), 20));
						List<String> tenorList = IrCurveSpotDao.getIrCurveTenorList(bssd, irCrv.getKey(), StringUtil.objectToPrimitive(irCurveSwMap.get(irCrv.getKey()).getLlp()));
						

						log.info("TenorList in [{}]: ID: [{}], llp: [{}], matCd: {}", jobLog.getJobId(), irCrv.getKey(), irCurveSwMap.get(irCrv.getKey()).getLlp(), tenorList);
						if(tenorList.isEmpty()) {
							log.warn("No Spot Rate Data [ID: {}] for [{}]", irCrv.getKey(), bssd);
							continue;
						}


						int delNum2 = session.createQuery("delete IrSprdAfnsCalc a where baseYymm=:param1 and a.irModelId=:param2 and a.irCurveId=:param3")
						                     .setParameter("param1", bssd)
									  		 .setParameter("param2", irModelId) // AFNS_STO
											 .setParameter("param3", irCrv.getKey())
											 .executeUpdate();

						log.info("[{}] has been Deleted in Job:[{}] [IR_MODEL_ID: {}, IR_CURVE_ID: {}, COUNT: {}]", Process.toPhysicalName(IrSprdAfnsCalc.class.getSimpleName()), jobLog.getJobId(), irModelId, irCrv.getKey(), delNum2);

						iRateHisStBaseDate =  IrCurveSpotWeekDao.getIrCurveSpotWeekHisDate(bssd, irCrv.getKey(), weekDay)
								.stream().limit(afnsHisDateNum)
								.sorted()
								.limit(1)
								.map(s->s.getBaseDate())
								.findFirst().orElse(iRateHisStBaseDate)
								;
						
						
						List<IrCurveSpotWeek> weekHisList    = IrCurveSpotWeekDao.getIrCurveSpotWeekHis(bssd, iRateHisStBaseDate, irCrv.getKey(), tenorList, weekDay, false);
						List<IrCurveSpotWeek> weekHisBizList = IrCurveSpotWeekDao.getIrCurveSpotWeekHis(bssd, iRateHisStBaseDate, irCrv.getKey(), tenorList, weekDay, true);
						log.info("weekHisList: [{}], [TOTAL: {}, BIZDAY: {}], [from {} to {}, weekDay:{}]", irCrv.getKey(), weekHisList.size(), weekHisBizList.size(), iRateHisStBaseDate.substring(0,6), bssd, weekDay);

						//for ensuring enough input size
						if(weekHisList.size() < 1000) {
							log.warn("Weekly SpotRate Data is not Enough [ID: {}, SIZE: {}] for [{}]", irCrv.getKey(), weekHisList.size(), bssd);
							continue;
						}

						List<IrCurveSpot> curveHisList = weekHisList.stream().map(s->s.convertToHis()).collect(toList());

						//Any curveBaseList result in same parameters and spreads.
						List<IrCurveSpot> curveBaseList = IrCurveSpotDao.getIrCurveSpot(bssd, irCrv.getKey(), tenorList);

						if(curveBaseList.size()==0) {
							log.warn("No IR Curve Data [IR_CURVE_ID: {}] for [{}]", irCrv.getKey(), bssd);
							continue;
						}

//						List<IrParamAfnsCalc> optParam = IrParamAfnsDao.getIrParamAfnsCalcList(bssd, upperirModelId , irCrv.getKey()).stream()
//								.sorted(Comparator.comparingInt(p -> p.getParamTypCd().ordinal()))
//								.collect(Collectors.toList());
						
						List<IrParamAfnsCalc> optParam = IrParamAfnsDao.getIrParamAfnsUsrList(bssd, upperirModelId , irCrv.getKey()).stream()
								.map(s-> s.convertToCalc())
								.sorted(Comparator.comparingInt(p -> p.getParamTypCd().ordinal()))
								.collect(Collectors.toList());
						
						if(optParam.size() ==0) {
							optParam = IrParamAfnsDao.getIrParamAfnsCalcList(bssd, upperirModelId , irCrv.getKey()).stream()
									.sorted(Comparator.comparingInt(p -> p.getParamTypCd().ordinal()))
									.collect(Collectors.toList());
							
							log.warn("Apply calucalted AFNS Parameter : {}, {}", optParam);
						}

						double errorTolerance = StringUtil.objectToPrimitive(modelMstMap.get(irCrv.getKey()).getItrTol(), 1E-8);

						Map<String, List<IrSprdAfnsCalc>> irShockSenario = new TreeMap<String, List<IrSprdAfnsCalc>>();
						irShockSenario = Esg740_ShkSprdAfnsSto.createAfnsShockScenario
								(
									FinUtils.toEndOfMonth(bssd)
									, irModelId // AFNS_STO
									, curveHisList
									, curveBaseList
									, tenorList
									, dt
									, sigmaInit
		                            , irCurveSwMap.get(irCrv.getKey()).getLtfr()
			                        , irCurveSwMap.get(irCrv.getKey()).getLtfrCp()
			                        , projectionYear
			                        , errorTolerance
			                        , kalmanItrMax
			                        , confInterval
			                        , epsilonInit
			                        , optParam // add
			                      );


						for(Map.Entry<String, List<IrSprdAfnsCalc>> rslt : irShockSenario.entrySet()) {
	//						if(!rslt.getKey().equals("CURVE")) rslt.getValue().forEach(s -> log.info("{}, {}", s.toString()));
							rslt.getValue().forEach(s -> session.save(s));

							session.flush();
							session.clear();
						}
					}
					completeJob("SUCCESS", jobLog);

				} catch (Exception e) {
					log.error("ERROR: {}", e);
					completeJob("ERROR", jobLog);
				}
				session.saveOrUpdate(jobLog);
				session.getTransaction().commit();
			}
		}

	private static void job760() {
		if(jobList.contains("760")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG760);
			kicsSwMap.entrySet().forEach(s-> log.info("aaa: {},{}", s.getKey(), s.getValue()));
			
			try {
				int delNum = session.createQuery("delete IrDcntRateBuIm a where a.baseYymm=:param").setParameter("param", bssd).executeUpdate();
				log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", Process.toPhysicalName(IrDcntRateBuIm.class.getSimpleName()), jobLog.getJobId(), bssd, delNum);

				List<IrDcntRateBuIm> imDetDcntRateBu = Esg760_IrDcntRateBu.setIrDcntRateBu(bssd, "AFNS_IM", "KICS_L", kicsSwMap);
				imDetDcntRateBu.stream().forEach(s -> session.save(s));
				session.flush();


				List<IrDcntRateBuIm> imDetDcntRateBuA = Esg760_IrDcntRateBu.setIrDcntRateBu(bssd, "AFNS_IM", "KICS_A", kicsSwMap);
				imDetDcntRateBuA.stream().forEach(s -> session.save(s));
				session.flush();

				List<IrDcntRateBuIm> imStoDcntRateBu = Esg760_IrDcntRateBu.setIrDcntRateBu(bssd, "AFNS_STO", "KICS_L", kicsSwMap);
				imStoDcntRateBu.stream().forEach(s -> session.save(s));
//				imStoDcntRateBu.stream().forEach(s -> log.info("zzzz : {}", s.toString()));
				session.flush();

				List<IrDcntRateBuIm> imStoDcntRateBuA = Esg760_IrDcntRateBu.setIrDcntRateBu(bssd, "AFNS_STO", "KICS_A", kicsSwMap);
				imStoDcntRateBuA.stream().forEach(s -> session.save(s));
//				imStoDcntRateBu.stream().forEach(s -> log.info("zzzz : {}", s.toString()));

				session.flush();
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}
	private static void job770() {
			if(jobList.contains("770")) {
				session.beginTransaction();
				CoJobInfo jobLog = startJogLog(EJob.ESG770);

				try {
					int delNum = session.createQuery("delete IrDcntSceIm a where a.baseYymm=:param").setParameter("param", bssd).executeUpdate();
					log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", Process.toPhysicalName(IrDcntSceIm.class.getSimpleName()), jobLog.getJobId(), bssd, delNum);


					List<IrDcntSceIm> imDetDcntSceDet = Esg770_ShkScen.createAfnsShockScenario(bssd, "AFNS_IM", "KICS_L", kicsSwMap, projectionYear);
					imDetDcntSceDet.stream().forEach(s -> save(s));
//					imDetDcntSceDet.stream().filter(s-> s.getIrCurveId().equals("1010000")).forEach(s -> session.save(s));
//					imDetDcntSceDet.stream().forEach(s ->  log.info("zzzz : {} ", s.toString()));
					session.flush();

					List<IrDcntSceIm> imDetDcntSceDetA = Esg770_ShkScen.createAfnsShockScenario(bssd, "AFNS_IM", "KICS_A", kicsSwMap, projectionYear);
					imDetDcntSceDetA.stream().forEach(s -> save(s));
					session.flush();


//					Map<String, Map<Integer, IrParamSw>> tempMap = new TreeMap<String, Map<Integer, IrParamSw>>();
//					tempMap.put("1010000", kicsSwMap.get("1010000"));
					
					List<IrDcntSceIm> imDetDcntSceSto = Esg770_ShkScen.createAfnsShockScenario(bssd, "AFNS_STO", "KICS_L", kicsSwMap, projectionYear);
					imDetDcntSceSto.stream().forEach(s -> save(s));
					imDetDcntSceSto.stream().filter(s-> s.getSpotRate().isNaN() && s.getMatCd().equals("M1086")).forEach(s -> log.info("zzzz : {} ", s.toString()));
					session.flush();


					List<IrDcntSceIm> imDetDcntSceStoA = Esg770_ShkScen.createAfnsShockScenario(bssd, "AFNS_STO", "KICS_A", kicsSwMap, projectionYear);
					imDetDcntSceStoA.stream().forEach(s -> save(s));
					imDetDcntSceStoA.stream().filter(s-> s.getSpotRate().isNaN() && s.getMatCd().equals("M1086")).forEach(s -> log.info("zzzz : {} ", s.toString()));
					session.flush();

					session.clear();
					completeJob("SUCCESS", jobLog);

				} catch (Exception e) {
					log.error("ERROR: {}", e);
					completeJob("ERROR", jobLog);
				}
				session.saveOrUpdate(jobLog);
				session.getTransaction().commit();
			}
		}

	private static void job810() {
		if(jobList.contains("810")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG810);

			try {
				int delNum = session.createQuery("delete RcCorpTm a where a.baseYymm = :param").setParameter("param", bssd).executeUpdate();
				log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", Process.toPhysicalName(RcCorpTm.class.getSimpleName()), jobLog.getJobId(), bssd, delNum);

				List<String> agencyCd = RcCorpPdDao.getAgencyCdUsr(bssd);
				log.info("Credit Rating Agency: {}", agencyCd);

				for(String agency : agencyCd) {
					List<RcCorpTm> rcCorpTmList = Esg810_SetTransitionMatrix.createCorpTmFromUsr(bssd, agency);
					rcCorpTmList.stream().forEach(s -> session.save(s));
				}

				session.flush();
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job820() {
		if(jobList.contains("820")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG820);

			try {
				int delNum1 = session.createQuery("delete RcCorpPd a where a.baseYymm = :param").setParameter("param", bssd).executeUpdate();
				log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", Process.toPhysicalName(RcCorpPd.class.getSimpleName()), jobLog.getJobId(), bssd, delNum1);

				int delNum2 = session.createQuery("delete RcCorpPdBiz a where a.baseYymm = :param").setParameter("param", bssd).executeUpdate();
				log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", Process.toPhysicalName(RcCorpPdBiz.class.getSimpleName()), jobLog.getJobId(), bssd, delNum2);

				List<String> agencyCd = RcCorpPdDao.getAgencyCd(bssd);
				log.info("Credit Rating Agency: {}", agencyCd);

				for(String agency : agencyCd) {
					List<RcCorpPd> rcCorpPdList = Esg820_RcCorpPd.createRcCorpPd(bssd, agency, projectionYear);
					rcCorpPdList.stream().forEach(s -> session.save(s));

					if(!agency.equals("NICE")) continue;

					List<RcCorpPdBiz> rcCorpPdBizKicsList = Esg820_RcCorpPd.createRcCorpPdBiz(bssd, "KICS", agency, rcCorpPdList);
					rcCorpPdBizKicsList.stream().forEach(s -> session.save(s));

					List<RcCorpPdBiz> rcCorpPdBizIfrsList = Esg820_RcCorpPd.createRcCorpPdBiz(bssd, "IFRS", agency, rcCorpPdList);
					rcCorpPdBizIfrsList.stream().forEach(s -> session.save(s));
				}

				session.flush();
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}

	private static void job830() {
		if(jobList.contains("830")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG830);

			try {
				int delNum2 = session.createQuery("delete RcSegLgdBiz a where a.baseYymm = :param").setParameter("param", bssd).executeUpdate();
				log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", Process.toPhysicalName(RcSegLgdBiz.class.getSimpleName()), jobLog.getJobId(), bssd, delNum2);

				RcDao.getRcSegLgdUsr(bssd).stream().map(s -> s.convertToBiz(bssd, "KICS")).forEach(s-> session.saveOrUpdate(s));
//				RcDao.getRcSegLgdUsr(bssd).stream().map(s -> s.convertToBiz(bssd, "IFRS")).forEach(s-> session.saveOrUpdate(s));

				session.flush();
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}

	private static void job840() {
		if(jobList.contains("840")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG840);

			try {
				int delNum2 = session.createQuery("delete RcSegPrepRateBiz a where a.baseYymm = :param").setParameter("param", bssd).executeUpdate();
				log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", Process.toPhysicalName(RcSegPrepRateBiz.class.getSimpleName()), jobLog.getJobId(), bssd, delNum2);

				RcDao.getRcSegPrepRateUsr(bssd).stream().map(s -> s.convertToBiz(bssd, "KICS")).forEach(s-> session.saveOrUpdate(s));
//				RcDao.getRcSegPrepRateUsr(bssd).stream().map(s -> s.convertToBiz(bssd, "IFRS")).forEach(s-> session.saveOrUpdate(s));

				session.flush();
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}
	
	private static void job850() {
		if(jobList.contains("850")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG850);

			try {
				int delNum2 = session.createQuery("delete RcInflationBiz a where a.baseYymm = :param").setParameter("param", bssd).executeUpdate();
				log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", Process.toPhysicalName(RcInflationBiz.class.getSimpleName()), jobLog.getJobId(), bssd, delNum2);

				RcDao.getRcInflationUsr(bssd).stream().limit(1).map(s -> s.convertToBiz(bssd, "KICS")).forEach(s-> session.saveOrUpdate(s));
				RcDao.getRcInflationUsr(bssd).stream().limit(1).map(s -> s.convertToBiz(bssd, "IFRS")).forEach(s-> session.saveOrUpdate(s));


				session.flush();
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}
	
	private static void job860() {
		if(jobList.contains("860")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG860);

			try {
				int delNum2 = session.createQuery("delete IrDiscRate a where a.baseYymm = :param").setParameter("param", bssd).executeUpdate();
				log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", Process.toPhysicalName(IrDiscRate.class.getSimpleName()), jobLog.getJobId(), bssd, delNum2);

				RcDao.getIrDiscRateUsr(bssd).stream().map(s -> s.convertToDiscRate()).forEach(s-> session.saveOrUpdate(s));

				session.flush();
				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}
	
	private static void job870() {
		if(jobList.contains("870")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG870);

			try {
//				int delNum2 = session.createQuery("delete RcInflationBiz a where a.baseYymm = :param").setParameter("param", bssd).executeUpdate();
//				log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", Process.toPhysicalName(RcInflationBiz.class.getSimpleName()), jobLog.getJobId(), bssd, delNum2);
//
//				RcDao.getRcInflationUsr(bssd).stream().limit(1).map(s -> s.convertToBiz(bssd, "KICS")).forEach(s-> session.saveOrUpdate(s));
//				RcDao.getRcInflationUsr(bssd).stream().limit(1).map(s -> s.convertToBiz(bssd, "IFRS")).forEach(s-> session.saveOrUpdate(s));
//
//
//				session.flush();
//				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}
	
	
	
	private static void job901() {

			if(jobList.contains("901")) {
				session.beginTransaction();
				CoJobInfo jobLog = startJogLog(EJob.ESG901);
				int seqArray[] = new int[10];
				int seq;
				try {
					seqArray[0] = HisDao.getSeqByBaseDate("HIrCurveYtm", bssd);
					seqArray[1] = HisDao.getSeqByBaseDate("HFxRateHis", bssd);
					seqArray[2] = HisDao.getSeqByBaseDate("HIrCurveSpot", bssd);
					seqArray[3] = HisDao.getSeqByBaseDate("HIrCurveSpotWeek", bssd);
					seqArray[4] = HisDao.getSeqByBaseDate("HIrCurveFwd", bssd);

					seq = Arrays.stream(seqArray).max().getAsInt() +1 ;
					log.info("Max Seq in Job 901 : {},{},{},{}", bssd,  seq);

					FxRateDao.getFxRateHis(bssd).stream().map(s -> HFxRateHis.convertFrom(s, bssd, seq)).forEach(s -> save(s));
					session.flush();

					IrCurveDao.getIrCurveFwdList(bssd).stream().map(s -> HIrCurveFwd.convertFrom(s, bssd, seq)).forEach(s -> save(s));
					session.flush();

					IrCurveSpotDao.getIrCurveSpot(bssd).stream().map(s -> HIrCurveSpot.convertFrom(s, bssd, seq)).forEach(s -> save(s));
					session.flush();

					IrCurveSpotWeekDao.getIrCurveSpotWeek(bssd).stream().map(s -> HIrCurveSpotWeek.convertFrom(s, bssd, seq)).forEach(s -> save(s));
					session.flush();

					IrCurveYtmDao.getIrCurveYtm(bssd).stream().map(s -> HIrCurveYtm.convertFrom(s, bssd, seq)).forEach(s -> save(s));
					session.flush();


					session.clear();


					completeJob("SUCCESS", jobLog);

				} catch (Exception e) {
					log.error("ERROR: {}", e);
					completeJob("ERROR", jobLog);
				}
				session.saveOrUpdate(jobLog);
				session.getTransaction().commit();
			}
		}


	private static void job902() {
		if(jobList.contains("902")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG902);
			int seqArray[] = new int[13];

			try {
				seqArray[0] = HisDao.getSeq("HIrVolSwpn", bssd);
				seqArray[1] = HisDao.getSeq("HRcCorpTm", bssd);
				seqArray[2] = HisDao.getSeq("HRcCorpPd", bssd);
				seqArray[3] = HisDao.getSeq("HRcCorpPdBiz", bssd);
				seqArray[4] = HisDao.getSeq("HIrSprdLp", bssd);
				seqArray[5] = HisDao.getSeq("HIrSprdLpBiz", bssd);
				seqArray[6] = HisDao.getSeq("HIrSprdCrd", bssd);
				seqArray[7] = HisDao.getSeq("HIrSprdCurve", bssd);
				seqArray[8] = HisDao.getSeq("HIrSprdAfnsCalc", bssd);
				seqArray[9] = HisDao.getSeq("HIrSprdAfnsBiz", bssd);

				seqArray[10] = HisDao.getSeq("HRcInflationBiz", bssd);
				seqArray[11] = HisDao.getSeq("HRcSegLgdBiz", bssd);
				seqArray[12] = HisDao.getSeq("HRcSegPrepRateBiz", bssd);

				int seq = Arrays.stream(seqArray).max().getAsInt() +1 ;
				log.info("Max Seq in Job 902 : {},{},{},{}", bssd,  seq);

				IrVolSwpnDao.getSwpnVol(bssd).stream().map(s-> HIrVolSwpn.convertFrom(s, bssd, seq)).forEach(s -> save(s));
				session.flush();

				IrSprdDao.getIrSprdLpList(bssd).stream().map(s-> HIrSprdLp.convertFrom(s, bssd, seq)).forEach(s -> save(s));
				session.flush();

				IrSprdDao.getIrSprdLpBizList(bssd).stream().map(s-> HIrSprdLpBiz.convertFrom(s, bssd, seq)).forEach(s -> save(s));
				session.flush();

				IrSprdDao.getIrSprdCrdList(bssd).stream().map(s-> HIrSprdCrd.convertFrom(s, bssd, seq)).forEach(s -> save(s));
				session.flush();

				IrSprdDao.getIrSprdCurveList(bssd).stream().map(s-> HIrSprdCurve.convertFrom(s, bssd, seq)).forEach(s -> save(s));
				session.flush();

				IrSprdDao.getIrSprdAfnsCalcList(bssd).stream().map(s-> HIrSprdAfnsCalc.convertFrom(s, bssd, seq)).forEach(s -> save(s));
				session.flush();

				IrSprdDao.getIrSprdAfnsBizList(bssd).stream().map(s-> HIrSprdAfnsBiz.convertFrom(s, bssd, seq)).forEach(s -> session.save(s));
				session.flush();

				RcCorpPdDao.getRcCorpTm(bssd).stream().map(s-> HRcCorpTm.convertFrom(s, bssd, seq)).forEach(s -> session.save(s));
				session.flush();

				RcCorpPdDao.getRcCorpPd(bssd).stream().map(s-> HRcCorpPd.convertFrom(s, bssd, seq)).forEach(s -> session.save(s));
				session.flush();

				RcCorpPdDao.getRcCorpPdBiz(bssd).stream().map(s-> HRcCorpPdBiz.convertFrom(s, bssd, seq)).forEach(s -> session.save(s));
				session.flush();

				RcCorpPdDao.getRcInflationBiz(bssd).stream().map(s-> HRcInflationBiz.convertFrom(s, bssd, seq)).forEach(s -> session.save(s));
				session.flush();

				RcCorpPdDao.getRcSegLgdBiz(bssd).stream().map(s-> HRcSegLgdBiz.convertFrom(s, bssd, seq)).forEach(s -> session.save(s));
				session.flush();

				RcCorpPdDao.getRcSegPrepRateBiz(bssd).stream().map(s-> HRcSegPrepRateBiz.convertFrom(s, bssd, seq)).forEach(s -> session.save(s));
				session.flush();

				session.clear();
				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}

	private static void job903() {

		if(jobList.contains("903")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG903);
			int seqArray[] = new int[10];
			int seq;
			try {
				seqArray[0] = HisDao.getSeq("HIrParamSw", bssd);
				seqArray[1] = HisDao.getSeq("HIrParamModel", bssd);
				seqArray[2] = HisDao.getSeq("HIrParamModelCalc", bssd);
				seqArray[3] = HisDao.getSeq("HIrParamModelBiz", bssd);
				seqArray[4] = HisDao.getSeq("HIrParamHwCalc", bssd);
				seqArray[5] = HisDao.getSeq("HIrParamHwBiz", bssd);
				seqArray[6] = HisDao.getSeq("HIrParamAfnsCalc", bssd);
				seqArray[7] = HisDao.getSeq("HIrParamAfnsBiz", bssd);

				seq = Arrays.stream(seqArray).max().getAsInt() +1 ;
				log.info("Max Seq in Job 903 : {},{},{},{}", bssd,  seq);

				IrParamSwDao.getIrParamSwList(bssd).stream().map(s -> HIrParamSw.convertFrom(s, bssd, seq)).forEach(s -> save(s));
				IrParamModelDao.getParamModelList().stream().map(s -> HIrParamModel.convertFrom(s, bssd, seq)).forEach(s -> save(s));
				IrParamModelDao.getIrParamModelCalcList(bssd).stream().map(s -> HIrParamModelCalc.convertFrom(s, bssd, seq)).forEach(s -> save(s));
				IrParamModelDao.getParamModelBizList(bssd).stream().map(s -> HIrParamModelBiz.convertFrom(s, bssd, seq)).forEach(s -> save(s));
				session.flush();

				IrParamHwDao.getIrParamHwCalcList(bssd).stream().map(s -> HIrParamHwCalc.convertFrom(s, bssd, seq)).forEach(s -> save(s));
				IrParamHwDao.getIrParamHwBizList(bssd).stream().map(s -> HIrParamHwBiz.convertFrom(s, bssd, seq)).forEach(s -> save(s));
				IrParamHwDao.getIrParamAfnsCalcList(bssd).stream().map(s -> HIrParamAfnsCalc.convertFrom(s, bssd, seq)).forEach(s -> save(s));
				IrParamHwDao.getIrParamAfnsBizList(bssd).stream().map(s -> HIrParamAfnsBiz.convertFrom(s, bssd, seq)).forEach(s -> save(s));
				session.flush();

				session.clear();


				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}
	private static void job904() {

		if(jobList.contains("904")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG904);
			int seqArray[] = new int[10];
			int seq;
			cnt =1;
			try {
				seqArray[0] = HisDao.getSeq("HIrDcntRateBu", bssd);
				seqArray[1] = HisDao.getSeq("HIrDcntRate", bssd);
				seqArray[2] = HisDao.getSeq("HIrDcntRateBiz", bssd);

				seqArray[3] = HisDao.getSeq("HIrQvalSce", bssd);
				seqArray[4] = HisDao.getSeq("HIrValidParamHw", bssd);
				seqArray[5] = HisDao.getSeq("HIrValidRnd", bssd);
				seqArray[6] = HisDao.getSeq("HIrValidSceSto", bssd);


				seq = Arrays.stream(seqArray).max().getAsInt() +1 ;
				log.info("Max Seq in Job 904 : {},{},{},{}", bssd,  seq);

				IrDcntRateDao.getIrDcntRateBuList(bssd).stream().map(s -> s.convertToHis(bssd, seq)).forEach(s -> saveOrUpdate(s));
				log.info("aaa : {},{},{},{}", "IrDcntRateBu", bssd, seq, cnt);

				IrDcntRateDao.getIrDcntRateList(bssd).stream().map(s -> s.convertToHis(bssd, seq)).forEach(s -> save(s));
				log.info("aaa : {},{},{},{}", "IrDcntRate", bssd, seq, cnt);

				IrDcntRateDao.getIrDcntRateBizList(bssd).stream().map(s -> s.convertToHis(bssd, seq)).forEach(s -> save(s));
				log.info("aaa : {},{},{},{}", "IrDcntRateBiz", bssd, seq, cnt);

				session.flush();

				IrValidDao.getIrQvalSceStream(bssd).map(s -> s.convertToHis(bssd, seq)).forEach(s -> save(s));
				log.info("aaa : {},{},{},{}", "HIrQvalSce", bssd, seq, cnt);



				IrValidDao.getIrValidParamHwStream(bssd).map(s -> s.convertToHis(bssd, seq)).forEach(s -> save(s));
				log.info("aaa : {},{},{},{}", "HIrValidParamHw", bssd, seq, cnt);

				IrValidDao.getIrValidRndStream(bssd).map(s -> s.convertToHis(bssd, seq)).forEach(s -> save(s));
				log.info("aaa : {},{},{},{}", "HIrValidRnd", bssd, seq, cnt);

				IrValidDao.getIrValidSceStoStream(bssd).map(s -> s.convertToHis(bssd, seq)).forEach(s -> save(s));
				log.info("aaa : {},{},{},{}", "HIrValidSceSto", bssd, seq, cnt);

				session.flush();
				session.clear();


				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}

	private static void job905() {
			if(jobList.contains("905")) {
				session.beginTransaction();
				CoJobInfo jobLog = startJogLog(EJob.ESG905);
				int seqArray[] = new int[10];
				int seq;
				cnt =1;
				try {
					seqArray[0] = HisDao.getSeq("HIrDcntSceStoBiz", bssd);
					seqArray[1] = HisDao.getSeq("HIrDcntSceDetBiz", bssd);

					seq = Arrays.stream(seqArray).max().getAsInt() +1 ;
					log.info("Max Seq in Job 905 : {},{},{},{}", bssd,  seq);

					IrCurveSceDao.getIrDcntSceDetBizStream(bssd).map(s -> HIrDcntSceDetBiz.convertFrom(s, bssd, seq)).forEach(s -> save(s));
					session.flush();

					log.info("-----------------------------------------");



					IrCurveSceDao.getIrDcntSceStoBizStream(session, bssd).map(s -> HIrDcntSceStoBiz.convertFrom(s, bssd, seq)).forEach(s -> save(s));
//					IrCurveSceDao.getIrDcntSceStoBizStream().filter(s-> s.getBaseYymm().equals(bssd))
//								.map(s -> HIrDcntSceStoBiz.convertFrom(s, bssd, seq)).forEach(s -> save(s));
					
					session.flush();
					session.clear();

					completeJob("SUCCESS", jobLog);

				} catch (Exception e) {
					log.error("ERROR: {}", e);
					completeJob("ERROR", jobLog);
				}
				session.saveOrUpdate(jobLog);
				session.getTransaction().commit();
			}
		}

	private static void job906() {
		if(jobList.contains("906")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG906);
			int seqArray[] = new int[10];
			int seq;
			cnt = 1;
			try {
				seqArray[0] = HisDao.getSeq("HIrDcntSceStoGnr", bssd);
				seqArray[1] = HisDao.getSeq("HIrParamHwRnd", bssd);

				seq = Arrays.stream(seqArray).max().getAsInt() +1 ;
				log.info("Max Seq in Job 906 : {},{},{},{}", bssd,  seq);

				IrParamHwRndDao.getIrParamHwRndStream(bssd).map(s -> HIrParamHwRnd.convertFrom(s, bssd, seq)).forEach(s -> save(s));
				session.flush();

				log.info("---------------------------------");

				IrCurveSceDao.getIrDcntSceStoGnrStream(bssd).map(s -> HIrDcntSceStoGnr.convertFrom(s, bssd, seq)).forEach(s -> save(s));
				session.flush();

				session.clear();


				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}

	private static void job907() {
		if(jobList.contains("907")) {
			session.beginTransaction();
			CoJobInfo jobLog = startJogLog(EJob.ESG906);
			int seqArray[] = new int[10];
			int seq;
			try {
				seqArray[2] = HisDao.getSeq("HIrDcntSceStoGnr", bssd);
				seqArray[3] = HisDao.getSeq("HIrValidSceSto", bssd);

				seq = Arrays.stream(seqArray).max().getAsInt() +1 ;

//				IrParamHwRndDao.getIrParamHwRndStream(bssd).map(s -> HIrParamHwRnd.convertFrom(s, bssd, seq)).forEach(s -> save(s));
//				session.flush();

				log.info("---------------------------------");

//				IrCurveSceDao.getIrDcntSceStoGnrStream(bssd).map(s -> HIrDcntSceStoGnr.convertFrom(s, bssd, seq)).forEach(s -> save(s));
//				session.flush();
				int qqq =0;
//				IrValidDao.getIrValidSceStoStream(bssd).map(s -> s.convertToHis(bssd, seq)).forEach(s -> save(s));
//				IrValidDao.getIrValidSceStoStream(bssd).map(s -> s.convertToHis(bssd, seq)).forEach(s -> log.info("aaa :{}, {}", qqq+1, s.toString()));

//				IrCurveSceDao.getIrDcntSceStoGnrStream(bssd).map(s -> HIrDcntSceStoGnr.convertFrom(s, bssd, seq)).forEach(s -> save(s));
//				IrCurveSceDao.getIrDcntSceStoGnrStream(bssd).map(s -> s.convertToHis(bssd, seq)).forEach(s -> log.info("aaa :{}, {}", qqq+1, s.toString()));

//				IrCurveSceDao.getIrDcntSceStoBizStream(bssd).map(s -> s.convertToHis(bssd, seq)).forEach(s -> log.info("aaa :{}, {}", qqq+1, s.toString()));
//				IrCurveSceDao.getIrDcntSceStoBizStream(bssd).map(s -> s.convertToHis(bssd, seq)).forEach(s -> save(s));
				session.clear();


				completeJob("SUCCESS", jobLog);

			} catch (Exception e) {
				log.error("ERROR: {}", e);
				completeJob("ERROR", jobLog);
			}
			session.saveOrUpdate(jobLog);
			session.getTransaction().commit();
		}
	}


	private static void job910() {
			if(jobList.contains("910")) {
				session.beginTransaction();
				CoJobInfo jobLog = startJogLog(EJob.ESG910);
				Map<String, Object> param;
				try {
					List<TransferJob> transferTables = HisDao.getTransferJob(bssd);

					for( TransferJob aa : transferTables) {
						cnt =1;
						if(aa.getToTable().getToTableName().equals("E_IR_DCNT_SCE_STO_BIZ")) {
							
							String table = "delete "  + aa.getToTable().getToTableName();
							int delNum = session.createQuery("delete IrDcntSceStoBiz a where a.baseYymm = :param1 and a.applBizDv = :param2")
													.setParameter("param1", bssd)
													.setParameter("param2", aa.getApplBizDv())
													.executeUpdate();
							log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", aa.getToTable().getToTableName(), jobLog.getJobId(), bssd, delNum);
						}

						param = new HashMap<String, Object>();
						param.put("baseYymm", bssd);
						param.put("seq", aa.getSeq());
//						param.put("seq", 3);
						if(!aa.getToTable().isAllBiz()) {
							param.put("applBizDv", aa.getApplBizDv());
						}

//						DaoUtil.getHEntityStream(aa.getToTable().getFromEntityName(), param).forEach(s ->log.info(s.toString()));
//						DaoUtil.getHEntityStream(aa.getToTable().getFromEntityName(), param).map(s-> s.convertToBiz(bssd, aa.getSeq())).forEach(s ->log.info(s.toString()));
						DaoUtil.getHEntityStream(session, aa.getToTable().getFromEntityName(), param).map(s-> s.convertToBiz(bssd, aa.getSeq())).forEach(s -> saveOrUpdate(s));
						log.info("Convert To Biz : {},{},{}", aa.getToTable().getToTableName(), bssd, cnt-1);

					}

//					int delNum = session.createQuery( "delete RcCorpPdBiz a where a.baseYymm = :param1").setParameter("param1", bssd).executeUpdate();
//					log.info("[{}] has been Deleted in Job:[{}] [BASE_YYMM: {}, COUNT: {}]", aa.getToTable().getToTableName(), jobLog.getJobId(), bssd, delNum);
//					HisDao.getHRcCorpPdBiz(bssd, 3, "IFRS").map(s-> s.convertToBiz(bssd, 1)).forEach(s-> save(s));
					session.flush();

					session.clear();

					completeJob("SUCCESS", jobLog);

				} catch (Exception e) {
					log.error("ERROR: {}", e);
					completeJob("ERROR", jobLog);
				}
				session.saveOrUpdate(jobLog);
				session.getTransaction().commit();
			}
		}


	private static CoJobInfo startJogLog(EJob job) {
		CoJobInfo jobLog = new CoJobInfo();
		jobLog.setJobStart(LocalDateTime.now());

		jobLog.setJobId(job.name());
		jobLog.setCalcStart(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
		jobLog.setBaseYymm(bssd);
		jobLog.setCalcDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMdd")));
		jobLog.setJobNm(job.getJobName());

		log.info("{}({}): Job Start !!! " , job.name(), job.getJobName());

		return jobLog;
	}

	private static void completeJob(String successDiv, CoJobInfo jobLog) {

		long timeElapse = Duration.between(jobLog.getJobStart(), LocalDateTime.now()).getSeconds();

//		log.info("timeElapse: {}, jobStart: {}", timeElapse, jobLog.getJobStart());
		jobLog.setCalcEnd(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
		jobLog.setCalcScd(successDiv);
		jobLog.setCalcElps(String.valueOf(timeElapse));
		jobLog.setLastModifiedBy(jobLog.getJobId());
		jobLog.setLastUpdateDate(LocalDateTime.now());

		log.info("{}({}): Job Completed with {} !!!", jobLog.getJobId(), jobLog.getJobNm(), successDiv);
	}


	private static void hold() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			log.error("ERROR: {}", e);
		}
	}


	protected static void saveOrUpdate(Object item) {
		session.saveOrUpdate(item);
//		log.info("itme : {}", item.toString());
		if(cnt % flushSize ==0) {
			session.flush();
			session.clear();
			log.info("in the flush : {}", cnt);
		}
		cnt = cnt+1;
	}


	protected static void save(Object item) {
		session.save(item);

		if(cnt % flushSize ==0) {
			session.flush();
			session.clear();
			log.info("in the flush : {},{},{}", cnt, item.getClass().getTypeName());
		}
		cnt = cnt+1;
	}
}
