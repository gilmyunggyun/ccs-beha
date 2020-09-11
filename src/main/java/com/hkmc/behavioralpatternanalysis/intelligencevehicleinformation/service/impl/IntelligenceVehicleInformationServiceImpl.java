package com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.relational.repository.query.RelationalEntityInformation;
import org.springframework.stereotype.Service;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.model.RedisVin;
import com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.model.BehaSvdvHist;
import com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.service.IntelligenceVehicleInformationService;

import ccs.core.db.repository.postgre.GenericPostgreRepository;
import ccs.core.db.repository.redis.GenericRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntelligenceVehicleInformationServiceImpl implements IntelligenceVehicleInformationService {

	@Autowired
	RedisTemplate<String, String> redisTemplate;
	
//	private final GenericRedisRepository<RedisVin, String> redisVinRepo;
	
	private final R2dbcEntityOperations postgresqlEntityOperations;
	private final R2dbcRepositoryFactory postgresqlRepositoryFactory;
	private final R2dbcConverter r2dbcConverter; 
	
//	@Autowired
//	R2dbcRepositoryFactory postgresqlRepositoryFactory;	
//	
//	@Autowired
//	R2dbcEntityOperations postgresqlEntityOperations;
//
//	@Autowired
//	R2dbcConverter r2dbcConverter;
	
	@Override
	public Map<String, Object> itlCarBreakpadDrvScore(Map<String, Object> reqBody) throws GlobalCCSException {
		int status = 200;
		String resMsg = "Success";
		Map<String, Object> resultData = new HashMap<String, Object>();
		
		try {
			BehaSvdvHist reqDto = new BehaSvdvHist();
			
			GenericRedisRepository<RedisVin, String> redisVinRepo = new GenericRedisRepository<>(RedisVin.class, redisTemplate);
			
			String srchPatt = "*_" + reqBody.get("vin").toString();
			List<RedisVin> receiveRedisVinData = redisVinRepo.findByAllHash(srchPatt);
			
			
			log.info("redis : {}", receiveRedisVinData.get(0).getCarOid());
			
//			GenericJpaRepository<BehaSvdvHist, Integer> jpaRepository = new GenericJpaRepository<>(BehaSvdvHist.class, entityManager);
			
			
//			RelationalEntityInformation<BehaSvdvHist, Integer> entity = postgresqlRepositoryFactory.getEntityInformation(BehaSvdvHist.class);
//			GenericPostgreRepository<BehaSvdvHist, Integer> repository = new GenericPostgreRepository<>(BehaSvdvHist.class, entity, postgresqlEntityOperations, r2dbcConverter);			
//			
//			reqDto.setCarOid(Integer.parseInt(receiveRedisVinData.get(0).getCarOid()));
//			repository.findAll(reqDto);
//			repository.findById(reqDto.getCarOid());
			
			
//	        String query = "SELECT "
//	        		+ "			crtn_ymd, nnid_vin, sale_caml_cd, good_severe_sctn, good_driv_cnt, cautn_driv_cnt, severe_driv_cnt, severe_driv_plus_cnt"
//	        		+ "		FROM beha_svdv_hist"
//	        		+ "		WHERE ('x'||lpad(SUBSTR(nnid_vin, 17, LENGTH(nnid_vin) - 19), 8, '0'))\\:\\:bit(32)\\:\\:integer = CAST(:param AS INTEGER)"
//	        		+ "		AND LENGTH(nnid_vin) >= 25"
//	        		;
	        
	        
//	        String query = "SELECT "
//	        		+ "			nnid_vin, crtn_ymd, f90d_run_dist, insr_dscn_psbl_yn, mdng_driv_grd, rgst_dtm, rpac_grd, rpvl_grd, sfty_driv_scor"
//	        		+ "		FROM BEHA_UBI_SDHB_INFO"
//	        		+ "		WHERE ('x'||lpad(SUBSTR(nnid_vin, 17, LENGTH(nnid_vin) - 19), 8, '0'))\\:\\:bit(32)\\:\\:integer = CAST(:param AS INTEGER)"
//	        		+ "		AND LENGTH(nnid_vin) >= 25"
//	        		;	        

//			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//			
//			CriteriaQuery<BehaSvdvHist> cq = entityManager.getCriteriaBuilder().createQuery(BehaSvdvHist.class);
//			Root<BehaSvdvHist> content = cq.from(BehaSvdvHist.class); 
//			
//			List<Predicate> predicate = new ArrayList<>();
//			predicate.add(cb.equal(content.get("caroid"), receiveRedisVinData.get(0).getCarOid()));
//			cq.select(content).where(predicate.toArray(new Predicate[] {}));
//			
//			
//			
//			
//			
//			
//			List<BehaSvdvHist> list = jpaRepository.findByQuery(cq);
			
//	        IntelligenceVehicleInformation resDto = postgreSrv.findNativeQuery(query, reqDto, receiveRedisVinData.get(0).getCarOid());
//			resultData.put("body", resDto);
//			resultData.put("resultStatus", "S");
//			resultData.put("status", status);
//			resultData.put("message", resMsg);
			
		} catch (Exception e) {
			log.error("InternelBizEXception : ", e);
			status = 595;
			resMsg = "InternelBizEXception";
			resultData.put("resultStatus", "F");
			resultData.put("errCd", status);
			resultData.put("errNm", "InternelBizEXception");
			resultData.put("status", status);
			resultData.put("message", resMsg);
		} finally {
			log.info("finally");
		}
        
   		return resultData;
	}
}
