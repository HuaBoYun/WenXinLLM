package com.huabo.legal.oracle.service;


import com.hbfk.util.JsonBean;
import com.huabo.legal.oracle.entity.TblFwglNoticeOracle;

public interface TblFwglNoticeOracleService {


	public JsonBean findAll(String code, String name, Integer pageNumber, Integer pageSize, String token) throws Exception;


	public JsonBean findbyid(Long noticeid, String token) throws Exception;

	public JsonBean delete(Long noticeid, String token) throws Exception;

	public JsonBean saveEnity(String token, TblFwglNoticeOracle notice, String attids) throws Exception;


	public JsonBean findattlist(String token, Long noticeid) throws Exception;


	public JsonBean deletefj(Long attid, String token) throws Exception;

	public JsonBean findbycode(String token) throws Exception;

}
