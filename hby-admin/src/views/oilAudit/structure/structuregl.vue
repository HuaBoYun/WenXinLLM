<template>
  <div class="content">
    <div class="left">
      <company-tree ref="leftList" @select="handleTreeSelect" />
    </div>
    <div class="right">
      <el-row :gutter="15">
        <el-form ref="form" label-width="125px" :model="form">
          <el-col :span="24">
            <el-divider>一、单位基本情况</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="统一社会信用代码" prop="realname">
              <el-input
                v-model="form.realname"
                clearable
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="原组织机构代码" prop="notunifiedcode">
              <el-input
                v-model="form.notunifiedcode"
                clearable
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位详细名称" prop="orgname">
              <el-input v-model="form.orgname" clearable style="width: 266px" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构类型" prop="organizationtype">
              <el-select
                v-model="form.organizationtype"
                placeholder="请选择人员类别"
                :style="{ width: '266px' }"
              >
                <el-option
                  v-for="item in JGtype"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider>行业类别/主要业务活动</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主要业务活动1" prop="mainactivityone">
              <el-input
                v-model="form.mainactivityone"
                clearable
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主要业务活动2" prop="mainactivitytwo">
              <el-input
                v-model="form.mainactivitytwo"
                clearable
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主要业务活动3" prop="mainactivitythr">
              <el-input
                v-model="form.mainactivitythr"
                clearable
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="行业代码(GB/T 4754-2017)" prop="industrycode">
              <el-input
                v-model="form.industrycode"
                clearable
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider>单位注册地及区划</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="省（自治区、直辖市）" prop="province">
              <el-input
                v-model="form.province"
                clearable
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地（区、市、州、盟）" prop="land">
              <el-input v-model="form.land" clearable style="width: 266px" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="县（区、市、旗）" prop="county">
              <el-input v-model="form.county" clearable style="width: 266px" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="乡（镇）" prop="country">
              <el-input v-model="form.country" clearable style="width: 266px" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="街（村）、门牌号" prop="street">
              <el-input v-model="form.street" clearable style="width: 266px" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="注册地位于" prop="subdistrictoffice">
              <el-input
                v-model="form.subdistrictoffice"
                clearable
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="街道办事处" prop="community">
              <el-input
                v-model="form.community"
                clearable
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="区域代码" prop="areacode">
              <el-input
                v-model="form.areacode"
                clearable
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="城乡代码" prop="ruralcode">
              <el-input
                v-model="form.ruralcode"
                clearable
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位规模" prop="ruralcode">
              <el-select
                v-model="form.unitsize"
                placeholder="请选择"
                style="width: 266px"
              >
                <el-option
                  v-for="item in DWscale"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="从业人员期末人数" prop="numberemployees">
              <el-input
                v-model="form.numberemployees"
                clearable
                type="number"
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="法定代表人（单位主要负责人）"
              prop="legalrepresentative"
            >
              <el-input
                v-model="form.legalrepresentative"
                clearable
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行会计标准类别" prop="accountingstandards">
              <el-select
                v-model="form.accountingstandards"
                placeholder="请选择"
                style="width: 266px"
              >
                <el-option
                  v-for="item in options3"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider>联系方式</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="长途电话" prop="phone">
              <el-input v-model="form.phone" clearable style="width: 266px" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="固定电话" prop="fixedtelephone">
              <el-input
                v-model="form.fixedtelephone"
                clearable
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮政编码" prop="postalcode">
              <el-input
                v-model="form.postalcode"
                clearable
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电子邮件" prop="email">
              <el-input v-model="form.email" clearable style="width: 266px" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="网址" prop="website">
              <el-input v-model="form.website" clearable style="width: 266px" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider>二、单位组织结构情况</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="本法人单位是否有上一级法人" prop="sfysjfr">
              <el-select
                v-model="form.sfysjfr"
                placeholder="请选择"
                style="width: 266px"
                @change="
                  setChange($event, [
                    'unifiedcodeone',
                    'unifiedcodetwo',
                    'unifiedcodethr',
                  ])
                "
              >
                <el-option
                  v-for="item in isNot"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上一级法人社会信用代码" prop="unifiedcodeone">
              <el-input
                v-model="form.unifiedcodeone"
                clearable
                :disabled="form.sfysjfr === 2"
                style="width: 266px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="尚未领取统一社会信用代码的填写原组织机构代码"
              prop="unifiedcodetwo"
            >
              <el-input
                v-model="form.unifiedcodetwo"
                clearable
                style="width: 266px"
                :disabled="form.sfysjfr === 2"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="上一级法人单位单位详细名称"
              prop="unifiedcodethr"
            >
              <el-input
                v-model="form.unifiedcodethr"
                clearable
                style="width: 266px"
                :disabled="form.sfysjfr === 2"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider>三、总审计师与内部审计机构基本情况</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否设置总审计师" prop="whetherfit">
              <el-select
                v-model="form.whetherfit"
                placeholder="请选择"
                style="width: 266px"
              >
                <el-option
                  v-for="item in isNot"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总审计师职位层级" prop="positionlevel">
              <el-select
                v-model="form.positionlevel"
                placeholder="请选择"
                style="width: 266px"
              >
                <el-option
                  v-for="item in level"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总审计师任职方式" prop="appointmentmode">
              <el-select
                v-model="form.appointmentmode"
                placeholder="请选择"
                style="width: 266px"
              >
                <el-option
                  v-for="item in options7"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否设置内部审计机构" prop="ifsetup">
              <el-select
                v-model="form.ifsetup"
                placeholder="请选择"
                style="width: 266px"
                @change="setChange($event, 'auditname')"
              >
                <el-option
                  v-for="item in isNot"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="内部审计机构名称" prop="auditname">
              <el-input
                :disabled="form.ifsetup === 2"
                v-model="form.auditname"
                style="width: 266px"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="内部审计工作的领导机构"
              prop="leadingorganization"
            >
              <el-select
                v-model="form.leadingorganization"
                placeholder="请选择"
                style="width: 266px"
              >
                <el-option
                  v-for="item in options9"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否独立设置内部机构" prop="ifindependently">
              <el-select
                v-model="form.ifindependently"
                placeholder="请选择"
                style="width: 266px"
              >
                <el-option
                  v-for="item in isNot"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="如为2，则内部审计机构与以下哪些职能部门合并设置？"
              prop="functionaldepartment"
            >
              <el-radio-group
                v-model="form.functionaldepartment"
                style="width: 266px"
              >
                <el-radio label="1">财务部</el-radio>
                <el-radio label="2">法务部门</el-radio>
                <el-radio label="3">内部控制部</el-radio>
                <el-radio label="4">纪检部门</el-radio>
                <el-radio label="5">其他部门</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="内部审计机构层级" prop="internalaudit">
              <el-select
                v-model="form.internalaudit"
                placeholder="请选择"
                style="width: 266px"
              >
                <el-option
                  v-for="item in options11"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider>四、内部审计人员配备基本情况</el-divider>
          </el-col>
        </el-form>
      </el-row>

      <table
        width="100%"
        border="1"
        cellspacing="0"
        cellpadding="0"
        style="border-collapse: collapse"
      >
        <tbody class="detailContent">
          <!-- <tr>
            <th align="center" colspan="8" class="py-10">一、单位基本情况</th>
          </tr>
          <tr>
            <td align="center">010101</td>
            <td colspan="2">
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
                  <td>统一社会信用代码</td>
                  <td><el-input v-model="form.unifiedcode"></el-input></td>
                </tr>
                <tr>
                  <td colspan="2">
                    尚未领取统一社会信用代码的填写原组织机构代码
                  </td>
                </tr>
                <tr>
                  <td colspan="2">
                    <el-input v-model="form.notunifiedcode"></el-input>
                  </td>
                </tr>
              </table>
            </td>
            <td align="center">010102</td>
            <td colspan="2" align="right">单位详细名称</td>
            <td colspan="2"><el-input v-model="form.orgname"></el-input></td>
          </tr>
          <tr>
            <td align="center">010103</td>
            <td colspan="2" align="right" class="pd-10">机构类型</td>
            <td colspan="4">
              <el-select v-model="form.organizationtype" placeholder="请选择">
                <el-option
                  v-for="item in JGtype"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </td>
            <td class="pd-10">
              10 企业 20 事业单位 30 机关 40 社会团体 51 民办非企业单位 52
              基金会 53 居委会 54 村委会 55 农民专业合作社 56 农村集体经济组织
              90 其他组织机构
            </td>
          </tr>
          <tr>
            <td align="center">010103</td>
            <td colspan="7" align="center">
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
                <tr colspan="4" align="center">
                  <td class="pd-10">行业类别</td>
                </tr>
                <tr colspan="4" align="center">
                  <td class="pd-10">主要业务活动</td>
                </tr>
                <tr align="center">
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
                      <td align="right" class="pd-10">1</td>
                      <td>
                        <el-input v-model="form.mainactivityone"></el-input>
                      </td>
                      <td align="right" class="pd-10">2</td>
                      <td>
                        <el-input v-model="form.mainactivitytwo"></el-input>
                      </td>
                      <td align="right" class="pd-10">3</td>
                      <td>
                        <el-input v-model="form.mainactivitythr"></el-input>
                      </td>
                    </tr>
                    <tr>
                      <td colspan="2">行业代码(GB/T 4754-2017)</td>
                      <td colspan="4">
                        <el-input v-model="form.industrycode"></el-input>
                      </td>
                    </tr>
                  </table>
                </tr>
              </table>
            </td>
          </tr>

          <tr>
            <td align="center">010104</td>
            <td colspan="7" align="center">
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
                <tr colspan="4" align="center">
                  <td class="pd-10">单位注册地及区划</td>
                </tr>
                <tr align="center">
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
                      <td><el-input v-model="form.province"></el-input></td>
                      <td align="left">省（自治区、直辖市）</td>
                      <td><el-input v-model="form.land"></el-input></td>
                      <td align="left">地（区、市、州、盟）</td>
                      <td><el-input v-model="form.county"></el-input></td>
                      <td align="left">县（区、市、旗）</td>
                    </tr>
                    <tr>
                      <td><el-input v-model="form.country"></el-input></td>
                      <td align="left">乡（镇）</td>
                      <td colspan="2">
                        <el-input v-model="form.street"></el-input>
                      </td>
                      <td colspan="2" align="left">街（村）、门牌号</td>
                    </tr>

                    <tr>
                      <td align="right">注册地位于</td>
                      <td>
                        <el-input v-model="form.subdistrictoffice"></el-input>
                      </td>
                      <td align="right">街道办事处</td>
                      <td>
                        <el-input v-model="form.community"></el-input>
                      </td>
                      <td colspan="2" align="left">社区（居委会）</td>
                    </tr>

                    <tr>
                      <td align="right">区域代码</td>
                      <td colspan="2">
                        <el-input v-model="form.areacode"></el-input>
                      </td>
                      <td align="right">城乡代码</td>
                      <td colspan="2">
                        <el-input v-model="form.ruralcode"></el-input>
                      </td>
                    </tr>
                  </table>
                </tr>
              </table>
            </td>
          </tr>

          <tr>
            <td align="center">010105</td>
            <td colspan="2">单位规模</td>
            <td colspan="3">
              <el-select v-model="form.unitsize" placeholder="请选择">
                <el-option
                  v-for="item in DWscale"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </td>
            <td colspan="2">1 大型 2 中型 3 小型 4 微型</td>
          </tr>

          <tr>
            <td align="center">010106</td>
            <td>从业人员</td>
            <td align="right">从业人员期末人数</td>
            <td colspan="4">
              <el-input v-model="form.numberemployees" type="number"></el-input>
            </td>
            <td>人</td>
          </tr>

          <tr>
            <td align="center">010107</td>
            <td>法定代表人（单位主要负责人）</td>
            <td><el-input v-model="form.legalrepresentative"></el-input></td>
            <td align="center">010108</td>
            <td align="right">执行会计标准类别</td>
            <td colspan="2">
              <el-select
                v-model="form.accountingstandards"
                placeholder="请选择"
              >
                <el-option
                  v-for="item in options3"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </td>
            <td>1 企业会计准则制度 2 政府会计准则制度</td>
          </tr>

          <tr>
            <td align="center">010109</td>
            <td colspan="8">
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
                  <td style="padding: 8px 0" colspan="2" align="left">
                    联系方式
                  </td>
                  <td rowspan="2" colspan="2" align="right">电子邮件</td>
                  <td rowspan="2" colspan="2">
                    <el-input v-model="form.email"></el-input>
                  </td>
                </tr>
                <tr>
                  <td align="right">长途电话</td>
                  <td>
                    <el-input v-model="form.phone" type="number"></el-input>
                  </td>
                </tr>
                <tr>
                  <td align="right">固定电话</td>
                  <td><el-input v-model="form.fixedtelephone"></el-input></td>
                  <td rowspan="2" colspan="2" align="right">网址</td>
                  <td rowspan="2" colspan="2">
                    <el-input v-model="form.website"></el-input>
                  </td>
                </tr>
                <tr>
                  <td align="right">邮政编码</td>
                  <td>
                    <el-input
                      v-model="form.postalcode"
                      type="number"
                    ></el-input>
                  </td>
                </tr>
              </table>
            </td>
          </tr>

          <tr>
            <th align="center" colspan="8" class="py-10">
              二、单位组织结构情况
            </th>
          </tr>

          <tr>
            <td align="center">010201</td>
            <td colspan="7">
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
                  <td align="right">本法人单位是否有上一级法人</td>
                  <td colspan="4">
                    <el-select
                      v-model="form.sfysjfr"
                      placeholder="请选择"
                      @change="
                        setChange($event, [
                          'unifiedcodeone',
                          'unifiedcodetwo',
                          'unifiedcodethr',
                        ])
                      "
                    >
                      <el-option
                        v-for="item in isNot"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                      ></el-option>
                    </el-select>
                  </td>
                </tr>

                <tr>
                  <td align="right">如为1，请填写</td>
                  <td>
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
                        <td align="right">上一级法人社会信用代码</td>
                        <td>
                          <el-input
                            :disabled="form.sfysjfr === 2"
                            v-model="form.unifiedcodeone"
                          ></el-input>
                        </td>
                      </tr>
                      <tr>
                        <td align="right">
                          尚未领取统一社会信用代码的填写原组织机构代码
                        </td>
                        <td>
                          <el-input
                            :disabled="form.sfysjfr === 2"
                            v-model="form.unifiedcodetwo"
                          ></el-input>
                        </td>
                      </tr>
                      <tr>
                        <td align="right">上一级法人单位单位详细名称</td>
                        <td>
                          <el-input
                            :disabled="form.sfysjfr === 2"
                            v-model="form.unifiedcodethr"
                          ></el-input>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </td>
          </tr>

          <tr>
            <th align="center" colspan="8" class="py-10">
              三、总审计师与内部审计机构基本情况
            </th>
          </tr>
          <tr>
            <td align="center">010301</td>
            <td align="right">是否设置总审计师</td>
            <td colspan="4">
              <el-select v-model="form.whetherfit" placeholder="请选择">
                <el-option
                  v-for="item in isNot"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </td>
            <td colspan="2" align="left">1 是 2 否</td>
          </tr>

          <tr>
            <td align="center">010302</td>
            <td align="right">总审计师职位层级</td>
            <td colspan="4">
              <el-select v-model="form.positionlevel" placeholder="请选择">
                <el-option
                  v-for="item in level"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </td>
            <td colspan="2" align="left">
              <div>1 领导班子成员 2 除领导班子成员以外的其他高级管理人员</div>
              <div>3 中层机构主要负责人或以下级别 4 其他</div>
            </td>
          </tr>

          <tr>
            <td align="center">010303</td>
            <td align="right">总审计师任职方式</td>
            <td colspan="4">
              <el-select v-model="form.appointmentmode" placeholder="请选择">
                <el-option
                  v-for="item in options7"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </td>

            <td colspan="2" align="left">
              <div>
                1 上一级法人单位委派 2 本单位自行产生（须报上级单位批准或备案）
              </div>
              <div>3 本单位自行产生（无须批准或备案） 4 其他</div>
            </td>
          </tr>

          <tr>
            <td align="center">010304</td>
            <td colspan="7">
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
                  <td align="right">是否设置内部审计机构</td>
                  <td colspan="2">
                    <el-select
                      v-model="form.ifsetup"
                      placeholder="请选择"
                      @change="setChange($event, 'auditname')"
                    >
                      <el-option
                        v-for="item in isNot"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                      ></el-option>
                    </el-select>
                  </td>
                  <td colspan="2" align="left">1 是 2 否</td>
                </tr>
                <tr>
                  <td align="right">内部审计机构名称</td>
                  <td colspan="3">
                    <el-input
                      :disabled="form.ifsetup === 2"
                      v-model="form.auditname"
                    ></el-input>
                  </td>
                </tr>
              </table>
            </td>
          </tr>

          <tr>
            <td align="center">010305</td>
            <td align="right">内部审计工作的领导机构</td>
            <td colspan="4">
              <el-select
                v-model="form.leadingorganization"
                placeholder="请选择"
              >
                <el-option
                  v-for="item in options9"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </td>
            <td colspan="2" align="left">
              <div>1 主要负责人分管 2 主要负责人分管（其他领导人员协管）</div>
              <div>3 其他班子成员分管 4 总审计师分管</div>
              <div>5 其他</div>
            </td>
          </tr>

          <tr>
            <td align="center">010306</td>
            <td colspan="7">
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
                  <td align="right">是否独立设置内部机构</td>
                  <td colspan="2">
                    <el-select
                      v-model="form.ifindependently"
                      placeholder="请选择"
                    >
                      <el-option
                        v-for="item in isNot"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                      ></el-option>
                    </el-select>
                  </td>
                  <td colspan="2" align="left">1 是 2 否</td>
                </tr>
                <tr>
                  <td align="right">
                    如为2，则内部审计机构与以下哪些职能部门合并设置？
                  </td>
                  <td colspan="3">
                    <el-input v-model="form.functionaldepartment"></el-input>
                  </td>
                </tr>
                <tr>
                  <td colspan="4">
                    <el-radio-group v-model="radio">
                      <el-radio label="1">财务部</el-radio>
                      <el-radio label="2">法务部门</el-radio>
                      <el-radio label="3">内部控制部</el-radio>
                      <el-radio label="4">纪检部门</el-radio>
                      <el-radio label="5">其他部门</el-radio>
                    </el-radio-group>
                  </td>
                </tr>
              </table>
            </td>
          </tr>

          <tr>
            <td align="center">010307</td>
            <td align="right">内部审计机构层级</td>
            <td colspan="4">
              <el-select v-model="form.internalaudit" placeholder="请选择">
                <el-option
                  v-for="item in options11"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </td>
            <td colspan="2" align="left">
              <div>1 本单位二级部门 2 本单位所属独立核算的二级单位</div>
              <div>3 本单位二级部门的内设部门 4 其他</div>
            </td>
          </tr>

          <tr>
            <th align="center" colspan="8" class="py-10">
              四、内部审计人员配备基本情况
            </th>
          </tr> -->

          <tr>
            <!-- <td align="center">010401</td> -->
            <td align="center">内部审计人员数量</td>
            <td class="pd-10" align="center">编制人数</td>
            <td>
              <span class="pd-10">
                <!-- <el-input v-model="form.functionaldepartment"></el-input> -->
              </span>
            </td>
            <td align="center" class="pd-10">实有人员数</td>
            <td>
              <span class="pd-10">{{ form['实有人数'] }}</span>
            </td>
            <td class="pd-10" align="center">其中：专职人员</td>
            <td>
              <span class="pd-10">{{ form['专职'] }}</span>
            </td>
          </tr>

          <tr>
            <!-- <td align="center">010402</td> -->
            <td class="pd-10" align="center">
              内部审计人员中拥有CIA资格的人员数
            </td>
            <td colspan="6">
              <span class="pd-10">{{ form.CIA }}</span>
            </td>
          </tr>

          <tr>
            <!-- <td align="center">010403</td> -->
            <td align="center">内部审计人员学历</td>
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
                    colspan="2"
                    align="center"
                    style="width: 150px"
                  >
                    硕士及以上
                  </td>
                  <td>
                    <span class="pd-10">{{ form['硕士及以上'] }}</span>
                  </td>
                  <td>人</td>
                </tr>
                <tr>
                  <td class="pd-10" align="center" colspan="2">大学本科</td>
                  <td>
                    <span class="pd-10">{{ form['本科'] }}</span>
                  </td>
                  <td>人</td>
                </tr>
                <tr>
                  <td class="pd-10" align="center" colspan="2">
                    大学专科及以下
                  </td>
                  <td>
                    <span class="pd-10">{{ form['大专及以下'] }}</span>
                  </td>
                  <td>人</td>
                </tr>
              </table>
            </td>
          </tr>

          <tr>
            <!-- <td align="center">010404</td> -->
            <td align="center">内部审计人员职称</td>
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
                    align="center"
                    colspan="2"
                    class="pd-10"
                    style="width: 150px"
                  >
                    高级职称
                  </td>
                  <td>
                    <span class="pd-10">{{ form['高级职称'] }}</span>
                  </td>
                  <td>人</td>
                </tr>
                <tr>
                  <td align="center" colspan="2" class="pd-10">中级职称</td>
                  <td>
                    <span class="pd-10">{{ form['中级职称'] }}</span>
                  </td>
                  <td>人</td>
                </tr>
                <tr>
                  <td align="center" colspan="2" class="pd-10">初级职称</td>
                  <td>
                    <span class="pd-10">{{ form['初级职称'] }}</span>
                  </td>
                  <td>人</td>
                </tr>
                <tr>
                  <td align="center" colspan="2" class="pd-10">无职称</td>
                  <td>
                    <span class="pd-10">{{ form['无职称'] }}</span>
                  </td>
                  <td>人</td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <!-- <td align="center">010405</td> -->
            <td align="center">内部审计人员年龄结构</td>
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
                    align="center"
                    colspan="2"
                    style="width: 150px"
                  >
                    50岁以上
                  </td>
                  <td>
                    <span class="pd-10">{{ form['五十岁以上'] }}</span>
                  </td>
                  <td>人</td>
                </tr>
                <tr>
                  <td class="pd-10" align="center" colspan="2">30-50岁</td>
                  <td>
                    <span class="pd-10">
                      {{ form['三十到五十岁'] }}
                    </span>
                  </td>
                  <td>人</td>
                </tr>
                <tr>
                  <td class="pd-10" align="center" colspan="2">30岁以下</td>
                  <td>
                    <span class="pd-10">{{ form['三十岁以下'] }}</span>
                  </td>
                  <td>人</td>
                </tr>
              </table>
            </td>
          </tr>

          <tr>
            <!-- <td align="center">010405</td> -->
            <td align="center">内部审计人员年龄结构</td>
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
                    align="center"
                    class="pd-10"
                    colspan="2"
                    style="width: 150px"
                  >
                    审计
                  </td>
                  <td>
                    <span class="pd-10">{{ form['审计'] }}</span>
                  </td>
                  <td>人</td>
                </tr>
                <tr>
                  <td align="center" class="pd-10" colspan="2">会计</td>
                  <td>
                    <span class="pd-10">{{ form['会计'] }}</span>
                  </td>
                  <td>人</td>
                </tr>
                <tr>
                  <td align="center" class="pd-10" colspan="2">经济</td>
                  <td>
                    <span class="pd-10">{{ form['经济'] }}</span>
                  </td>

                  <td>人</td>
                </tr>
                <tr>
                  <td align="center" class="pd-10" colspan="2">法律</td>
                  <td>
                    <span class="pd-10">{{ form['法律'] }}</span>
                  </td>
                  <td>人</td>
                </tr>
                <tr>
                  <td align="center" class="pd-10" colspan="2">管理</td>
                  <td>
                    <span class="pd-10">{{ form['管理'] }}</span>
                  </td>

                  <td>人</td>
                </tr>
                <tr>
                  <td align="center" class="pd-10" colspan="2">信息技术</td>
                  <td>
                    <span class="pd-10">{{ form['信息技术'] }}</span>
                  </td>
                  <td>人</td>
                </tr>
                <tr>
                  <td align="center" class="pd-10" colspan="2">工程</td>
                  <td>
                    <span class="pd-10">{{ form['工程'] }}</span>
                  </td>
                  <td>人</td>
                </tr>
                <tr>
                  <td align="center" class="pd-10" colspan="2">其他</td>
                  <td>
                    <span class="pd-10">{{ form['其他'] }}</span>
                  </td>
                  <td>人</td>
                </tr>
              </table>
            </td>
          </tr>
        </tbody>
      </table>

      <div class="foo">
        <div>
          <div class="flex mt-10">
            <div class="flex">
              <div class="text-width">统计负责人:</div>
              <el-input v-model="form.statistical"></el-input>
            </div>
            <div class="flex">
              <div class="text-width">填表人:</div>
              <el-input v-model="form.preparer"></el-input>
            </div>
          </div>
          <div class="flex mt-10">
            <div class="flex">
              <div class="text-width">联系电话:</div>
              <el-input v-model="form.contactnumber"></el-input>
            </div>
            <div class="flex">
              <div class="text-width">填表日期:</div>
              <el-date-picker
                v-model="form.fill"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                placeholder="选择日期"
              ></el-date-picker>
            </div>
          </div>
        </div>
        <div class="mt-10">
          <div><el-button type="primary" @click="Submit">提交</el-button></div>
          <!-- <div class="mt-10">
            <el-button type="success">成功按钮</el-button>
          </div> -->
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import {
    getAuditorInformationList,
    organInfoDetail,
    saveOrUpdateOrgan,
  } from '@/oapi/structure.js'
  import CompanyTree from '@/components/CompanyTree.vue'
  export default {
    components: { CompanyTree },
    name: 'auditInstitutions',
    data() {
      return {
        form: {},
        value: '',
        value1: '',
        JGtype: [
          {
            value: 10,
            label: '企业',
          },
          {
            value: 20,
            label: '事业单位',
          },
          {
            value: 30,
            label: '机关',
          },
          {
            value: 40,
            label: '社会团体',
          },
          {
            value: 51,
            label: '民办非企业单位',
          },
          {
            value: 52,
            label: '基金会',
          },
          {
            value: 53,
            label: '居委会',
          },
          {
            value: 54,
            label: '村委会',
          },
          {
            value: 55,
            label: '农民专业合作社',
          },
          {
            value: 56,
            label: '农村集体经济组织',
          },
          {
            value: 90,
            label: '其他组织机构',
          },
        ],
        value2: '',
        DWscale: [
          {
            value: 1,
            label: '大型',
          },
          {
            value: 2,
            label: '中型',
          },
          {
            value: 3,
            label: '小型',
          },
          {
            value: 4,
            label: '微型',
          },
        ],
        value3: '',
        options3: [
          {
            value: 1,
            label: '企业会计准则制度',
          },
          {
            value: 2,
            label: '政府会计准则制度',
          },
        ],
        value4: '',
        value5: '',
        isNot: [
          {
            value: 1,
            label: '是',
          },
          {
            value: 2,
            label: '否',
          },
        ],
        value6: '',
        level: [
          {
            value: 1,
            label: '领导班子成员',
          },
          {
            value: 2,
            label: '除领导班子成员以外的其他高级管理人员',
          },
          {
            value: 3,
            label: '中层机构主要负责人或以下级别',
          },
          {
            value: 4,
            label: '其他',
          },
        ],
        value7: '',
        options7: [
          {
            value: 1,
            label: '上一级法人单位委派',
          },
          {
            value: 2,
            label: '本单位自行产生（须报上级单位批准或备案）',
          },
          {
            value: 3,
            label: '本单位自行产生（无须批准或备案）',
          },
          {
            value: 4,
            label: '其他',
          },
        ],
        value8: '',
        radio: '',
        value11: '',
        value9: '',
        options9: [
          {
            value: 1,
            label: '主要负责人分管',
          },
          {
            value: 2,
            label: '主要负责人分管（其他领导人员协管）',
          },
          {
            value: 3,
            label: '其他班子成员分管',
          },
          {
            value: 4,
            label: '总审计师分管',
          },
          {
            value: 5,
            label: '其他',
          },
        ],
        value10: '',
        options11: [
          {
            value: 1,
            label: '本单位二级部门',
          },
          {
            value: 2,
            label: '本单位所属独立核算的二级单位',
          },
          {
            value: 3,
            label: '本单位二级部门的内设部门',
          },
          {
            value: 4,
            label: '其他',
          },
        ],
      }
    },
    methods: {
      handleTreeSelect(v) {
        if (v.id) {
          this.id = v.id
        }
        this.id = v.id
        this.form = {}
        organInfoDetail({ orgId: this.id }).then((res) => {
          if (res.code === 1) {
            if (res.data.info) {
              this.form = res.data.info
              this.form.fill = res.data.info.fillingdate
              this.radio = res.data.info.functionaldepartment
            }
            this.form.orgid = this.id
            this.form.orgname = v.label
          }

          getAuditorInformationList({ orgId: this.id }).then((res1) => {
            if (res1.code === 1) {
              this.form = { ...this.form, ...res1.data.data }
            }
          })
        })
      },
      setChange(v, type) {
        if (type === 'string') {
          if (v === 2) {
            this.form[type] = ''
          }
        } else {
          if (v === 2) {
            type.map((v) => {
              this.form[v] = ''
            })
          }
        }
      },
      Submit() {
        Reflect.deleteProperty(this.form, 'fillingdate')
        this.form.functionaldepartment = this.radio
        this.form.fill = this.form.fill.split('T')[0]

        if (this.form.orgid) {
          saveOrUpdateOrgan(this.form).then((res) => {
            this.$message.success(res.data.msg)
          })
        } else {
          this.$message.error('请先选择左侧公司')
        }
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
  .foo {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
  }
</style>
