package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.entity.*;
import com.huabo.fxgl.mapper.RiskAssessmentstdMapper;
import com.huabo.fxgl.mapper.RiskAssplanRiskMapper;
import com.huabo.fxgl.mapper.RiskLevelmappingMapper;
import com.huabo.fxgl.mapper.RiskMapper;
import com.huabo.fxgl.service.IRiskAssplanRiskService;
import com.huabo.fxgl.service.IRiskAssplanService;
import com.huabo.fxgl.service.IRiskLevelmappingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSInput;

import java.math.BigDecimal;
import java.util.*;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xujiajun
 * @since 2022-08-12
 */
@Service
@Slf4j
public class RiskLevelmappingServiceImpl extends ServiceImpl<RiskLevelmappingMapper, RiskLevelmapping> implements IRiskLevelmappingService {
    @Override
    public RiskLevelmapping getRiskLevelMappingBymentIdAndDegreeId(BigDecimal mentId, String DegreeId) {
        List<RiskLevelmapping> levelMappings = baseMapper.getRiskLevelMappingBymentIdAndDegreeId(mentId, DegreeId);
        if (null != levelMappings && levelMappings.size() > 0) {
            return levelMappings.get(0);
        }
        return null;
    }

    @Autowired
    private RiskLevelmappingMapper riskLevelmappingMapper;
    @Autowired
    private IRiskAssplanRiskService riskAssplanRiskService;

    @Autowired
    private RiskAssplanRiskMapper riskAssplanRiskMapper;
    
    @Autowired
    private RiskAssessmentstdMapper riskAssessmentstdMapper;
    
    @Autowired
    private RiskMapper riskMapper;
    
    @Autowired
    private IRiskAssplanService riskAssplanService;

    @Override
    public RiskLevelmapping getByInfluId(String id) {
    	RiskLevelmapping levelMappings = riskLevelmappingMapper.getByInfluId(id);
        return levelMappings;
    }

    /**
     * 默认显示页面的详情信息
     *
     * @param riskcatid
     * @param planId
     * @return
     * @author wanghongtuo
     * @Date 2022/8/10
     * @version 1.0.1
     */
    public Map<String,Object> forCycle(BigDecimal planId) {
        Map<String,Object> result = new HashMap<String,Object>(0);
        Map<String, List<Risk>> map = new HashMap<>();
        List<RiskAssplanRisk> assPlanRisks = riskAssplanRiskService.findRiskByPlanId(planId);
        if (assPlanRisks.size() > 0) {
            for (RiskAssplanRisk riskAssPlanRisk : assPlanRisks) {
                String res = riskAssPlanRisk.getSeverity() + "" + riskAssPlanRisk.getFrequency();
                log.info("------------------------res: " + res);
                if (null != map.get(res)) {
                    List<Risk> risks = map.get(res);
                    risks.add(riskAssPlanRisk.getRisk());
                    map.put(res, risks);
                } else {
                    List<Risk> risks = new ArrayList<>();
                    risks.add(riskAssPlanRisk.getRisk());
                    map.put(res, risks);
                }
            }
            log.info("-------------------------------------------assPlanRisks: " + assPlanRisks);
            // 获取评估标准
//            RiskAssessmentstd assessMentsTd = assPlanRisks.get(0).getAssplan().getAssessmentstd();
            BigDecimal assplanid = assPlanRisks.get(0).getAssplanid();

            RiskAssessmentstd assessMentsTd = riskAssplanService.getById(assplanid).getAssessmentstd();
            log.info("-------------------------------------------hei");
            List<RiskPossibility> possibilities = new ArrayList<>();
            log.info("-------------------------------------------hei1");
            possibilities.addAll(assessMentsTd.getPossibilities());

            Collections.sort(possibilities, new Comparator<RiskPossibility>() {
                @Override
                public int compare(RiskPossibility o1, RiskPossibility o2) {
                    return o1.getRplevel().compareTo(o2.getRplevel());
                }
            });
            log.info("-------------------------------------------0");
            List<RiskInfludegree> infludegrees = new ArrayList<>();
            infludegrees.addAll(assessMentsTd.getRiskInfludegrees());
            Collections.sort(infludegrees, new Comparator<RiskInfludegree>() {
                @Override
                public int compare(RiskInfludegree o1, RiskInfludegree o2) {
                    return o1.getRilevel().compareTo(o2.getRilevel());
                }
            });
            Collections.reverse(infludegrees);
            // 影响程度
            for (RiskInfludegree riskInfludegree : infludegrees) {
                riskInfludegree.setRiskLevelMapping(
                        getByInfluId(riskInfludegree.getDegreeid().toString()));
            }
            for (int i = 1; i <= 5; i++) {
                if (i == 1) {
                    for (RiskInfludegree riskInfludegree : infludegrees) {
                        List<Risk> risks = map.get(riskInfludegree.getRilevel() + "" + i);
                        if (null != risks) {
                            riskInfludegree.getRiskLevelMapping().setCount1(risks.size());
                            StringBuffer sb = new StringBuffer();
                            for (Risk tblRisk : risks) {
                                sb.append(tblRisk.getRiskid() + ",");
                            }
                            riskInfludegree.getRiskLevelMapping().setRisk1Ids(sb.toString());
                        }
                    }
                }
                if (i == 2) {
                    for (RiskInfludegree riskInfludegree : infludegrees) {
                        List<Risk> risks = map.get(riskInfludegree.getRilevel() + "" + i);
                        if (null != risks) {
                            riskInfludegree.getRiskLevelMapping().setCount2(risks.size());
                            StringBuffer sb = new StringBuffer();
                            for (Risk tblRisk : risks) {
                                sb.append(tblRisk.getRiskid() + ",");
                            }
                            riskInfludegree.getRiskLevelMapping().setRisk2Ids(sb.toString());
                        }
                    }
                }
                if (i == 3) {
                    for (RiskInfludegree riskInfludegree : infludegrees) {
                        List<Risk> risks = map.get(riskInfludegree.getRilevel() + "" + i);
                        if (null != risks) {
                            riskInfludegree.getRiskLevelMapping().setCount3(risks.size());
                            StringBuffer sb = new StringBuffer();
                            for (Risk tblRisk : risks) {
                                sb.append(tblRisk.getRiskid() + ",");
                            }
                            riskInfludegree.getRiskLevelMapping().setRisk3Ids(sb.toString());
                        }
                    }
                }
                if (i == 4) {
                    for (RiskInfludegree riskInfludegree : infludegrees) {
                        List<Risk> risks = map.get(riskInfludegree.getRilevel() + "" + i);
                        if (null != risks) {
                            riskInfludegree.getRiskLevelMapping().setCount4(risks.size());
                            StringBuffer sb = new StringBuffer();
                            for (Risk tblRisk : risks) {
                                sb.append(tblRisk.getRiskid() + ",");
                            }
                            riskInfludegree.getRiskLevelMapping().setRisk4Ids(sb.toString());
                        }
                    }
                }
                if (i == 5) {
                    for (RiskInfludegree riskInfludegree : infludegrees) {
                        List<Risk> risks = map.get(riskInfludegree.getRilevel() + "" + i);
                        if (null != risks) {
                            riskInfludegree.getRiskLevelMapping().setCount5(risks.size());
                            StringBuffer sb = new StringBuffer();
                            for (Risk tblRisk : risks) {
                                sb.append(tblRisk.getRiskid() + ",");
                            }
                            riskInfludegree.getRiskLevelMapping().setRisk5Ids(sb.toString());
                        }
                    }
                }
            }
            result.put("map", map);
            if (infludegrees != null) {
                result.put("retuList", infludegrees);
            }
            result.put("infludegrees", infludegrees);
            result.put("planId", planId);
        }
        return result;
    }

    
    @Override
    public Map<String,Object> forSyCycle(String orgid,String type,String value) throws Exception{
        Map<String,Object> result = new HashMap<String,Object>(0);
        Map<String, List<Risk>> map = new HashMap<>();
        List<RiskInfludegree> infludegrees = new ArrayList<>();
        RiskAssessmentstd assessMentsTd=null;
        List<RiskPossibility> possibilities = new ArrayList<>();
        List<RiskAssplanRisk> getSyFxtjRlt=null;
        if(type.equals("pgz")||type.equals("ypg")){
           getSyFxtjRlt=riskAssplanRiskMapper.getSyFxtjRlt(orgid,value);
        }else if(type.equals("ygb")){
        	getSyFxtjRlt=riskAssplanRiskMapper.getSyFxtjRltYgb(orgid);
        }
        for (RiskAssplanRisk riskAssPlanRisk : getSyFxtjRlt) {
            String res = riskAssPlanRisk.getSeverity() + "" + riskAssPlanRisk.getFrequency();
            if (null != map.get(res)) {
                List<Risk> risks = map.get(res);
                Risk r=riskMapper.selectById(riskAssPlanRisk.getRiskid());
                risks.add(r);
                map.put(res, risks);
            } else {
                List<Risk> risks = new ArrayList<>();
                Risk r=riskMapper.selectById(riskAssPlanRisk.getRiskid());
                risks.add(r);
                map.put(res, risks);
            }
            //只能配置一个影响/频率矩阵
            if(riskAssPlanRisk.getAssplanid()!=null&&possibilities.size()==0){
                BigDecimal assplanid = riskAssPlanRisk.getAssplanid();
                if(assessMentsTd==null){
                	RiskAssplan plan=riskAssplanService.getById(assplanid);
                	if(plan==null||plan.getAssessmentstd()==null){
                		continue; 
                	}
                    assessMentsTd = plan.getAssessmentstd();
					if (assessMentsTd.getPossibilities().size() == 5) {
						possibilities.addAll(assessMentsTd.getPossibilities());
						Collections.sort(possibilities, new Comparator<RiskPossibility>() {
							@Override
							public int compare(RiskPossibility o1, RiskPossibility o2) {
								return o1.getRplevel().compareTo(o2.getRplevel());
							}
						});
						infludegrees.addAll(assessMentsTd.getRiskInfludegrees());
						Collections.sort(infludegrees, new Comparator<RiskInfludegree>() {
							@Override
							public int compare(RiskInfludegree o1, RiskInfludegree o2) {
								return o1.getRilevel().compareTo(o2.getRilevel());
							}
						});
						Collections.reverse(infludegrees);
						   // 影响程度
		                for (RiskInfludegree riskInfludegree : infludegrees) {
		                    riskInfludegree.setRiskLevelMapping(
		                            getByInfluId(riskInfludegree.getDegreeid().toString()));
		                }
					}else{
						assessMentsTd=null;
					}
				}

             
    	        }
        }
            for (int i = 1; i <= 5; i++) {
                if (i == 1) {
                    for (RiskInfludegree riskInfludegree : infludegrees) {
                        List<Risk> risks = map.get(riskInfludegree.getRilevel() + "" + i);
                        if (null != risks) {
                            riskInfludegree.getRiskLevelMapping().setCount1(risks.size());
                            StringBuffer sb = new StringBuffer();
                            for (Risk tblRisk : risks) {
                            	if(tblRisk!=null){
                                  sb.append(tblRisk.getRiskid() + ",");
                            	}
                            }
                            riskInfludegree.getRiskLevelMapping().setRisk1Ids(sb.toString());
                        }
                    }
                }
                if (i == 2) {
                    for (RiskInfludegree riskInfludegree : infludegrees) {
                        List<Risk> risks = map.get(riskInfludegree.getRilevel() + "" + i);
                        if (null != risks) {
                            riskInfludegree.getRiskLevelMapping().setCount2(risks.size());
                            StringBuffer sb = new StringBuffer();
                            for (Risk tblRisk : risks) {
                            	if(tblRisk!=null){
                                sb.append(tblRisk.getRiskid() + ",");
                            	}
                            }
                            riskInfludegree.getRiskLevelMapping().setRisk2Ids(sb.toString());
                        }
                    }
                }
                if (i == 3) {
                    for (RiskInfludegree riskInfludegree : infludegrees) {
                        List<Risk> risks = map.get(riskInfludegree.getRilevel() + "" + i);
                        if (null != risks) {
                            riskInfludegree.getRiskLevelMapping().setCount3(risks.size());
                            StringBuffer sb = new StringBuffer();
                            for (Risk tblRisk : risks) {
                            	if(tblRisk!=null){
                                sb.append(tblRisk.getRiskid() + ",");
                            	}
                            }
                            riskInfludegree.getRiskLevelMapping().setRisk3Ids(sb.toString());
                        }
                    }
                }
                if (i == 4) {
                    for (RiskInfludegree riskInfludegree : infludegrees) {
                        List<Risk> risks = map.get(riskInfludegree.getRilevel() + "" + i);
                        if (null != risks) {
                            riskInfludegree.getRiskLevelMapping().setCount4(risks.size());
                            StringBuffer sb = new StringBuffer();
                            for (Risk tblRisk : risks) {
                            	if(tblRisk!=null){
                                sb.append(tblRisk.getRiskid() + ",");
                            	}
                            }
                            riskInfludegree.getRiskLevelMapping().setRisk4Ids(sb.toString());
                        }
                    }
                }
                if (i == 5) {
                    for (RiskInfludegree riskInfludegree : infludegrees) {
                        List<Risk> risks = map.get(riskInfludegree.getRilevel() + "" + i);
                        if (null != risks) {
                            riskInfludegree.getRiskLevelMapping().setCount5(risks.size());
                            StringBuffer sb = new StringBuffer();
                            for (Risk tblRisk : risks) {
                            	if(tblRisk!=null){
                                sb.append(tblRisk.getRiskid() + ",");
                            	}
                            }
                            riskInfludegree.getRiskLevelMapping().setRisk5Ids(sb.toString());
                        }
                    }
                }
	    result.put("map", map);
//        if (infludegrees != null) {
//            result.put("retuList", infludegrees);
//        }
        result.put("infludegrees", infludegrees);
        }
        return result;
    }

    
    
	@Override
	public Map<String, Object> getFxpgRlt() throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> result=new HashMap<String, Object>();
		try {
			 List<Map<String, Object>> list=riskAssplanRiskMapper.selectRiskGroupUnit();
			 for(Map<String, Object> map:list){
				 String unit=map.get("UNIT").toString();
				 Integer wpg=riskAssplanRiskMapper.getWpgSize(unit);
				 map.put("WPG", wpg);
				 map.putAll(setYpg(unit,"ypg"));
				 map.putAll(setYpg(unit,"ygb"));
			 }
			 result.put("data", list);
			
//			//获取 orgid 下的未评估  评估中  已评估  已关闭数量
//			Integer wpg=riskLevelmappingMapper.getFxpgRlt(orgid,"0");
//			Integer ypg=riskLevelmappingMapper.getFxpgRlt(orgid,"1");
//			Integer pgz=riskLevelmappingMapper.getFxpgRlt(orgid,"2");
//			Integer ygb=riskLevelmappingMapper.getFxpgRltGb(orgid);
//			result.put("wpg", wpg);
//			result.put("ypg", ypg);
//			result.put("pgz", pgz);
//			result.put("ygb", ygb);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	
	public Map<String, Object> setYpg(String orgid,String type)throws Exception{
		Map<String, Object> map=new HashMap<String, Object>();
		 List<RiskAssplanRisk> ypgList=null;
		 try {
		if(type.equals("ypg")){
			ypgList=riskAssplanRiskMapper.getYpg(orgid);
			 // 遍历风险评估计划风险列表，根据风险级别更新风险评估计划的计数
			 int count1=0;
			 int count2=0;
			 int count3=0;
			 int count4=0;
			 int count5=0;
	         for (RiskAssplanRisk riskAssPlanRisk : ypgList) {
	             switch (Integer.parseInt(riskAssPlanRisk.getRisklevel())) {
	                 case 1:
	                	 count1++;
	                     break;
	                 case 2:
	                	 count2++;
	                     break;
	                 case 3:
	                	 count3++;
	                     break;
	                 case 4:
	                	 count4++;
	                     break;
	                 case 5:
	                	 count5++;
	                     break;
	                 default:
	                     break;
	             }
	         }
	         map.put("COUNT0", count1+count2+count3+count4+count5);
	         map.put("COUNT1", count1);
	         map.put("COUNT2", count2);
	         map.put("COUNT3", count3);
	         map.put("COUNT4", count4);
	         map.put("COUNT5", count5);
		}else{
			ypgList=riskAssplanRiskMapper.getYgb(orgid);
			 // 遍历风险评估计划风险列表，根据风险级别更新风险评估计划的计数
			 int count7=0;
			 int count8=0;
			 int count9=0;
			 int count10=0;
			 int count11=0;
	         for (RiskAssplanRisk riskAssPlanRisk : ypgList) {
	             switch (Integer.parseInt(riskAssPlanRisk.getRisklevel())) {
	                 case 1:
	                	 count7++;
	                     break;
	                 case 2:
	                	 count8++;
	                     break;
	                 case 3:
	                	 count9++;
	                     break;
	                 case 4:
	                	 count10++;
	                     break;
	                 case 5:
	                	 count11++;
	                     break;
	                 default:
	                     break;
	             }
	         }
	         map.put("COUNT6", count7+count8+count9+count10+count11);
	         map.put("COUNT7", count7);
	         map.put("COUNT8", count8);
	         map.put("COUNT9", count9);
	         map.put("COUNT10", count10);
	         map.put("COUNT11", count11);
		}
		 } catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
         return map;
     }
}
