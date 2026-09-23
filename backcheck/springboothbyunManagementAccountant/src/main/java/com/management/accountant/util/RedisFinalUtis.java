package com.management.accountant.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RedisFinalUtis {

	
	private static final Logger logger = LoggerFactory.getLogger(RedisFinalUtis.class);
	
	/**
	 * 用户拥有什么样的权限
	 * 用途：读取当前用户的权限
	 * HYG
	 */
	public static final String  USERMANGERRIGHT ="userMangerRight";
	
	/**
	 * 根据组织架构查询其对应的组织权限
	 */
	public static final String MANAGEORGLIST = "orgFatherRight_list_";
	
	
	
	/**
	 * 更新部门信息时需要更新的缓存
	 */
	
	/**
	 * 风险管理模块
	 * 	  风险创建
	 * @author haoyuge
	 * 将组织架构做成treeList
	 * select * from tbl_organization where ORGID = " + orgid + " and STATUS=0 and orgtype < 100 ORDER BY orderid ASC
	 * 根据组织ID获取下一级的部门 ，操作部门信息时应及时更新该部门公司的缓存信息
	 */
	public static final String ORGLIST="orglistMenu_risk_";
	
	/**
	 * 返回List<Tree> 查询部门处理
	 * from TblOrganization  t where t.fatherorgid = ?  order by t.orderid asc
	 *  根据组织ID获取下一级的部门 ，操作部门信息时应及时更新该部门公司的缓存信息
	 */
	public static final String ORGTREEDEPRATMENT = "orgTree_depart_";
	
	
	/**
	 * 将组织下的部门做成treeList--智能监控模块
	 * 根据组织ID获取下一级的部门 ，操作部门信息时应及时更新该部门公司的缓存信息
	 * 
	 */
	public static final String ORGDEPTLIST="orgdept_list_";
	
	/**
	 * 返回 String str
	 * select * from TBL_ORGANIZATION org where orgtype < 100 and STATUS = 0 start with orgid = "+orgid+" connect by prior orgid = fatherorgid ORDER BY orderid ASC
	 * 
	 * 根据传入的公司ID 获取该公司下的部门
	 * 调用出 CommonController qxLeft、qxLeftorg
	 * 更新部门信息时 需要 传入上级公司ID更新该公司下的部门缓存信息
	 */
	public static final String ORGUSERSTR =  "orgUser_str_";

	/**
	 * 用户权限管理菜单设定。
	 * select * from TBL_ORGANIZATION org where orgtype < 100 and STATUS = 0 start with ORGID = "+orgid+" connect by prior orgid = fatherorgid ORDER BY orderid ASC
	 */
	public static final String ORGMENUSTR = "orgMenu_str_";
	
	
	/**
	 * 操作公司，部门信息时需要更新的缓存
	 */

	/**
	 * 获取传入公司ID下的部门和子公司，不包括子公司的部门
	 * 使用方法 CommonController中的findOrganizationByJTTreeAllQH 方法
	 * 操作部门、公司信息时需要及时更新所有上级的缓存信息
	 * */
	public static final String JTTREEGSQH = "orgJtt_reeGsqh_";
	
	/**
	 * 将组织架构做成root,str
	 * select * from tbl_organization  WHERE ORGTYPE<100 and orgid=" + orgid + " and  status = 0 ORDER BY orderid ASC
	 * 根据传入的公司ID,存储该公司下的所有部门、公司缓存信息
	 * 操作部门、公司信息时需要及时更新所有上级的缓存信息
	 */
	public static final String ORGROOTSTR="orgRoot_str_";
	
	//select * from TBL_ORGANIZATION org where orgtype < 100 and STATUS = 0 start with fatherorgid = -1 connect by prior orgid = fatherorgid ORDER BY orderid ASC
		//除行业库外，从长江投资（中国）有限公司开始，获取下面的所有子级信息 、一共三个类型 type=1,2,3
		//使用的地方：commonControlle 中的tree、ctree2、rtree、rtree2 4 个方法
		//无用
		public static final String ORGALLUSER = "orgAll_User_";
		
		/**
		 * 返回List<Tree> 展示该公司下的部门
		 * select * from tbl_organization where ORGID = " + nodeId + "  and orgtype < 100 ORDER BY orderid ASC* 
		 * 操作组织信息时，需要更新上级组织缓存信息
		 */
		public static final String ORGTREEDEPTLIST = "orgDept_list_";
		
		/**
		 * 返回tree str
		 * select * from tbl_organization where  STATUS = 0 ORDER BY orderid ASC
		 * 操作部门公司信息时，需要更新 无用
		 */
		public static final String ORGALLSTRLIST="orgAllStr_list_";
		
		/**根据传入的公司ID,存储该公司下的所有部门、公司缓存信息
		 * 操作部门、公司信息时需要及时更新上一级的缓存信息
		 */
		public static final String ORGCHILIST = "orgChildren_tree_";
		
		/**
		 * 将组织架构做成treeList
		 * from TblOrganization  t where t.fatherorgid = ? and status = 0 order by t.orderid asc
		 * 同上
		 */
		public static final String ORGTYPELIST="orgType_list_";
		
		/**
		 * 根据组织架构id查询right
		 * select * from TBL_ORGANIZATION org where orgtype < 100 and STATUS = 0 start with orgid = "+orgid+" connect by prior orgid = fatherorgid ORDER BY orderid ASC
		 * 操作部门、公司信息时需要及时更新所有上级的缓存信息
		 */
		public static final String ORGPROIDTREE = "orgPrior_tree_";
		
	
		
		/**
	 * 操作公司时需要更新的缓存
	 */
	
	
	/**
	 * 获取传入公司ID子公司，只获取下一级的子公司
	 * 使用方法 CommonController中的findOrganizationByJTTreeAllQH 方法
	 * 操作公司信息时需要及时更新上一级的缓存信息
	 * */
	public static final String JTNODEAllGSQh = "orgJtn_deallgsoh_";
	
	/**
	 * 返回string
	 * select * from tbl_organization  WHERE ORGTYPE<100 and ORGTYPE != 0 and FATHERORGID=" + orgid + " and  status = 0 ORDER BY orderid ASC
	 * 获取传入公司ID子公司，只获取下一级的子公司
	 * 操作公司信息时需要及时更新上一级的缓存信息
	 */
	public static final String ORGFATHERIDSTR="orgFather_str";
	
	/**
	 *  返回tree List
	 * select * from tbl_organization  WHERE ORGTYPE<100 and ORGTYPE != 0 and FATHERORGID=" + orgid + " and  status = 0 ORDER BY orderid ASC
	 *  获取传入公司ID子公司，只获取下一级的子公司
	 * 操作公司信息时需要及时更新上一级的缓存信息
	 */
	public static final String ORGFATHERTREE="orgFather_tree_";
	
	/**
	 * 返回List<Tree>
	 * select * from tbl_organization where ORGID = " + nodeId + "  and orgtype < 100 ORDER BY orderid ASC
	 *  获取传入公司ID子公司，只获取下一级的子公司
	 * 操作公司信息时需要及时更新上一级的缓存信息
	 */
	public static final String ORGTREEJTNODE = "orgJTnode_tree_";
	
	/**
	 * 用户登陆以后可以切换组织的redis
	 * 修改公司时，更新上一级的缓存
	 */
	public static final String  ORGTREE ="orgtree_";
	
	/**
	 * 存储某个公司下的所有子级公司，
	 * 使用方法 NkcsController中的getTreeOeg 方法
	 * 操作公司信息时需要及时更新所有上级的缓存信息
	 * */
	public static final String COMPANYBYTREE = "orgCom_tree_";
	
	/***
	 * 返回 root String
	 * select * from TBL_ORGANIZATION where ICODE LIKE '101%' and   (STATUS != 1 or STATUS IS NULL) AND ORGTYPE = 100 ORDER BY orderid ASC
	 * 行业架构
	 */
	public static final String ORGBYFATHERSTR = "orgByFather_str_";
	
	/**
	 * 返回List<Tree>
	 * String sql = "select * from tbl_organization where ORGID = " + nodeId + " and orgtype = 100 ORDER BY orderid ASC";
	 * 行业架构
	 */
	public static final String ORGNODEHYTREE = "orgNodeHy_tree_";
  }



