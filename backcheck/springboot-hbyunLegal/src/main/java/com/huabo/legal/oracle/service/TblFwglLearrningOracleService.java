package com.huabo.legal.oracle.service;

import com.hbfk.util.JsonBean;
import com.huabo.legal.oracle.entity.TblFwglLearrningOracle;

public interface TblFwglLearrningOracleService {


	public JsonBean findAll(String code, String name, Integer pageNumber, Integer pageSize, String token) throws Exception;


	public JsonBean findbyid(Long lingid, String token) throws Exception;

	public JsonBean delete(Long lingid, String token) throws Exception;

	public JsonBean saveEnity(String token, TblFwglLearrningOracle ling, String attids) throws Exception;


	public JsonBean findattlist(String token, Long lingid) throws Exception;


	public JsonBean deletefj(Long attid, String token) throws Exception;

	public JsonBean findbycode(String token) throws Exception;

}
