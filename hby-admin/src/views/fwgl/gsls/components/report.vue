<template>
	<div>
		<el-button v-print="printContent" id="printBtn" ref="print">打印</el-button>
	<div style="display: none">
		<div id="printMe" class="printMes">
      <div style="text-align: right;">
        <img
            src="@/assets/zhezi.png"
            style="width: 140px; height: 17px"
            alt=""
          />
      </div>
			<div style="text-align: center;margin-bottom: 10px;">
        <span style="font-size: 30px;font-weight: 700;">公司律师申请表</span>
      </div>
				
  				<table
        width="100%"
        border="1"
        cellspacing="0"
        cellpadding="0"
        style="border-collapse: collapse"
      >
        <tbody class="detailContent" >
          <tr>
            <td colspan="6">
              <table
                border="1"
                cellspacing="0"
                cellpadding="0"
                style="
                  width: 100%;
                  border-collapse: collapse;
                  border-width: 0px;
                  border-style: hidden;
                "
              >
                <tr>
                  <td class="pd-10" align="center" style="width: 70px">姓名</td>
                  <td>
                    <span class="pd-10" >{{formData.realName}}</span>
                  </td>
                  <td align="center" class="pd-5" style="width: 50px">性别</td>
                  <td>
                    <span class="pd-5">{{formData.sex=='1'?'男':'女'}}</span>
                  </td>
                  <td class="pd-5" align="center" style="width: 50px">年龄</td>
                  <td>
                    <span class="pd-5">{{formData.age}}</span>
                  </td>
                  <td class="pd-5" align="center" style="width: 50px">民族</td>
                  <td>
                    <span class="pd-5">{{formData.nation}}</span>
                  </td>
                </tr>
                <tr>
                  <td
                    class="pd-10"
                    align="center"
                    style="width: 80px; height: 60px"
                  >
                    身份证号
                  </td>
                  <td colspan="3">
                    <span>{{formData.identityCard}}</span>
                  </td>
                  <td align="center" class="pd-5" style="width: 80px">
                    政治面貌
                  </td>
                  <td colspan="3">
                    <span class="pd-5">{{formData.politicsStatus}}</span>
                  </td>
                </tr>
                <tr>
                  <td
                    class="pd-10"
                    align="center"
                    style="width: 80px; height: 60px"
                  >
                    律师资格 或法律职业资格证书号
                  </td>
                  <td colspan="3">
                    <span>{{formData.certificationNumber}}</span>
                  </td>
                  <td align="center" class="pd-5" style="width: 80px">
                    住所地址
                  </td>
                  <td colspan="3">
                    <span class="pd-5">{{formData.homeAddress}}</span>
                  </td>
                </tr>
              </table>
            </td>
            <td align="center" style="width: 150px">照片</td>
          </tr>
          <tr>
            <td class="pd-10" align="center" style="width: 80px">毕业学校</td>
            <td>
              <span class="pd-10">{{ formData.schoolOfGraduation }}</span>
            </td>
            <td align="center" class="pd-5" style="width: 80px">最高学历</td>
            <td>
              <span class="pd-5">{{ status[+formData.highestEducation] }}</span>
            </td>
            <td class="pd-5" align="center" style="width: 80px">专业</td>
            <td colspan="2">
              <span class="pd-5">{{ formData.specialty }}</span>
            </td>
          </tr>
          <tr>
            <td class="pd-10" align="center" style="width: 80px">现工作单位</td>
            <td>
              <span class="pd-10">{{ formData.nowWorkUnit }}</span>
            </td>
            <td align="center" class="pd-5" style="width: 80px">
              具体工作部门
            </td>
            <td>
              <span class="pd-5">{{ formData.workDepartment }}</span>
            </td>
            <td class="pd-5" align="center" style="width: 80px" colspan="3">
              <table
                border="1"
                cellspacing="0"
                cellpadding="0"
                style="
                  width: 100%;
                  border-collapse: collapse;
                  border-width: 1px;
                  border-style: hidden;
                "
              >
                <tr>
                  <td class="pd-10" align="center" style="width: 77px">
                    办公电话
                  </td>
                  <td>
                    <span class="pd-5">{{ formData.officePhone }}</span>
                  </td>
                </tr>
                <tr>
                  <td class="pd-10" align="center" style="width: 70px">邮编</td>
                  <td>
                    <span class="pd-5">{{ formData.postalCode }}</span>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td class="pd-10" align="center" style="width: 80px">
              取得何种专业技术职务
            </td>
            <td>
              <span class="pd-10">{{ formData.technicalPosition }}</span>
            </td>
            <td align="center" class="pd-5" style="width: 80px">
              掌握何种外语及水平
            </td>
            <td>
              <span class="pd-5">{{ formData.foreignLanguageLevel }}</span>
            </td>
            <td class="pd-5" align="center" style="width: 80px">手机号</td>
            <td colspan="2">
              <span class="pd-5">{{ formData.phone }}</span>
            </td>
          </tr>

          <tr>
            <td align="center">简历</td>
            <td colspan="6">
              <table
                border="1"
                cellspacing="0"
                cellpadding="0"
                style="
                  width: 100%;
                  border-collapse: collapse;
                  border-width: 0px;
                  border-style: hidden;
                "
              >
                <tr>
                  <td
                    class="pd-10"
                    colspan="1"
                    align="center"
                    style="width: 150px"
                  >
                    起止时间
                  </td>
                  <td align="center" colspan="2">在何地何部门（学习）工作</td>
                  <td align="center">职务</td>
                </tr>
              
                <tr v-if="tableData.length>0" v-for="(item) in tableData" :key="item.practiceApplyExtId" >
                  <td class="pd-10" align="center" colspan="1">{{ formatTime(item.createdTime) +'--'+formatTime(item.endTime)}}</td>
                  <td align="center" colspan="2">{{ item.oldWorkUnit }}</td>
                  <td align="center">{{ item.position }}</td>
                </tr>
                <tr v-else>
                  <td class="pd-10" align="center" colspan="1"></td>
                  <td align="center" colspan="2"></td>
                  <td align="center"></td>
                </tr>
              
              </table>
            </td>
          </tr>

          <tr>
            <td class="pd-10" align="center" style="width: 80px">
              受过何种奖励
            </td>
            <td colspan="6">
              <span class="pd-10">{{ formData.award }}</span>
            </td>
          </tr>
          <tr>
            <td class="pd-10" align="center" style="width: 80px">
              受过何种处分
            </td>
            <td colspan="6">
              <span class="pd-10">{{ formData.punishment }}</span>
            </td>
          </tr>
          <tr>
            <td class="pd-10" align="center" style="width: 80px">申请书</td>
            <td colspan="6">
              <div
                style="
                  height: 800px;
                  display: flex;
                  flex-direction: column;
                  justify-content: space-between;
                  margin-bottom: 50px;
                "
              >
                <span>
                  （包括在所在单位从事法律事务工作的情况和自我鉴定；对填写的相关信息和提交的材料真实性、未存在不能从事律师职业的情形等作出承诺）
                </span>
                <span></span>
                <div>
                  <div style="text-align: right; margin-right: 100px">
                    申请人（签名）
                  </div>
                  <div
                    style="
                      text-align: right;
                      margin-right: 150px;
                      margin-top: 15px;
                    "
                  >
                    &nbsp;年&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日
                  </div>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <td class="pd-10" align="center" style="width: 80px">
              所在单位意见
            </td>
            <td colspan="6">
              <div
                style="
                  height: 400px;
                  display: flex;
                  flex-direction: column;
                  justify-content: space-between;
                  margin-bottom: 50px;
                "
              >
                <span>
                  （所在单位应对申请人属于本单位工作人员且与有效期限内劳动合同的身份作出证明，对其是否专门从事法律事务工作、品行情况及是否同意担任公司律师等出具意见；同时，简要说明本单位对公司律师的岗位职责要求及相关履职保障机制。）
                </span>
                <span></span>
                <div>
                  <div style="text-align: right; margin-right: 200px">
                    所在单位（章）：
                  </div>
                  <div
                    style="
                      text-align: right;
                      margin-right: 120px;
                      margin-top: 40px;
                    "
                  >
                    &nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日
                  </div>
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
	</div>
	</div>
  
   

   

</template>

<script>
import { getZYSQDefaultInfo } from '@/api/fwgl/gsls'
 import {parseTime} from '@/utils/index'
  export default {
    name: '',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        footer: true,
        dialogFormVisible: false,
        title: '',
        printContent: {
          id: 'printMe',
          popTitle: '',
          preview: false,
          previewTitle: '',
          etraCss: ' ',
          extraHead: ' ',
        },
				formData: {},
				tableData: [],
			
				status:['','博士研究生','硕士研究生','大学本科','大学专科','中专','大学及以下']
      }
    },
    computed: {},

  methods: {
       formatTime(val) {
        return parseTime(val, '{y}/{m}/{d}')
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
        this.footer = true
      },
			async printClick(row) {
				const res=	await getZYSQDefaultInfo({
            id: row.practiceApplyId,
          })	
        this.formData = res.data.practiceApply
        this.tableData = res.data.practiceApplyExt
        
				this.$forceUpdate()
			
				const btn = document.getElementById('printBtn')
          btn.click()
      },
    },
  }
</script>
<style lang="scss" scoped>
  .content {
    display: flex;
    .left {
      width: 20%;
    }
    .right {
      width: 80%;
    }
  }
  .flex {
    display: flex;
    align-items: center;
  }
  .mt-10 {
    margin-top: 10px;
  }
  .py-10 {
    padding: 10px 0;
    background: #ccccff;
  }
  .text-width {
    min-width: 130px;
    text-align: right;
  }
  .pd-10 {
    padding: 10px;
  }
  .pd-5 {
    padding: 3px;
  }
  .pd-12 {
    padding: 0 10px;
  }
  .foo {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
  }
	#printBtn {
    display: none;
  }
  .printMes {
    padding-top: 30px;
    padding-left: 30px;
    padding-right: 30px;
    padding-bottom: 30px;
  }
</style>

<style media="print">
  @media print {
    @page {
      size: auto;
    }
    body,
    html {
      height: auto !important;
    }
  }
