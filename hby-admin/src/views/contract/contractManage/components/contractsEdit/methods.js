/*
 * @Date: 2022-04-15 11:07:27
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-22 20:21:18
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/contractsEdit/methods.js
 */
import * as names from './specify'
import { defaultFormFields } from './default'

export const comboFields = function (typename, isChange) {
  let currentEdit = 'moren'
  let showSubFields = false
  let subFields = []
  let subTitle = ''
  // switch (typename) {
  //   case '货物运输合同':
  //     currentEdit = 'huowu'
  //     showSubFields = true
  //     subTitle = '运输货物信息'
  //     subFields = [
  //       { value: 'infoname', label: '货物名称' },
  //       { value: 'infodesc', label: '运输说明' },
  //       { value: 'infoxh', label: '规格型号' },
  //       { value: 'infoprice', label: '货物价值' },
  //     ]
  //     break
  //   case '保管合同':
  //     currentEdit = 'baoguan'
  //     showSubFields = true
  //     subTitle = '保管货物信息'
  //     subFields = [
  //       { value: 'infoname', label: '保管内容' },
  //       { value: 'infodesc', label: '保管注意事项' },
  //       { value: 'infoprice', label: '货物价值' },
  //       { value: 'infomomo', label: '备注' },
  //     ]
  //     break
  //   case '仓储合同':
  //     currentEdit = 'cangchu'
  //     showSubFields = true
  //     subTitle = '仓储物品详情'
  //     subFields = [
  //       { value: 'infoname', label: '物品名称' },
  //       { value: 'infoxh', label: '物品规格' },
  //       { value: 'infodesc', label: '租金说明' },
  //       { value: 'infonum', label: '数量' },
  //     ]
  //     break
  //   case '汽车租赁合同':
  //     currentEdit = 'qiche'
  //     break
  //   case '房屋土地租赁合同':
  //     currentEdit = 'fangwu'
  //     showSubFields = true
  //     subTitle = '租赁信息详情'
  //     subFields = [
  //       { value: 'infoname', label: '租赁内容' },
  //       { value: 'infoxh', label: '地理位置' },
  //       {
  //         value: 'infostartdate',
  //         label: '租赁开始日期',
  //         type: 'date-picker',
  //       },
  //       { value: 'infoenddate', label: '租赁结束日期', type: 'date-picker' },
  //       { value: 'infopinpai', label: '交接单位' },
  //       { value: 'infomomo', label: '备注' },
  //     ]
  //     break
  //   case '生产主辅料采购合同':
  //     currentEdit = 'shengchanzfl'
  //     showSubFields = true
  //     subTitle = '采购辅料信息'
  //     subFields = [
  //       { value: 'infoname', label: '产品名称' },
  //       { value: 'infotype', label: '采购方式' },
  //       { value: 'infoxh', label: '规格型号' },
  //       { value: 'infoorg', label: '单位' },
  //       { value: 'infonum', label: '数量' },
  //       { value: 'infoprice', label: '单价' },
  //     ]
  //     break
  //   case '市场活动采购合同':
  //     currentEdit = 'shichang'
  //     showSubFields = true
  //     subTitle = '产品详细信息'
  //     subFields = [
  //       { value: 'infoname', label: '产品名称' },
  //       { value: 'infopinpai', label: '品牌' },
  //       { value: 'infoxh', label: '规格型号' },
  //       { value: 'infoorg', label: '单位' },
  //       { value: 'infonum', label: '数量' },
  //       { value: 'infoprice', label: '单价' },
  //     ]
  //     break
  //   case '项目外包服务合同':
  //     currentEdit = 'xiangmu'
  //     showSubFields = true
  //     subTitle = '外包服务信息'
  //     subFields = [
  //       { value: 'infoname', label: '服务名称' },
  //       { value: 'infodesc', label: '服务内容' },
  //       { value: 'infonum', label: '数量' },
  //       { value: 'infoprice', label: '单价' },
  //     ]
  //     break
  //   case '公司年度旅游服务合同':
  //     currentEdit = 'gongsi'
  //     break
  //   case '非生产性采购合同':
  //     currentEdit = 'feishengchan'
  //     break
  //   case '产品销售合同':
  //     currentEdit = 'chanpinxs'
  //     showSubFields = true
  //     subTitle = '产品详细信息'
  //     subFields = [
  //       { value: 'infoname', label: '产品名称' },
  //       { value: 'infoxh', label: '规格型号' },
  //       { value: 'infoorg', label: '单位' },
  //       { value: 'infonum', label: '数量' },
  //       { value: 'infoprice', label: '单价' },
  //     ]
  //     break
  //   case '升级服务合同':
  //     currentEdit = 'shengji'
  //     break
  //   case '常规二次销售合同':
  //     currentEdit = 'changgui'
  //     showSubFields = true
  //     subTitle = '产品详情信息'
  //     subFields = [
  //       { value: 'infoname', label: '产品名称' },
  //       { value: 'infoxh', label: '规格型号' },
  //       { value: 'infoorg', label: '单位' },
  //       { value: 'infoprice', label: '单价' },
  //       { value: 'infonum', label: '数量' },
  //     ]
  //     break
  //   case '售后服务合同':
  //     currentEdit = 'shouhou'
  //     showSubFields = true
  //     subTitle = '服务内容详情'
  //     subFields = [
  //       { value: 'infoname', label: '内容' },
  //       { value: 'infoprice', label: '收费标准' },
  //     ]
  //     break
  //   case '担保合同':
  //     currentEdit = 'danbao'
  //     break
  //   case '抵押合同':
  //     currentEdit = 'diya'
  //     showSubFields = true
  //     subTitle = '抵押信息详情'
  //     subFields = [
  //       { value: 'infoname', label: '抵押物品' },
  //       { value: 'infoxh', label: '规格型号' },
  //       { value: 'infonum', label: '抵押数量' },
  //       { value: 'infoprice', label: '物品价值' },
  //     ]
  //     break
  //   case '质押合同':
  //     currentEdit = 'zhiya'
  //     showSubFields = true
  //     subTitle = '质押信息详情'
  //     subFields = [
  //       { value: 'infoname', label: '质押物' },
  //       { value: 'infoxh', label: '规格型号' },
  //       { value: 'infodesc', label: '备注' },
  //       { value: 'infoprice', label: '物品价值' },
  //     ]
  //     break
  //   case '建筑设计合同':
  //     currentEdit = 'jianzhu'
  //     break
  //   case '装修施工合同':
  //     currentEdit = 'zhuangxiu'
  //     break
  //   case '工程施工合同':
  //     currentEdit = 'gongcheng'
  //     break
  //   case '战略合作协议合同':
  //     currentEdit = 'zhanlue'
  //     break
  //   case '代理协议合同':
  //     currentEdit = 'daili'
  //     break
  //   case '保密协议合同':
  //     currentEdit = 'baomi'
  //     break
  //   default:
  //     currentEdit = 'moren'
  // }

  let formFields = Object.assign({}, defaultFormFields, names[currentEdit])
  if (isChange) {
    formFields = Object.assign(
      {
        changetype: {
          label: '合同变更类型',
        },
        changedate: {
          label: '合同变更时间',
        },
        changedesc: {
          label: '合同变更',
        },
      },
      formFields
    )
  }

  let formData = {
    flowId: 622316,
    flowid: 622316,
    flowname: 'HTGL002',
    recordtype: 'HTGL002',
    contractid: undefined,
    zxunit: undefined, // 执行单位key
    orgname: undefined, // 执行单位value
    contractdept: undefined, // 执行部门key
    orgmeno: undefined, // 执行部门value
    contractstaff: undefined, // 执行人key
    realname: undefined, // 执行人value
    contractxdfxinfo: undefined, //相对方主键key
    budgetname: undefined, // 相对方value
    topicname: undefined, // 立项信息key（是name）
    topicid: undefined, // 立项信息value
    counterpartbank: undefined, // 银行key
    bankaccount: undefined, // 银行value
    bankkhyh: undefined, // 开户银行
    recordparent: undefined, // 关联合同key
    parentname: undefined, // 关联合同value
    describe: undefined,
    informationList: [], // 物品列表
    nodeList: [], // 合同履行阶段列表
    attList: [], // 附件列表
    signingList: [], //盖章文件列表
    payList: [], // 付款信息
    colList: [], // 收款信息
    parentList: [], // 合同版本信息
    oppositeList: [], // 相对方信息
    isbigmatter: '是',
    matterorg: undefined,
    ismany: '否',
    agreementcount: 0,
    typefl: '',
    templateId: '',
    uploadUrl: '',
    menuid: '',
    secrectLevelId: '',
    staffScopeIds: '',
    staffScopeNames: '',
  }

  if (isChange) {
    formData.flowId = 622325
    formData.flowid = 622325
  }

  // 设置初始值
  Object.keys(formFields).forEach((key) => {
    if (currentEdit == 'moren') {
      if (key == 'contractdatetype') {
        formData[key] = '固定期限'
      } else if (key == 'contractxz') {
        formData[key] = '初始合同'
      } else if (key == 'contractplan') {
        formData[key] = '是'
      } else if (key == 'contractzd') {
        formData[key] = '是'
      } else if (key == 'contractchildren') {
        formData[key] = '是'
      } else if (key == 'jijiatype') {
        formData[key] = '固定总价'
      } else if (key == 'dctype') {
        formData[key] = '收款'
      } else {
        formData[key] = undefined
      }
    } else {
      if (key == 'dctype') {
        formData[key] = '收款'
      } else {
        formData[key] = undefined
      }
    }
  })
  return {
    formFields,
    formData,
    subFields,
    subTitle,
    showSubFields,
    currentEdit,
  }
}
