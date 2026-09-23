package com.huabo.system.service;

import com.huabo.system.entity.TblVideoType;

import java.util.Map;

public interface TblVideoTypeService {

	String save(TblVideoType tnt, String token, String staffId);

	TblVideoType findByName(String typename);

	Map<String, Object> findAll(String token,String staffId,Integer pageNumber,Integer pageSize);

	Map<String, Object> findByid(String selectid);

	void deleteById(String typeId);

    Map<String, Object> findAll(String token, String staffId);
}
